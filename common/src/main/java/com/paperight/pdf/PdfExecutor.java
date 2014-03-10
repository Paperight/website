package com.paperight.pdf;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;

public abstract class PdfExecutor {

	public abstract void execute(InputStream html, OutputStream out) throws Exception;
	
	public abstract File execute(String htmlLocation, List<String> cssFiles) throws Exception;
	
	protected String getNewPdfFilename(String htmlLocation) {
		String filename = htmlLocation.substring( htmlLocation.lastIndexOf('/')+1, htmlLocation.length() );
		if (filename.lastIndexOf('.') > -1) {
			filename = filename.substring(0, filename.lastIndexOf('.'));
		}
		filename = filename + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + ".pdf";
		filename = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename);
		return filename;
	}

}
