package com.paperight.pdf;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.paperight.licence.PageLayout;
import com.paperight.licence.Watermark;

public class ItextPDFWatermarkWriter extends AbstractPdfWatermarkWriter {
	
	private Logger logger = LoggerFactory.getLogger(ItextPDFWatermarkWriter.class);
	
	private PdfReader document;
	private PdfStamper stamper;
	private PDFLine column1line1 = new PDFLine();
	private PDFLine column1line2 = new PDFLine();
	private PDFLine column2line1 = new PDFLine();
	private PDFLine column2line2 = new PDFLine();
	
	@Override
	public void writeWatermark(String inputFile, OutputStream outputStream, Watermark watermark, PageLayout layout) throws Exception {
		String tempFile = inputFile + ".tmp";
		writeWatermark(inputFile, tempFile, watermark, layout);
		FileInputStream inputStream = new FileInputStream(tempFile);
		IOUtils.copy(inputStream, outputStream);
	}
	
	@Override
	public void writeWatermark(String inputFile, String outputFile, Watermark watermark, PageLayout layout) throws Exception {
		try {
			logger.debug("Writing watermark on " + inputFile);
			document = new PdfReader(inputFile);
			try {
				stamper = new PdfStamper(document, new FileOutputStream(outputFile));
				setPageLayout(layout);
				internalWriteWatermark(watermark);
				stamper.close();
			} catch (Exception e) {
				throw new IOException("Unable to write watermark", e);
			}
		} finally {
			if (document != null) {
				document.close();
			}
			if (isEncrypt()) {
				//PdfEncryptor.encrypt(outputFile);
			}
		}
	}
	
	private void internalWriteWatermark(Watermark watermark) throws IOException {
		setWatermark(watermark);
		createPDFLines();
		boolean alternateWatermark = true;
		if (getPageLayout() == PageLayout.TWO_UP) {
			alternateWatermark = false;
		}
		boolean leftPage = true;
		stamper.setRotateContents(false);
		for (int i = 1; i <= stamper.getReader().getNumberOfPages(); i++) {
			if (i== 1) {
				continue;
			}
			PdfContentByte canvas = stamper.getOverContent(i);
			if (alternateWatermark) {
				if (leftPage) {
					writeColumn1Line1(canvas, i);
					writeColumn1Line2(canvas, i);
				} else {
					writeColumn2Line1(canvas, i);
					writeColumn2Line2(canvas, i);
				}
			} else {
				writeColumn1Line1(canvas, i);
				writeColumn1Line2(canvas, i);
				writeColumn2Line1(canvas, i);
				writeColumn2Line2(canvas, i);
			}
			leftPage = !leftPage;
		}
	}
	
	private void writeColumn1Line1(PdfContentByte canvas, int pageNumber) throws IOException {
		Phrase phrase = new Phrase();
		for (PDFLineText lineText : getColumn1line1().getLineTexts()) {
			phrase.add(lineText.getPhrase());
		}
		
		float pageWidth = document.getPageSize(pageNumber).getWidth();
		
		if (getPageLayout() == PageLayout.A5) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
		} else if (getOrientation(canvas, pageNumber) == PAGE_ORIENTATION_PORTRAIT) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
			/*float pageCentreYPosition = pageHeight / 2f;		
			float columnCentreYPosition = pageCentreYPosition / 2f * 3f;
			float centeredYPosition = columnCentreYPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 92 - getOffset(), centeredYPosition, 270);*/
		} else {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition / 2f;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
		}
		
	}
	
	private void writeColumn1Line2(PdfContentByte canvas, int pageNumber) throws IOException {
		Phrase phrase = new Phrase();
		for (PDFLineText lineText : getColumn1line2().getLineTexts()) {
			phrase.add(lineText.getPhrase());
		}
			
		float pageWidth = document.getPageSize(pageNumber).getWidth();

		if (getPageLayout() == PageLayout.A5) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
		} else if (getOrientation(canvas, pageNumber) == PAGE_ORIENTATION_PORTRAIT) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
			/*float pageCentreYPosition = pageHeight / 2f;				
			float columnCentreYPosition = pageCentreYPosition / 2f * 3f;
			float centeredYPosition = columnCentreYPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 82 - getOffset(), centeredYPosition, 270);*/
		} else {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition / 2f;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
		}
	}
	
	private void writeColumn2Line1(PdfContentByte canvas, int pageNumber) throws IOException {
		Phrase phrase = new Phrase();
		for (PDFLineText lineText : getColumn2line1().getLineTexts()) {
			phrase.add(lineText.getPhrase());
		}
			
		float pageWidth = document.getPageSize(pageNumber).getWidth();

		if (getPageLayout() == PageLayout.A5) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
		} else if (getOrientation(canvas, pageNumber) == PAGE_ORIENTATION_PORTRAIT) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
			/*float pageCentreYPosition = pageHeight / 2f;			
			float columnCentreYPosition = pageCentreYPosition / 2f;
			float centeredYPosition = columnCentreYPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 92 - getOffset(), centeredYPosition, 270);*/
		} else {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition / 2f * 3f;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 92 - getOffset(), 0);
		}
	}
	
	private void writeColumn2Line2(PdfContentByte canvas, int pageNumber) throws IOException {
		Phrase phrase = new Phrase();
		for (PDFLineText lineText : getColumn2line2().getLineTexts()) {
			phrase.add(lineText.getPhrase());
		}
			
		float pageWidth = document.getPageSize(pageNumber).getWidth();

		if (getPageLayout() == PageLayout.A5) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
		} else if (getOrientation(canvas, pageNumber) == PAGE_ORIENTATION_PORTRAIT) {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
			/*float pageCentreYPosition = pageHeight / 2f;
			float columnCentreYPosition = pageCentreYPosition / 2f;
			float centeredYPosition = columnCentreYPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 82 - getOffset(), centeredYPosition, 270);*/
		} else {
			float pageCentreXPosition = pageWidth / 2f;
			float columnCentreXPosition = pageCentreXPosition / 2f * 3f;
			float centeredXPosition = columnCentreXPosition;
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, centeredXPosition, 82 - getOffset(), 0);
		}
	}
	
	private void createPDFLines() {
		
		getColumn1line1().addLineText(FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Font.NORMAL, new Color(0, 0, 0)), getWatermark().getDocumentTitle());
		if (!StringUtils.isBlank(getWatermark().getDocumentRightsHolder())) {
			getColumn1line1().addLineText(FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0)), " (" + getWatermark().getDocumentRightsHolder() + ") |");
		}
		getColumn1line1().addLineText(FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0)), " Printed by " + getWatermark().getOutletName());
		
		getColumn1line2().addLineText(FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0)), "for " + getWatermark().getCustomerName() + " | " + getWatermark().getTransactionDate().toString("yyyy/MM/dd"));
		
		getColumn2line1().addLineText(FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Font.NORMAL, new Color(0, 0, 0)), getWatermark().getUrl());

		getColumn2line2().addLineText(FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0)), getWatermark().getAdditionalText());
		
	}

	private static final int PAGE_ORIENTATION_PORTRAIT = 0;
	private static final int PAGE_ORIENTATION_LANDSCAPE = 1;
	
	private int getOrientation(PdfContentByte canvas, int pageNumber) {
		Rectangle pageSize = document.getPageSize(pageNumber);
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
		
		public void addLineText(Font font, String text) {
			PDFLineText lineText = new PDFLineText();
			lineText.setFont(font);
			//lineText.setFontSize(fontSize);
			lineText.setText(text);
			addLineText(lineText);
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
		
		private Font font = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0));
		//private float fontSize = 7.0f;
		private String text = "";

		public Font getFont() {
			return font;
		}

		public void setFont(Font font) {
			this.font = font;
		}

//		public float getFontSize() {
//			return fontSize;
//		}
//
//		public void setFontSize(float fontSize) {
//			this.fontSize = fontSize;
//		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
		
//		public float getWidth() throws IOException {
//			//getPhrase().
//			return getFont().getStringWidth(getText()) * getFontSize() / 1000f;
//		}
		
		public Phrase getPhrase() {
			return new Phrase(getText(), getFont());
		}
		
	}
	
}

