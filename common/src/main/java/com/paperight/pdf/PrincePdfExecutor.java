package com.paperight.pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.princexml.Prince;

@Configurable
public class PrincePdfExecutor extends PdfExecutor {
	
	@Value("${princepdf.executable.location}")
	private String princePdfExecutableLocation;
	
	@Value("${html.pdf.css.folder:none}")
	private String htmlToPdfCssFolder;
	
	@Value("${html.folder:none}")
	private String htmlFolder;
	
	@Override
	public void execute(InputStream html, OutputStream out) throws IOException, InterruptedException {
		Prince prince = new Prince(princePdfExecutableLocation);
		prince.setInputType("html");
		prince.convert(html, out);
	}
	
	@Override
	public File execute(String htmlLocation, List<String> cssFiles) throws IOException, InterruptedException {
		Prince prince = new Prince(princePdfExecutableLocation);
		
		String newPdfFilename = getNewPdfFilename(htmlLocation);
		if (!StringUtils.startsWith(htmlLocation, "http")) {
			htmlLocation = FilenameUtils.concat(htmlFolder, htmlLocation);
			File f = new File(htmlLocation);
			if(!f.exists()) { 
				throw new IOException("File '" + htmlLocation + "' not found");
			}
			htmlLocation = "file://" + htmlLocation;
		}
		for (String cssFile : cssFiles) {
			prince.addStyleSheet(FilenameUtils.concat(htmlToPdfCssFolder, cssFile));
		}		
		prince.convert(htmlLocation, newPdfFilename);
		
        return new File(newPdfFilename);
	}
	
}
