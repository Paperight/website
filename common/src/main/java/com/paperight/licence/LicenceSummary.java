package com.paperight.licence;

import java.math.BigDecimal;

public class LicenceSummary {
	
	private Long productId;
	private String productTitle;
	private Long numberOfTransactions;
	private Long numberOfCopies;
	private BigDecimal totalInCredits;
	private BigDecimal totalInCurrency;
	private BigDecimal totalOutletCharges;
	private String currencyCode;
	
	public Long getNumberOfTransactions() {
		return numberOfTransactions;
	}
	
	public void setNumberOfTransactions(Long numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public Long getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(Long numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public BigDecimal getTotalInCredits() {
		return totalInCredits;
	}

	public void setTotalInCredits(BigDecimal totalInCredits) {
		this.totalInCredits = totalInCredits;
	}

	public BigDecimal getTotalInCurrency() {
		return totalInCurrency;
	}

	public void setTotalInCurrency(BigDecimal totalInCurrency) {
		this.totalInCurrency = totalInCurrency;
	}

	public BigDecimal getTotalOutletCharges() {
		return totalOutletCharges;
	}

	public void setTotalOutletCharges(BigDecimal totalOutletCharges) {
		this.totalOutletCharges = totalOutletCharges;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public BigDecimal getTotalCharged() {
		BigDecimal totalCharged = getTotalInCurrency().add(getTotalOutletCharges());
		return totalCharged;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

}
