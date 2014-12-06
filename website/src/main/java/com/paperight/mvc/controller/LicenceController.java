package com.paperight.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paperight.Application;
import com.paperight.LicenceFileService;
import com.paperight.authentication.AuthenticationService;
import com.paperight.content.ContentService;
import com.paperight.credit.InsufficientCreditsException;
import com.paperight.credit.PaperightCreditService;
import com.paperight.currency.CurrencyService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.licence.Licence;
import com.paperight.licence.Licence.InvoiceState;
import com.paperight.licence.LicenceInvoiceService;
import com.paperight.licence.LicenceStatementService;
import com.paperight.licence.LicenceStatus;
import com.paperight.licence.PageLayout;
import com.paperight.licence.Preview;
import com.paperight.licence.PreviewCreator;
import com.paperight.pdf.PreviewCreatorPdfImpl;
import com.paperight.product.Product;
import com.paperight.publisherearning.PublisherEarningService;
import com.paperight.rest.util.RestHttpClientFactory;
import com.paperight.user.Company;
import com.paperight.user.User;

@Controller
@PreAuthorize("hasRole('ROLE_OUTLET')")
public class LicenceController {
	
	private Logger logger = LoggerFactory.getLogger(LicenceController.class);

	@Autowired
	private PaperightCreditService paperightCreditService;

	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private LicenceFileService licenceFileService;
	
	@Autowired
	private Application application;
	
	@Autowired
	private PublisherEarningService publisherEarningService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private EmailGateway emailGateway;
	
	@Autowired
	private LicenceInvoiceService licenceInvoiceService;
	
	@Autowired
	private LicenceStatementService licenceStatementService;

	@Transactional
	private void completeLicencePurchase(Licence licence, Company company, BigDecimal totalLicenceCost) throws InsufficientCreditsException {
		paperightCreditService.spendPaperightCredits(company, totalLicenceCost);
		if (licence.getPageLayout() == PageLayout.PHOTOCOPY) {
		    licence = licence.merge();
		    try {
                publisherEarningService.recordPublisherEarning(licence);
            } catch (Exception e) {
                logger.error("Unable to record publisher earnings for licence " + licence.getId(), e);
            }
		} else {
		    licence.persist();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/licence/purchase")
	public @ResponseBody Object licencePurchase(@RequestParam("productId") long productId, @RequestParam("layout") String layout, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("outletcharges") BigDecimal outletcharges, @RequestParam("quantity") int quantity) {

		User user = AuthenticationService.currentActingUser();
		Product product = Product.find(productId);
		Company company = user.getCompany();

		BigDecimal costInCredits = product.getLicenceFeeInCredits().multiply(new BigDecimal(quantity));
		BigDecimal costInDollars = product.getLicenceFeeInDollars().multiply(new BigDecimal(quantity));
		BigDecimal costInCurrency = currencyService.convert(costInDollars, currencyService.getBaseCurrency(), company.getCurrency());
		BigDecimal costInInvoiceCurrency = currencyService.convert(costInDollars, currencyService.getBaseCurrency(), currencyService.getInvoiceCurrency());

		Licence licence = new Licence();
		licence.setUser(user);
		licence.setCompany(company);
		licence.setProduct(product);
		licence.setOwnerCompany(product.getOwnerCompany());
		licence.setCustomerName(firstName);
		licence.setCustomerLastName(lastName);
		licence.setCustomerPhoneNumber(phoneNumber);
		licence.setNumberOfCopies(quantity);
		licence.setStatus(LicenceStatus.NEW);
		licence.setCostInCredits(costInCredits);
		licence.setCostInCurrency(costInCurrency);
		licence.setOutletCharge(outletcharges);
		licence.setCurrencyCode(company.getCurrency().getCode());
		licence.setRandsInvoiceValue(costInInvoiceCurrency);
		licence.setDollarsInvoiceValue(costInDollars);
		licence.setTrackingUrl("");
		licence.setPageLayout(PageLayout.valueOf(layout));
		licence.setPaperightCreditBaseCurrencyCode(currencyService.getBaseCurrency().getCode());
		licence.setPaperightCreditToBaseCurrencyRate(paperightCreditService.getPaperightCreditToBaseCurrencyRate());
		licence.setInvoiceState(InvoiceState.NEW);

		Map<String, Object> response = new HashMap<>();
		response.put("result", true);
		response.put("message", "");
		try {
			completeLicencePurchase(licence, company, costInCredits);
			AuthenticationService.updateActingUser(User.find(AuthenticationService.currentActingUser().getId()));
			// Send licence-purchase emails
			emailGateway.licencePurchaseComplete(licence);
		} catch (InsufficientCreditsException e) {
		    response.put("result", false);
		    response.put("message", contentService.getSnippetValue("dialog-purchasing-licence-message-insufficient-credits", "product-detail-licences", "Sorry, you don't have enough credits. Please top up.", false) );
		}
		return response;
	}
	
    @RequestMapping(method = RequestMethod.POST, value = "/licence/{licenceId}/generate")
    public @ResponseBody Object generateLicence(@PathVariable Long licenceId) throws Exception {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            User user = AuthenticationService.currentActingUser();
            Licence licence = Licence.findByIdAndCompanyId(licenceId, user.getCompany().getId());
            if (licence.getStatus() == LicenceStatus.NEW) {
                if (licence.getPageLayout() == PageLayout.PHOTOCOPY) {
                    String filename = FilenameUtils.concat(licenceFileService.getLicencedPdfFileFolder(), licence.getId() + ".pdf");
                    licenceInvoiceService.generateOutletInvoice(licence, filename);
                    licence.setStatus(LicenceStatus.GENERATED);
                    licence.merge();
                    map.put("result", true);
                } else {
                    ClientHttpResponse serverResponse = RestHttpClientFactory.execute("rest/licence/generate/" + licence.getId(), HttpMethod.GET);
                    InputStream inputStream = serverResponse.getBody();
                    try {
                        if (serverResponse.getStatusCode() == HttpStatus.OK) {
                            Response response = new ObjectMapper().readValue(inputStream, Response.class);
                            if (response.getStatus() == ResponseStatus.OK) {
                                map.put("result", true);
                            } else {
                                map.put("result", false);
                                map.put("message", response.getResponseObject());
                            }
                        } else {
                            map.put("result", false);
                            map.put("message", "Error generating licence");
                        }
                    } finally {
                        inputStream.close();
                    }
                }
            } else {
                map.put("result", false);
                map.put("message", "Licence already generated");
            }
            return map;
        } catch (Exception e) {
            logger.error("Unable to generate licenceid: " + licenceId, e);
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/licence/{licenceId}/download")
    public void licenceDownload(@PathVariable Long licenceId, HttpServletResponse response) throws Exception {
        try {
            User user = AuthenticationService.currentActingUser();
            Licence licence = Licence.findByIdAndCompanyId(licenceId, user.getCompany().getId());
            if (licence != null) {
                /*if (licence.getPageLayout() == PageLayout.PHOTOCOPY) {
                    response.setContentType("application/pdf");
                    response.addHeader("content-disposition", "attachment; filename=print_licence_" + licenceId + ".pdf");
                    licenceInvoiceService.generateOutletInvoice(licence, response.getOutputStream());
                    response.flushBuffer();
                    if (licence.getFirstDownloadedDate() == null) {
                        licence.setFirstDownloadedDate(DateTime.now());
                    }
                    licence.setStatus(LicenceStatus.DOWNLOADED);
                    licence.merge();                    
                } else {*/
                    if (licence.getStatus() == LicenceStatus.GENERATED) {
                        String filename = FilenameUtils.concat(licenceFileService.getLicencedPdfFileFolder(), licence.getId() + ".pdf");
                        File file = new File(filename);
                        InputStream inputStream = new FileInputStream(file);
                        try {
                            response.setContentType("application/pdf");
                            String tempFilename = URLEncoder.encode(licence.getProduct().getTitle(), "UTF-8");
                            tempFilename = tempFilename + "_" + URLEncoder.encode(licence.getCustomerFullName(), "UTF-8");
                            tempFilename = tempFilename + "_" + DateTime.now().getMillis();
                            response.setContentLength(new Long(file.length()).intValue());
                            response.addHeader("content-disposition", "attachment; filename=" + tempFilename + ".pdf");
                            IOUtils.copy(inputStream, response.getOutputStream());
                            response.flushBuffer();
                            if (licence.getFirstDownloadedDate() == null) {
                                licence.setFirstDownloadedDate(DateTime.now());
                            }
                            licence.setStatus(LicenceStatus.DOWNLOADED);
                            licence.merge();
                            try {
                                publisherEarningService.recordPublisherEarning(licence);
                            } catch (Exception e) {
                                logger.error("Unable to record publisher earnings for licence " + licence.getId(), e);
                            }
                        } finally {
                            inputStream.close();
                        }
                    }
                //}
            } else {
                throw new Exception("Licence not found");
            }
        } catch (Exception e) {
            logger.error("Unable to download licenceid: " + licenceId, e);
            throw e;
        }
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/licence/{licenceId}/invoice")
    public  @ResponseBody Object licenceInvoice(Model model, @PathVariable("licenceId") long licenceId, HttpServletResponse response) throws Exception {
        User user = AuthenticationService.currentActingUser();
        Licence licence = Licence.findByIdAndCompanyId(licenceId, user.getCompany().getId());
        if (licence != null) {
            response.setContentType("application/pdf");
            response.addHeader("content-disposition", "attachment; filename=invoice_" + licenceId + ".pdf");
            licenceInvoiceService.generateOutletInvoice(licence, response.getOutputStream());
            response.flushBuffer();
            licence.setInvoiceState(InvoiceState.DOWNLOADED);
            licence.merge();
            model.addAttribute("result", true);
            model.addAttribute("message", "");
        }
        return model;
    }

	@RequestMapping(method = RequestMethod.GET, value = "/licence/{licenceId}/cancel")
	public @ResponseBody Object cancelLicence(@PathVariable Long licenceId) {
		Licence licence = Licence.find(licenceId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (licence != null) {
			cancelLicence(licence);
			AuthenticationService.updateActingUser(User.find(AuthenticationService.currentActingUser().getId()));
			map.put("result", true);
		} else {
			map.put("result", false);
		}
		return map;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/licence/{licenceId}/reset")
	public @ResponseBody Object resetLicence(@PathVariable Long licenceId) {
		Licence licence = Licence.find(licenceId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (licence != null) {
			resetLicence(licence);
			//AuthenticationService.updateActingUser(User.find(AuthenticationService.currentActingUser().getId()));
			map.put("result", true);
		} else {
			map.put("result", false);
		}
		return map;
	}
	
	@RequestMapping(value = "/licences/search", method = RequestMethod.GET)
	public String searchLicences() {
		return "licences/search";
	}
	
	@RequestMapping(value = "/licences/search", method = RequestMethod.POST)
	public String searchLicences(@RequestParam(value = "customerFirstName", defaultValue = "") String customerFirstName, @RequestParam(value = "customerLastName", defaultValue = "") String customerLastName, @RequestParam(value = "customerPhoneNumber", defaultValue = "") String customerPhoneNumber, Model model, HttpServletRequest request) {
		User user = AuthenticationService.currentActingUser();
		List<Licence> licences = Licence.findByCustomerDetails(user.getCompany().getId(), StringUtils.trim(customerFirstName), StringUtils.trim(customerLastName), StringUtils.trim(customerPhoneNumber));
		model.addAttribute("licences", licences);
		return "licences/search";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/licence/preview")
	public void licencePreview(@RequestParam("productId") long productId, @RequestParam("layout") String layout, HttpServletResponse response) throws Exception {
		User user = AuthenticationService.currentActingUser();
		Product product = Product.find(productId);
		if (product != null) {
			PageLayout pageLayout = PageLayout.valueOf(layout);
			String filename = product.getOneUpFilename();
			if (pageLayout == PageLayout.TWO_UP) {
				filename = product.getTwoUpFilename();
			} else if (pageLayout == PageLayout.A5) {
				filename = product.getA5Filename();
			}
			filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), filename);
			
			String tempFilename = URLEncoder.encode(product.getTitle(), "UTF-8");
			tempFilename = tempFilename + "_preview";
			tempFilename = tempFilename + "_" + DateTime.now().getMillis();
			response.addHeader("content-disposition", "attachment; filename=" + tempFilename + ".pdf");
			
			PreviewCreator previewCreator = new PreviewCreatorPdfImpl();
			String samplePageRange = product.getSamplePageRange();
			if (StringUtils.isBlank(samplePageRange)) {
				samplePageRange = application.getPdfSampleRange();
			}
			previewCreator.createPreview(filename, response.getOutputStream(), samplePageRange, user, product, pageLayout);
			Preview preview = new Preview();
			preview.setUser(user);
			preview.setCompany(user.getCompany());
			preview.setProduct(product);
			preview.setLayout(pageLayout);
			preview.persist();
			response.flushBuffer();
		}
		
	}
	
	@Transactional
	private void cancelLicence(Licence licence) {
		paperightCreditService.reimbersePaperightCredits(licence.getCompany(), licence.getCostInCredits());
		licence.setStatus(LicenceStatus.CANCELLED);
		licence.merge();
	}
	
	@Transactional
	private void resetLicence(Licence licence) {
		licence.setStatus(LicenceStatus.NEW);
		licence.merge();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/licence/statement")
	public void statement(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime toDate, HttpServletResponse response) throws Exception {
		User user = AuthenticationService.currentActingUser();
		response.setContentType("application/pdf");
		String filename = "statement_" + fromDate.toString("yyyy-MM-dd") + "_to_" + toDate.toString("yyyy-MM-dd") + ".pdf";
		response.addHeader("content-disposition", "attachment; filename=" + filename);
		licenceStatementService.generateLicenceStatement(user.getCompany().getId(), fromDate, toDate, response.getOutputStream());
		response.flushBuffer();
	}

}