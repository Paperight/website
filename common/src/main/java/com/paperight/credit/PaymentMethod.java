package com.paperight.credit;

public enum PaymentMethod {

	EFT("EFT"),
	PAYPAL("Paypal");
	
	private String displayName;
	
	private PaymentMethod(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
