package com.paperight.email.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.email.ContactMessage;
import com.paperight.email.ResetPassword;
import com.paperight.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/email-context.xml", "classpath:META-INF/spring/security-context.xml" })
public class EmailGatewayTest {
	
	@Autowired
	private EmailGateway emailGateway;
	
	@Test
	public final void testContactUs() {
		ContactMessage contactMessage = new ContactMessage();
		contactMessage.setEmail("admin@paperight.com");
		contactMessage.setName("Paperight Test");
		contactMessage.setSubject("Contact Us Email Gateway Test");
		contactMessage.setMessage("Test Message");
		emailGateway.contactUs(contactMessage);
	}
	
	@Test
	public final void testResetPassword() {
		ResetPassword resetPassword = new ResetPassword();
		User user = User.findByUsername("admin@paperight.com");
		resetPassword.setUser(user);
		emailGateway.resetPassword(resetPassword);
	}
	
	@Test
	public final void testUserRegistration() {
		User user = User.findByUsername("admin@paperight.com");
		emailGateway.userRegistration(user);
	}
	
	@Test
	public final void testActivateUser() {
		User user = User.findByUsername("admin@paperight.com");
		emailGateway.activateUser(user);
	}
	
}
