package com.paperight.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml",  "classpath:/META-INF/spring/security-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/cache-context.xml" })
public class CurrencyServiceTest {

	@Autowired
	private CurrencyService currencyService;
	
	@Test
	public final void testGetCurrencies() throws IOException {
		Map<String, Currency> currencies = currencyService.getCurrencies();
		currencies = currencyService.getCurrencies();
		Assert.assertTrue(currencies.size() > 0);
	}
	
	@Test
	public final void testConvertion() throws IOException {
		Currency ZAR = currencyService.findByCode("ZAR");
		Currency USD = currencyService.findByCode("USD");
		BigDecimal rands = new BigDecimal(100);
		BigDecimal pounds = currencyService.convert(rands, ZAR, USD);
		Assert.assertTrue((!rands.equals(pounds)));
	}

}
