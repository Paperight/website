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

import com.paperight.email.SuggestBook;

public class SuggestBookEmailTransformer extends EmailTransformer {
	
	@Transformer
    public MimeMessage transform(final SuggestBook suggestBook) throws MessagingException, IOException {
		MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(getDefaultToAddress());
    	helper.setFrom(suggestBook.getEmail(), suggestBook.getName());
    	helper.setReplyTo(suggestBook.getEmail());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(suggestBook);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final SuggestBook suggestBook) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("appUrl", getApplicationUrl());
    	model.put("suggestBook", suggestBook);
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-suggest-book";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/suggestBookEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Suggest a book";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/suggestBookEmail.vm";
//	}

}
