package com.paperight.pdf;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paperight.licence.PageLayout;
import com.paperight.pdf.PdfExecutorFactory.PdfExecutorType;

@Service
public class PdfAutomationService {
	
	private Logger logger = LoggerFactory.getLogger(PdfAutomationService.class);
	
	public File epubToPdf(MultipartFile uploadFile) throws Exception {
		String filename = uploadFile.getOriginalFilename();
		filename = FilenameUtils.getBaseName(filename) + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + "." + FilenameUtils.getExtension(filename);
		filename = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename);
		File file = new File(filename);
		logger.info("saving uploaded ePub file " + uploadFile.getOriginalFilename() + " to " + filename);
		FileUtils.writeByteArrayToFile(file, uploadFile.getBytes());
		EpubToPdfConverter epubToPdfConverter = new EpubToPdfConverter();
		try {
			File newPdf = epubToPdfConverter.convert(filename);
			return newPdf;
		} finally {
			FileUtils.deleteQuietly(file);
		}
	}
	
	public File htmlToPdf(String htmlLocation, List<String> cssFiles, PdfExecutorType pdfConversion) throws Exception {
		PdfExecutor pdfExecutor = PdfExecutorFactory.create(pdfConversion);
		return pdfExecutor.execute(htmlLocation, cssFiles);
	}
	
	public PdfConversion createPaperightPdfs(File originalPdfFile, File originalA5PdfFile, List<PageLayout> pageLayouts, String outputFolder) throws Exception {
		PaperightPdfConverter paperightPdfConverter = new PaperightPdfConverter();
		if (!StringUtils.isBlank(outputFolder)) {
		    paperightPdfConverter.setPdfFileFolder(outputFolder);
		}
		String originalFilePath = null;
		if (originalPdfFile != null) {
			originalFilePath = originalPdfFile.getAbsolutePath();
		}
		String originalA5FilePath = null;
		if (originalA5PdfFile != null) {
			originalA5FilePath = originalA5PdfFile.getAbsolutePath();
		}
		String croppedFilePath = null;
		String croppedA5FilePath = null;
		try {
			if (!StringUtils.isEmpty(originalFilePath)) {
				croppedFilePath = paperightPdfConverter.cropPdf(originalFilePath);
			}
			if (!StringUtils.isEmpty(originalA5FilePath)) {
				croppedA5FilePath = paperightPdfConverter.cropPdf(originalA5FilePath);
			}
			return paperightPdfConverter.createPaperightPdfs(croppedFilePath, croppedA5FilePath, pageLayouts);
		} finally {
			if (!StringUtils.isEmpty(croppedFilePath)) {
				FileUtils.deleteQuietly(new File(croppedFilePath));
			}
			if (!StringUtils.isEmpty(croppedA5FilePath)) {
				FileUtils.deleteQuietly(new File(croppedA5FilePath));
			}
		}
	}
	

}
