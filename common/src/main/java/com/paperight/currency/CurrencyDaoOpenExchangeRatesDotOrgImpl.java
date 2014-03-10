package com.paperight.currency;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyDaoOpenExchangeRatesDotOrgImpl extends CurrencyDaoDefaultImpl implements CurrencyDao {
	
	private Logger logger = LoggerFactory.getLogger(CurrencyDaoOpenExchangeRatesDotOrgImpl.class);
	
	protected CurrencyRates loadCurrencyRates() throws Exception {
		logger.info("loading currency rates");
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://openexchangerates.org/latest.json");
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				return new ObjectMapper().readValue(instream, CurrencyRates.class);
			} catch (IOException ex) {
				throw ex;

			} catch (RuntimeException ex) {
				httpget.abort();
				throw ex;

			} finally {
				instream.close();
				httpclient.getConnectionManager().shutdown();
			}
		}
		return null;
	}

}