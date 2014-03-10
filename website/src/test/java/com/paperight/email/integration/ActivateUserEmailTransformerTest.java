package com.paperight.email.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/email-context.xml", "classpath:META-INF/spring/security-context.xml" })
public class ActivateUserEmailTransformerTest {
	
	@Test
	public final void testTransform() throws MessagingException, IOException {
		ActivateUserEmailTransformer transformer = new ActivateUserEmailTransformer();
		User user = User.findByUsername("admin@paperight.com");
		MimeMessage message = transformer.transform(user);
		assertNotNull(message);
	}

}
