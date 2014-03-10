package com.paperight.pdf;

import static com.amphisoft.util.Print.printlnerr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;

import com.amphisoft.epub.Epub;
import com.amphisoft.epub.metadata.Ncx;
import com.amphisoft.epub.metadata.Ncx.NavPoint;
import com.amphisoft.epub.metadata.Opf;
import com.amphisoft.epub2pdf.content.TextFactory;
import com.amphisoft.epub2pdf.content.XhtmlHandler;
import com.amphisoft.epub2pdf.metadata.TocTreeNode;
import com.amphisoft.pdf.ITPageSize;
import com.amphisoft.util.jgtree.Tree;
import com.amphisoft.util.jgtree.TreeNode;
import com.amphisoft.util.units.Length;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.RectangleReadOnly;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

@Configurable
public class EpubToPdfConverter {
	
	private Logger logger = LoggerFactory.getLogger(EpubToPdfConverter.class);
	
	protected static float marginTopPt = 8.0f;
    protected static float marginRightPt = 8.0f;
    protected static float marginBottomPt = 8.0f;
    protected static float marginLeftPt = 8.0f;
    protected static Rectangle pageSize = ITPageSize.FOXIT_ESLICK;
    private static Properties epub2pdfProps;
    
    private static void loadProperties() {
    	epub2pdfProps = new Properties();
        String propsFilename = "epub2pdf.properties";
        try {
        	ClassPathResource classPathResource = new ClassPathResource("epub2pdf.properties");
    		InputStreamReader reader = new InputStreamReader(classPathResource.getInputStream());
    		try {
    			epub2pdfProps.load(reader);
    		} finally {
    			reader.close();
    		}
        } catch (IOException e) {
            printlnerr("IOException reading properties from " + propsFilename + "; continuing anyway");
        }    	
    }

    static {
    	loadProperties();
    }
    
    private Epub epubIn;
	File outputDir = new File(System.getProperty("java.io.tmpdir"));

    public void applyProperties(Properties props) {
        SortedSet<String> propsSorted = new TreeSet<String>();
        for (Object o : props.keySet()) {
            propsSorted.add(o.toString());
        }
        for (String propName : propsSorted) {
            if ("font.default.name".equals(propName)) {
                setDefaultFont(props);
            }
            if ("font.default.sizebase".equals(propName)) {
                setDefaultFontBaseSize(props);
            }
            if ("font.monospace.name".equals(propName)) {
                setMonospaceFont(props);
            }
            if ("font.monospace.sizebase".equals(propName)) {
                setMonospaceFontBaseSize(props);
            }
            if ("page.size".equals(propName)) {
                setPageSize(props);
            }
            if ("margins.size".equals(propName)) {
                setMargins(props);
            }
            if ("output.dir".equals(propName)) {
                setOutputDir(props);
            }
            if ("full.justify".equals(propName)) {
                setDefaultAlignment(props);
            }
        }
    }

    private void setDefaultAlignment(Properties props) {
        String justify = props.getProperty("full.justify").trim();
        if (justify.toLowerCase().contains("true")) {
            XhtmlHandler.setDefaultAlignment(Paragraph.ALIGN_JUSTIFIED);
            logger.debug("Default paragraph alignment: justified");
        } else {
            XhtmlHandler.setDefaultAlignment(Paragraph.ALIGN_LEFT);
            logger.debug("Default paragraph alignment: left");
        }
    }

    private void setOutputDir(Properties props) {
        String outputDirStr = props.getProperty("output.dir").trim();
        if (outputDirStr.length() == 0) {
            // leave as default
        } else {
            outputDir = new File(outputDirStr);
            if (!(outputDir.canRead() && outputDir.canWrite())) {
            	logger.error("Cannot access output directory " + outputDirStr);
                System.exit(1);
            }
        }
    }

    private void setPageSize(Properties props) {
        String pageSizeStr = props.getProperty("page.size").trim();
        pageSizeStr = pageSizeStr.toLowerCase();
        if (pageSizeStr.contains("x")) {
            int sepIdx = pageSizeStr.indexOf('x');
            try {
                String widthStr = pageSizeStr.substring(0,sepIdx);
                String heightStr = pageSizeStr.substring(sepIdx+1);

                Length width = Length.fromString(widthStr);
                Length height = Length.fromString(heightStr);

                logger.debug(
                    "Page size (w x h): " + width.toString() +
                    " x " + height.toString());

                pageSize = new RectangleReadOnly(
                    width.toPoints().getMagnitude(),
                    height.toPoints().getMagnitude());

            } catch (IndexOutOfBoundsException ioobe) {
                pageSizeErrorNotice();
            }
        } else {
            pageSizeErrorNotice();
        }

    }

    private void pageSizeErrorNotice() {
    	logger.error("Could not parse page size string; using default 90mmx115mm");
    }

    private void setMonospaceFontBaseSize(Properties props) {
        String sizeStr = props.getProperty("font.monospace.sizebase").trim();
        if (sizeStr == null) {
            sizeStr = "10pt";
        }
        Length mfbsLength = Length.fromString(sizeStr);
        float size = mfbsLength.toPoints().getMagnitude();
        TextFactory.setDefaultFontMonoSize(size);
        logger.debug("Monospace base size: " + size + "pt");
    }

    private void setMonospaceFont(Properties props) {
        String fontName = props.getProperty("font.monospace.name").trim();
        if (TextFactory.setDefaultFontMonoByName(fontName)) {
        	logger.debug("Default monospace font: " + fontName);
        } else {
        	logger.error("Failed to set default monospace font to " + fontName + "; retaining previous value");
        }
    }

    private void setMargins(Properties props) {
        String marginStr = props.getProperty("margins.size").trim();
        StringTokenizer sT = new StringTokenizer(marginStr, ",");
        ArrayList<String> marginParams = new ArrayList<String>();
        while (sT.hasMoreTokens()) {
            marginParams.add(sT.nextToken());
        }
        int marginParamCount = marginParams.size();
        if (marginParamCount < 1 || marginParamCount > 4) {
            throw new IllegalArgumentException("Could not parse margins.size; retaining previous value");
        } else {
            ArrayList<Length> margins = new ArrayList<Length>();
            for (String param : marginParams) {
                margins.add(Length.fromString(param));
            }
            marginTopPt = margins.get(0).toPoints().getMagnitude();
            marginRightPt = margins.get(1).toPoints().getMagnitude();
            marginBottomPt = marginTopPt;
            marginLeftPt = marginRightPt;
            if (margins.size()>2)
                marginBottomPt = margins.get(2).toPoints().getMagnitude();
            if (margins.size()>3)
                marginLeftPt = margins.get(3).toPoints().getMagnitude();

            String logMessage = "Margins (top right bottom left): ";
            for (Length el: margins) {
            	logMessage += el.toString() + " ";
            }
            logger.debug(logMessage);
        }
    }

    private void setDefaultFontBaseSize(Properties props) {
        String sizeStr = props.getProperty("font.default.sizebase").trim();
        if (sizeStr == null) {
            sizeStr = "12pt";
        }
        Length dfbsLength = Length.fromString(sizeStr);
        float size = dfbsLength.toPoints().getMagnitude();
        TextFactory.setDefaultFontSize(size);
        logger.debug("Default font base size set to " + size + "pt");
    }

    private void setDefaultFont(Properties props) {
        String fontName = props.getProperty("font.default.name").trim();
        if (TextFactory.setDefaultFontByName(fontName)) {
        	logger.debug("Default font set to " + fontName);
        } else {
        	logger.error("Failed to reset default font to " + fontName);
        }
    }
	
	public File convert(String epubPath) throws IOException,DocumentException {
		applyProperties(epub2pdfProps);
        File epubFile = new File(epubPath);
        if (!(epubFile.canRead())) {
            throw new IOException("Could not read " + epubPath);
        } else {
        	logger.debug("Converting " + epubFile.getAbsolutePath());
        }
        String epubFilename = epubFile.getName();
        String epubFilenameBase = epubFilename.substring(0, epubFilename.length()-5);
        String pdfFilename = epubFilenameBase + ".pdf";

        File outputFile = new File(outputDir.getAbsolutePath() + File.separator + pdfFilename);

        epubIn = Epub.fromFile(epubPath);
        XhtmlHandler.setSourceEpub(epubIn);

        Opf opf = epubIn.getOpf();
        List<String> contentPaths = opf.spineHrefs();
        List<File> contentFiles = new ArrayList<File>();
        for (String path : contentPaths) {
            contentFiles.add(new File(epubIn.getContentRoot(),path));
        }
        Ncx ncx = epubIn.getNcx();
        
        List<NavPoint> ncxToc = new ArrayList<NavPoint>();
        if(ncx != null) {
        	ncxToc.addAll(ncx.getNavPointsFlat());
        }
        
        Tree<TocTreeNode> tocTree = TocTreeNode.buildTocTree(ncx);
        XhtmlHandler.setTocTree(tocTree);
        
        Document doc = new Document();
        boolean pageSizeOK = doc.setPageSize(pageSize);
        boolean marginsOK = doc.setMargins(marginLeftPt, marginRightPt, marginTopPt, marginBottomPt);

        logger.debug("Writing PDF to " + outputFile.getAbsolutePath());
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
        writer.setStrictImageSequence(true);
        PdfOutline bookmarkRoot = null;

        if (!(pageSizeOK && marginsOK)) {
            throw new RuntimeException("Failed to set PDF page size a/o margins");
        }
        
        int fileCount = contentFiles.size();
        logger.debug("Processing " + fileCount + " HTML file(s): ");
        int currentFile = 0;

        for (File file : contentFiles) {
        	currentFile++;
        	
        	String progressChar;
        	
        	int mod10 = currentFile % 10;
        	if(mod10 == 5)
        		progressChar = "5";
        	else if(mod10 == 0) 
        		progressChar = "0";
        	else
        		progressChar = ".";
        	
        	logger.debug(progressChar);
            if (!(doc.isOpen())) {
                doc.open();
                doc.newPage();
                bookmarkRoot = writer.getRootOutline();
                XhtmlHandler.setBookmarkRoot(bookmarkRoot);
            }
            NavPoint fileLevelNP = Ncx.findNavPoint(ncxToc, file.getName());
            TreeNode<TocTreeNode> npNode = TocTreeNode.findInTreeByNavPoint(tocTree, fileLevelNP);
            
            if(fileLevelNP != null) {
            	doc.newPage();
            	PdfOutline pdfOutlineParent = bookmarkRoot;
            	if(npNode != null) {
            		TreeNode<TocTreeNode> parent = npNode.getParent();
            		if(parent != null) {
            			TocTreeNode parentTTN = parent.getValue();
            			if(parentTTN != null && parentTTN.getPdfOutline() != null) {
            				pdfOutlineParent = parentTTN.getPdfOutline();
            			}
            		}
            	}
            	
            	PdfDestination here = new PdfDestination(PdfDestination.FIT);
            	PdfOutline pdfTocEntry = new PdfOutline(pdfOutlineParent, here, fileLevelNP.getNavLabelText());
            	if(npNode != null) {
            		npNode.getValue().setPdfDestination(here);
            		npNode.getValue().setPdfOutline(pdfTocEntry);
            	}
            }
            XhtmlHandler.process(file.getCanonicalPath(), doc);
        }
        doc.close();
        logger.debug("PDF written to " + outputFile.getAbsolutePath());
        epubIn.cleanup();
        return outputFile;
	}

}
