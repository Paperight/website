package com.paperight.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ThirdPartyProduct extends Product {

	private static final long serialVersionUID = 6533590818987372668L;

	private String detailPageUrl;
	private String imageUrl = "/img/third_party_default_jacket_w100h130.jpg";
	private List<String> contributors = new ArrayList<String>();
	
	public ThirdPartyProduct() {
		super();
	}

	public String getDetailPageUrl() {
		return detailPageUrl;
	}

	public void setDetailPageUrl(String detailPageUrl) {
		this.detailPageUrl = detailPageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}	
	
	public void setPriceInCents(int priceInCents) {
		setLicenceFeeInDollars(new BigDecimal(priceInCents).divide(new BigDecimal(100)));
	}
	
	public int getPriceInCents() {
		return getLicenceFeeInDollars().multiply(new BigDecimal(100)).intValue();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Transient
	public List<String> getContributors() {
		return contributors;
	}
	
	public void setContributors(List<String> contributors) {
		this.contributors = contributors;
	}
	
	@Override
	public String getPrimaryCreators() {
		List<String> contributors = getContributors();
		if (contributors != null) {
			String primaryCreators = "";
			for (String contributor : contributors) {
				if (!StringUtils.isBlank(primaryCreators)) {
					primaryCreators = primaryCreators + ", ";
				}
				primaryCreators = primaryCreators + contributor;
			}
			return primaryCreators;
		}
		return super.getPrimaryCreators();
	}
	
}
