package com.paperight.mvc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.paperight.FileUpload;
import com.paperight.mvc.dto.ImportJobDto;
import com.paperight.mvc.util.Pagination;
import com.paperight.product.ImportItem;
import com.paperight.product.ImportJob;
import com.paperight.repository.ImportItemRepository;
import com.paperight.repository.ImportJobRepository;
import com.paperight.service.ImportJobService;

@Controller
@RequestMapping(value = "/bulk")
public class BulkImportController {
    
    private Logger logger = LoggerFactory.getLogger(BulkImportController.class);

    @Autowired
    private ImportJobRepository importJobRepository;
    
    @Autowired
    private ImportItemRepository importItemRepository;
    
    @Autowired
    private ImportJobService importJobService;

    @Value("${product.file.upload.folder}")
    private String productFileUploadFolder;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "bulk/upload";
    }
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(FileUpload fileUpload, BindingResult result) {
        if (!fileUpload.getFile().isEmpty()) {
            try {
                saveFile(fileUpload.getFile());
            } catch (Throwable e) {
                result.reject(e.getMessage());
                return "bulk/upload";
            }
           return "bulk/upload";
       } else {
           return "bulk/upload";
       }
    }
    
    private static final int PAGE_SIZE = 10;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(Model model, @PageableDefault(size = PAGE_SIZE, sort="createdDate", direction=Direction.DESC) Pageable pageable) {
        Page<ImportJob> page = importJobRepository.findAll(pageable);
        Pagination.buildPaginationModel(model, page);
        List<ImportJobDto> importJobDtos = createImportJobDtos(page.getContent());
        model.addAttribute("importJobDtos", importJobDtos);
        return "bulk/jobs";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(Model model, @PathVariable("id") ImportJob importJob, @PageableDefault(size = PAGE_SIZE, sort="createdDate", direction=Direction.DESC) Pageable pageable) {
        ImportJobDto importJobDto = createImportJobDto(importJob);
        model.addAttribute("importJobDto", importJobDto);
        
        return "bulk/detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/items.html", produces = "text/html")
    public String items(@PathVariable("id") ImportJob importJob, Model model, HttpServletRequest request) throws Exception {
        List<ImportItem> importItems = importItemRepository.findByImportJob(importJob);
        model.addAttribute("importItems", importItems);
        return "bulk/items";
    }
    
    @RequestMapping(value = "/item/{id}/cancel", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Object cancel(@PathVariable("id") ImportItem importItem) {
        importJobService.cancelImportItem(importItem);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
    
    @RequestMapping(value = "/item/{id}/apply", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Object apply(@PathVariable("id") ImportItem importItem) {
        importJobService.applyImportItem(importItem);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
    
    private List<ImportJobDto> createImportJobDtos(List<ImportJob> importJobs) {
        List<ImportJobDto> dtos = new ArrayList<>();
        for (ImportJob importJob : importJobs) {
            ImportJobDto dto = createImportJobDto(importJob);
            dtos.add(dto);
        }
        return dtos;
    }
    
    private ImportJobDto createImportJobDto(ImportJob importJob) {
        ImportJobDto dto = new ImportJobDto();
        dto.setImportJob(importJob);
        List<ImportItem> importItems = importItemRepository.findByImportJob(importJob, new Sort("health", "status")); 
        dto.setImportItems(importItems);
        return dto;
    }
    
    private void saveFile(MultipartFile uploadFile) throws Exception {
        String filename = uploadFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        filename = FilenameUtils.getBaseName(filename) + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + "." + extension;
        filename = FilenameUtils.concat(productFileUploadFolder, filename);
        logger.info("saving uploaded file " + uploadFile.getOriginalFilename() + " to " + filename);
        File file = new File(filename);
        FileUtils.writeByteArrayToFile(file, uploadFile.getBytes());
    }
    
}
