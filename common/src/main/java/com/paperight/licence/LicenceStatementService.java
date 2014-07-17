package com.paperight.licence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.paperight.Application;
import com.paperight.currency.CurrencyService;
import com.paperight.currency.VatRate;
import com.paperight.pdf.PdfExecutor;
import com.paperight.pdf.PdfExecutorFactory;
import com.paperight.user.Company;

@Service
public class LicenceStatementService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${base.url}")
	private String applicationUrl;
	
	@Autowired
	private Application application;
	
	@Autowired
	private CurrencyService currencyService;
	
	public void generateLicenceStatement(Long companyId, DateTime fromDate, DateTime toDate, OutputStream outputStream) throws Exception {
		toDate = toDate.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue();
		String invoiceHtml = generateLicenceStatementHtml(companyId, fromDate, toDate);
		generatePdf(invoiceHtml, outputStream);
	}
	
	private void generatePdf(String html, OutputStream outputStream) throws Exception {
		InputStream inputStream = IOUtils.toInputStream(html);
		PdfExecutor pdfExecutor = PdfExecutorFactory.create(application.getInvoicePdfConversion());
		pdfExecutor.execute(inputStream, outputStream);
	}
	
	private String generateLicenceStatementHtml(Long companyId, DateTime fromDate, DateTime toDate) throws IOException {
		Map<String, Object> model = buildLicenceStatementModel(companyId, fromDate, toDate);
		return populateLicenceStatementTemplate(model);
	}
	
	private Map<String, Object> buildLicenceStatementModel(Long companyId, DateTime fromDate, DateTime toDate) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<Licence> licences = Licence.findByCompanyIdAndStatus(companyId, LicenceStatus.DOWNLOADED, fromDate, toDate);
		sortLicences(licences);
		VatRate vatRate = VatRate.findByCountryCode("ZA");
		LicenceBreakDownList licenceBreakDownList = calculateLicenceBreakDowns(licences, vatRate);
		BigDecimal totalLicenceFees = licenceBreakDownList.getTotalLicenceFees();//.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal totalLicenceFeesVat = licenceBreakDownList.getTotalLicenceFeesVat();
		model.put("totalLicenceFees", totalLicenceFees);
		model.put("totalLicenceFeesVat", totalLicenceFeesVat);
		model.put("fromDate", fromDate);
		model.put("toDate", toDate);
		model.put("vatRate", vatRate);
		model.put("licences", licences);
		model.put("toCompany", Company.find(companyId));

		model.put("applicationUrl", applicationUrl);
		return model;
	}
	
	private void sortLicences(List<Licence> licences) {
		Collections.sort(licences, new Comparator<Licence>() {

			@Override
			public int compare(Licence licence, Licence otherLicence) {
				return licence.getTransactionDate().compareTo(otherLicence.getTransactionDate());
			}
			
		});
	}
	
	private LicenceBreakDownList calculateLicenceBreakDowns(List<Licence> licences, VatRate vatRate) {
		LicenceBreakDownList LicenceBreakDownList = new LicenceBreakDownList();
		for (Licence licence : licences) {
			LicenceBreakDownList.add(calculateLicenceBreakDown(licence, vatRate));
		}
		return LicenceBreakDownList;
	}
	
	private LicenceBreakDown calculateLicenceBreakDown(Licence licence, VatRate vatRate) {
		LicenceBreakDown licenceBreakDown = new LicenceBreakDown();
		
		BigDecimal licenceFee = licence.getRandsInvoiceValue();
		BigDecimal licenceFeeVat = BigDecimal.ZERO;
		if (currencyService.isLiableForVat(licence.getOwnerCompany(), licence.getTransactionDate())) {
			licenceFeeVat = currencyService.backCalculateVat(licenceFee, vatRate.getRate0());
		}
		
		licenceBreakDown.setLicenceFee(licenceFee);
		licenceBreakDown.setLicenceFeeVat(licenceFeeVat);
		
		return licenceBreakDown;
	}
	
	private String populateLicenceStatementTemplate(Map<String, Object> model) throws IOException {
		String templateContents = getLicenceStatementTemplateContent();
		StringWriter writer = new StringWriter();
		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.evaluate(velocityContext, writer, this.getClass().getName(), templateContents);
		return writer.toString();
	}
	
	private String getLicenceStatementTemplateContent() throws IOException {
		String templateContent =  getDefaulLicenceTemplateContent();
		//String templateContent =  contentService.getSnippetValue("content", "outlet-statement-template", getDefaultPublisherStatementTemplateContent(), true); 
		return wrapTemplateContent(templateContent); 
	}
	
	protected String getDefaulLicenceTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/outletStatement.vm");
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

class LicenceBreakDownList extends ArrayList<LicenceBreakDown> {

	private static final long serialVersionUID = 1L;

	public BigDecimal getTotalLicenceFees() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (LicenceBreakDown licenceBreakDown : this) {
			result = result.add(licenceBreakDown.getLicenceFee());
		}
		return result;
	}
	
	public BigDecimal getTotalLicenceFeesVat() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (LicenceBreakDown licenceBreakDown : this) {
			result = result.add(licenceBreakDown.getLicenceFeeVat());
		}
		return result;
	}
	
}

class LicenceBreakDown {
	
	private BigDecimal licenceFee;
	private BigDecimal licenceFeeVat;
	
	public BigDecimal getLicenceFee() {
		return licenceFee;
	}
	
	public void setLicenceFee(BigDecimal licenceFee) {
		this.licenceFee = licenceFee;
	}

	public BigDecimal getLicenceFeeVat() {
		return licenceFeeVat;
	}

	public void setLicenceFeeVat(BigDecimal licenceFeeVat) {
		this.licenceFeeVat = licenceFeeVat;
	}
	
}