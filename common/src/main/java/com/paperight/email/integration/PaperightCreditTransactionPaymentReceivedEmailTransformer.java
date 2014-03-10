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

import com.paperight.credit.PaperightCreditTransaction;

public class PaperightCreditTransactionPaymentReceivedEmailTransformer extends
		EmailTransformer {

	@Transformer
    public MimeMessage transform(final PaperightCreditTransaction transaction) throws MessagingException, IOException {
		MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(transaction.getUser().getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(transaction);
    	helper.setText(getText(model), true);
    	return message;
	}
	
	private Map<String, Object> buildModel(PaperightCreditTransaction transaction) {
		Map<String, Object> model = new HashMap<String, Object>();
    	model.put("appUrl", getApplicationUrl());
    	model.put("user", transaction.getUser());
    	model.put("transaction", transaction);
    	return model;
	}
	
    @Override
    protected String getTemplateName() {
    	return "email-template-paperight-credit-transaction-payment-received";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/paperightCreditTransactionPaymentReceived.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Paperight credit payment confirmation";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/paperightCreditTransactionPaymentReceived.vm";
//	}

}
