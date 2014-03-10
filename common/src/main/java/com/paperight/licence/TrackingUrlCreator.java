package com.paperight.licence;

import com.paperight.product.Product;


public class TrackingUrlCreator {
	
	protected String defaultUrl() {
		return "http://www.facebook.com/paperight";
	}
	
	protected String defaultCallToAction() {
		return "Get more from your reading, go to: ";
	}

	public String execute(Licence licence) {
		return defaultTrackingUrl();
	}
	
	public String execute(Product product) {
		return defaultTrackingUrl();
	}
	
	protected String defaultTrackingUrl() {
		return "Meet other readers on Facebook: go to http://on.fb.me/paperluv";
	}
	
	protected String additionalUrlMetadata(Licence licence) {
		String result;
		result = "licenceId=" + licence.getId().toString();
		result = result + "&" + additionalUrlMetadata(licence.getProduct());
		result = result + "&customer_name=" + licence.getCustomerFullName();
		result = result + "&customer_phoneNumber=" + licence.getCustomerPhoneNumber();
		return result;
	}
	
	protected String additionalUrlMetadata(Product product) {
		return "product_title= " + product.getTitle();
	}
}
