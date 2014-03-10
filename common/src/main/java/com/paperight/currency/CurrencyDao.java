package com.paperight.currency;

import java.util.Map;

public interface CurrencyDao {
	
	public Map<String, Currency> loadCurrencies() throws Exception;

}
