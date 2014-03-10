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

import com.paperight.user.User;
import com.paperight.user.UserService;

public class CloseUserEmailTransformer extends EmailTransformer {
	
	@Autowired
	UserService userService;

	@Transformer
    public MimeMessage transform(final User user) throws MessagingException, IOException {
    	MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(user.getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(user);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final User user) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("appUrl", getApplicationUrl());
    	model.put("user", user);
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-close-user";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/closeUserEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Your paperight.com account has been closed";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/closeUserEmail.vm";
//	}
    
}
