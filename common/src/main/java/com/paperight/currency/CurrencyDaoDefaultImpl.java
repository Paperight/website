package com.paperight.currency;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public abstract class CurrencyDaoDefaultImpl implements CurrencyDao {
	
	private Logger logger = LoggerFactory.getLogger(CurrencyDaoDefaultImpl.class);
	private CurrencyRates lastLoadedCurrencyRates;

	@Override
	public Map<String, Currency> loadCurrencies() throws Exception {
		Map<String, Currency> currencies = new LinkedHashMap<String, Currency>();
		CurrencyRates currencyRates;
		try {
			currencyRates = loadCurrencyRates();
		} catch (Exception exception) {
			logger.error("Unable to load currency rates", exception);
			currencyRates = getLastLoadedCurrencyRates();
		}
		Map<String, String> currencyNames = loadCurrencyNames();
		for( String key: currencyNames.keySet() ){
			Currency currency = new Currency();
			currency.setCode(key);
			currency.setRate(currencyRates.getRates().get(key));
			currency.setName(currencyNames.get(key));
			currencies.put(key, currency);
		}
		Map<String, Currency> sortedMap = new TreeMap<String, Currency>(new CurrencyComparator(currencies));
		sortedMap.putAll(currencies);
		return sortedMap;
	}
	
	protected abstract CurrencyRates loadCurrencyRates() throws Exception ;
	
	private Map<String, String> loadCurrencyNames() throws Exception {
		logger.info("loading currency names");
		ClassPathResource classPathResource = new ClassPathResource("usable-currencies.json");
		InputStreamReader reader = new InputStreamReader(classPathResource.getInputStream());
		try {
			return new ObjectMapper().readValue(classPathResource.getInputStream(), new TypeReference<Map<String, String>>() {});
		} finally {
			reader.close();
		}
	}

	public CurrencyRates getLastLoadedCurrencyRates() {
		return lastLoadedCurrencyRates;
	}

	public void setLastLoadedCurrencyRates(CurrencyRates lastLoadedCurrencyRates) {
		this.lastLoadedCurrencyRates = lastLoadedCurrencyRates;
	}

}
