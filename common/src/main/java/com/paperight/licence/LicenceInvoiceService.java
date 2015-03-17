package com.paperight.licence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.paperight.Application;
import com.paperight.content.ContentService;
import com.paperight.currency.CurrencyService;
import com.paperight.currency.VatRate;
import com.paperight.licence.Licence.InvoiceState;
import com.paperight.mvc.tags.PriceTag;
import com.paperight.pdf.PdfExecutor;
import com.paperight.pdf.PdfExecutorFactory;
import com.paperight.publisherearning.PublisherEarning;
import com.paperight.user.Company;

@Service
public class LicenceInvoiceService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${base.url}")
	private String applicationUrl;
	
	@Autowired
	private Application application;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private CurrencyService currencyService;
	
	public void generateOutletInvoice(Licence licence, OutputStream outputStream) throws Exception {
		String invoiceHtml = generateOutletInvoiceHtml(licence);
		generatePdf(invoiceHtml, outputStream);
	}
	
	public void generateOutletInvoice(Licence licence, String outputFilename) throws Exception {
	    OutputStream out = new FileOutputStream(outputFilename);
        try {
            generateOutletInvoice(licence, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
	
	public void generateLicenceInvoices(List<Licence> licences, String baseFolder, String timeStamp) {
		for (Licence licence : licences) {
			if (licence != null) {
				generateLicenceInvoice(licence, baseFolder, timeStamp);
			}
		}
	}
	
	public void generateEarningsInvoices(List<PublisherEarning> publisherEarnings, String baseFolder, String timeStamp) {
		for (PublisherEarning publisherEarning : publisherEarnings) {
			if (publisherEarning.getLicence() != null) {
				generateLicenceInvoice(publisherEarning.getLicence(), baseFolder, timeStamp);
			}
		}
	}

	private void generateLicenceInvoice(Licence licence, String baseFolder, String timeStamp) {
		try {
			File file = new File(baseFolder, "invoice_" + licence.getId() + "_" + timeStamp + ".pdf");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			this.generateOutletInvoice(licence, fileOutputStream);
			licence.setInvoiceState(InvoiceState.DOWNLOADED);
			licence.merge();
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			logger.error("Error generating invoice", e);
		}
	}
	
	private void generatePdf(String html, OutputStream outputStream) throws Exception {
		InputStream inputStream = IOUtils.toInputStream(html);
		PdfExecutor pdfExecutor = PdfExecutorFactory.create(application.getInvoicePdfConversion());
		pdfExecutor.execute(inputStream, outputStream);
	}
	
	private String generateOutletInvoiceHtml(Licence licence) throws IOException {
		Map<String, Object> model = buildOutletInvoiceModel(licence);
		return populateOutletInvoiceTemplate(model);
	}
	
	private Map<String, Object> buildOutletInvoiceModel(Licence licence) {
		Map<String, Object> model = new HashMap<String, Object>();
		Company fromCompany = licence.getOwnerCompany();
		if (fromCompany == null) {
			fromCompany = Company.find(application.getDefaultOwnerCompanyId());
		}
		VatRate vatRate = VatRate.findByCountryCode(fromCompany.getPrimaryAddress().getCountryCode());
		boolean mustIncludeVat = mustIncludeVat(licence, fromCompany, vatRate);
		model.put("mustIncludeVat", mustIncludeVat);
		if (mustIncludeVat) {
			BigDecimal vatRatePercentage = vatRate.getRate0();
			BigDecimal vatAmount = currencyService.backCalculateVat(licence.getRandsInvoiceValue(), vatRatePercentage);
			model.put("vatRatePercentage", vatRatePercentage);
			model.put("vatAmount", vatAmount);
		}
		boolean showOutletCurrency = showOutletCurrency(licence);
		model.put("showOutletCurrency", showOutletCurrency);
		if (showOutletCurrency) {
			String costInOutletCurrency = getCostInOutletCurrency(licence);
			model.put("costInOutletCurrency", costInOutletCurrency);
		}
		model.put("licence", licence);
		model.put("fromCompany", fromCompany);
		model.put("applicationUrl", applicationUrl);
		return model;
	}
	
	private String getCostInOutletCurrency(Licence licence) {
		PriceTag priceTag = new PriceTag();
		return priceTag.buildPriceTag(licence.getCurrency(), licence.getCostInCurrency());
	}
	
	private boolean showOutletCurrency(Licence licence) {
		boolean result = false;
		if (!StringUtils.equals(currencyService.getInvoiceCurrency().getCode(), licence.getCurrencyCode())) {
			result = true;
		}
		return result;
	}
	
	private boolean mustIncludeVat(Licence licence, Company fromCompany, VatRate vatRate) {
		boolean result = false;
		if (currencyService.isLiableForVat(fromCompany, licence.getTransactionDate())) {
			if (StringUtils.equalsIgnoreCase(fromCompany.getPrimaryAddress().getCountryCode(), "ZA")) {
				result = true;
			} else if (StringUtils.equalsIgnoreCase(licence.getCompany().getPrimaryAddress().getCountryCode(), fromCompany.getPrimaryAddress().getCountryCode())) {
				if (vatRate != null) {
					result = true;
				}
			}
			
		}
		return result;
	}
		
	
	private String populateOutletInvoiceTemplate(Map<String, Object> model) throws IOException {
		String templateContents = getOutletInvoiceTemplateContent();
		StringWriter writer = new StringWriter();
		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.evaluate(velocityContext, writer, this.getClass().getName(), templateContents);
		return writer.toString();
	}
		
	private String getOutletInvoiceTemplateContent() throws IOException {
		String templateContent =  getDefaultOutletInvoiceTemplateContent();
		//String templateContent =  contentService.getSnippetValue("content", "outlet-invoice-template", getDefaultOutletInvoiceTemplateContent(), true); 
		return wrapTemplateContent(templateContent); 
	}
	
	protected String getDefaultOutletInvoiceTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/outletInvoice.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	private String wrapTemplateContent(String templateContent) throws IOException {
		Resource headerResource = new ClassPathResource("/velocity/header.vm");
		String header = IOUtils.toString(headerResource.getInputStream());
		
		Resource footerResource = new ClassPathResource("/velocity/footer.vm");
		String footer = IOUtils.toString(footerResource.getInputStream());
		return header + templateContent + footer;
	}
	
	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
}
