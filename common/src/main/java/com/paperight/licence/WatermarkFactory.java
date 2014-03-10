package com.paperight.licence;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.product.Product;
import com.paperight.user.User;


@Configurable
public class WatermarkFactory {
	
	@Autowired
	private TrackingUrlCreator trackingUrlCreator;	
	
	private static TrackingUrlCreator trackingUrlCreator() {
		WatermarkFactory factory = new WatermarkFactory();
		return factory.trackingUrlCreator;
	}
	
	public static Watermark create() {
		return new Watermark();
	}
	
	public static Watermark fromLicence(Licence licence) {
		Watermark watermark = create();
		watermark.setDocumentTitle(licence.getProduct().getTitle());
		watermark.setDocumentRightsHolder(licence.getProduct().getPublisher());
		watermark.setOutletName(licence.getCompany().getName());
		watermark.setCustomerName(StringUtils.trim(licence.getCustomerFullName() + " " + licence.getCustomerPhoneNumber()));
		watermark.setTransactionDate(licence.getCreatedDate());
		String licenceStatement = licence.getProduct().getLicenceStatement();
		if (StringUtils.isBlank(licenceStatement)) {
			licenceStatement = "Make copies only with a licence from Paperight.com";
		}
		watermark.setAdditionalText(licenceStatement);
		watermark.setUrl(licence.getTrackingUrl());
		watermark.setSupportsAds(licence.getProduct().isSupportsAds());
		return watermark;
	}
	
	public static Watermark create(User user, Product product) {
		Watermark watermark = create();
		watermark.setDocumentTitle(product.getTitle());
		watermark.setDocumentRightsHolder(product.getPublisher());
		watermark.setOutletName(user.getCompany().getName());
		watermark.setCustomerName("Paperight Sample");
		watermark.setTransactionDate(new DateTime());
		watermark.setAdditionalText("Purchase a licence from Paperight.com");
		watermark.setUrl(trackingUrlCreator().execute(product));
		watermark.setSupportsAds(product.isSupportsAds());
		return watermark;
	}

}
