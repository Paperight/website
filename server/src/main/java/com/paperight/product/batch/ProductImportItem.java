package com.paperight.product.batch;

import com.paperight.product.Product;

public class ProductImportItem {
	
	private Product product;
	private String importLine;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getImportLine() {
		return importLine;
	}

	public void setImportLine(String importLine) {
		this.importLine = importLine;
	}
	
	public static ProductImportItem newInstance(Product product, String importLine) {
		ProductImportItem productImportItem = new ProductImportItem();
		productImportItem.setProduct(product);
		productImportItem.setImportLine(importLine);
		return productImportItem;
	}

}
