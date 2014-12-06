package com.paperight.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lowagie.text.DocumentException;
import com.paperight.licence.PageLayout;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml" })
public class PaperightPdfConverterTest {
	
	private PaperightPdfConverter converter = new PaperightPdfConverter();

	//@Test
	public void testCropPdf() throws IOException, DocumentException, Exception {
		//String croppedFilename = converter.cropPdf("d:/downloads/study-and-master_accounting-grade-12_sample_20131202.pdf");
		//String croppedFilename = converter.cropPdf("d:/downloads/bukantswe-ya-dipalomathaithai_cropped_20131213.pdf");
		String croppedFilename = converter.cropPdf("d:/downloads/9781775781431_xkit-achieve_grade-10-english-hl_20131218.pdf");
		System.out.println("testCropPdf " + croppedFilename);	
	}
	
	@Test
	public void testCreatePaperightPdfs() throws Exception {
		converter.setPdfFileFolder("d:/projects/paperight/data/pdfs");
		List<PageLayout> layouts = new ArrayList<PageLayout>();
		layouts.add(PageLayout.ONE_UP);
		layouts.add(PageLayout.TWO_UP);
		//layouts.add(PageLayout.A5);
		String croppedFilename = null;
		String croppedA5Filename = null;
		//croppedFilename = converter.cropPdf("d:/projects/paperight/data/ngiyaddela-ngobuntu_mdlalose_extracted-pages_20130826.pdf", false);
		croppedFilename = converter.cropPdf("d:/downloads/9781770304598.pdf");
		//croppedA5Filename = converter.cropPdf("d:/downloads/9780992213206_a-grain-of-sand_charles-cilliers_20131217.pdf");
		PdfConversion conversion = converter.createPaperightPdfs(croppedFilename, croppedA5Filename, layouts);
		System.out.println("ONE-UP: " + conversion.getOneUpFilename());
		System.out.println("TWO-UP: " + conversion.getTwoUpFilename());
		System.out.println("A5: " + conversion.getA5Filename());
	}
	
	
}
