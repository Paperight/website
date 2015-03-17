package com.paperight.licence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paperight.LicenceFileService;

@Service
public class LicenceService {
	
	@Autowired
	private TrackingUrlCreator trackingUrlCreator;
	
	@Autowired
	private LicenceFileService licenceFileService;
	
	public LicenceService() {
		super();
	}
	
	@PostConstruct
	public void postConstruct() {
		registerWatermarkWriters();
	}
	
	private void registerWatermarkWriters() {
		
	}
	
	public List<Licence> searchLicences(LicenceSearch licenceSearch) {
		if (licenceSearch.getFromDate() == null && licenceSearch.getToDate() == null) {
			return new ArrayList<Licence>();
		} else {
			return Licence.findByDateRange(licenceSearch.getFromDate(), licenceSearch.getToDate());
		}
	}
	
	public boolean generateFile(Licence licence) throws Exception {
		finalizeLicence(licence);
		WatermarkWriter watermarkWriter = WatermarkWriterFactory.create(FilenameUtils.getExtension(licence.getOriginalFileName()));
		try {
			watermarkWriter.writeWatermark(sourceLicenceFile(licence), targetLicenceFile(licence), WatermarkFactory.fromLicence(licence), licence.getPageLayout());
			return true;
		} catch (IOException e) {
			throw new Exception("Unable to generate PDF", e);
		}
	}
	
	private String sourceLicenceFile(Licence licence) {
		String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), licence.getOriginalFileName());
		return filename;
	}
	
	private String targetLicenceFile(Licence licence) {
		String filename = FilenameUtils.concat(licenceFileService.getLicencedPdfFileFolder(), licence.getId() + ".pdf");
		return filename;
	}

	private void finalizeLicence(Licence licence) {
		if (StringUtils.isBlank(licence.getTrackingUrl())) {
			String trackingUrl = trackingUrlCreator.execute(licence);
			licence.setTrackingUrl(trackingUrl);
			licence.merge();
		}
	}	
}
