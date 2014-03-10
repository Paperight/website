package com.paperight.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class WkHtmlToPdfExecutor {
	
	private Logger logger = LoggerFactory.getLogger(WkHtmlToPdfExecutor.class);
	
	@Value("${wkhtmltopdf.executable.location}")
	private String wkhtmltopdfExecutableLocation;
	
	@Value("${html.pdf.css.folder}")
	private String htmlToPdfCssFolder;
	
	@Value("${html.folder}")
	private String htmlFolder;
	
	

	public File execute(String htmlLocation, String cssFile) throws IOException, InterruptedException {
		String newPdfFilename = getNewPdfFilename(htmlLocation);
		if (!StringUtils.startsWith(htmlLocation, "http")) {
			htmlLocation = FilenameUtils.concat(htmlFolder, htmlLocation);
			File f = new File(htmlLocation);
			if(!f.exists()) { 
				throw new IOException("File '" + htmlLocation + "' not found");
			}
			htmlLocation = "file:///" + htmlLocation;
		}
		//String[] cmd = { wkhtmltopdfExecutableLocation,"--user-style-sheet", "\"file:///" + FilenameUtils.concat(htmlToPdfCssFolder, cssFile) + "\"", htmlLocation, newPdfFilename };
		//String cmd = wkhtmltopdfExecutableLocation + " --user-style-sheet " + "\"" + FilenameUtils.concat(htmlToPdfCssFolder, cssFile) + "\" " + htmlLocation + " " + newPdfFilename;
		String cmd = wkhtmltopdfExecutableLocation + " " + FilenameUtils.concat(htmlToPdfCssFolder, cssFile) + " " + htmlLocation + " " + newPdfFilename;
		
		//logger.debug(Arrays.toString(cmd));
		logger.debug(cmd);
		Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(cmd);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String line=null;

        List<String> output = new ArrayList<String>();
        while((line=input.readLine()) != null) {
        	output.add(line);
        }
        
        int exitVal = p.waitFor();
        if (exitVal != 0) {
        	throw new IOException(output.get(output.size() -1));
        }
        return new File(newPdfFilename);
	}
	
	private String getNewPdfFilename(String htmlLocation) {
		String filename = htmlLocation.substring( htmlLocation.lastIndexOf('/')+1, htmlLocation.length() );
		if (filename.lastIndexOf('.') > -1) {
			filename = filename.substring(0, filename.lastIndexOf('.'));
		}
		filename = filename + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + ".pdf";
		filename = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename);
		return filename;
	}
	
}
