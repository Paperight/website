package com.paperight.currency;

import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public abstract class DefaultCurrencySymbolDao {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultCurrencySymbolDao.class);

	private static Map<String, String> map;
	
	public static String getDefaultSymbol(String currencyCode) {
		try {
			String symbol = getSymbolMap().get(currencyCode);
			if (StringUtils.isBlank(symbol)) {
				symbol = currencyCode;
			}
			return symbol;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return currencyCode;
		}
	}
	
	private static Map<String, String> getSymbolMap() throws Exception {
		if( map == null ) {   
			synchronized( DefaultCurrencySymbolDao.class ) {   
				if( map == null ) {
					ClassPathResource classPathResource = new ClassPathResource("default-currency-symbols.json");
					InputStreamReader reader = new InputStreamReader(classPathResource.getInputStream());
					try {
						map = new ObjectMapper().readValue(classPathResource.getInputStream(), new TypeReference<Map<String, String>>() {});
					} finally {
						reader.close();
					}
				}
            }
        }
        return map;
	}
	
}
