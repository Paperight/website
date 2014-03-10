package com.paperight.licence;

import org.joda.time.DateTime;

public class Watermark {

	private String documentTitle;
	private String documentRightsHolder;
	private String outletName;
	private String customerName;
	private DateTime transactionDate;
	private String url;
	private String additionalText;
	private boolean supportsAds;
	
	public String getDocumentTitle() {
		return documentTitle;
	}
	
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getDocumentRightsHolder() {
		return documentRightsHolder;
	}

	public void setDocumentRightsHolder(String documentRightsHolder) {
		this.documentRightsHolder = documentRightsHolder;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public DateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(DateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdditionalText() {
		return additionalText;
	}

	public void setAdditionalText(String additionalText) {
		this.additionalText = additionalText;
	}

	public boolean isSupportsAds() {
		return supportsAds;
	}

	public void setSupportsAds(boolean supportsAds) {
		this.supportsAds = supportsAds;
	}
	
}
