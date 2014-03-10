package com.paperight.search;

import java.util.List;

public class SearchResult<T> {
	
	private int totalResults;
	private int pageNumber;
	private int resultSize;
	private List<T> items;
	
	public int getTotalResults() {
		return totalResults;
	}
	
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
