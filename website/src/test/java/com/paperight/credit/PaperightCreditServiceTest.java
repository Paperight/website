package com.paperight.credit;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/cache-context.xml" })
public class PaperightCreditServiceTest {

	@Autowired
	PaperightCreditService paperightCreditService;
	
	@Test
	@SuppressWarnings("unused")
	public final void testPaperightCreditsValueInCurrency() {
		BigDecimal credits = new BigDecimal(10);
		String currencyCode = "ZAR";
		BigDecimal currencyValue = paperightCreditService.paperightCreditsValueInCurrency(credits, currencyCode);
		fail("Not yet implemented");
	}

}
