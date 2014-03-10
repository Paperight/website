package com.paperight.country;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
	
	@Autowired
	private CountryDao countryDao;
	
	@Cacheable("countries")
	public Map<String, Country> getCountries() {
		try {
			return countryDao.loadCountries();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Cacheable("country-currency-mappings")
	public Map<String, String> getCountryToCurrencyMappings() {
		try {
			return countryDao.loadCountryToCurrencyMappings();
		} catch (Exception e) {
			return null;
		}
	}

	public Country findByCode(String code) {
		return getCountries().get(code);
	}

}