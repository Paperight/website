package com.paperight.email.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.paperight.publisherearning.PublisherPaymentRequest;

public class PublisherPaymentRequestEmailTransformer extends EmailTransformer {
	
	@Transformer
    public MimeMessage transform(final PublisherPaymentRequest publisherPaymentRequest) throws MessagingException, IOException {
		MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(getDefaultToAddress());
    	helper.setFrom(publisherPaymentRequest.getUser().getEmail(), publisherPaymentRequest.getUser().getCompany().getName());
    	helper.setReplyTo(publisherPaymentRequest.getUser().getEmail());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(publisherPaymentRequest);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final PublisherPaymentRequest publisherPaymentRequest) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	String appUrl = getApplicationUrl();
    	String publisherPaymentRequestUrl = appUrl + "/server/publisher-payment-request/" + publisherPaymentRequest.getId();
    	model.put("appUrl", appUrl);
    	model.put("publisherPaymentRequestUrl", publisherPaymentRequestUrl);
    	model.put("publisherPaymentRequest", publisherPaymentRequest);
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-publisher-payment-request";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/publisherPaymentRequestEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Publisher Payment Request";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/publisherPaymentRequestEmail.vm";
//	}

}
