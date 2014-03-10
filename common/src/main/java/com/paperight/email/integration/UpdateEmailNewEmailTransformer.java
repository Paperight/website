package com.paperight.email.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.paperight.email.UpdateEmail;
import com.paperight.user.UserService;

public class UpdateEmailNewEmailTransformer extends EmailTransformer {
	
	@Autowired
	UserService userService;

	@Transformer
    public MimeMessage transform(final UpdateEmail updateEmail) throws MessagingException, IOException {
    	MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(updateEmail.getUser().getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(updateEmail);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final UpdateEmail updateEmail) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	String userActivationHash = userService.buildUserActivationHash(updateEmail.getUser());
    	String activateUrl = getApplicationUrl() + "/activate?email=" + StringEscapeUtils.escapeHtml4(updateEmail.getUser().getEmail()) + "&hash=" + userActivationHash;
    	model.put("activateUrl", activateUrl);
    	model.put("oldEmail", updateEmail.getOldEmail());
    	model.put("user", updateEmail.getUser());
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-update-email-new-email";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/updateEmailNewEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Your email has been updated on Paperight";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/updateEmailNewEmail.vm";
//	}
    
}
