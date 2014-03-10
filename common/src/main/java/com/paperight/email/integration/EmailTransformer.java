package com.paperight.email.integration;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;

import com.paperight.content.ContentService;

@Configurable
public abstract class EmailTransformer {
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private ContentService contentService;
	
	@Value("${email.from.default}")
	private String defaultFromAddress;
	
	@Value("${email.from.default.name}")
	private String defaultFromName;
	
	@Value("${email.to.default}")
	private String defaultToAddress;
	
	@Value("${base.url}")
	private String applicationUrl;
	
	protected String getText(Map<String, Object> model) throws IOException {
		String templateContents = getTemplateContent();
    	return mergeTemplateIntoString(getVelocityEngine(), templateContents, model);
    }
	
	private String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateContents, Map model)
			throws VelocityException {
		StringWriter writer = new StringWriter();
		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.evaluate(velocityContext, writer, this.getClass().getName(), templateContents);
		return writer.toString();
	}

	public JavaMailSender getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public String getDefaultFromAddress() {
		return defaultFromAddress;
	}

	public void setDefaultFromAddress(String defaultFromAddress) {
		this.defaultFromAddress = defaultFromAddress;
	}

	public String getDefaultToAddress() {
		return defaultToAddress;
	}

	public void setDefaultToAddress(String defaultToAddress) {
		this.defaultToAddress = defaultToAddress;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
	
	private String getTemplateContent() throws IOException {
		String templateContent = contentService.getSnippetValue("content", getTemplateName(), getDefaultTemplateContent(), true);
		return wrapTemplateContent(templateContent); 
	}
	
	protected abstract String getTemplateName();
	
	protected abstract String getDefaultTemplateContent() throws IOException;
	
	private String wrapTemplateContent(String templateContent) throws IOException {
		Resource headerResource = new ClassPathResource("/velocity/header.vm");
		String header = IOUtils.toString(headerResource.getInputStream());
		
		Resource footerResource = new ClassPathResource("/velocity/footer.vm");
		String footer = IOUtils.toString(footerResource.getInputStream());
		return header + templateContent + footer;
	}
	
	protected String getSubject() {
		return contentService.getSnippetValue("subject", getTemplateName(), getInternalSubject(), false);
	}
	
	protected abstract String getInternalSubject();

	public String getDefaultFromName() {
		return defaultFromName;
	}

	public void setDefaultFromName(String defaultFromName) {
		this.defaultFromName = defaultFromName;
	}
	
}
