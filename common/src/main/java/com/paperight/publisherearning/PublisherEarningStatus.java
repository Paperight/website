package com.paperight.publisherearning;

public enum PublisherEarningStatus {
	
	PENDING("Pending"),
	REQUESTED("Requested"),
	PAID("Paid");
	
	private String displayName;
	
	private PublisherEarningStatus(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
