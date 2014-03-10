package com.paperight.pdf;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfUtils {
	
	public static int countPages(String filename) throws IOException {
		PDDocument document = null;
		try {
			document = PDDocument.load(filename);
			return document.getDocumentCatalog().getAllPages().size();
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

}
