package com.paperight.product.batch;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.paperight.product.Product;

public class ProductFieldSetMapper implements FieldSetMapper<ProductImportItem> {
	
	@Override
	public ProductImportItem mapFieldSet(FieldSet fs) throws BindException {
		if (fs == null) {
			return null;
		}
		Product product = new Product();
		product.setIdentifier(fs.readString("IDENTIFIER"));
		product.setIdentifierType(fs.readString("IDENTIFIER_TYPE"));
		product.setCopyrightStatus(fs.readString("COPYRIGHT_STATUS"));
		product.setPublisher(fs.readString("PUBLISHER"));
		product.setTitle(fs.readString("TITLE"));
		product.setSubTitle(fs.readString("SUBTITLE"));
		product.setAlternativeTitle(fs.readString("ALTERNATIVE_TITLE"));
		product.setPrimaryCreators(fs.readString("PRIMARY_CREATORS"));
		product.setSecondaryCreators(fs.readString("SECONDARY_CREATORS"));
		product.setEdition(fs.readString("EDITION"));
		product.setPrimaryLanguages(fs.readString("PRIMARY_LANGUAGES"));
		product.setSecondaryLanguages(fs.readString("SECONDARY_LANGUAGES"));
		product.setSubjectArea(fs.readString("SUBJECT_AREA"));
		product.setPublicationDate(getDateTime(fs.readString("PUBLICATION_DATE")));
		product.setEmbargoDate(getDateTime(fs.readString("EMBARGO_DATE")));
		product.setLicenceFeeInDollars(fs.readBigDecimal("LICENCE_FEE_IN_DOLLARS"));
		product.setShortDescription(fs.readString("SHORT_DESCRIPTION"));
		product.setLongDescription(fs.readString("LONG_DESCRIPTION"));
		product.setParentIsbn(fs.readString("PARENT_ISBN"));
		product.setAlternateIsbn(fs.readString("ALTERNATE_ISBN"));
		product.setAudience(fs.readString("AUDIENCE"));
		product.setOneUpFilename(fs.readString("ONE_UP_PDF_FILENAME"));
		product.setTwoUpFilename(fs.readString("TWO_UP_PDF_FILENAME"));
		product.setA5Filename(fs.readString("A5_PDF_FILENAME"));
		product.setJacketImageFilename(fs.readString("JACKET_IMAGE_NAME"));
		product.setDisallowedCountries(fs.readString("TERRITORY"));
		product.setTags(fs.readString("TAGS"));
		product.setUrl(fs.readString("URL"));
		product.setUrlCallToAction(fs.readString("URL_CALL_TO_ACTION"));
		product.setSupportsAds(StringUtils.equals(fs.readString("SUPPORTS_ADS"), "1") ? true : false);
		product.setSamplePageRange(fs.readString("SAMPLE_PAGE_RANGE"));
		product.setLicenceStatement(fs.readString("LICENCE_STATEMENT"));
		return ProductImportItem.newInstance(product, fs.toString());
	}
	
	private LocalDateTime getDateTime(String dateString) {
		if (!StringUtils.isBlank(dateString)) {
			String pattern;
			if (dateString.matches("(19|20)[0-9]{2}[/](0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])")) {
				pattern = "yyyy/MM/dd";
			} else {
				pattern = "yyyy-MM-dd";
			}
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
			return dateTimeFormatter.parseLocalDateTime(dateString);
		}
		return null;
	}
}
