package com.paperight.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paperight.authentication.AuthenticationService;
import com.paperight.licence.Licence;
import com.paperight.licence.LicenceSummary;
import com.paperight.publisherearning.OverallPublisherEarningSummary;
import com.paperight.publisherearning.PublisherEarning;
import com.paperight.publisherearning.PublisherEarningSearch;
import com.paperight.publisherearning.PublisherEarningService;
import com.paperight.user.User;

@Controller
@PreAuthorize("isAuthenticated()")
public class DashboardController {
	
	@Autowired
	private PublisherEarningService publisherEarningService;

	@RequestMapping(value = "/dashboard" , method = RequestMethod.GET)
	public String dashboard(Model model) {
		User user = User.find(AuthenticationService.currentActingUser().getId());
		model.addAttribute("user", user);
		
		List<LicenceSummary> licenceSummaries = LicenceSummary.findByCompanyId(user.getCompany().getId());
		List<Licence> licences = Licence.findByCompanyId(user.getCompany().getId(), 1, 5);
		model.addAttribute("licenceSummaries", licenceSummaries);
		model.addAttribute("licences", licences);
		
		List<PublisherEarning> latestPublisherEarnings = PublisherEarning.findByCompanyId(user.getCompany().getId(), 1, 5);
		List<PublisherEarning> allPublisherEarnings = PublisherEarning.findByCompanyId(user.getCompany().getId());
		OverallPublisherEarningSummary overallPublisherEarningSummary = new OverallPublisherEarningSummary(allPublisherEarnings);
		model.addAttribute("publisherEarningSearch", new PublisherEarningSearch());
		model.addAttribute("overallPublisherEarningSummary", overallPublisherEarningSummary);
		model.addAttribute("publisherEarnings", latestPublisherEarnings);
		model.addAttribute("hasPendingEarnings", publisherEarningService.hasPendingEarnings(latestPublisherEarnings));
		
		return "dashboard";
	}

}

