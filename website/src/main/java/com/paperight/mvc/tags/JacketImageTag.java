package com.paperight.mvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.paperight.product.Product;

public class JacketImageTag extends SimpleTagSupport {

	private Product product;
	private Long productId;
	private String title;
	private int width = -1;
	private int height = -1;
	private int quality = 100;
	private String cssClass;
	private String alt;
	private String customImageUrl;

	public void doTag() throws JspException, IOException {
		if (productId != null) {
			product = Product.find(productId);
		}
		if (product == null) {
			throw new JspException("Product or ProductId must be specified");
		}
		if (StringUtils.isEmpty(title)) {
			title = product.getTitle();
		}
		if (StringUtils.isEmpty(alt)) {
			alt = product.getTitle();
		}
		String image = "<img";
		if (hasCustomImageUrl()) {
			image += " width=\"100\"";
		}
		image += " src=\"" + getImageSrc() + "\"";
		image += " alt=\"" + StringEscapeUtils.escapeJavaScript(alt) + "\"";
		image += " title=\"" + StringEscapeUtils.escapeJavaScript(product.getTitle()) + "\"";
		image += "/>";
		getJspContext().getOut().println(image);
	}

	private String getImageSrc() {
		if (hasCustomImageUrl()) {
			return getCustomImageUrl();
		} else {
			String url = product.getJacketImageUrl();
			if (width > -1 && height > -1) {
				url = url + "/w" + width + "h" + height + ".jpg";
			}
			return url;
		}
	}
	
	private boolean hasCustomImageUrl() {
		return !StringUtils.isBlank(getCustomImageUrl());
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCustomImageUrl() {
		return customImageUrl;
	}

	public void setCustomImageUrl(String customImageUrl) {
		this.customImageUrl = customImageUrl;
	}

}