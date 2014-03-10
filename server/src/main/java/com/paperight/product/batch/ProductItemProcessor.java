package com.paperight.product.batch;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.LicenceFileService;
import com.paperight.product.Product;

@Configurable
public class ProductItemProcessor implements ItemProcessor<ProductImportItem, ProductImportItem> {

	@Autowired
	private LicenceFileService licenceFileService;
	
	@Override
	public ProductImportItem process(ProductImportItem item) throws Exception {
		defaultFileExtensions(item.getProduct());
		validate(item.getProduct());
		return item;
	}
	
	private void defaultFileExtensions(Product product) {
		if (!StringUtils.isBlank(product.getOneUpFilename())) {
			if (!StringUtils.endsWithIgnoreCase(product.getOneUpFilename(), ".pdf")) {
				product.setOneUpFilename(product.getOneUpFilename() + ".pdf");
			}
		}
		if (!StringUtils.isBlank(product.getTwoUpFilename())) {
			if (!StringUtils.endsWithIgnoreCase(product.getTwoUpFilename(), ".pdf")) {
				product.setTwoUpFilename(product.getTwoUpFilename() + ".pdf");
			}
		}
	}

	private void validate(Product product) throws Exception {
		if (StringUtils.isBlank(product.getOneUpFilename()) && StringUtils.isBlank(product.getTwoUpFilename())  && StringUtils.isBlank(product.getA5Filename())) {
			throw new NoProductFilesException("Either one of ONE_UP_PDF_FILENAME, TWO_UP_PDF_FILENAME or A5_PDF_FILENAME must be present for product identifier " + product.getIdentifier());
		}
		if (!StringUtils.isBlank(product.getOneUpFilename())) {
			String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), product.getOneUpFilename());
			if (!licenceFileService.fileExists(filename)) {
				throw new ProductFileNotFoundException("ONE_UP_PDF_FILENAME '" + product.getOneUpFilename() +"' specified for product identifier " + product.getIdentifier() + " does not exist on server.");
			}
		}
		if (!StringUtils.isBlank(product.getTwoUpFilename())) {
			String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), product.getTwoUpFilename());
			if (!licenceFileService.fileExists(filename)) {
				throw new ProductFileNotFoundException("TWO_UP_PDF_FILENAME '" + product.getTwoUpFilename() +"' specified for product identifier " + product.getIdentifier() + " does not exist on server.");
			}
		}
		if (!StringUtils.isBlank(product.getA5Filename())) {
			String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), product.getA5Filename());
			if (!licenceFileService.fileExists(filename)) {
				throw new ProductFileNotFoundException("A5_PDF_FILENAME '" + product.getA5Filename() +"' specified for product identifier " + product.getIdentifier() + " does not exist on server.");
			}
		}
	}
	

}
