package com.paperight;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.paperight.pdf.PdfExecutorFactory.PdfExecutorType;

@Component
public class Application implements Serializable {
	
	private static final long serialVersionUID = -8862305854522318433L;
	
	private static final String PAPERIGHT_CREDIT_BASE_CURRENCY_CODE = "paperightCreditBaseCurrencyCode";
	private static final String PAPERIGHT_CREDIT_BASE_CURRENCY_CODE_DEFAULT_VALUE = "USD";
			
	private static final String PAPERIGHT_CREDIT_TO_BASE_CURRENCY_RATE = "paperightCreditToBaseCurrencyRate";
	private static final BigDecimal PAPERIGHT_CREDIT_TO_BASE_CURRENCY_RATE_DEFAULT_VALUE = new BigDecimal(1);		
	
	private static final String DEFAULT_CURRENCY_CODE = "defaultCurrencyCode";
	private static final String DEFAULT_CURRENCY_CODE_DEFAULT_VALUE = "ZAR";
	
	private static final String PDF_SAMPLE_RANGE = "pdfSampleRange";
	private static final String PDF_SAMPLE_RANGE_DEFAULT_VALUE = "20%";
	
	private static final String PUBLISHER_EARNING_PERCENT = "publisherEarningPercent";
	private static final int PUBLISHER_EARNING_PERCENT_DEFAULT_VALUE = 80;
	
	private static final String DEFAULT_OWNER_COMPANY_ID = "defaultOwnerCompanyId";
	private static final Long DEFAULT_OWNER_COMPANY_ID_DEFAULT_VALUE = 1L;
	
	private static final String DEFAULT_PDF_CONVERSION = "defaultPdfConversion";
	private static final String INVOICE_PDF_CONVERSION = "invoicePdfConversion";
	
	public String getPaperightCreditBaseCurrencyCode() {
		String value = getSetting(PAPERIGHT_CREDIT_BASE_CURRENCY_CODE);
		if (StringUtils.isBlank(value)) {
			value = PAPERIGHT_CREDIT_BASE_CURRENCY_CODE_DEFAULT_VALUE;
		}
		return value;
	}
	
	public void setPaperightCreditBaseCurrencyCode(String currencyCode) {
		setSetting(PAPERIGHT_CREDIT_BASE_CURRENCY_CODE, currencyCode);
	}
	
	public BigDecimal getPaperightCreditToBaseCurrencyRate() {
		String value = getSetting(PAPERIGHT_CREDIT_TO_BASE_CURRENCY_RATE);
		if (StringUtils.isBlank(value)) {
			return PAPERIGHT_CREDIT_TO_BASE_CURRENCY_RATE_DEFAULT_VALUE;
		} else {
			return new BigDecimal(value);
		}
	}
	
	public void setPaperightCreditToBaseCurrencyRate(BigDecimal rate) {
		setSetting(PAPERIGHT_CREDIT_TO_BASE_CURRENCY_RATE, "" + rate);
	}
	
	public String getDefaultCurrencyCode() {
		String value = getSetting(DEFAULT_CURRENCY_CODE);
		if (StringUtils.isBlank(value)) {
			value = DEFAULT_CURRENCY_CODE_DEFAULT_VALUE;
		}
		return value;
	}
	
	public void setDefaultCurrencyCode(String currencyCode) {
		setSetting(DEFAULT_CURRENCY_CODE, currencyCode);
	}
	
	public String getPdfSampleRange() {
		String value = getSetting(PDF_SAMPLE_RANGE);
		if (StringUtils.isBlank(value)) {
			value = PDF_SAMPLE_RANGE_DEFAULT_VALUE;
		}
		return value;
	}
	
	public void setPdfSampleRange(String pdfSampleRange) {
		setSetting(PDF_SAMPLE_RANGE, pdfSampleRange);
	}
	
	public int getPublisherEarningPercent() {
		String value = getSetting(PUBLISHER_EARNING_PERCENT);
		if (StringUtils.isBlank(value)) {
			value = "" + PUBLISHER_EARNING_PERCENT_DEFAULT_VALUE;
		}
		return Integer.valueOf(value);
	}
	
	public void setPublisherEarningPercent(int publisherEarningPercent) {
		setSetting(PUBLISHER_EARNING_PERCENT, "" + publisherEarningPercent);
	}
	
	public Long getDefaultOwnerCompanyId() {
		String value = getSetting(DEFAULT_OWNER_COMPANY_ID);
		if (StringUtils.isBlank(value)) {
			value = "" + DEFAULT_OWNER_COMPANY_ID_DEFAULT_VALUE;
		}
		return Long.valueOf(value);
	}
	
	public void setDefaultOwnerCompanyId(Long defaultOwnerCompanyId) {
		setSetting(DEFAULT_OWNER_COMPANY_ID, "" + defaultOwnerCompanyId);
	}
	
	public PdfExecutorType getDefaultPdfConversion() {
		PdfExecutorType value = null;
		try {
			value = PdfExecutorType.valueOf(getSetting(DEFAULT_PDF_CONVERSION));
		} catch (Exception e) {

		}
		if (value == null) {
			value = PdfExecutorType.DOCRAPTOR_TEST;
		}
		return value;
	}
	
	public void setDefaultPdfConversion(PdfExecutorType defaultPdfConversion) {
		setSetting(DEFAULT_PDF_CONVERSION, defaultPdfConversion.name());
	}
	
	public PdfExecutorType getInvoicePdfConversion() {
		PdfExecutorType value = null;
		try {
			value = PdfExecutorType.valueOf(getSetting(INVOICE_PDF_CONVERSION));
		} catch (Exception e) {
			
		}
		if (value == null) {
			value = PdfExecutorType.DOCRAPTOR_TEST;
		}
		return value;
	}
	
	public void setInvoicePdfConversion(PdfExecutorType defaultPdfConversion) {
		setSetting(INVOICE_PDF_CONVERSION, defaultPdfConversion.name());
	}
	
	@Cacheable(value = "applicationSettings", key = "#name")
	private String getSetting(String name) {
		ApplicationSetting applicationSetting = ApplicationSetting.find(name);
		if (applicationSetting == null) {
			return null;
		} else {
			return applicationSetting.getValue();
		}
	}
	
	@CacheEvict(value = "applicationSettings", key = "#name")
	private void setSetting(String name, String value) {
		ApplicationSetting applicationSetting = ApplicationSetting.find(name);
		if (applicationSetting == null) {
			applicationSetting = new ApplicationSetting();
			applicationSetting.setName(name);
		}
		applicationSetting.setValue(value);
		applicationSetting.merge();
	}

}
