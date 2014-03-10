package com.paperight.publisherearning;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.paperight.email.integration.EmailGateway;
import com.paperight.user.Company;
import com.paperight.user.Role;

public class PublisherEarningReminderTask {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmailGateway emailGateway;
	
	public void runTask() {
		List<Company> activePublishers = Company.findAllActiveByRole(Role.ROLE_PUBLISHER);
		for (Company company : activePublishers) {
			emailGateway.publisherEarningReminder(company);
		}
	}
	
}
