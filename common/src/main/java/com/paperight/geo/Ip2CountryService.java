package com.paperight.geo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;

@Service
public class Ip2CountryService implements InitializingBean {

	private LookupService lookupService;
	
	@Value("${ip2country.lookup.data.filename}")
	private String ip2CountryLookupDataFilename;

	@Override
	public void afterPropertiesSet() throws Exception {
		lookupService = new LookupService(ip2CountryLookupDataFilename, LookupService.GEOIP_CHECK_CACHE);	
	}
	
	public String ip2CountryCode(String ip) {
		Country country = lookupService.getCountry(ip);
		if (country != null) {
			return country.getCode();
		} else {
			return null;
		}
	}
	
}
