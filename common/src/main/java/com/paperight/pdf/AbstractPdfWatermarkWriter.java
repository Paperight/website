package com.paperight.pdf;

import com.paperight.licence.PageLayout;
import com.paperight.licence.Watermark;
import com.paperight.licence.WatermarkWriter;


public abstract class AbstractPdfWatermarkWriter implements WatermarkWriter {

	private Watermark watermark;
	private PageLayout pageLayout;
	private boolean encrypt = true;
	
	protected float getOffset() {
		float offset = 0;
		if (!watermark.isSupportsAds()) {
			offset = 38;
		}
		return offset;
	}
	
	protected Watermark getWatermark() {
		return watermark;
	}
	
	protected void setWatermark(Watermark watermark) {
		this.watermark = watermark;
	}

	protected PageLayout getPageLayout() {
		return pageLayout;
	}

	protected void setPageLayout(PageLayout pageLayout) {
		this.pageLayout = pageLayout;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

}
