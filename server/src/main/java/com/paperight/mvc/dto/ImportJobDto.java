package com.paperight.mvc.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.paperight.Health;
import com.paperight.product.ImportItem;
import com.paperight.product.ImportItem.Status;
import com.paperight.product.ImportJob;

public class ImportJobDto {

    private ImportJob importJob;
    private List<ImportItem> importItems = new ArrayList<>();

    public ImportJob getImportJob() {
        return importJob;
    }

    public void setImportJob(ImportJob importJob) {
        this.importJob = importJob;
    }

    public List<ImportItem> getImportItems() {
        return importItems;
    }

    public void setImportItems(List<ImportItem> importItems) {
        this.importItems = importItems;
    }
    
    public int getTotalItemCount() {
        return getImportItems().size();
    }
    
    public int getTotalProcessedItemCount() {
        return Collections2.filter(getImportItems(), new Predicate<ImportItem>() {

            @Override
            public boolean apply(ImportItem input) {
                return input.getStatus() == Status.PROCESSED || input.getStatus() == Status.CANCELLED;
            }
            
        }).size();
    }
    
    public Health getItemsHealth() {
        Health health = Health.OK;
        
        Optional<ImportItem> optional = Iterables.tryFind(getImportItems(), new Predicate<ImportItem>() {

            @Override
            public boolean apply(ImportItem input) {
                return input.getHealth() == Health.ERROR;
            }

        });

        if (optional.isPresent()) {
            health = Health.ERROR;
        }

        return health;
    }
    
    public String getItemsStatus() {
        String status = "PROCESSED";
        
        Optional<ImportItem> optional = Iterables.tryFind(getImportItems(), new Predicate<ImportItem>() {

            @Override
            public boolean apply(ImportItem input) {
                return input.getStatus() != Status.PROCESSED;
            }

        });

        if (optional.isPresent()) {
            status = "PROCESSING";
        }

        return status;
    }
    
}
