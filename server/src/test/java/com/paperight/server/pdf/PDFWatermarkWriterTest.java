package com.paperight.server.pdf;

import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.licence.PageLayout;
import com.paperight.licence.Watermark;
import com.paperight.licence.WatermarkWriter;
import com.paperight.pdf.ItextPDFWatermarkWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml" })
public class PDFWatermarkWriterTest {

	@Test
	public void testWriteWatermark() throws Exception {
		Watermark watermark = new Watermark();
		watermark.setDocumentTitle("Pride and Prejudice");
		watermark.setDocumentRightsHolder("Paperight");
		watermark.setOutletName("Paperight");
		watermark.setCustomerName("Paperight Sample");
		watermark.setTransactionDate(new DateTime(2012, 03, 14, 0, 0));
		watermark.setAdditionalText("Purchase a licence from Paperight.com");
		watermark.setUrl("Get more from your reading, go to: http://j.mp/X7Tgfx");
		watermark.setSupportsAds(true);

		testA5(watermark);
		testOneUp(watermark);
		testTwoUp(watermark);
		fail("Not yet implemented");
	}
	
	private WatermarkWriter createWatermarkWriter() {
		return new ItextPDFWatermarkWriter();
	}
	
	private void testOneUp(Watermark watermark) throws Exception {
		String oneUpInFile = "C:\\projects\\Paperight\\data\\pdfs\\5842A559-B90D-EB0F-10B56C8BB6CE4D8D_using-media-in-teaching_1-up_20120508.pdf";
		String oneUpOutFile = "C:\\projects\\Paperight\\watermark_test_" + DateTime.now().toString("HHmmss") + "_one-up.pdf";
		createWatermarkWriter().writeWatermark(oneUpInFile, oneUpOutFile, watermark, PageLayout.ONE_UP);
	}
	
	private void testTwoUp(Watermark watermark) throws Exception {
		String twoUpInFile = "C:\\projects\\Paperight\\data\\pdfs\\978-190-707-6237_53-interesting-things-lectures_2-up_20120503.pdf";
		String twoUpOutFile = "C:\\projects\\Paperight\\watermark_test_" + DateTime.now().toString("HHmmss") + "_two-up.pdf";
		createWatermarkWriter().writeWatermark(twoUpInFile, twoUpOutFile, watermark, PageLayout.TWO_UP);
	}
	
	private void testA5(Watermark watermark) throws Exception {
		String a5InFile = "C:\\projects\\Paperight\\data\\pdfs\\4FC9D556-FF28-562E-DF6A9ED32E08D236_pride-and-prejudice_a5_20130507.pdf";
		String a5outFile = "C:\\projects\\Paperight\\watermark_test_" + DateTime.now().toString("HHmmss") + "_a5.pdf";
		createWatermarkWriter().writeWatermark(a5InFile, a5outFile, watermark, PageLayout.A5);
	}

}
