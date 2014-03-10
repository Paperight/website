package com.paperight.licence;

import java.io.OutputStream;

public interface WatermarkWriter {

	public void writeWatermark(String inputFile, OutputStream outputStream,	Watermark watermark, PageLayout layout) throws Exception;

	public void writeWatermark(String inputFile, String outputFile,	Watermark watermark, PageLayout layout) throws Exception;
	
}