package com.paperight;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paperight.pdf.PdfUtils;

@Service
public class LicenceFileService {
	
	private Logger logger = LoggerFactory.getLogger(LicenceFileService.class);
	
	@Value("${pdf.file.folder}")
	private String pdfFileFolder;
	
	@Value("${licenced.pdf.file.folder}")
	private String licencedPdfFileFolder;
	
	public boolean fileExists(String filename) throws IOException {
		logger.debug("checking file " + filename + " exists");
		File file = FileUtils.getFile(filename);
		return file.exists();
	}
	
	public String getPdfFileFolder() {
		return pdfFileFolder;
	}

	public String getLicencedPdfFileFolder() {
		return licencedPdfFileFolder;
	}
	
	public String getPdfPreviewFileFolder() {
		return FilenameUtils.concat(getPdfFileFolder(), "preview");
	}

	public BigDecimal getFileSize(String fileName) {
		try {
			if (fileExists(fileName)) {
				File file = FileUtils.getFile(fileName);
				BigDecimal fileSize = new BigDecimal(file.length());
				fileSize = fileSize.divide(new BigDecimal(1024 * 1024));
				return fileSize.setScale(2, RoundingMode.HALF_UP);
			}
		} catch (IOException e) {
		}
		return null;
	}
	
	public int getPageCount(String fileName) throws Exception {
		String fullPath = FilenameUtils.concat(getPdfFileFolder(), fileName);
		return PdfUtils.countPages(fullPath);
	}

}
