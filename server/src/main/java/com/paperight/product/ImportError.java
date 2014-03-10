package com.paperight.product;

import org.joda.time.DateTime;

public class ImportError {
	
	private Long id;
	private DateTime createdDate;
	private ImportExecution importExecution;
	private String error;
	private String lineContent;
	
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

	public ImportExecution getImportExecution() {
		return importExecution;
	}

	public void setImportExecution(ImportExecution importExecution) {
		this.importExecution = importExecution;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getLineContent() {
		return lineContent;
	}

	public void setLineContent(String lineContent) {
		this.lineContent = lineContent;
	}
	
}
