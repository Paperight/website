package com.paperight.licence;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.paperight.product.Product;
import com.rosaloves.bitlyj.Url;

@Component
public class TrackingUrlCreatorBitlyImpl extends TrackingUrlCreator {
	
	private Logger logger = LoggerFactory.getLogger(TrackingUrlCreatorBitlyImpl.class);
	
	@Value("${bitly.username}")
	private String username;
	
	@Value("${bitly.api.key}")
	private String apiKey;
		
	@Override
	public String execute(Licence licence) {
		String trackingUrl = licence.getProduct().getUrl();
		if (StringUtils.isBlank(trackingUrl)) {
			trackingUrl = defaultUrl();
		}
		if (StringUtils.contains(trackingUrl, "?")) {
			trackingUrl = trackingUrl + "&";
		} else {
			trackingUrl = trackingUrl + "?";
		}
		trackingUrl = trackingUrl + additionalUrlMetadata(licence);
		try {
			trackingUrl = URIUtil.encodeQuery(trackingUrl);
		} catch (URIException e1) {
		}
		String callToAction = licence.getProduct().getUrlCallToAction();
		if (StringUtils.isBlank(callToAction)) {
			callToAction = defaultCallToAction();
		}
		try {
			Url url = as(username, apiKey).call(shorten(trackingUrl));
			return callToAction + url.getShortUrl();
		} catch (Exception e) {
			logger.error("Unable to bit.ly tracking url for licence " + licence.getId() , e);
			return defaultTrackingUrl();
		}
	}
	
	@Override
	public String execute(Product product) {
		String trackingUrl = product.getUrl();
		if (StringUtils.isBlank(trackingUrl)) {
			trackingUrl = defaultUrl();
		}
		if (StringUtils.contains(trackingUrl, "?")) {
			trackingUrl = trackingUrl + "&";
		} else {
			trackingUrl = trackingUrl + "?";
		}
		trackingUrl = trackingUrl + additionalUrlMetadata(product);
		try {
			trackingUrl = URIUtil.encodeQuery(trackingUrl);
		} catch (URIException e1) {
		}
		String callToAction = product.getUrlCallToAction();
		if (StringUtils.isBlank(callToAction)) {
			callToAction = defaultCallToAction();
		}
		try {
			Url url = as(username, apiKey).call(shorten(trackingUrl));
			return callToAction + url.getShortUrl();
		} catch (Exception e) {
			logger.error("Unable to bit.ly tracking url for product " + product.getId() , e);
			return defaultTrackingUrl();
		}
	}
	
}
