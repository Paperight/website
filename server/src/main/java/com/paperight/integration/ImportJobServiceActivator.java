package com.paperight.integration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.paperight.Health;
import com.paperight.product.ImportJob;
import com.paperight.product.ImportJob.Status;
import com.paperight.repository.ImportJobRepository;
import com.paperight.service.CsvFileService;
import com.paperight.service.ImportJobService;

@Component
public class ImportJobServiceActivator {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ImportJobService importJobService;

    @Autowired
    private ImportJobRepository importJobRepository;

    @Autowired
    private CsvFileService csvFileService;

    @Value("${product.file.upload.staged.folder}")
    private String stagingFolder;

    public void stageFile(File file) throws IOException {
        String filename = FilenameUtils.getName(file.getPath());
        FileUtils.moveFileToDirectory(file, new File(stagingFolder), true);
        importJobService.createImportJob(FilenameUtils.concat(stagingFolder, filename));
        return;
    }

    public void processNewCsv(ImportJob importJob) {
        try {
            csvFileService.validateFile(importJob.getFilename());
            csvFileService.splitFile(importJob, importJob.getFilename());
            importJob.setStatus(Status.PROCESSED);
        } catch (Exception e) {
            logger.error("unable to process CSV file", e);
            importJob.setHealth(Health.ERROR);
            importJob.setError(e.getMessage());
        }
        importJobRepository.save(importJob);
    }

}
