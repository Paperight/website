package com.paperight.product.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.paperight.product.Product;

public class ProductItemWriter implements ItemWriter<ProductImportItem> {

	@Override
	public void write(List<? extends ProductImportItem> items) throws Exception {
		for (ProductImportItem item : items) {
			Product product = item.getProduct();
			if (!mergeProduct(product)) {
				product.persist();
			}
		}
	}
	
	private boolean mergeProduct(Product product) {
		Product existingProduct = Product.findByIdentifierAndIdentifierType(product.getIdentifier(), product.getIdentifierType());
		if (existingProduct == null) {
			return false;
		}
		existingProduct.setCopyrightStatus(product.getCopyrightStatus());
		existingProduct.setPublisher(product.getPublisher());
		existingProduct.setTitle(product.getTitle());
		existingProduct.setSubTitle(product.getSubTitle());
		existingProduct.setAlternativeTitle(product.getAlternativeTitle());
		existingProduct.setPrimaryCreators(product.getPrimaryCreators());
		existingProduct.setSecondaryCreators(product.getSecondaryCreators());
		existingProduct.setEdition(product.getEdition());
		existingProduct.setPrimaryLanguages(product.getPrimaryLanguages());
		existingProduct.setSecondaryLanguages(product.getSecondaryLanguages());
		existingProduct.setSubjectArea(product.getSubjectArea());
		existingProduct.setPublicationDate(product.getPublicationDate());
		existingProduct.setEmbargoDate(product.getEmbargoDate());
		existingProduct.setLicenceFeeInDollars(product.getLicenceFeeInDollars());
		existingProduct.setShortDescription(product.getShortDescription());
		existingProduct.setLongDescription(product.getLongDescription());
		existingProduct.setParentIsbn(product.getParentIsbn());
		existingProduct.setAlternateIsbn(product.getAlternateIsbn());
		existingProduct.setAudience(product.getAudience());
		existingProduct.setOneUpFilename(product.getOneUpFilename());
		existingProduct.setTwoUpFilename(product.getTwoUpFilename());
		existingProduct.setA5Filename(product.getA5Filename());
		existingProduct.setJacketImageFilename(product.getJacketImageFilename());
		existingProduct.setDisallowedCountries(product.getDisallowedCountries());
		existingProduct.setTags(product.getTags());
		existingProduct.setUrl(product.getUrl());
		existingProduct.setUrlCallToAction(product.getUrlCallToAction());
		existingProduct.setSupportsAds(product.isSupportsAds());
		existingProduct.setSamplePageRange(product.getSamplePageRange());
		existingProduct.setLicenceStatement(product.getLicenceStatement());
		existingProduct.merge();
		return true;
	}

}

