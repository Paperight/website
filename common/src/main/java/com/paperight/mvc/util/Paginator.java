package com.paperight.mvc.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class Paginator {

	public static int DEFAULT_MAX_LINKED_PAGES = 5;
	public static final String GET_VAR_PAGE_NUMBER_NAME = "p";

	private int itemCount = 0;
	private int pageCount = 0;
	private int page = 0;
	private int pageSize = 0;
	private int maxLinkedPages = DEFAULT_MAX_LINKED_PAGES;
	private String url;

	public Paginator(int itemCount, int pageSize, int page, HttpServletRequest request) {
		this.itemCount = itemCount;
		this.pageSize = pageSize;
		this.pageCount = (int) Math.ceil((double) this.itemCount / (double) this.pageSize) ;
		this.page = page;
		this.url = buildUrl(request);
	}
	
	private String buildUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		url.append(UrlUtils.buildRequestUrl(request));
		
		String queryParams = UrlUtils.getQueryParams(request, GET_VAR_PAGE_NUMBER_NAME);
		url.append("?");
		if (!StringUtils.isBlank(queryParams)) {
			url.append(queryParams);
			url.append("&");
		}
		url.append(GET_VAR_PAGE_NUMBER_NAME + "=");
		return url.toString();
	}

	public int getFirstLinkedPage() {
		return Math.max(0, getPage() - (getMaxLinkedPages() / 2));
	}

	public int getLastLinkedPage() {
		return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isFirstPage() {
		return (page == 1);
	}

	public boolean isLastPage() {
		return (page == pageCount);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxLinkedPages() {
		return maxLinkedPages;
	}

	public void setMaxLinkedPages(int maxLinkedPages) {
		this.maxLinkedPages = maxLinkedPages;
	}
	
	public String getUrl() {
		return url;
	}

}