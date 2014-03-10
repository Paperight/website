package com.paperight.pdf;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paperight.licence.PageLayout;
import com.paperight.licence.Watermark;

public class PdfBoxPDFWatermarkWriter extends AbstractPdfWatermarkWriter {
	
	private Logger logger = LoggerFactory.getLogger(PdfBoxPDFWatermarkWriter.class);
	
	private PDDocument document;
	private PDFLine column1line1 = new PDFLine();
	private PDFLine column1line2 = new PDFLine();
	private PDFLine column2line1 = new PDFLine();
	private PDFLine column2line2 = new PDFLine();
	
	@Override
	public void writeWatermark(String inputFile, OutputStream outputStream, Watermark watermark, PageLayout layout) throws IOException {
		try {
			logger.debug("Writing watermark on " + inputFile);
			document = PDDocument.load(inputFile);
			setPageLayout(layout);
			internalWriteWatermark(watermark);
			try {
				document.save(outputStream);
			} catch (Exception e) {
				throw new IOException("Unable to save to output stream", e);
			}
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}
	
	@Override
	public void writeWatermark(String inputFile, String outputFile, Watermark watermark, PageLayout layout) throws IOException {
		try {
			document = PDDocument.load(inputFile);
			setPageLayout(layout);
			internalWriteWatermark(watermark);
			try {
				document.save(outputFile);
			} catch (Exception e) {
				throw new IOException("Unable to save file " + outputFile, e);
			}
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}
	
	private void internalWriteWatermark(Watermark watermark) throws IOException {
		setWatermark(watermark);
		createPDFLines();
		List allPages = document.getDocumentCatalog().getAllPages();
		boolean alternateWatermark = false;
		if (getPageLayout() == PageLayout.A5) {
			alternateWatermark = true;
		}
		boolean leftPage = true;
		for (int i = 0; i < allPages.size(); i++) {
			PDPage page = (PDPage) allPages.get(i);
			PDRectangle mediaBox = page.findMediaBox();
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true);
			if (alternateWatermark) {
				if (leftPage) {
					writeColumn1Line1(contentStream, page, mediaBox);
					writeColumn1Line2(contentStream, page, mediaBox);
				} else {
					writeColumn2Line1(contentStream, page, mediaBox);
					writeColumn2Line2(contentStream, page, mediaBox);
				}
			} else {
				writeColumn1Line1(contentStream, page, mediaBox);
				writeColumn1Line2(contentStream, page, mediaBox);
				writeColumn2Line1(contentStream, page, mediaBox);
				writeColumn2Line2(contentStream, page, mediaBox);
			}
			contentStream.close();
			leftPage = !leftPage;
		}
	}

	private void writeColumn1Line1(PDPageContentStream contentStream, PDPage page, PDRectangle mediaBox) throws IOException {
		float pageWidth = mediaBox.getWidth();
		float pageHeight = mediaBox.getHeight();
		
		float stringWidth = getColumn1line1().getWidth();
		
		contentStream.beginText();
		
		if (getPageLayout() == PageLayout.A5) {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 92 - getOffset());
		} else if (getOrientation(page) == PAGE_ORIENTATION_PORTRAIT) {
			double pageCentreYPosition = pageHeight / 2f;		
			double columnCentreYPosition = pageCentreYPosition / 2f * 3f;
			double centeredYPosition = columnCentreYPosition + (stringWidth / 2f);
			contentStream.setTextRotation(Math.toRadians(270), 92 - getOffset(), centeredYPosition);
		} else {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition / 2f;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 92 - getOffset());
		}
		
		contentStream.setNonStrokingColor(Color.BLACK);
		for (PDFLineText lineText : getColumn1line1().getLineTexts()) {
			contentStream.setFont(lineText.getFont(), lineText.getFontSize());
			contentStream.drawString(lineText.getText());
		}
		contentStream.endText();
	}

	private void writeColumn1Line2(PDPageContentStream contentStream, PDPage page, PDRectangle mediaBox) throws IOException {
		float pageWidth = mediaBox.getWidth();
		float pageHeight = mediaBox.getHeight();		
		
		float stringWidth = getColumn1line2().getWidth();		
		
		contentStream.beginText();
		
		if (getPageLayout() == PageLayout.A5) {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 82 - getOffset());
		} else if (getOrientation(page) == PAGE_ORIENTATION_PORTRAIT) {
			double pageCentreYPosition = pageHeight / 2f;				
			double columnCentreYPosition = pageCentreYPosition / 2f * 3f;
			double centeredYPosition = columnCentreYPosition + (stringWidth / 2f);
			contentStream.setTextRotation(Math.toRadians(270), 82 - getOffset(), centeredYPosition);
		} else {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition / 2f;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 82 - getOffset());
		}
		
		contentStream.setNonStrokingColor(Color.BLACK);
		for (PDFLineText lineText : getColumn1line2().getLineTexts()) {
			contentStream.setFont(lineText.getFont(), lineText.getFontSize());
			contentStream.drawString(lineText.getText());
		}
		contentStream.endText();
	}
	
	private void writeColumn2Line1(PDPageContentStream contentStream, PDPage page, PDRectangle mediaBox) throws IOException {
		float pageWidth = mediaBox.getWidth();
		float pageHeight = mediaBox.getHeight();
		
		float stringWidth = getColumn2line1().getWidth();		
		
		contentStream.beginText();
		
		if (getPageLayout() == PageLayout.A5) {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 92 - getOffset());
		} else if (getOrientation(page) == PAGE_ORIENTATION_PORTRAIT) {
			double pageCentreYPosition = pageHeight / 2f;			
			double columnCentreYPosition = pageCentreYPosition / 2f;
			double centeredYPosition = columnCentreYPosition + (stringWidth / 2f);
			contentStream.setTextRotation(Math.toRadians(270), 92 - getOffset(), centeredYPosition);
		} else {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition / 2f * 3f;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 92 - getOffset());
		}
		
		contentStream.setNonStrokingColor(Color.BLACK);
		for (PDFLineText lineText : getColumn2line1().getLineTexts()) {
			contentStream.setFont(lineText.getFont(), lineText.getFontSize());
			contentStream.drawString(lineText.getText());
		}
		contentStream.endText();
	}
	
	private void writeColumn2Line2(PDPageContentStream contentStream, PDPage page, PDRectangle mediaBox) throws IOException {
		float pageWidth = mediaBox.getWidth();
		float pageHeight = mediaBox.getHeight();
			
		float stringWidth = getColumn2line2().getWidth();		
		
		contentStream.beginText();
		
		if (getPageLayout() == PageLayout.A5) {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 82 - getOffset());
		} else if (getOrientation(page) == PAGE_ORIENTATION_PORTRAIT) {
			double pageCentreYPosition = pageHeight / 2f;
			double columnCentreYPosition = pageCentreYPosition / 2f;
			double centeredYPosition = columnCentreYPosition + (stringWidth / 2f);
			contentStream.setTextRotation(Math.toRadians(270), 82 - getOffset(), centeredYPosition);
		} else {
			double pageCentreXPosition = pageWidth / 2f;
			double columnCentreXPosition = pageCentreXPosition / 2f * 3f;
			double centeredXPosition = columnCentreXPosition - (stringWidth / 2f);
			contentStream.setTextTranslation(centeredXPosition, 82 - getOffset());
		}
		
		contentStream.setNonStrokingColor(Color.BLACK);
		for (PDFLineText lineText : getColumn2line2().getLineTexts()) {
			contentStream.setFont(lineText.getFont(), lineText.getFontSize());
			contentStream.drawString(lineText.getText());
		}
		contentStream.endText();
	}
	
	private void createPDFLines() {
		getColumn1line1().addLineText(PDType1Font.HELVETICA_OBLIQUE, 6.0f, getWatermark().getDocumentTitle());
		if (!StringUtils.isBlank(getWatermark().getDocumentRightsHolder())) {
			getColumn1line1().addLineText(PDType1Font.HELVETICA, 6.0f, " (" + getWatermark().getDocumentRightsHolder() + ") |");
		}
		getColumn1line1().addLineText(PDType1Font.HELVETICA, 6.0f, " Printed by " + getWatermark().getOutletName());
		
		getColumn1line2().addLineText(PDType1Font.HELVETICA, 6.0f, "for " + getWatermark().getCustomerName() + " | " + getWatermark().getTransactionDate().toString("yyyy/MM/dd"));
		
		getColumn2line1().addLineText(PDType1Font.HELVETICA_OBLIQUE, 6.0f, getWatermark().getUrl());

		getColumn2line2().addLineText(PDType1Font.HELVETICA, 6.0f, getWatermark().getAdditionalText());
		
	}

	private static final int PAGE_ORIENTATION_PORTRAIT = 0;
	private static final int PAGE_ORIENTATION_LANDSCAPE = 1;
	
	private int getOrientation(PDPage page) {
		PDRectangle pageSize = page.findMediaBox();
		if (pageSize.getHeight() > pageSize.getWidth()) {
			return PAGE_ORIENTATION_PORTRAIT;
		} else {
			return PAGE_ORIENTATION_LANDSCAPE;
		}
	}

	private PDFLine getColumn1line1() {
		return column1line1;
	}

	private PDFLine getColumn1line2() {
		return column1line2;
	}

	private PDFLine getColumn2line1() {
		return column2line1;
	}

	private PDFLine getColumn2line2() {
		return column2line2;
	}
	
	class PDFLine {
		
		private double xPosition;
		private double yPosition;
		private List<PDFLineText> lineTexts = new ArrayList<PDFLineText>();
		
		public void addLineText(PDFLineText lineText) {
			lineTexts.add(lineText);
		}
		
		public void addLineText(PDFont font, float fontSize, String text) {
			PDFLineText lineText = new PDFLineText();
			lineText.setFont(font);
			lineText.setFontSize(fontSize);
			lineText.setText(text);
			addLineText(lineText);
		}
		
		public float getWidth() throws IOException {
			float width = 0;
			for (PDFLineText lineText : getLineTexts()) {
				width += lineText.getWidth();
			}
			return width;
		}
		
		public double getXPosition() {
			return xPosition;
		}
		
		public void setXPosition(double centreXPosition) {
			this.xPosition = centreXPosition;
		}
		
		public double getYPosition() {
			return yPosition;
		}
		
		public void setYPosition(double centreYPosition) {
			this.yPosition = centreYPosition;
		}

		public List<PDFLineText> getLineTexts() {
			return lineTexts;
		}

		public void setLineTexts(List<PDFLineText> lineTexts) {
			this.lineTexts = lineTexts;
		}
		
	}

	class PDFLineText {
		
		private PDFont font = PDType1Font.HELVETICA;
		private float fontSize = 7.0f;
		private String text = "";

		public PDFont getFont() {
			return font;
		}

		public void setFont(PDFont font) {
			this.font = font;
		}

		public float getFontSize() {
			return fontSize;
		}

		public void setFontSize(float fontSize) {
			this.fontSize = fontSize;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
		
		public float getWidth() throws IOException {
			return getFont().getStringWidth(getText()) * getFontSize() / 1000f;
		}
		
	}
	
}