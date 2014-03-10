package com.paperight.email.integration;

import java.io.IOException;
import java.net.URLEncoder;
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

import com.paperight.email.ResetPassword;
import com.paperight.user.UserService;

public class ResetPasswordEmailTransformer extends EmailTransformer {
	
	@Autowired
	UserService userService;

	@Transformer
    public MimeMessage transform(final ResetPassword resetPassword) throws MessagingException, IOException {
    	MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(resetPassword.getUser().getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(resetPassword);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final ResetPassword resetPassword) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("appUrl", getApplicationUrl());
    	String resetPasswordHash = userService.buildResetPasswordHash(resetPassword.getUser());
    	String redirectUrl = URLEncoder.encode(resetPassword.getRedirectUrl());
    	String resetPasswordUrl = getApplicationUrl() + "/password/reset?redirectUrl=" + redirectUrl +  "&email=" + StringEscapeUtils.escapeHtml4(resetPassword.getUser().getEmail()) + "&hash=" + resetPasswordHash;
    	model.put("resetPasswordUrl", resetPasswordUrl);
    	model.put("user", resetPassword.getUser());
    	return model;
    }
    
    @Override
    protected String getTemplateName() {
    	return "email-template-reset-password";
    }

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/resetPasswordEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}
	
	@Override
	protected String getInternalSubject() {
		return "Reset your password on Paperight";
	}

//	@Override
//	protected String getTemplate() {
//		return "/velocity/resetPasswordEmail.vm";
//	}
    
}
