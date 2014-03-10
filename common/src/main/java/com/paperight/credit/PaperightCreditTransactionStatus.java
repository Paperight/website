package com.paperight.credit;

public enum PaperightCreditTransactionStatus {

	PENDING("Pending"), 
	PAID("Paid"),
	CANCELLED("Cancelled");
	
	private String displayName;
	
	private PaperightCreditTransactionStatus(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
