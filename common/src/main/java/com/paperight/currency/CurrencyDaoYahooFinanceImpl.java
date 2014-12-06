package com.paperight.currency;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyDaoYahooFinanceImpl extends CurrencyDaoDefaultImpl implements CurrencyDao {

	private Logger logger = LoggerFactory.getLogger(CurrencyDaoYahooFinanceImpl.class);
	
	@Override
	protected CurrencyRates loadCurrencyRates() throws Exception {
		logger.info("loading currency rates");
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
    		HttpGet httpget = new HttpGet("http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json");
    		HttpResponse response = httpclient.execute(httpget);
    		HttpEntity entity = response.getEntity();
    		if (entity != null) {
    			
    			try (InputStream instream = entity.getContent()){
    				CurrencyRates currencyRates = new CurrencyRates();
    				ObjectMapper objectMapper = new ObjectMapper();
    				JsonNode jsonRootNode = objectMapper.readTree(instream);
    				List<JsonNode> resourceJsonNodes = jsonRootNode.findValues("resource");
    //				resourceJsonNodes.
    				for (JsonNode resourceJsonNode : resourceJsonNodes) {
    					JsonNode fieldsNode = resourceJsonNode.findValue("fields");
    					String currencyName = fieldsNode.get("name").getValueAsText();
    					currencyName = StringUtils.remove(currencyName, "USD/");
    					BigDecimal currencyRate = new BigDecimal(fieldsNode.get("price").getValueAsText());
    					currencyRates.getRates().put(currencyName, currencyRate);					
    				}
    				currencyRates.getRates().put("USD", new BigDecimal(1));
    				setLastLoadedCurrencyRates(currencyRates);
    				return getLastLoadedCurrencyRates();
    			} catch (IOException ex) {
    				throw ex;
    
    			} catch (RuntimeException ex) {
    				httpget.abort();
    				throw ex;
    
    			}
    		}
		}
		return null;
	}

}
