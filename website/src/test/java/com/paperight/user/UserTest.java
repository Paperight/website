package com.paperight.user;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/security-context.xml" })
public class UserTest {

	@Transactional
	@Test
	@Rollback(false)
	public void testCreate() {
		Company company = createCompany();
		company.persist();
	
		User user = createUser();
		user.setCompany(company);
		
		user.persist();
		Assert.assertNotSame(user.getId(), 0);
	}

	@Transactional
	@Test
	public void testFindByUsername() {
		User user = User.findByUsername("userName");
		Assert.assertNotNull(user);
	}
	
	public static User createUser() {
		User user = new User();
		user.setUsername("userName");
		user.setPassword("password");
		user.setEnabled(true);
		user.addRole(Role.ROLE_USER);
		user.addRole(Role.ROLE_ADMIN);
		user.addRole(Role.ROLE_COMPANY_ADMIN);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		return user;
	}

	public static Company createCompany() {
		Company company = new Company();
		company.setName("myCompany");
		company.setDateOfEstablishment(new DateTime());
		company.setDescription("description");
		company.setWebsiteAddress("http://www.dog.com");
		company.setPhoneNumber("021 5584343");
		company.setCurrencyCode("ZAR");
		return company;
	}

}
