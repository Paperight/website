package com.paperight.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paperight.product.ImportItem;
import com.paperight.product.ImportJob;

public interface ImportItemRepository  extends JpaRepository<ImportItem, Long> {

    ImportItem findByIdentifier(String identifier);
    List<ImportItem> findByImportJob(ImportJob importJob);
    List<ImportItem> findByImportJob(ImportJob importJob, Sort sort);
}
