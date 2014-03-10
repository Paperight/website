package com.paperight.licence;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.paperight.product.Product;
import com.paperight.user.User;

public abstract class PreviewCreatorImpl implements PreviewCreator {
	
	private String source;
	private OutputStream target;
	private String pages;
	private User user;
	private Product product;
	private PageLayout layout;
	private List<Integer> pagesList = new ArrayList<Integer>();
	
	public void createPreview(String source, OutputStream target, String pages, User user, Product product, PageLayout layout) throws Exception {
		setSource(source);
		setTarget(target);
		setPages(pages);
		setUser(user);
		setProduct(product);
		setLayout(layout);
		validatePages();	
		validateSource();
		extractPages();
		internalCreatePreview();
	}

	private static final String PAGES_SEPARATOR = ",";
	private static final String PAGE_RANGE_SEPARATOR = "-";

	private void extractPages() {
		if (isPageRange()) {
			String[] pagesArray = getPages().split(PAGES_SEPARATOR);
			for (String pageItem : pagesArray) {
				if (!StringUtils.isBlank(pageItem)) {
					if (StringUtils.contains(pageItem, PAGE_RANGE_SEPARATOR)) {
						extractPageRange(pageItem);
					} else {
						getPagesList().add(Integer.parseInt(StringUtils.trim(pageItem)));
					}
				}
			}		
		}
		fixPagesForLayout();
	}
	
	private void fixPagesForLayout() {
		if (layout == PageLayout.TWO_UP) {
			List<Integer> newPagesList = new ArrayList<Integer>();
			for (Integer pageNumber : getPagesList()) {
				Double newPageNumberDouble = new Double(pageNumber);
				newPageNumberDouble = newPageNumberDouble / 2;
				newPageNumberDouble = Math.ceil(newPageNumberDouble);
				newPagesList.add(newPageNumberDouble.intValue());
			}
			getPagesList().clear();
			getPagesList().addAll(newPagesList);
		}
	}

	private void extractPageRange(String pageRange) {
		String[] pageRangeArray = pageRange.split(PAGE_RANGE_SEPARATOR);
		int firstPage = Integer.parseInt(StringUtils.trim(pageRangeArray[0]));
		int lastPage = Integer.parseInt(StringUtils.trim(pageRangeArray[1]));
		for (int i = firstPage; i < lastPage + 1; i++) {
			getPagesList().add(i);			
		}
	}

	protected abstract void internalCreatePreview() throws Exception;
	
	private static final String PAGE_RANGE_REGEX = "^(\\d+|\\d+-\\d+\\s*)(,\\s*?(\\d+|\\d+-\\d+)\\s*)*$";
	private static final String PAGE_RANGE_PERCENT_REGEX = "^(\\d+%)$";
	
	private boolean isPageRange() {
		return getPages().matches(PAGE_RANGE_REGEX);
	}
	
	protected boolean isPageRangePercent() {
		return getPages().matches(PAGE_RANGE_PERCENT_REGEX);
	}
	
	private void validatePages() throws Exception {
		if (!isPageRange() && !isPageRangePercent()) {
			throw new Exception("pages format invalid");
		}
	}
	
	private void validateSource() throws Exception {
		File file = FileUtils.getFile(getSource());
		if (!file.exists()) {
			throw new Exception("Source file '" + getSource() + "' does not exist");
		}
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public OutputStream getTarget() {
		return target;
	}

	public void setTarget(OutputStream target) {
		this.target = target;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public List<Integer> getPagesList() {
		return pagesList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PageLayout getLayout() {
		return layout;
	}

	public void setLayout(PageLayout layout) {
		this.layout = layout;
	}
	
}
