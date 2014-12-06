package com.paperight.service;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.io.PatternFilenameFilter;
import com.paperight.Health;
import com.paperight.product.ImportItem;
import com.paperight.product.ImportItem.Status;
import com.paperight.product.ImportJob;
import com.paperight.repository.ImportItemRepository;

public abstract class FileService {
    
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected ImportItemRepository importItemRepository;

    @Autowired
    protected ImportJobService importJobService;

    @Value("${bulk.product.data.folder}")
    private String bulkProductDataFolder;

    @Value("${pdf.file.folder}")
    private String existingPdfFileFolder;
    
    @Value("${jacket.image.file.folder}")
    private String existingJacketImagesFileFolder;

    public abstract void splitFile(ImportJob importJob, String filename) throws Exception;

    public abstract void validateFile(String filename) throws Exception;

    public void extractImportItem(ImportItem importItem) {
        try {
            extractRawData(importItem);
            if (!importItemFileExists(importItem)) {
                throw new Exception("No PDF's or HTML files found");
            }
            importItem.setStatus(Status.EXTRACTED);
        } catch (Exception e) {
            logger.error("Unable to extract ImportItem", e);
            importItem.setHealth(Health.ERROR);
            importItem.setError(e.getMessage());
        }
        importItemRepository.save(importItem);
    }
    
    private boolean importItemFileExists(ImportItem importItem) throws Exception {
        boolean result = false;
        String[] files = unprocessedImportItemFiles(importItem);
        if (files == null || files.length == 0) {
            result = true;
        }
        if (!result) {
            files = existingImportItemFiles(importItem);
            if (files == null || files.length == 0 || importItem.canPhotocopy()) {
                result = true;
            }
        }
        return result;
    }
    
    public String[] unprocessedImportItemFiles(ImportItem importItem) {
        String[] pdfFiles = unprocessedImportItemPdfFiles(importItem);
        String[] htmlFiles = unprocessedImportItemHtmlFiles(importItem);
        return ArrayUtils.addAll(pdfFiles, htmlFiles);
    }
    
    public String[] unprocessedImportItemPdfFiles(ImportItem importItem) {
        File folder = new File(bulkProductDataFolder);
        return folder.list(new PatternFilenameFilter("(?i)" + Pattern.quote(importItem.getIdentifier()) + "\\.(pdf)"));
    }
    
    public String[] unprocessedImportItemHtmlFiles(ImportItem importItem) {
        File folder = new File(bulkProductDataFolder);
        return folder.list(new PatternFilenameFilter("(?i)" + Pattern.quote(importItem.getIdentifier()) + "\\.(htm|html)"));
    }
    
    public String[] existingImportItemFiles(ImportItem importItem) {
        File folder = new File(existingPdfFileFolder);
        return folder.list(new PatternFilenameFilter("(?i)" + Pattern.quote(importItem.getIdentifier()) + "\\.(pdf)"));
    }
    
    public String[] unprocessedImportItemJacketImages(ImportItem importItem) {
        File folder = new File(bulkProductDataFolder);
        logger.debug("unprocessedImportItemJacketImages folder: " + folder.getAbsolutePath());
        return folder.list(new PatternFilenameFilter("(?i)" + Pattern.quote(importItem.getIdentifier()) + "\\.(jpg|png)"));
    }

    public String getPdfFilename(ImportItem importItem) {
        String result = null;
        String[] files = unprocessedImportItemPdfFiles(importItem);
        if (files.length > 0) {
            result = FilenameUtils.concat(bulkProductDataFolder, files[0]);
        }
        return result;
    }
    
    public String getHtmlFilename(ImportItem importItem) {
        String result = null;
        String[] files = unprocessedImportItemHtmlFiles(importItem);
        if (files.length > 0) {
            result = FilenameUtils.concat(bulkProductDataFolder, files[0]);
        }
        return result;
    }
    
    public String getJacketImageFilename(ImportItem importItem) {
        String result = null;
        String[] files = unprocessedImportItemJacketImages(importItem);
        logger.debug("getJacketImageFilename files: " + files);
        if (files.length > 0) {
            result = FilenameUtils.concat(bulkProductDataFolder, files[0]);
        }
        return result;
    }
    
    protected abstract void extractRawData(ImportItem importItem) throws Exception;

    protected void saveImportItems(ImportJob importJob, List<String> rawDataList) {
        for (String rawData : rawDataList) {
            createImportItem(importJob, rawData);
        }
    }

    protected void createImportItem(ImportJob importJob, String rawData) {
        ImportItem importItem = new ImportItem();
        importItem.setImportJob(importJob);
        importItem.setRawData(rawData);
        importItemRepository.save(importItem);
    }

}
