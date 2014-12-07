package com.paperight.pdf;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class PdfExecutorTest {

    private class TestPdfExecutor extends PdfExecutor {

        @Override
        public void execute(InputStream html, OutputStream out) throws Exception {

        }

        @Override
        public File execute(String htmlLocation, List<String> cssFiles) throws Exception {
            return null;
        }
    }

    private PdfExecutor pdfExecutor;

    @Before
    public void setUp() throws Exception {
        pdfExecutor = new TestPdfExecutor();
    }

    //@Test
    public void testGetNewPdfFilename() throws Exception {
        String htmlFilename = "d:\\projects\\Paperight\\data\\product_file_uploads\\bulk_data\\5842A4FF-EDC6-893C-1B62E3AEBBADCE1E.html";
        String newFilename = pdfExecutor.getNewPdfFilename(htmlFilename);

        Assert.assertTrue(StringUtils.startsWith(newFilename, FilenameUtils.concat(System.getProperty("java.io.tmpdir"), "5842A4FF-EDC6-893C-1B62E3AEBBADCE1E")));
        Assert.assertTrue(StringUtils.endsWith(newFilename, ".pdf"));
    }
}