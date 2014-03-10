package com.paperight;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.user.Address;
import com.paperight.user.AddressContext;
import com.paperight.user.AddressContextType;
import com.paperight.user.Company;
import com.paperight.user.Permission;
import com.paperight.user.Role;
import com.paperight.user.User;

@Component
public class AppDefaultsInitializer implements ApplicationListener<ContextRefreshedEvent>{

	private boolean initialized;
	
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	initialize();
    }
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private void initialize() {
		if (!initialized) {
			initializeUser();
			initialized = true;
		}
	}

	@Transactional
	private void initializeUser() {
		User user = User.findByUsername("admin@paperight.com");
		if (user == null) {
			Company company = initializeCompany();
			user = new User();		
			user.setFirstName("Admin");
			user.setLastName("User");
			user.setEmail("admin@paperight.com");
			user.setEnabled(true);
			user.setVerified(true);
			user.setUsername("admin@paperight.com");
			user.setPassword(passwordEncoder.encodePassword("Nmne2b!", null));
			user.setCompany(company);
		}

		for (Role role : Role.values()) {
			user.addRole(role);
		}
		for (Permission permission : Permission.values()) {
			user.addPermission(permission);
		}
		user.merge();

	}
	
	private Company initializeCompany() {
		Company company = new Company();
		company.setName("Paperight");
		company.setCredits(new BigDecimal(6000));
		company.setCurrencyCode("ZAR");
		company.setDateOfEstablishment(new DateTime());
		company.setDescription("Turns anyone with any printer into a print-on-demand bookstore");
		company.setPhoneNumber("021 671 1278");
		company.setWebsiteAddress("http://www.paperight.com");
		Address address = new Address();
		address.setAddressLine1("address line 1");
		address.setAddressLine2("address line 2");
		address.setAddressLine3("address line 3");
		address.setAddressLine4("address line 4");
		address.setAddressLine5("address line 5");
		address.setAddressLine6("address line 6");
		address.setPostalCode("post code");
		address.setCountryCode("ZA");
		AddressContext addressContext = new AddressContext();
		addressContext.setAddress(address);
		addressContext.setType(AddressContextType.DEFAULT_PRIMARY);
		company.addAddressContext(addressContext);
		company.persist();
		return company;
	}
	
}
