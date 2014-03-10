package com.paperight.email.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.paperight.email.LicencePurchaseEmail;
import com.paperight.mvc.tags.PriceTag;

public class LicencePurchaseEmailTransformer extends EmailTransformer {

	private Logger logger = LoggerFactory.getLogger(LicencePurchaseEmailTransformer.class);

	@Transformer
    public MimeMessage transform(final LicencePurchaseEmail licencePurchaseEmail) throws MessagingException, IOException {
    	MimeMessage message = getEmailSender().createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(licencePurchaseEmail.getUser().getEmail());
    	helper.setFrom(getDefaultFromAddress(), getDefaultFromName());
    	helper.setReplyTo(getDefaultFromAddress());
    	helper.setSubject(getSubject());
    	Map<String, Object> model = buildModel(licencePurchaseEmail);
    	helper.setText(getText(model), true);
    	return message;
    }

    private Map<String, Object> buildModel(final LicencePurchaseEmail licencePurchaseEmail) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	
    	String topupUrl = getApplicationUrl() + "/account/topup";
    	String productUrl = getApplicationUrl() + "/product/" + licencePurchaseEmail.getLicence().getProduct().getId() + "/" + licencePurchaseEmail.getLicence().getProduct().getDisplayName();
    	model.put("topupUrl", topupUrl);
    	model.put("productUrl", productUrl);
    	
    	model.put("user", licencePurchaseEmail.getUser());
    	model.put("licence", licencePurchaseEmail.getLicence());
    	
    	String productTitle = licencePurchaseEmail.getLicence().getProduct().getTitle();
    	model.put("productTitle", productTitle);
    	
    	PriceTag priceTag = new PriceTag();
   		String valueCurrency = priceTag.buildPriceTag(licencePurchaseEmail.getLicence().getCurrency(), licencePurchaseEmail.getLicence().getCostInCurrency());
   		String outletCharge = priceTag.buildPriceTag(licencePurchaseEmail.getLicence().getCurrency(), licencePurchaseEmail.getLicence().getOutletCharge());
   		String totalCharge = priceTag.buildPriceTag(licencePurchaseEmail.getLicence().getCurrency(), licencePurchaseEmail.getLicence().getCostInCurrency().add(licencePurchaseEmail.getLicence().getOutletCharge()));
       	model.put("valueCurrency", valueCurrency);
       	model.put("outletCharge", outletCharge);
       	model.put("totalCharge", totalCharge);

       	return model;
    }

	@Override
	protected String getTemplateName() {
    	return "email-template-licence-purchase";
	}

	@Override
	protected String getDefaultTemplateContent() throws IOException {
		Resource resource = new ClassPathResource("/velocity/licencePurchaseEmail.vm");
		return IOUtils.toString(resource.getInputStream());
	}

	@Override
	protected String getInternalSubject() {
		return "Paperight licence-purchase notification";
	}

}
