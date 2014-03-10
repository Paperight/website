package com.paperight.country;

import java.util.Map;

public interface CountryDao {

	public Map<String, Country> loadCountries() throws Exception;
	public Map<String, String> loadCountryToCurrencyMappings() throws Exception;
	
}
