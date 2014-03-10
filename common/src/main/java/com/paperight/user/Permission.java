package com.paperight.user;

public enum Permission {
	
	IMPERSONATE_USER("Can impersonate other users"),
	POSTERS("Can edit homepage thumbnails (posters)"),
	EDIT_PAPERIGHT_STAFF("Can add and edit users and permissions"),
	AUTHORISE_TOPUPS("Can authorise credit top ups"),
	PRODUCT_OWNERSHIP("Can assign/unassign ownership of documents"),
	CONFIGURATION("Can change configuration options"),
	SNIPPETS("Can edit snippets"),
	ARTICLES("Can edit articles"),
	PROCESS_PUBLISHER_PAYMENT_REQUESTS("Can process publisher payment requests"),
	EDIT_VAT_RATES("Can edit VAT rates"),
	PDF_GENERATION_CONFIGURATION("Can change HTML conversion PDF generator"),
	INVOICE_PDF_GENERATION("Can change invoice PDF generator"),
	EDIT_COMPANY_CREDITS("Can edit company credits");
	
	private String displayName;
	
	private Permission(String displayName) {
		this.setDisplayName(displayName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}