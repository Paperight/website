package com.paperight.geo;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paperight.product.Product;

@Service
public class AvailabilityService {

	@Autowired
	private Ip2CountryService ip2CountryService;
	
	public boolean isAvailable(Product product, HttpServletRequest request) {
		boolean result = true;
		
		String disallowedCountries = product.getDisallowedCountries();
		if (!StringUtils.isBlank(disallowedCountries)) {
			String[] disallowedCountriesArray = disallowedCountries.split(";");
			if (disallowedCountriesArray.length > 0) {
				String ipAddress = getIp(request);
				String countryCode = ip2CountryService.ip2CountryCode(ipAddress);
				for (String disallowedCountry : disallowedCountriesArray) {
					if (StringUtils.equalsIgnoreCase(StringUtils.trim(disallowedCountry), countryCode)) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private String getIp(HttpServletRequest request) {
		String clientIPAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtils.isBlank(clientIPAddress) || StringUtils.equals(StringUtils.lowerCase(clientIPAddress), "unknown")) {
			clientIPAddress = request.getHeader("X-Forwarded-For");
			if (StringUtils.isBlank(clientIPAddress) || StringUtils.equals(StringUtils.lowerCase(clientIPAddress), "unknown")) {
				clientIPAddress = request.getRemoteAddr();
			}
		}
		return clientIPAddress;
	}
	
}
