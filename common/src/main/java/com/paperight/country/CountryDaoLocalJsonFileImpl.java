package com.paperight.country;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class CountryDaoLocalJsonFileImpl implements CountryDao {

	@Override
	public Map<String, Country> loadCountries() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("countries.json");
		InputStreamReader reader = new InputStreamReader(classPathResource.getInputStream());
		try {
			Map<String, String> countryMap = new ObjectMapper().readValue(classPathResource.getInputStream(), new TypeReference<Map<String, String>>() {});
			Map<String, Country> countries = new LinkedHashMap<String, Country>();
			for (String key : countryMap.keySet()) {
				Country country = new Country();
				country.setName(WordUtils.capitalizeFully(key));
				country.setCode(countryMap.get(key));
				countries.put(country.getCode(), country);
			}
			return countries;
		} finally {
			reader.close();
		}
	}

	@Override
	public Map<String, String> loadCountryToCurrencyMappings() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("country-currency-mapping.json");
		InputStreamReader reader = new InputStreamReader(classPathResource.getInputStream());
		try {
			Map<String, String> countryCurrencyMap = new ObjectMapper().readValue(classPathResource.getInputStream(), new TypeReference<Map<String, String>>() {});
			return countryCurrencyMap;
		} finally {
			reader.close();
		}
	}

}
