package com.paperight.publisherearning;

public enum PublisherPaymentRequestStatus {
	
	PENDING("Pending"),
	CANCELLATION_REQUESTED("Cancellation Requested"),
	CANCELLED("Cancelled"),
	PAID("Paid");
	
	private String displayName;
	
	private PublisherPaymentRequestStatus(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
