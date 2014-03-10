package com.paperight.product.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.product.ImportError;
import com.paperight.product.ImportExecution;
import com.paperight.product.ImportStatus;

public class ProductItemListener extends ItemListenerSupport<ProductImportItem, ProductImportItem> implements  StepExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(ProductItemListener.class);
	private String inputFilename;
	
	@Override
	public void onReadError(Exception e) {
		logger.error("read error");
		ImportExecution importExecution = ImportExecution.findByFilename(getInputFilename());
		if (importExecution != null) {
			ImportError importError = new ImportError();
			importError.setImportExecution(importExecution);
			importError.setError(e.getCause().getMessage() + e.getMessage());
			importError.persist();
		}
	}
	
	@Override
	public void onProcessError(ProductImportItem item, Exception e) {
		logger.error("process error");
		ImportExecution importExecution = ImportExecution.findByFilename(getInputFilename());
		if (importExecution != null) {
			ImportError importError = new ImportError();
			importError.setImportExecution(importExecution);
			importError.setError(e.getMessage());
			importError.setLineContent(item.getImportLine());
			importError.persist();
		}
	}
	
	@Override
	public void onWriteError(Exception ex, List<? extends ProductImportItem> item) {
		logger.error("write error");
		ImportExecution importExecution = ImportExecution.findByFilename(getInputFilename());
		if (importExecution != null) {
			for (ProductImportItem productImportItem : item) {
				ImportError importError = new ImportError();
				importError.setImportExecution(importExecution);
				importError.setError(ex.getMessage());
				importError.setLineContent(productImportItem.getImportLine());
				importError.persist();
			}
			
		}
	}
	
	public String getInputFilename() {
		return inputFilename;
	}

	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ImportExecution importExecution = new ImportExecution();
		importExecution.setFilename(getInputFilename());
		importExecution.persist();
	}

	@Transactional
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ImportExecution importExecution = ImportExecution.findByFilename(getInputFilename());
		if (importExecution != null) {
			if (importExecution.hasErrors()) {
				importExecution.setStatus(ImportStatus.COMPLETE_WITH_ERRORS);
			} else {
				importExecution.setStatus(ImportStatus.COMPLETE);
			}
			importExecution.merge();
		}
		return null;
	}

}
