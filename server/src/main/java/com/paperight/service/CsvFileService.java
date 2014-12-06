package com.paperight.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import com.paperight.product.ImportItem;
import com.paperight.product.ImportJob;
import com.paperight.utils.CsvUtils;

@Service
public class CsvFileService extends FileService {

    public static final String[] EXPECTED_CSV_HEADERS = new String[] { "COPYRIGHT_STATUS", "IDENTIFIER", "IDENTIFIER_TYPE", "PUBLISHER", "TITLE", "SUBTITLE", "ALTERNATIVE_TITLE", "PRIMARY_CREATORS", "SECONDARY_CREATORS", "EDITION", "PRIMARY_LANGUAGES", "SECONDARY_LANGUAGES", "SUBJECT_AREA", "PUBLICATION_DATE", "EMBARGO_DATE", "LICENCE_FEE_IN_DOLLARS", "SHORT_DESCRIPTION", "LONG_DESCRIPTION", "PARENT_ISBN", "ALTERNATE_ISBN", "AUDIENCE", "DISALLOWED_COUNTRIES", "TAGS", "URL", "URL_CALL_TO_ACTION", "SUPPORTS_ADS", "SAMPLE_PAGE_RANGE", "LICENCE_STATEMENT", "FORMATS", "CSS_FILE" };

    public void splitFile(ImportJob importJob, String filename) throws Exception {
        List<String> csvLines = getCsvLines(filename);
        saveImportItems(importJob, csvLines);
    }

    @Override
    public void validateFile(String filename) throws Exception {
        CsvUtils.validateCsv(filename, EXPECTED_CSV_HEADERS);
    }

    private List<String> getCsvLines(String filename) throws Exception {
        return CsvUtils.getCsvLines(filename, EXPECTED_CSV_HEADERS, true);
    }

    @Override
    protected void extractRawData(ImportItem importItem) throws Exception {
        Map<String, String> map = CsvUtils.csvLineToMap(importItem.getRawData(), EXPECTED_CSV_HEADERS);
        importItem.setCopyrightStatus(map.get("COPYRIGHT_STATUS"));
        importItem.setIdentifier(map.get("IDENTIFIER"));
        importItem.setIdentifierType(map.get("IDENTIFIER_TYPE"));
        importItem.setPublisher(map.get("PUBLISHER"));
        importItem.setTitle(map.get("TITLE"));
        importItem.setSubTitle(map.get("SUBTITLE"));
        importItem.setAlternativeTitle(map.get("ALTERNATIVE_TITLE"));
        importItem.setPrimaryCreators(map.get("PRIMARY_CREATORS"));
        importItem.setSecondaryCreators(map.get("SECONDARY_CREATORS"));
        importItem.setEdition(map.get("EDITION"));
        importItem.setPrimaryLanguages(map.get("PRIMARY_LANGUAGES"));
        importItem.setSecondaryLanguages(map.get("SECONDARY_LANGUAGES"));
        importItem.setSubjectArea(map.get("SUBJECT_AREA"));
        importItem.setPublicationDate(getDateTime(map.get("PUBLICATION_DATE")));
        importItem.setEmbargoDate(getDateTime(map.get("EMBARGO_DATE")));
        importItem.setLicenceFeeInDollars(new BigDecimal(map.get("LICENCE_FEE_IN_DOLLARS")));
        importItem.setShortDescription(map.get("SHORT_DESCRIPTION"));
        importItem.setLongDescription(map.get("LONG_DESCRIPTION"));
        importItem.setParentIsbn(map.get("PARENT_ISBN"));
        importItem.setAlternateIsbn(map.get("ALTERNATE_ISBN"));
        importItem.setAudience(map.get("AUDIENCE"));
        importItem.setDisallowedCountries(map.get("DISALLOWED_COUNTRIES"));
        importItem.setTags(map.get("TAGS"));
        importItem.setUrl(map.get("URL"));
        importItem.setUrlCallToAction(map.get("URL_CALL_TO_ACTION"));
        importItem.setSupportsAds(StringUtils.equals(map.get("SUPPORTS_ADS"), "1") ? true : false);
        importItem.setSamplePageRange(map.get("SAMPLE_PAGE_RANGE"));
        importItem.setLicenceStatement(map.get("LICENCE_STATEMENT"));
        importItem.setFormats(map.get("FORMATS"));
        importItem.setCssFile(map.get("CSS_FILE"));
        importItem.getFormatList();
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
