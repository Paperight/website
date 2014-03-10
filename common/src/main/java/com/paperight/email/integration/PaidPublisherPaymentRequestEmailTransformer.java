package com.paperight.email.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.paperight.currency.CurrencyService;
import com.paperight.mvc.tags.PriceTag;
import com.paperight.publisherearning.PublisherPaymentRequest;

public class PaidPublisherPaymentRequestEmailTransformer extends EmailTransformer {
	
	@Autowired
	private CurrencyService currencyService;
	
	@Transformer
    public MimeMessage transform(final PublisherPaymentRequest publisherPaymentRequest) throws MessagingException, IOException {
		MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(publisherPaymentRequest.getUser().getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(publisherPaymentRequest);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final PublisherPaymentRequest publisherPaymentRequest) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	
    	PriceTag priceTag = new PriceTag();
   		String amountInCurrency = priceTag.buildPriceTag(publisherPaymentRequest.getCompany().getCurrency(), currencyService.convert(publisherPaymentRequest.getAmount(), currencyService.getBaseCurrency(), publisherPaymentRequest.getCompany().getCurrency()));    	
    	String appUrl = getApplicationUrl();
    	model.put("appUrl", appUrl);
    	model.put("publisherPaymentRequest", publisherPaymentRequest);
    	model.put("amountInCurrency", amountInCurrency);
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-paid-publisher-payment-request";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/paidPublisherPaymentRequestEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Publisher Payment Request Approved";
	}

}
