package com.paperight.publisherearning;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class PublisherEarningStatementService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${base.url}")
	private String applicationUrl;
	
	@Autowired
	private Application application;
	
	@Autowired
	private CurrencyService currencyService;
	
	public void generatePublisherStatement(Long companyId, DateTime fromDate, DateTime toDate, OutputStream outputStream) throws Exception {
		toDate = toDate.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue();
		String invoiceHtml = generatePublisherStatementHtml(companyId, fromDate, toDate);
		generatePdf(invoiceHtml, outputStream);
	}
	
	private void generatePdf(String html, OutputStream outputStream) throws Exception {
		InputStream inputStream = IOUtils.toInputStream(html);
		PdfExecutor pdfExecutor = PdfExecutorFactory.create(application.getInvoicePdfConversion());
		pdfExecutor.execute(inputStream, outputStream);
	}
	
	private String generatePublisherStatementHtml(Long companyId, DateTime fromDate, DateTime toDate) throws IOException {
		Map<String, Object> model = buildPublisherStatementModel(companyId, fromDate, toDate);
		return populatePublisherStatementTemplate(model);
	}
	
	private Map<String, Object> buildPublisherStatementModel(Long companyId, DateTime fromDate, DateTime toDate) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<PublisherEarning> publisherEarnings = PublisherEarning.findByCompanyId(companyId, fromDate, toDate);
		List<PublisherPaymentRequest> paidPaymentRequests = PublisherPaymentRequest.findByCompanyIdAndStatus(companyId, PublisherPaymentRequestStatus.PAID, fromDate, toDate);
		VatRate vatRate = VatRate.findByCountryCode("ZA");
		Company toCompany = Company.find(companyId);
		PublisherEarningBreakDownList publisherEarningBreakDownList = calculatePublisherEarningBreakDowns(publisherEarnings, toCompany, vatRate);
		BigDecimal totalLicenceFees = publisherEarningBreakDownList.getTotalLicenceFees();//.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal totalLicenceFeesVat = publisherEarningBreakDownList.getTotalLicenceFeesVat();
		BigDecimal totalPublisherAmount = publisherEarningBreakDownList.getTotalPublisherAmount();//.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal totalPaperightCommission = publisherEarningBreakDownList.getTotalPaperightCommission(); //totalLicenceFees.subtract(totalPublisherAmount).setScale(2);
		BigDecimal totalVatOnCommission = publisherEarningBreakDownList.getTotalVatOnCommission(); //currencyService.backCalculateVat(totalPaperightCommission, vatRate.getRate0());
		model.put("totalLicenceFees", totalLicenceFees);
		model.put("totalLicenceFeesVat", totalLicenceFeesVat);
		model.put("totalPublisherAmount", totalPublisherAmount);
		model.put("totalPaperightCommission", totalPaperightCommission);
		model.put("totalVatOnCommission", totalVatOnCommission);
		model.put("paidPaymentRequests", paidPaymentRequests);
		model.put("fromDate", fromDate);
		model.put("toDate", toDate);
		model.put("vatRate", vatRate);
		model.put("publisherEarnings", publisherEarnings);
		model.put("toCompany", toCompany);

		model.put("applicationUrl", applicationUrl);
		return model;
	}
	
	private PublisherEarningBreakDownList calculatePublisherEarningBreakDowns(List<PublisherEarning> publisherEarnings, Company toCompany, VatRate vatRate) {
		PublisherEarningBreakDownList publisherEarningBreakDowns = new PublisherEarningBreakDownList();
		for (PublisherEarning publisherEarning : publisherEarnings) {
			publisherEarningBreakDowns.add(calculatePublisherEarningBreakDown(publisherEarning, toCompany, vatRate));
		}
		return publisherEarningBreakDowns;
	}
	
	private PublisherEarningBreakDown calculatePublisherEarningBreakDown(PublisherEarning publisherEarning, Company toCompany, VatRate vatRate) {
		PublisherEarningBreakDown publisherEarningBreakDown = new PublisherEarningBreakDown();
		
		BigDecimal licenceFee = publisherEarning.getLicence().getRandsInvoiceValue();
		BigDecimal licenceFeeVat = BigDecimal.ZERO;
		if (currencyService.isLiableForVat(publisherEarning.getCompany(), publisherEarning.getCreatedDate())) {
			licenceFeeVat = currencyService.backCalculateVat(licenceFee, vatRate.getRate0());
		}
		BigDecimal publisherAmount = publisherEarning.getPublisherRandAmount();
		BigDecimal paperightCommission = licenceFee.subtract(publisherAmount);
		BigDecimal vatOnCommission = currencyService.backCalculateVat(paperightCommission, vatRate.getRate0());
		
		publisherEarningBreakDown.setLicenceFee(licenceFee);
		publisherEarningBreakDown.setLicenceFeeVat(licenceFeeVat);
		publisherEarningBreakDown.setPublisherAmount(publisherAmount);
		publisherEarningBreakDown.setPaperightCommission(paperightCommission);
		publisherEarningBreakDown.setVatOnCommission(vatOnCommission);
		
		return publisherEarningBreakDown;
	}
	
	private String populatePublisherStatementTemplate(Map<String, Object> model) throws IOException {
		String templateContents = getPublisherStatementTemplateContent();
		StringWriter writer = new StringWriter();
		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.evaluate(velocityContext, writer, this.getClass().getName(), templateContents);
		return writer.toString();
	}
	
	private String getPublisherStatementTemplateContent() throws IOException {
		String templateContent =  getDefaultPublisherStatementTemplateContent();
		//String templateContent =  contentService.getSnippetValue("content", "publisher-statement-template", getDefaultPublisherStatementTemplateContent(), true); 
		return wrapTemplateContent(templateContent); 
	}
	
	protected String getDefaultPublisherStatementTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/publisherStatement.vm");
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

class PublisherEarningBreakDownList extends ArrayList<PublisherEarningBreakDown> {
	
	private static final long serialVersionUID = 1L;

	public BigDecimal getTotalLicenceFees() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarningBreakDown publisherEarningBreakDown : this) {
			result = result.add(publisherEarningBreakDown.getLicenceFee());
		}
		return result;
	}
	
	public BigDecimal getTotalLicenceFeesVat() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarningBreakDown publisherEarningBreakDown : this) {
			result = result.add(publisherEarningBreakDown.getLicenceFeeVat());
		}
		return result;
	}
	
	public BigDecimal getTotalPublisherAmount() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarningBreakDown publisherEarningBreakDown : this) {
			result = result.add(publisherEarningBreakDown.getPublisherAmount());
		}
		return result;
	}
	
	public BigDecimal getTotalPaperightCommission() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarningBreakDown publisherEarningBreakDown : this) {
			result = result.add(publisherEarningBreakDown.getPaperightCommission());
		}
		return result;
	}

	public BigDecimal getTotalVatOnCommission() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarningBreakDown publisherEarningBreakDown : this) {
			result = result.add(publisherEarningBreakDown.getVatOnCommission());
		}
		return result;
	}
	
}

class PublisherEarningBreakDown {
	
	private BigDecimal licenceFee;
	private BigDecimal licenceFeeVat;
	private BigDecimal publisherAmount;
	private BigDecimal paperightCommission;
	private BigDecimal vatOnCommission;
	
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

	public BigDecimal getPublisherAmount() {
		return publisherAmount;
	}

	public void setPublisherAmount(BigDecimal publisherAmount) {
		this.publisherAmount = publisherAmount;
	}

	public BigDecimal getPaperightCommission() {
		return paperightCommission;
	}

	public void setPaperightCommission(BigDecimal paperightCommission) {
		this.paperightCommission = paperightCommission;
	}

	public BigDecimal getVatOnCommission() {
		return vatOnCommission;
	}

	public void setVatOnCommission(BigDecimal vatOnCommission) {
		this.vatOnCommission = vatOnCommission;
	}
	
}