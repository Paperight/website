package com.paperight.email.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.email.ContactMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/email-context.xml", "classpath:META-INF/spring/security-context.xml" })
public class ContactUsEmailTransformerTest {
	
	@Test
	public final void testTransform() throws MessagingException, IOException {
		ContactMessage contactMessage = new ContactMessage();
		contactMessage.setEmail("admin@paperight.com");
		contactMessage.setName("Paperight Test");
		contactMessage.setSubject("Contact Us Email Transformer Test");
		contactMessage.setMessage("Test Message");
		ContactUsEmailTransformer transformer = new ContactUsEmailTransformer();
		MimeMessage message = transformer.transform(contactMessage);
		assertNotNull(message);
	}

}
