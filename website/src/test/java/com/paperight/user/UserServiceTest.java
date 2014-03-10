package com.paperight.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resources/resource-context.xml", "classpath:/META-INF/spring/services/persistence-context.xml", "classpath:/META-INF/spring/security/spring-security-context.xml" })
public class UserServiceTest {

	@Autowired UserService userService;
	
	@Rollback(false)
	@Test
	public void testCreateUser() throws Exception {

	}
	
}
