package com.paperight.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.authentication.AuthenticationService;
import com.paperight.dto.company.CreditCompanyDto;
import com.paperight.user.Company;
import com.paperight.user.CompanyCreditRecord;
import com.paperight.user.User;


@Controller
@PreAuthorize("hasPermission(#user, 'EDIT_COMPANY_CREDITS')")
@RequestMapping(value = "/company")
public class CompanyController {

	private Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@Value("${base.url}/company/reload/")
	private String reloadCompanyUrl;
	
	@RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String search(Model model) {
		List<Company> companies = Company.findAll();
		Company.sortCompanies(companies);
		model.addAttribute("companies", CreditCompanyDto.buildDtos(companies));
		model.addAttribute("companyCreditRecord", new CompanyCreditRecord());
		return "company/search";
	}
	
	@RequestMapping(value = "/search.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object searchCompanies(@RequestBody String searchString) {
		List<Company> companies;
		if (StringUtils.isBlank(searchString)) {
			companies = Company.findAll();
		} else {
			companies = Company.findByNameOrUserEmail(searchString);
		}
		Company.sortCompanies(companies);
		Map<String, Object> response = new HashMap<>();
		response.put("data", CreditCompanyDto.buildDtos(companies));
		response.put("success", true);
		return response;
	}
	
	@RequestMapping(value = "/credits", method = RequestMethod.GET)
	public String getCompanyCreditRecords(Model model) {
		List<CompanyCreditRecord> companyCreditRecords = CompanyCreditRecord.findAll();
		CompanyCreditRecord.sortCompanyCreditRecords(companyCreditRecords);
		model.addAttribute("companyCreditRecords", companyCreditRecords);
		return "company/credits";
	}
	
	@RequestMapping(value = "/credits/detail/{companyCreditRecordId}", method = RequestMethod.GET)
	public String getCompanyCreditRecord(@PathVariable Long companyCreditRecordId, Model model) {
		CompanyCreditRecord companyCreditRecord = CompanyCreditRecord.find(companyCreditRecordId);
		model.addAttribute("companyCreditRecord", companyCreditRecord);
		return "company/credits/detail";
	}
	
	@RequestMapping(value = "/credit/update/{companyId}", method = RequestMethod.POST)
	public String update(@ModelAttribute CompanyCreditRecord companyCreditRecord, 
			@PathVariable Long companyId, 
			BindingResult result, 
			Model model, 
			SessionStatus sessionStatus, 
			RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			return "company/search";
		}
		Company company = Company.find(companyId);
		companyCreditRecord.setCompany(company);
		
		User user = User.find(AuthenticationService.currentActingUser().getId());
		companyCreditRecord.setUser(user);
		
		companyCreditRecord.persist();
		
		company.setCredits(company.getCredits().add(companyCreditRecord.getCredits()));
		company = company.merge();

		sendReloadCompany(companyId);

		sessionStatus.setComplete();
		redirectAttributes.addFlashAttribute("notificationType", "success");
		redirectAttributes.addFlashAttribute("notificationMessage", "Company credits updated");
		
		return "redirect:/company/credits";
	}
	
	private void sendReloadCompany(Long companyId) {
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
			HttpGet httpget = new HttpGet(reloadCompanyUrl + companyId);
			httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send reloadCompany request for companyId " + companyId, e);
		}
	}
}

class CompanySearch {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
