package com.paperight.service;

import com.paperight.Application;
import com.paperight.Health;
import com.paperight.licence.PageLayout;
import com.paperight.pdf.PdfAutomationService;
import com.paperight.pdf.PdfConversion;
import com.paperight.product.ImportItem;
import com.paperight.product.ImportItem.Status;
import com.paperight.product.ImportJob;
import com.paperight.product.Product;
import com.paperight.repository.ImportItemRepository;
import com.paperight.repository.ImportJobRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportJobService {
    
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ImportJobRepository importJobRepository;
    
    @Autowired
    private ImportItemRepository importItemRepository;
    
    @Autowired
    private PdfAutomationService pdfAutomationService;
    
    @Autowired
    private Application application;
    
    @Value("${converted.bulk.product.data.folder}")
    private String convertedPdfFolder;
    
    @Value("${pdf.file.folder}")
    private String finalPdfFileFolder;
    
    @Value("${jacket.image.file.folder}")
    private String jacketImageFolder;
    
    @Autowired
    private FileService fileService;

    public ImportJob createImportJob(String filename) {
        ImportJob importJob = new ImportJob();
        importJob.setFilename(filename);
        return importJobRepository.save(importJob);
    }
    
    public void processExtractedImportItem(ImportItem importItem) {
        try {
            String jacketImageFilename = fileService.getJacketImageFilename(importItem);
            if (!StringUtils.isBlank(jacketImageFilename)) {
                importItem.setJacketImageFilename(jacketImageFilename);
            }
            String pdfFilename = fileService.getPdfFilename(importItem);
            String htmlFilename = fileService.getHtmlFilename(importItem);
            if (!StringUtils.isBlank(pdfFilename) || !StringUtils.isBlank(htmlFilename)) {
                if (!StringUtils.isBlank(pdfFilename)) {
                    convertPdf(pdfFilename, importItem);
                } else {
                    convertHtml(htmlFilename, importItem);
                }
                importItem.setStatus(Status.CONVERTED);
            } else if (importItem.isHasPrintFormats()) {
                throw new Exception("No PDF or HTML file found to convert");
            } else {
                importItem.setStatus(Status.META_ONLY);
            }
        } catch (Exception e) {
            logger.error("Unable to process extracted ImportItem", e);
            importItem.setHealth(Health.ERROR);
            importItem.setError(e.getMessage());
        }
        importItemRepository.save(importItem);
    }
    
    private void convertPdf(String pdfFilename, ImportItem importItem) throws Exception {
        File newPdf = null;
        File newA5Pdf = null;
        if (importItem.hasA5Format()) {
            newA5Pdf = new File(pdfFilename);
        } else {
            newPdf = new File(pdfFilename);
        }
        PdfConversion pdfConversion = pdfAutomationService.createPaperightPdfs(newPdf, newA5Pdf, importItem.getFormatList(), convertedPdfFolder);
        if (!StringUtils.isBlank(pdfConversion.getOneUpFilename())) {
            importItem.setOneUpFilename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getOneUpFilename()));
        }
        if (!StringUtils.isBlank(pdfConversion.getTwoUpFilename())) {
            importItem.setTwoUpFilename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getTwoUpFilename()));
        }
        if (!StringUtils.isBlank(pdfConversion.getA5Filename())) {
            importItem.setA5Filename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getA5Filename()));
        }
    }
    
    @Value("${html.pdf.css.folder}")
    private String cssFolder;
    
    @Value("${bulk.default.css.file}")
    private String defaultCssFile;
    
    private void convertHtml(String htmlFilename, ImportItem importItem) throws Exception {
        List<String> cssFilesList = new ArrayList<String>();
        if (!StringUtils.isBlank(importItem.getCssFile())) {
            File cssFile = new File(cssFolder, importItem.getCssFile());
            if (!cssFile.exists()) {
                throw new Exception(String.format("Cannot find specified CSS file '%s' in folder '%s'", importItem.getCssFile(), cssFolder));
            }
            cssFilesList.add(importItem.getCssFile());
        } else {
            File cssFile = new File(cssFolder, defaultCssFile);
            if (!cssFile.exists()) {
                throw new Exception(String.format("Cannot find default CSS file '%s' in folder '%s'", defaultCssFile, cssFolder));
            }
            cssFilesList.add(defaultCssFile);
        }
        File newPdf = null;
        if (importItem.hasFormat(PageLayout.ONE_UP) || importItem.hasFormat(PageLayout.TWO_UP)) {
            newPdf = pdfAutomationService.htmlToPdf(htmlFilename, cssFilesList, application.getDefaultPdfConversion());
        }
        File newA5Pdf = null;
        if (importItem.hasA5Format()) {
            cssFilesList.add("a5.css");
            newA5Pdf = pdfAutomationService.htmlToPdf(htmlFilename, cssFilesList, application.getDefaultPdfConversion());
        }
        try {
            PdfConversion pdfConversion = pdfAutomationService.createPaperightPdfs(newPdf, newA5Pdf, importItem.getFormatList(), convertedPdfFolder);
            if (!StringUtils.isBlank(pdfConversion.getOneUpFilename())) {
                importItem.setOneUpFilename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getOneUpFilename()));
            }
            if (!StringUtils.isBlank(pdfConversion.getTwoUpFilename())) {
                importItem.setTwoUpFilename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getTwoUpFilename()));
            }
            if (!StringUtils.isBlank(pdfConversion.getA5Filename())) {
                importItem.setA5Filename(FilenameUtils.concat(convertedPdfFolder, pdfConversion.getA5Filename()));
            }
        } finally {
            FileUtils.deleteQuietly(newA5Pdf);
            FileUtils.deleteQuietly(newPdf);
        }
        
    }

    public void cancelImportItem(ImportItem importItem) {
        importItem.setStatus(Status.CANCELLED);
        importItem = importItemRepository.save(importItem);
        if (!StringUtils.isBlank(importItem.getOneUpFilename())) {
            FileUtils.deleteQuietly(new File(importItem.getOneUpFilename()));
        }
        if (!StringUtils.isBlank(importItem.getTwoUpFilename())) {
            FileUtils.deleteQuietly(new File(importItem.getTwoUpFilename()));
        }
        if (!StringUtils.isBlank(importItem.getA5Filename())) {
            FileUtils.deleteQuietly(new File(importItem.getA5Filename()));
        }
    }

    public void applyImportItem(ImportItem importItem) {
        try {
            applyFiles(importItem);
            Product product = mergeWithProduct(importItem);
            importItem.setProduct(product);
            importItem.setStatus(Status.PROCESSED);
        } catch (Exception exception) {
            logger.error("Unable to apply ImportItem", exception);
            importItem.setError(exception.getMessage());
            importItem.setHealth(Health.ERROR);
        }
        importItemRepository.save(importItem);
    }
    
    private void applyFiles(ImportItem importItem) throws IOException {
        if (!StringUtils.isBlank(importItem.getOneUpFilename())) {
            moveFile(importItem.getOneUpFilename(), finalPdfFileFolder);
        }
        if (!StringUtils.isBlank(importItem.getTwoUpFilename())) {
            moveFile(importItem.getTwoUpFilename(), finalPdfFileFolder);
        }
        if (!StringUtils.isBlank(importItem.getA5Filename())) {
            moveFile(importItem.getA5Filename(), finalPdfFileFolder);
        } 
        if (!StringUtils.isBlank(importItem.getJacketImageFilename())) {
            moveFile(importItem.getJacketImageFilename(), jacketImageFolder);
        }
    }

    private void moveFile(String filename, String destination) throws IOException {
        createFolder(new File(destination));
        FileUtils.copyFileToDirectory(new File(filename), new File(destination));
        FileUtils.deleteQuietly(new File(filename));
    }

    private void createFolder(File folder) throws FileNotFoundException {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!folder.exists()) {
            throw new FileNotFoundException("Destination directory '" + folder + "' does not exist");
        }
    }

    private Product mergeWithProduct(ImportItem importItem) throws Exception {
        Product product = Product.findByIdentifierAndIdentifierType(importItem.getIdentifier(), importItem.getIdentifierType());
        if (product == null) {
            product = new Product();
            product.setIdentifier(importItem.getIdentifier());
            product.setIdentifierType(importItem.getIdentifierType());
        }
        
        if (!StringUtils.isBlank(importItem.getCopyrightStatus())) {
            product.setCopyrightStatus(importItem.getCopyrightStatus());
        }
        if (!StringUtils.isBlank(importItem.getPublisher())) {
            product.setPublisher(importItem.getPublisher());
        }
        if (!StringUtils.isBlank(importItem.getTitle())) {
            product.setTitle(importItem.getTitle());
        }
        if (!StringUtils.isBlank(importItem.getSubTitle())) {
            product.setSubTitle(importItem.getSubTitle());
        }
        if (!StringUtils.isBlank(importItem.getAlternativeTitle())) {
            product.setAlternativeTitle(importItem.getAlternativeTitle());
        }
        if (!StringUtils.isBlank(importItem.getPrimaryCreators())) {
            product.setPrimaryCreators(importItem.getPrimaryCreators());
        }
        if (!StringUtils.isBlank(importItem.getSecondaryCreators())) {
            product.setSecondaryCreators(importItem.getSecondaryCreators());
        }
        if (!StringUtils.isBlank(importItem.getEdition())) {
            product.setEdition(importItem.getEdition());
        }
        if (!StringUtils.isBlank(importItem.getPrimaryLanguages())) {
            product.setPrimaryLanguages(importItem.getPrimaryLanguages());
        }
        if (!StringUtils.isBlank(importItem.getSecondaryLanguages())) {
            product.setSecondaryLanguages(importItem.getSecondaryLanguages());
        }
        if (!StringUtils.isBlank(importItem.getSubjectArea())) {
            product.setSubjectArea(importItem.getSubjectArea());
        }
        if (importItem.getPublicationDate() != null) {
            product.setPublicationDate(importItem.getPublicationDate());
        }
        if (importItem.getEmbargoDate() != null) {
            product.setEmbargoDate(importItem.getEmbargoDate());
        }
        if (importItem.getLicenceFeeInDollars() != null) {
            product.setLicenceFeeInDollars(importItem.getLicenceFeeInDollars());
        }
        if (!StringUtils.isBlank(importItem.getShortDescription())) {
            product.setShortDescription(importItem.getShortDescription());
        }
        if (!StringUtils.isBlank(importItem.getLongDescription())) {
            product.setLongDescription(importItem.getLongDescription());
        }
        if (!StringUtils.isBlank(importItem.getParentIsbn())) {
            product.setParentIsbn(importItem.getParentIsbn());
        }
        if (!StringUtils.isBlank(importItem.getAlternateIsbn())) {
            product.setAlternateIsbn(importItem.getAlternateIsbn());
        }
        if (!StringUtils.isBlank(importItem.getAudience())) {
            product.setAudience(importItem.getAudience());
        }
        if (!StringUtils.isBlank(importItem.getDisallowedCountries())) {
            product.setDisallowedCountries(importItem.getDisallowedCountries());
        }
        if (!StringUtils.isBlank(importItem.getTags())) {
            product.setTags(importItem.getTags());
        }
        if (!StringUtils.isBlank(importItem.getUrl())) {
            product.setUrl(importItem.getUrl());
        }
        if (!StringUtils.isBlank(importItem.getUrlCallToAction())) {
            product.setUrlCallToAction(importItem.getUrlCallToAction());
        }
        product.setSupportsAds(importItem.isSupportsAds());
        if (!StringUtils.isBlank(importItem.getSamplePageRange())) {
            product.setSamplePageRange(importItem.getSamplePageRange());
        }
        if (!StringUtils.isBlank(importItem.getLicenceStatement())) {
            product.setLicenceStatement(importItem.getLicenceStatement());
        }

        if (!StringUtils.isBlank(importItem.getOneUpFilename())) {
            product.setOneUpFilename(FilenameUtils.getName(importItem.getOneUpFilename()));
        }
        if (!StringUtils.isBlank(importItem.getTwoUpFilename())) {
            product.setTwoUpFilename(FilenameUtils.getName(importItem.getTwoUpFilename()));
        }
        if (!StringUtils.isBlank(importItem.getA5Filename())) {
            product.setA5Filename(FilenameUtils.getName(importItem.getA5Filename()));
        }
        if (!StringUtils.isBlank(importItem.getJacketImageFilename())) {
            product.setJacketImageFilename(FilenameUtils.getName(importItem.getJacketImageFilename()));
        }
        
        if (importItem.canPhotocopy()) {
            product.setCanPhotocopy(importItem.canPhotocopy());
        }
        
        product = product.merge();
        return product;
    }

}
