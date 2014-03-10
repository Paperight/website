package com.paperight.product.batch;

public class NoProductFilesException extends ProductItemProcessException {

	private static final long serialVersionUID = -2110024772550043604L;
	
	public NoProductFilesException(String message) {
		super(message);
	}

}
