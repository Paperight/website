package com.paperight.product;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class ImportExecution {

	private Long id;
	private DateTime createdDate;
	private ImportStatus status = ImportStatus.STARTED;
	private String filename;
	private List<ImportError> errors = new ArrayList<ImportError>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}
	
	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}

	public List<ImportError> getErrors() {
		return errors;
	}

	public void setErrors(List<ImportError> errors) {
		this.errors = errors;
	}
	
}
