package com.paperight.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.LicenceFileService;
import com.paperight.licence.PreviewCreatorImpl;
import com.paperight.licence.WatermarkFactory;
import com.paperight.licence.WatermarkWriter;
import com.paperight.licence.WatermarkWriterFactory;

@Configurable
public class PreviewCreatorPdfImpl extends PreviewCreatorImpl {
	
	@Autowired
	LicenceFileService fileService;
		
	@Override
	protected void internalCreatePreview() throws Exception {
		PDDocument sourceDocument = PDDocument.load(getSource());
		String tempFilename = DateTime.now().getMillis() + ".pdf";
		try {
			List<?> allPages = sourceDocument.getDocumentCatalog().getAllPages();
			PDDocument targetDocument = new PDDocument();
			try {
				if (isPageRangePercent()) {
					extractPageRangePercent(allPages);
				}
				for (int i = 0; i < allPages.size(); i++) {
					if (getPagesList().contains(i + 1)) {
						PDPage page = (PDPage) allPages.get(i);
						targetDocument.addPage(page);						
					}
					
				}
				targetDocument.save(tempFilename);
			} finally {
				targetDocument.close();
			}
		} finally {
			sourceDocument.close();
		}
		try {
			WatermarkWriter watermarkWriter = WatermarkWriterFactory.create(FilenameUtils.getExtension(tempFilename));
			try {
				watermarkWriter.writeWatermark(tempFilename, getTarget(), WatermarkFactory.create(getUser(), getProduct()), getLayout());
			} catch (IOException e) {
				throw new Exception("Unable to generate PDF", e);
			}
		} finally {
			File file = FileUtils.getFile(tempFilename);
			if (file.exists()) {
				file.delete();
			}					
		}
	}
	
	private void extractPageRangePercent(List<?> allPages) {
		Double percent = new Double(StringUtils.remove(getPages(), "%"));
		int totalPages = (int)((percent / 100.0f) * (allPages.size() + 1));
		for (int i = 0; i < allPages.size(); i++) {
			getPagesList().add(Integer.valueOf(i + 1));
			if (i + 1 == totalPages) {
				break;
			}
		}
		
	}
}
