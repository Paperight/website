package com.paperight.paypal;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.credit.PaperightCreditTransaction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:/META-INF/spring/security-context.xml" })
public class PaypalServiceTest {
	
	@Autowired
	private PaypalService paypalService;
	
	@Value("${base.url}")
	private String applicationUrl;

	@Test
	public final void testInitiatePayment() {
		PaperightCreditTransaction transaction = new PaperightCreditTransaction();
		transaction.setNumberOfCredits(new BigDecimal(123).setScale(0));
		PaypalResponse response = paypalService.initiatePayment(transaction, applicationUrl + "/transaction/complete", applicationUrl + "/transaction/cancel");
		if (response.isSuccessful()) {
			System.out.println("Token: " + response.getToken());
			System.out.println("Redirect Url: " + response.getRedirectUrl());
			System.out.println("Response Map: " + response.getResponse());
		}
		Assert.assertTrue(response.isSuccessful());
	}
	
	@Test
	public final void testCompletePayment() {
		PaperightCreditTransaction transaction = new PaperightCreditTransaction();
		transaction.setNumberOfCredits(new BigDecimal(123).setScale(0));
		PaypalResponse response = paypalService.completePayment(transaction, "EC-786270384N085350D", "ZHTTV47G8KZ8Y");
		if (response.isSuccessful()) {
			System.out.println("Token: " + response.getToken());
			System.out.println("Redirect Url: " + response.getRedirectUrl());
			System.out.println("Response Map: " + response.getResponse());
		}
		Assert.assertTrue(response.isSuccessful());
	}

}
