package com.paperight.licence;

public enum PageLayout {

	ONE_UP("One Up A4"), 
	TWO_UP("Two Up A4"),
	A5("A5"),
	PHOTOCOPY("Photocopy");
	
	private String displayName;
	
	private PageLayout(String displayName) {
		setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
