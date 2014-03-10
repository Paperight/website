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

import com.paperight.email.ContactMessage;
import com.paperight.user.UserService;

public class ContactUsEmailTransformer extends EmailTransformer {
	
	@Autowired
	UserService userService;

	@Transformer
    public MimeMessage transform(final ContactMessage contactMessage) throws MessagingException, IOException {
    	MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(getDefaultToAddress());
    	helper.setFrom(contactMessage.getEmail(), contactMessage.getName());
    	helper.setReplyTo(contactMessage.getEmail());
    	helper.setSubject(contactMessage.getSubject());
    	Map<String, Object> model = buildModel(contactMessage);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final ContactMessage contactMessage) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("appUrl", getApplicationUrl());
    	model.put("contactMessage", contactMessage);
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-contact-us";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/contactUsEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/contactUsEmail.vm";
//	}
    
}
