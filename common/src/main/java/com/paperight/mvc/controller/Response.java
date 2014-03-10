package com.paperight.mvc.controller;

public class Response {
	
	private ResponseStatus status = ResponseStatus.ERROR;
	private Object responseObject;	

	public Object getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	
}

enum ResponseStatus {
	
	ERROR,
	OK;
	
}