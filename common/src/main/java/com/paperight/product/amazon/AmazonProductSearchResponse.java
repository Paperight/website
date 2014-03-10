package com.paperight.product.amazon;

import java.util.ArrayList;
import java.util.List;

import com.paperight.product.ThirdPartyProduct;

public class AmazonProductSearchResponse {
	
	private int totalResults;
	private List<ThirdPartyProduct> products = new ArrayList<ThirdPartyProduct>();

	public List<ThirdPartyProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ThirdPartyProduct> products) {
		this.products = products;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

}
