package com.paperight.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.RectangleReadOnly;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.paperight.licence.PageLayout;

@Configurable
public class PaperightPdfConverter {
	
	@Value("${pdf.file.folder}")
	private String pdfFileFolder;
	
	private Logger logger = LoggerFactory.getLogger(PaperightPdfConverter.class);
	
	public PdfConversion createPaperightPdfs(String originalPdfFile, String originalA5PdfFile, List<PageLayout> pageLayouts) throws Exception {
		PdfConversion convertedPdfs = new PdfConversion();
		if (pageLayouts == null || pageLayouts.contains(PageLayout.ONE_UP)) {
			String oneUpFilename = createOneUp(originalPdfFile);
			convertedPdfs.setOneUpFilename(FilenameUtils.getName(oneUpFilename));
		}
		if (pageLayouts == null || pageLayouts.contains(PageLayout.TWO_UP)) {
			String twoUpFilename = createTwoUp(originalPdfFile);
			convertedPdfs.setTwoUpFilename(FilenameUtils.getName(twoUpFilename));
		}
		if (pageLayouts == null || pageLayouts.contains(PageLayout.A5)) {
			String a5Filename = createA5(originalA5PdfFile);
			convertedPdfs.setA5Filename(FilenameUtils.getName(a5Filename));
		}
		return convertedPdfs;
	}
		
	private String createOneUp(String originalPdfFile) throws IOException, DocumentException {
		logger.debug("Started create 1-up file from " + originalPdfFile);
		String filename = FilenameUtils.getBaseName(originalPdfFile) + "_1up." + FilenameUtils.getExtension(originalPdfFile);
		String tempFilename =  FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename + ".tmp");
		filename = FilenameUtils.concat(getPdfFileFolder(), filename);
		File tempFile = new File(tempFilename);
		//float scale = 0.80f;
		PdfReader reader = new PdfReader(originalPdfFile);
		Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
		//Document doc = new Document(new RectangleReadOnly(606.96f, 850.32f), 0, 0, 0, 0);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(tempFile));
		doc.open();
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			doc.newPage();
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page = writer.getImportedPage(reader, i);
			
			
			float documentWidth = doc.getPageSize().getWidth();
			float documentHeight = doc.getPageSize().getHeight();
			if (i > 1) {
				documentHeight = documentHeight - 65f;
			}
			
			
			/*float documentHeight = doc.getPageSize().getHeight();
			if (i > 1) {
				documentHeight = documentHeight - 65f;
			}
			float documentWidth = doc.getPageSize().getWidth();*/

			float pageWidth = page.getWidth();
			float pageHeight = page.getHeight();
			
			float widthScale = documentWidth / pageWidth;
			float heightScale = documentHeight / pageHeight;
			float scale = Math.min(widthScale, heightScale);
			
			float offsetX = (documentWidth - (pageWidth * scale)) / 2;
			float offsetY = 0f;
			if (i > 1) {
				offsetY = 65f;
			}
			/*
			
			float offsetX = 0f;
			if (i > 1) {
				offsetX = 65f; //100f
			}
			//float offsetX = 65f;
			float offsetY = ((documentHeight) - (pageHeight * scale)) / 2;*/
			
			cb.addTemplate(page, scale, 0, 0, scale, offsetX, offsetY);

		}
		doc.close();
		overlayLicenceArea(tempFilename, filename, PageLayout.ONE_UP);
		logger.debug("Deleting temporary file " + tempFile.getAbsolutePath());
		FileUtils.deleteQuietly(tempFile);
		logger.debug("Completed create 1-up file from " + originalPdfFile);
		return filename;
	}
	
	private String createTwoUp(String originalPdfFile) throws IOException, DocumentException {
		logger.debug("Started create 2-up file from " + originalPdfFile);
		String filename = FilenameUtils.getBaseName(originalPdfFile) + "_2up." + FilenameUtils.getExtension(originalPdfFile);
		String tempFilename =  FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename + ".tmp");
		filename = FilenameUtils.concat(getPdfFileFolder(), filename);
		File tempFile = new File(tempFilename);
		//float scale = 0.60f;
		PdfReader reader = new PdfReader(originalPdfFile);
		Document doc = new Document(new RectangleReadOnly(842f, 595f), 0, 0, 0, 0);
		//Document doc = new Document(new RectangleReadOnly(850.32f, 606.96f), 0, 0, 0, 0);
		//doc.setMargins(50f, 50f, 50f, 50f);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(tempFile));
		doc.open();
		int totalPages = reader.getNumberOfPages();
		for (int i = 1; i <= totalPages; i = i + 2) {
			doc.newPage();
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page = writer.getImportedPage(reader, i); // page #1
			
			float documentWidth = doc.getPageSize().getWidth() / 2;
			float documentHeight = doc.getPageSize().getHeight();
			if (i > 1) {
				documentHeight = documentHeight - 65f;
			}

			float pageWidth = page.getWidth();
			float pageHeight = page.getHeight();
			
			float widthScale = documentWidth / pageWidth;
			float heightScale = documentHeight / pageHeight;
			float scale = Math.min(widthScale, heightScale);
			
			//float offsetX = 50f;
			float offsetX = (documentWidth - (pageWidth * scale)) / 2;
			float offsetY = 0f;
			if (i > 1) {
				offsetY = 65f; //100f
			}
			
			cb.addTemplate(page, scale, 0, 0, scale, offsetX, offsetY);
			
			if (i+1 <= totalPages) {
			
				PdfImportedPage page2 = writer.getImportedPage(reader, i+1); // page #2
				
				pageWidth = page.getWidth();
				pageHeight = page.getHeight();
				
				widthScale = documentWidth / pageWidth;
				heightScale = documentHeight / pageHeight;
				scale = Math.min(widthScale, heightScale);
				
				offsetX = ((documentWidth - (pageWidth * scale)) / 2) + documentWidth;
				//offsetY = 65f; //100f
				
				cb.addTemplate(page2, scale, 0, 0, scale, offsetX, offsetY);//430f
			}
		}
		
		doc.close();
		overlayLicenceArea(tempFilename, filename, PageLayout.TWO_UP);
		logger.debug("Deleting temporary file " + tempFile.getAbsolutePath());
		FileUtils.deleteQuietly(tempFile);
		logger.debug("Completed create 2-up file from " + originalPdfFile);
		return filename;
	}
	
	private String createA5(String originalPdfFile) throws IOException, DocumentException {
		logger.debug("Started create A5 file from " + originalPdfFile);
		String filename = FilenameUtils.getBaseName(originalPdfFile) + "_a5." + FilenameUtils.getExtension(originalPdfFile);
		String tempFilename =  FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename + ".tmp");
		filename = FilenameUtils.concat(getPdfFileFolder(), filename);
		File tempFile = new File(tempFilename);
		//float scale = 0.72906403940886699507389162561576f;
		PdfReader reader = new PdfReader(originalPdfFile);
		
		//new RectangleReadOnly(425.16f, 606.96f)
		Document doc = new Document(PageSize.A5, 0, 0, 0, 0);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(tempFile));
		doc.open();
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			doc.newPage();
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page = writer.getImportedPage(reader, i); // page #1
			
			float documentWidth = doc.getPageSize().getWidth();
			float documentHeight = doc.getPageSize().getHeight();
			if (i > 1) {
				documentHeight = documentHeight - 65f;
			}
			//float documentHeight = doc.getPageSize().getHeight() - 65f;

			float pageWidth = page.getWidth();
			float pageHeight = page.getHeight();
			
			float widthScale = documentWidth / pageWidth;
			float heightScale = documentHeight / pageHeight;
			float scale = Math.min(widthScale, heightScale);
			
			//float offsetX = 50f;
			float offsetX = (documentWidth - (pageWidth * scale)) / 2;
			float offsetY = 0f;
			if (i > 1) {
				offsetY = 65f;
			}
			//float offsetY = ((documentHeight) - (pageHeight * scale)) / 2;
			
			cb.addTemplate(page, scale, 0, 0, scale, offsetX, offsetY);
			//cb.addTemplate(page, scale, 0, 0, scale, 50f, 100f);
		}
		doc.close();
		//FileUtils.moveFile(tempFile, new File(filename));
		overlayLicenceArea(tempFilename, filename, PageLayout.A5);
		logger.debug("Deleting temporary file " + tempFile.getAbsolutePath());
		FileUtils.deleteQuietly(tempFile);
		logger.debug("Completed create A5 file from " + originalPdfFile);
		return filename;
	}
	
	private void overlayLicenceArea(String filename, String outFilename, PageLayout pageLayout) throws IOException, DocumentException {
		logger.debug("Started overlay licence area on " + filename);
		PdfReader reader = new PdfReader(filename);
		Rectangle pageSize = reader.getPageSize(1);
		PdfReader stampReader;
		if (pageLayout == PageLayout.A5) {
		    ClassPathResource classPathResource = new ClassPathResource("a5-template.pdf");
		    try (InputStream inputStream = classPathResource.getInputStream()) {
		        stampReader = new PdfReader(inputStream);
		    }
		} else {
			if (pageSize.getWidth() < pageSize.getHeight()) {
			    ClassPathResource classPathResource = new ClassPathResource("1-up-template.pdf");
			    try (InputStream inputStream = classPathResource.getInputStream()) {
			        stampReader = new PdfReader(inputStream);
			    }
			} else {
			    ClassPathResource classPathResource = new ClassPathResource("2-up-template.pdf");
			    try (InputStream inputStream = classPathResource.getInputStream()) {
			        stampReader = new PdfReader(inputStream);
			    }
			}
		}
		// Create the stamper
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFilename));
		// Add the stationery to each page
		PdfImportedPage page = stamper.getImportedPage(stampReader, 1);
		int n = reader.getNumberOfPages();
		
		PdfContentByte background;
		for (int i = 1; i <= n; i++) {
			if (i == 1) {
				continue;
			}
		    background = stamper.getUnderContent(i);
		    background.addTemplate(page, 0, 0);
		}
		// CLose the stamper
		stamper.close();
		logger.debug("Completed overlay licence area on " + filename);
	}
	
	
	public String cropPdf(String pdfFilePath) throws DocumentException, IOException, Exception {
		String filename = FilenameUtils.getBaseName(pdfFilePath) + "_cropped." + FilenameUtils.getExtension(pdfFilePath);
		filename = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename);
		PdfReader reader = new PdfReader(pdfFilePath);
		try {
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(filename));
			try {
				for (int i = 1; i <= reader.getNumberOfPages(); i++) {
					PdfDictionary pdfDictionary = reader.getPageN(i);
					PdfArray cropArray = new PdfArray();
					Rectangle box = getSmallestBox(reader, i);
					//Rectangle cropbox = reader.getCropBox(i);
					if (box != null) {
						cropArray.add(new PdfNumber(box.getLeft()));
						cropArray.add(new PdfNumber(box.getBottom()));
						cropArray.add(new PdfNumber(box.getLeft() + box.getWidth()));
						cropArray.add(new PdfNumber(box.getBottom() + box.getHeight()));
						pdfDictionary.put(PdfName.CROPBOX, cropArray);
						pdfDictionary.put(PdfName.MEDIABOX, cropArray);
						pdfDictionary.put(PdfName.TRIMBOX, cropArray);
						pdfDictionary.put(PdfName.BLEEDBOX, cropArray);
					}
				}
				return filename;
			} finally {
				stamper.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			reader.close();
		}
	}
	
	private Rectangle getSmallestBox(PdfReader reader, int pageNumber) {
		Map<Float, Rectangle> boxAreas = new TreeMap<Float, Rectangle>();
		
		Rectangle cropBox = reader.getBoxSize(pageNumber, "crop");
		Rectangle trimBox = reader.getBoxSize(pageNumber, "trim");
		Rectangle artBox = reader.getBoxSize(pageNumber, "art");
		Rectangle bleedBox = reader.getBoxSize(pageNumber, "bleed");
		
		if (cropBox != null) {
			boxAreas.put(getBoxArea(cropBox), cropBox);
		}
		if (trimBox != null) {
			boxAreas.put(getBoxArea(trimBox), trimBox);
		}
		if (artBox != null) {
			boxAreas.put(getBoxArea(artBox), artBox);
		}
		if (bleedBox != null) {
			boxAreas.put(getBoxArea(bleedBox), bleedBox);
		}
		Rectangle result = null;
		for (Float area : boxAreas.keySet()) {
			result = boxAreas.get(area);
			break;
		}
		return result;
	}
	
	private float getBoxArea(Rectangle box) {
		float result = -1;
		if (box != null) {
			result = box.getWidth() * box.getHeight();
		}
		return result;
	}

	public String getPdfFileFolder() {
		return pdfFileFolder;
	}

	public void setPdfFileFolder(String pdfFileFolder) {
		this.pdfFileFolder = pdfFileFolder;
	}

}
