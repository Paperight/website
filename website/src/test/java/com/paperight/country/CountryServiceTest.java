package com.paperight.country;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/cache-context.xml" })
public class CountryServiceTest {

	@Autowired
	private CountryService countryService;
	
	@Test
	public final void testGetCountries() {
		Map<String, Country> countries = countryService.getCountries();
		Assert.assertTrue(countries.size() > 0);
	}

}
