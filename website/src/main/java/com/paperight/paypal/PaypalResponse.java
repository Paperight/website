package com.paperight.paypal;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import paypalnvp.core.PayPal;
import paypalnvp.request.Request;

public class PaypalResponse {
	
	private Request request;
	private PayPal payPal;
	private Map<String, String> response;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public PayPal getPayPal() {
		return payPal;
	}

	public void setPayPal(PayPal payPal) {
		this.payPal = payPal;
	}

	public Map<String, String> getResponse() {
		return response;
	}

	public void setResponse(Map<String, String> response) {
		this.response = response;
	}
	
	public boolean isSuccessful() {
		return StringUtils.equals(response.get("ACK"), "Success");
	}
	
	public String getRedirectUrl() {
		return getPayPal().getRedirectUrl(getRequest());
	}
	
	public String getToken() {
		return response.get("TOKEN");
	}
	
	public String getTransactionId() {
		return response.get("TRANSACTIONID"); 
	}
}
