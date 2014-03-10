package com.paperight.licence;

public enum PageLayout {

	ONE_UP("One Up"), 
	TWO_UP("Two Up"),
	A5("A5");
	
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
