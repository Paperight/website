package com.paperight.mvc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.com.bytecode.opencsv.CSVWriter;

import com.paperight.licence.Licence;
import com.paperight.licence.LicenceService;
import com.paperight.licence.LicenceStatus;
import com.paperight.product.Product;
import com.paperight.user.Address;
import com.paperight.user.AddressContextType;

@Controller
public class LicenceController {
	
	@Autowired
	private LicenceService licenceService;
	
	private Logger logger = LoggerFactory.getLogger(LicenceController.class);
	
	@RequestMapping(value = "/rest/licence/generate/{licenceId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object generate(@PathVariable Long licenceId) {
		Response response = new Response();
		try {
			Licence licence = Licence.find(licenceId);
			if (licence == null) {
				response.setResponseObject("Licence not found");
			} else if (licence.getStatus() == LicenceStatus.NEW) {
				licenceService.generateFile(licence);
				licence.setStatus(LicenceStatus.GENERATED);
				licence.merge();
				response.setStatus(com.paperight.mvc.controller.ResponseStatus.OK);
			} else if (licence.getStatus() == LicenceStatus.CANCELLED) {
				response.setResponseObject("Licence cancelled");
			} else {
				response.setResponseObject("Licence already downloaded");
			}
		} catch (Exception e) {
			logger.error("Unable to generate licenceid: " + licenceId, e);
			response.setResponseObject(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value="/licence/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String search(@ModelAttribute LicenceSearch licenceSearch, Model model) {
		List<Licence> licences = searchLicences(licenceSearch);
		model.addAttribute("licenceSearch", licenceSearch);
		model.addAttribute("licences", licences);
		return "licence/search";
	}
	
	private List<Licence> searchLicences(LicenceSearch licenceSearch) {
		if (licenceSearch.getFromDate() == null && licenceSearch.getToDate() == null) {
			return new ArrayList<Licence>();
		} else {
			return Licence.findByDateRange(licenceSearch.getFromDate(), licenceSearch.getToDate());
		}
	}
	
	@RequestMapping(value="/licence/export", method = RequestMethod.GET )
	public void export(@ModelAttribute LicenceSearch licenceSearch, HttpServletResponse response) throws Exception {
		List<Licence> licences = searchLicences(licenceSearch);
		String csvContent = buildCsv(licences);			
		response.setContentType("text/csv");
		String filename = "licence_export_" + new DateTime().toString("yyyyMMdd-hhmmss");
		response.addHeader("content-disposition", "attachment; filename=" + filename + ".csv");
		InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
		try {
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} finally {
			inputStream.close();
		}
		
	}
	
	private String buildCsv(List<Licence> licences) throws IOException {
		List<String[]> csvLines = new ArrayList<String[]>();
		String[] csvHeader = { "RightsholderName", "RightsholderEmailAddress", "TransactionDate", "Title", "Subtitle", "Edition", "Author", "Identifier", "SubjectArea", "OutletName", "OutletId", "OutletCity", "OutletCountry", "OutletCurrency", "OutletServiceCharge", "NumberOfCopies", "Layout", "DoubleSidedSheets", "CreditsReserved", "DownloadedYN", "CreditsCharged", "DollarEquivalent", "PaperightFee", "AmountOwingToRightsholder" };
		csvLines.add(csvHeader);
		for (Licence licence : licences) {
			Product product = licence.getProduct();
			Address address = licence.getCompany().getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress();
			String[] csvLine = new String[24];
			csvLine[0] = product.getPublisher();//RightsholderName
			csvLine[1] = "";//RightsholderEmailAddress
			csvLine[2] = licence.getCreatedDate().toString();//TransactionDate
			csvLine[3] = product.getTitle();//Title
			csvLine[4] = product.getSubTitle();//Subtitle
			csvLine[5] = product.getEdition();//Edition
			csvLine[6] = product.getPrimaryCreators();//Author
			csvLine[7] = product.getIdentifier();//Identifier
			csvLine[8] = product.getSubjectArea();//SubjectArea
			csvLine[9] = licence.getCompany().getName();//OutletName
			csvLine[10] = licence.getCompany().getId().toString();//OutletId
			csvLine[11] = address.getAddressLine4();//OutletCity
			csvLine[12] = address.getCountry().getName();//OutletCountry
			csvLine[13] = licence.getCurrencyCode();//OutletCurrency
			csvLine[14] = licence.getOutletCharge().toString();//OutletServiceCharge
			csvLine[15] = licence.getNumberOfCopies() + "";//NumberOfCopies
			csvLine[16] = licence.getPageLayout().toString();//Layout
			csvLine[17] = licence.getPageExtent() + "";//DoubleSidedSheets
			csvLine[18] = licence.getCostInCredits().toString();//CreditsReserved
			csvLine[19] = licence.isDownloaded() ? "Y" : "N" ;//DownloadedYN
			csvLine[20] = licence.isDownloaded() ? licence.getCostInCredits().toString() : "";//CreditsCharged
			BigDecimal dollarEquivalent = licence.getCostInCredits().multiply(licence.getPaperightCreditToBaseCurrencyRate()).setScale(2, RoundingMode.UP);
			BigDecimal paperightFree = dollarEquivalent.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
			csvLine[21] = dollarEquivalent.toString();//DollarEquivalent
			csvLine[22] = paperightFree.toString();//PaperightFee
			csvLine[23] = dollarEquivalent.subtract(paperightFree).toString();//AmountOwingToRightsholder
			csvLines.add(csvLine);
		}
		Writer writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);
		try {
			csvWriter.writeAll(csvLines);
			return writer.toString();
		} finally {
			csvWriter.close();
		} 
	}

}

class ResponseError {
	
	private Long licenceId;
	private String error;
	
	public ResponseError(Long id, String error) {
		this.licenceId = id;
		this.error = error;
	}
	
	public Long getLicenceId() {
		return licenceId;
	}
	
	public String getError() {
		return error;
	}
}

class LicenceSearch {
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime fromDate;
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime toDate;
	
	public DateTime getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(DateTime fromDate) {
		this.fromDate = fromDate.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	}
	
	public DateTime getToDate() {
		return toDate;
	}
	
	public void setToDate(DateTime toDate) {
		this.toDate = toDate.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
	}
	
}
