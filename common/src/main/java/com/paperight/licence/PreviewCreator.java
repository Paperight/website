package com.paperight.licence;

import java.io.OutputStream;

import com.paperight.product.Product;
import com.paperight.user.User;

public interface PreviewCreator {
	
	public void createPreview(String source, OutputStream target, String pages, User user, Product product, PageLayout layout) throws Exception;

}
