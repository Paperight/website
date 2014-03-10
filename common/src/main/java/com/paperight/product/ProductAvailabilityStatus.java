package com.paperight.product;

public enum ProductAvailabilityStatus {

	ON_SALE("On Sale"),
	EMBARGOED("Embargoed");
	
	private String displayName;
	
	private ProductAvailabilityStatus(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
