package com.paperight.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import paypalnvp.core.PayPal;
import paypalnvp.core.PayPal.Environment;
import paypalnvp.fields.Currency;
import paypalnvp.fields.Payment;
import paypalnvp.fields.PaymentAction;
import paypalnvp.fields.PaymentItem;
import paypalnvp.profile.BaseProfile;
import paypalnvp.profile.Profile;
import paypalnvp.request.DoExpressCheckoutPayment;
import paypalnvp.request.SetExpressCheckout;
import paypalnvp.request.SetExpressCheckout.LandingPage;

import com.paperight.credit.PaperightCreditService;
import com.paperight.credit.PaperightCreditTransaction;

@Service
public class PaypalService {
	
	@Value("${paypal.api.sandbox}")
	private boolean sandbox;
	
	@Value("${paypal.api.user}")
	private String apiUser;
	
	@Value("${paypal.api.password}")
	private String apiPassword;
	
	@Value("${paypal.api.signature}")
	private String apiSignature;
	
	@Autowired
	private PaperightCreditService paperightCreditService;

	public PaypalResponse initiatePayment(PaperightCreditTransaction transaction, String returnUrl, String cancelUrl) {
		Profile user = new BaseProfile.Builder(apiUser, apiPassword).signature(apiSignature).build();
		/* create new instance of paypal nvp */
		Environment environment;
		if (sandbox) {
			environment = Environment.SANDBOX;
		} else {
			environment = Environment.LIVE;
		}
		PayPal payPal = new PayPal(user, environment);
			
		
		/* create items (items from a shopping basket) */
		PaymentItem item = new PaymentItem();
		//
		item.setAmount("" + paperightCreditService.paperightCreditsInBaseCurrency(transaction.getNumberOfCredits()));
		item.setDescription(transaction.getNumberOfCredits().setScale(0) + " Paperight Credits");

		PaymentItem[] items = {item};

		/* create payment (now you can create payment from the items) */
		Payment payment = new Payment(items);
		payment.setCurrency(Currency.USD);
		
		/* create set express checkout - the first paypal request */
		SetExpressCheckout setEC = new SetExpressCheckout(payment,  returnUrl, cancelUrl);
		setEC.setLandingPage(LandingPage.BILLING);

		/* send request and set response */
		payPal.setResponse(setEC);

		PaypalResponse paypalResponse = new PaypalResponse();
		paypalResponse.setRequest(setEC);
		paypalResponse.setPayPal(payPal);
		paypalResponse.setResponse(setEC.getNVPResponse());
		/* now you have set express checkout containting */
		/* request and response as well */
		return paypalResponse;
	}
	
	public PaypalResponse completePayment(PaperightCreditTransaction transaction, String token, String payerId) {
		Profile user = new BaseProfile.Builder(apiUser, apiPassword).signature(apiSignature).build();
		/* create new instance of paypal nvp */
		Environment environment;
		if (sandbox) {
			environment = Environment.SANDBOX;
		} else {
			environment = Environment.LIVE;
		}
		PayPal payPal = new PayPal(user, environment);
			
		
		/* create items (items from a shopping basket) */
		PaymentItem item = new PaymentItem();

		item.setAmount("" + paperightCreditService.paperightCreditsInBaseCurrency(transaction.getNumberOfCredits()));
		item.setDescription(transaction.getNumberOfCredits().setScale(0) + " Paperight Credits");

		PaymentItem[] items = {item};

		/* create payment (now you can create payment from the items) */
		Payment payment = new Payment(items);
		payment.setCurrency(Currency.USD);
		
		DoExpressCheckoutPayment doExpressCheckoutPayment = new DoExpressCheckoutPayment(payment, token, PaymentAction.SALE, payerId);
		
		payPal.setResponse(doExpressCheckoutPayment);

		PaypalResponse paypalResponse = new PaypalResponse();
		paypalResponse.setRequest(doExpressCheckoutPayment);
		paypalResponse.setPayPal(payPal);
		paypalResponse.setResponse(doExpressCheckoutPayment.getNVPResponse());
		/* now you have set express checkout containting */
		/* request and response as well */
		return paypalResponse;
	}
	
}
