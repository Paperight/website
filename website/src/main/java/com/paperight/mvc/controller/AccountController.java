package com.paperight.mvc.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.authentication.AuthenticationService;
import com.paperight.content.ContentService;
import com.paperight.country.CountryService;
import com.paperight.currency.CurrencyService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.licence.LicenceSummary;
import com.paperight.mvc.util.Paginator;
import com.paperight.user.Address;
import com.paperight.user.AddressContext;
import com.paperight.user.AddressContextType;
import com.paperight.user.Company;
import com.paperight.user.CompanyHeirachy;
import com.paperight.user.PublisherPaymentDetails;
import com.paperight.user.Role;
import com.paperight.user.User;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({"childCompany", "childCompany.company", "childCompany.address", "companyUser", "userAccount", "userAccount.user", "userAccount.company", "userAccount.address" })
public class AccountController {

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ContentService contentService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailGateway emailGateway;

	@Autowired
	private Validator validator;
	
    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

	@InitBinder("userAccount")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("user.id", "user.email", "user.roles");
	}
	
	@Transactional
	private void saveUserAndCompany(User user, Company company) {
		user.merge();
		company.merge();
		if (!user.hasRole(Role.ROLE_PUBLISHER)) {
			company.deactivateProducts();
		} else {
			company.activateProducts();
		}
		if (user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
			company.syncUserRoles(user);
		}
		AuthenticationService.updateActingUser(user);
	}

	@Transactional
	private void updateUserPassword(User user, String newpasswordEncoded) {
		user.setPassword(newpasswordEncoded);
		user.merge();
		AuthenticationService.updateActingUser(user);
	}

	private UserAccount getUserAccount() {
		User user = User.find(AuthenticationService.currentActingUser().getId());
		UserAccount userAccount = new UserAccount(user);
		userAccount.setAddress(user.getCompany().getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress());
		userAccount.setUser(user);
		userAccount.setCompany(user.getCompany());
		return userAccount;
	}
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String view(Model model) {
		User user = User.find(AuthenticationService.currentActingUser().getId());
		model.addAttribute("publisherPaymentDetails", PublisherPaymentDetails.findByCompanyId(user.getCompany().getId()));
		model.addAttribute("user", user);
		model.addAttribute("companyHeirachy", new CompanyHeirachy(user.getCompany()));
		model.addAttribute("childCompany", new ChildCompany());
		model.addAttribute("companyUser", new CompanyUser());
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("countries", countryService.getCountries());
		model.addAttribute("showCloseUser", showCloseUser(user));
		model.addAttribute("showCloseCompany", showCloseCompany(user));
		return "account";
	}
	
	public boolean showCloseUser(User user) {
		boolean result = false;
		if (!user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
			result = true;
		}else if (user.getCompany().getActiveUsers().size() == 1 && user.getCompany().getActiveChildCompanies().size() == 0) {
			result = true;
		}else if (user.getCompany().getActiveCompanyAdmins().size() > 1 ) {
			result = true;
		}
		return result;
	}
	
	public boolean showCloseCompany(User user) {
		boolean result = false;
		if (!user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
			result = false;
		} else if (user.getCompany().getActiveUsers().size() > 1 || user.getCompany().getActiveChildCompanies().size() > 0) {
			result = true;
		}
		return result;
	}

	@RequestMapping(value = "/account/update", method = RequestMethod.GET)
	public String update(Model model) {
		UserAccount userAccount = getUserAccount();
		model.addAttribute("userAccount", userAccount);
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("countries", countryService.getCountries());
		model.addAttribute("mapDisplay", Company.MapDisplay.values());
		return "account/update";
	}

	@RequestMapping(value = "/account/update", method = RequestMethod.POST)
	public String update(Model model, @Valid UserAccount userAccount, BindingResult result) {
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("countries", countryService.getCountries());
		if (result.hasErrors()) {
			return "account/update";
		}
		userAccount.applyUserRoles();
		saveUserAndCompany(userAccount.getUser(), userAccount.getCompany());
		model.addAttribute("notificationType", "success");
		model.addAttribute("notificationMessage", contentService.getSnippetValue("account-updated", "notifications", "Account updated!", false));
		return "account/update/success";
	}

	@RequestMapping(value = "/account/changepassword", method = RequestMethod.GET)
	public String changePassword(Model model) {
		model.addAttribute("changePassword", new ChangePassword());
		return "change/password";
	}

	@RequestMapping(value = "/account/changepassword", method = RequestMethod.POST)
	public String changePassword(@Valid ChangePassword changePassword, BindingResult binding, Model model) {
		User user = AuthenticationService.currentActingUser();
		String oldPasswordEncoded = passwordEncoder.encodePassword(changePassword.getOldPassword(), null);
		if (!StringUtils.equals(user.getPassword(), oldPasswordEncoded)) {
			binding.addError(new FieldError("changePassword", "oldPassword", "Old password incorrect."));
		}
		if (binding.hasErrors()) {
			return "change/password";
		}
		updateUserPassword(user, passwordEncoder.encodePassword(changePassword.getNewPassword(), null));
		model.addAttribute("notificationType", "success");
		model.addAttribute("notificationMessage", contentService.getSnippetValue("account-password-updated", "notifications", "Password updated!", false));
		return "change/password/success";
	}

	@RequestMapping(value = "/account/licences", method = RequestMethod.GET)
	public String licences(@RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String page, Model model, HttpServletRequest request) {
		User user = AuthenticationService.currentActingUser();
		List<LicenceSummary> licenceSummaries = LicenceSummary.findByCompanyId(user.getCompany().getId());
		List<LicenceSummary> productLicenceSummaries = LicenceSummary.findByCompanyIdGroupedByProduct(user.getCompany().getId());
		model.addAttribute("licenceSummaries", licenceSummaries);
		model.addAttribute("productLicenceSummaries", productLicenceSummaries);
		return "account/licences";
	}
	
	@RequestMapping(value = "/account/activate", method = RequestMethod.GET)
	public String activate(RedirectAttributes redirectAttributes) {
		activateUser();
		redirectAttributes.addFlashAttribute("notificationType", "success");
		redirectAttributes.addFlashAttribute("notificationMessage", contentService.getSnippetValue("email-activation-sent", "notifications", "Please check your email, we have sent you a confirmation message that you must click to confirm your email address.", false));
		return "redirect:/account";
	}
	
	@RequestMapping(value = "/account/activate", method = RequestMethod.POST)
	public @ResponseBody Object activate() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			activateUser();
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
		}
		return map;
	}
	
	private void activateUser() {
		User user = AuthenticationService.currentActingUser();
		emailGateway.activateUser(user);
	}
	
	@RequestMapping(value = "/account/email/update", method = RequestMethod.GET)
	public String updateEmail(Model model) {
		model.addAttribute("updateEmail", new UpdateEmail());
		return "account/email/update"; 
	}
	
	@RequestMapping(value = "/account/email/update", method = RequestMethod.POST)
	public String updateEmail(@Valid UpdateEmail updateEmail, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "account/email/update";
		}
		User user = AuthenticationService.currentActingUser();
		String oldEmail = user.getEmail();
		updateUserEmail(user, updateEmail.getNewEmail());
		emailGateway.updateEmail(new com.paperight.email.UpdateEmail(user, oldEmail));
		redirectAttributes.addFlashAttribute("notificationType", "success");
		redirectAttributes.addFlashAttribute("notificationMessage", contentService.getSnippetValue("email-update-success", "notifications", "Email update successful. You will receive confirmation emails shortly.", false) );
		return "redirect:/account";
	}
	
	@RequestMapping(value = "/account/enable", method = RequestMethod.GET)
	public String enableUser() {
		User user = AuthenticationService.currentActingUser();
		user.enable();
		AuthenticationService.updateActingUser(user);
		return "redirect:/account";
	}
	
	@RequestMapping(value = "/account/disable", method = RequestMethod.GET)
	public String disableUser() {
		User user = AuthenticationService.currentActingUser();
		if (mustAlsoCloseCompany(user)) {
			user.getCompany().disable();
		} else {
			user.disable();
		}
		AuthenticationService.updateActingUser(user);
		return "redirect:/account";
	}
	
	@RequestMapping(value = "/account/company/enable", method = RequestMethod.GET)
	public String enableCompany() {
		User user = AuthenticationService.currentActingUser();
		user.getCompany().enable();
		AuthenticationService.updateActingUser(user);
		return "redirect:/account";
	}
	
	@RequestMapping(value = "/account/company/disable", method = RequestMethod.GET)
	public String disableCompany() {
		User user = AuthenticationService.currentActingUser();
		user.getCompany().disable();
		AuthenticationService.updateActingUser(user);
		return "redirect:/account";
	}

	@RequestMapping(value = "/account/close", method = RequestMethod.GET)
	public String closeUser() {
		User user = AuthenticationService.currentActingUser();
		
		if (mustAlsoCloseCompany(user)) {
			user.getCompany().close();
		} else {
			user.close();
		}
		AuthenticationService.updateActingUser(user);
		emailGateway.closeUser(user);
		if (AuthenticationService.isImpersonatingUser()) {
			return "redirect:/account";
		} else {
			return "redirect:/logout";
		}
	}
	
	private boolean mustAlsoCloseCompany(User user) {
		if (user.getCompany().getActiveUsers().size() == 1 && user.getCompany().getActiveChildCompanies().size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/close", method = RequestMethod.GET)
	public String closeCompany() {
		User user = AuthenticationService.currentActingUser();
		user.getCompany().close();
		AuthenticationService.updateActingUser(user);
		if (AuthenticationService.isImpersonatingUser()) {
			return "redirect:/account";
		} else {
			return "redirect:/logout";
		}
	}

	@Transactional
	private void updateUserEmail(User user, String newEmail) {
		user.setEmail(newEmail);
		user.setUsername(user.getEmail());
		user.setVerified(false);
		user.merge();
		AuthenticationService.updateActingUser(user);
	}

	@Transactional
	private void saveUser(User user) {
		user.persist();
	}

	@Transactional
	private void saveCompany(Company company) {
		company.persist();
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/companies.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object companies() {
        Map<String, Object> response = new HashMap<>();
		User user = AuthenticationService.currentActingUser();
		if (!isUserAuthorized(user.getCompany())) {
		    response.put("success", false);
		    response.put("message", "Permission denied");
			return response;
		}
		Company company = Company.find(user.getCompany().getId());
		response.put("data", buildCompanyMap(company));
		response.put("success", true);
		return response;
	}
	
	private Map<String, Object> buildCompanyMap(Company company){
		Map<String, Object> response = new HashMap<String, Object>();
		List<Company> companies = Company.findByParentCompany(company.getId());
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (Company childCompany : companies) {
			if (childCompany.isDeleted() == false) {
				children.add(buildCompanyMap(childCompany));
			}
		}
		response.put("children", children);
		response.put("companyId", company.getId());
		response.put("description", company.getDescription());
		response.put("name", company.getName());
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/{companyId}/users.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object companyUsers(@PathVariable Long companyId) {
        Map<String, Object> response = new HashMap<>();
		Company company = Company.find(companyId);
		if (company != null) {
			if (!isUserAuthorized(company)) {
			    response.put("success", false);
			    response.put("message", "Permission denied");
				return response;
			}
			List<User> users = User.findByCompany(company.getId());
			List<CompanyUser> companyUsers = new ArrayList<CompanyUser>();
			for (User companyUser : users) {
				companyUsers.add(new CompanyUser(companyUser));
			}
			response.put("data", companyUsers);
			response.put("success", true);
		} else {
		    response.put("data", null);
		    response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/{companyId}/{userId}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object companyUser(@PathVariable Long companyId, @PathVariable Long userId) {
	    Map<String, Object> response = new HashMap<>();
		User user = User.find(userId);
		if (user != null) {
			if (!isUserAuthorized(user.getCompany())) {
			    response.put("success", false);
			    response.put("message", "Permission denied");
				return response;
			}
			CompanyUser companyUser = new CompanyUser(user);
			response.put("data", companyUser);
			response.put("success", true);
		} else {
		    response.put("data", null);
		    response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/{companyId}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object company(@PathVariable Long companyId) {
	    Map<String, Object> response = new HashMap<>();
		Company company = Company.find(companyId);
		if (!isUserAuthorized(company)) {
		    response.put("success", false);
		    response.put("message", "Permission denied");
			return response;
		}
		if (company != null) {
			CompanyHashMap companyMap = new CompanyHashMap(company);
			response.put("data", companyMap);
			response.put("success", true);
		} else {
		    response.put("data", null);
		    response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/save/{companyId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object companyUpdate(@Valid ChildCompany childCompany, @PathVariable Long companyId, BindingResult result, HttpServletResponse servletResponse) {
		Map<String, Object> response = new HashMap<String, Object>();
		Set<ConstraintViolation<ChildCompany>> errors = getValidator().validate(childCompany);
		Address address = childCompany.getAddress();
		Company company = Company.find(companyId);
		if (errors.isEmpty() && company != null) {
			if (!isUserAuthorized(company)) {
				response.put("success", false);
				response.put("message", "Permission denied");
				return response;
			}
			company.setPhoneNumber(childCompany.getCompany().getPhoneNumber());
			company.setWebsiteAddress(childCompany.getCompany().getWebsiteAddress());
			company.setName(childCompany.getCompany().getName());
			company.setDescription(childCompany.getCompany().getDescription());
			Address companyAddress = company.getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress();
			companyAddress.setAddressLine1(address.getAddressLine1());
			companyAddress.setAddressLine2(address.getAddressLine2());
			companyAddress.setAddressLine3(address.getAddressLine3());
			companyAddress.setAddressLine4(address.getAddressLine4());
			companyAddress.setPostalCode(address.getPostalCode());
			companyAddress.setCountryCode(address.getCountryCode());
			saveCompany(company);
			response.put("success", true);
			response.put("companyId", companyId);
		}else{
			Map<String, String> validationMessages = new HashMap<String, String>();
			for (ConstraintViolation<ChildCompany> error : errors) {
				validationMessages.put(error.getPropertyPath().toString(), error.getMessage());
			}
			response.put("message", validationMessages);
			response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/users/save/{userId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> companyUserUpdate(Model model, CompanyUser companyUser, @PathVariable Long userId, BindingResult result, HttpServletResponse servletResponse) {
		Map<String, Object> response = new HashMap<String, Object>();
		Set<ConstraintViolation<CompanyUser>> errors = getValidator().validate(companyUser);
		User updateUser = User.find(userId);
		if (errors.isEmpty() && updateUser != null) {
			if (!isUserAuthorized(updateUser.getCompany())) {
				response.put("success", false);
				response.put("message", "Permission denied");
				return response;
			}
			updateUser.setFirstName(companyUser.getFirstName());
			updateUser.setLastName(companyUser.getLastName());
			updateUser.setEmail(companyUser.getEmail());
			updateUser.setSubscribed(companyUser.isSubscribed());
			
			applyCompanyUserRoles(updateUser, companyUser.getRoles());
			saveUser(updateUser);
			response.put("success", true);
			response.put("newUser", false);
			response.put("userId", userId);
		} else {
			Map<String, String> validationMessages = new HashMap<String, String>();
			for (ConstraintViolation<CompanyUser> error : errors) {
				validationMessages.put(error.getPropertyPath().toString(), error.getMessage());
			}
			response.put("message", validationMessages);
			response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/users/save", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> companyUserAdd(Model model, CompanyUser companyUser, @RequestParam Long companyId, BindingResult result, HttpServletResponse servletResponse) {
		Map<String, Object> response = new HashMap<String, Object>();
		Set<ConstraintViolation<CompanyUser>> errors = getValidator().validate(companyUser);
		if (errors.isEmpty()) {
			Company company = Company.find(companyId);
			if (!isUserAuthorized(company)) {
				response.put("success", false);
				response.put("message", "Permission denied");
				return response;
			}
			User user = new User();
			user.setPassword(passwordEncoder.encodePassword(user.getEmail(), "TEMP_SALT"));
			user.setCompany(company);
			user.setEnabled(true);
			user.setUsername(companyUser.getEmail());
			user.setEmail(companyUser.getEmail());
			user.setFirstName(companyUser.getFirstName());
			user.setLastName(companyUser.getLastName());
			user.setSubscribed(companyUser.isSubscribed());
			applyCompanyUserRoles(user, companyUser.getRoles());
			saveUser(user);
			emailGateway.newUser(user);
			response.put("success", true);
			response.put("newUser", true);
			response.put("userId", user.getId());
		} else {
			Map<String, String> validationMessages = new HashMap<String, String>();
			for (ConstraintViolation<CompanyUser> error : errors) {
				validationMessages.put(error.getPropertyPath().toString(), error.getMessage());
			}
			response.put("message", validationMessages);
			response.put("success", false);
		}
		return response;
	}
	
	private void applyCompanyUserRoles(User user, Set<Role> roles){
		User authUser = AuthenticationService.currentActingUser();
		user.removeRole(Role.ROLE_OUTLET);
		user.removeRole(Role.ROLE_PUBLISHER);
		user.removeRole(Role.ROLE_COMPANY_ADMIN);
		if(roles != null){
			for(Role role : roles){
				user.addRole(role);
			}
		}
		Set<Role> userRoles = authUser.getRoles();
		for(Role role : userRoles){
			if(role.equals(Role.ROLE_OUTLET) || role.equals(Role.ROLE_PUBLISHER)){
				user.addRole(role);
			}
		}
		if (!user.hasRole(Role.ROLE_COMPANY_ADMIN) && user.getId() != null && user.getId().equals(authUser.getId()) && authUser.hasRole(Role.ROLE_COMPANY_ADMIN)) {
			user.addRole(Role.ROLE_COMPANY_ADMIN);
		}
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/save", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object companyAdd(@Valid ChildCompany childCompany, @RequestParam Long parentCompanyId, BindingResult result) {
		Map<String, Object> response = new HashMap<String, Object>();
		Set<ConstraintViolation<ChildCompany>> errors = getValidator().validate(childCompany);
		Company parentCompany = Company.find(parentCompanyId);
		Company company = childCompany.getCompany();
		Address address = childCompany.getAddress();
		if (!isUserAuthorized(parentCompany)) {
			response.put("success", false);
			response.put("message", "Permission denied");
			return response;
		}
		if (errors.isEmpty() && parentCompany != null) {
			company.setParentCompany(parentCompany);
			company.setCredits(new BigDecimal(0));
			company.setAveragePrintingCost(parentCompany.getAveragePrintingCost());
			company.setAverageBindingCost(parentCompany.getAverageBindingCost());
			company.setCurrencyCode(parentCompany.getCurrencyCode());
			//Address parentAddress = parentCompany.getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress();
			//address.setCountryCode(parentAddress.getCountryCode());
			AddressContext addressContext = new AddressContext();
			addressContext.setAddress(address);
			addressContext.setType(AddressContextType.DEFAULT_PRIMARY);
			company.addAddressContext(addressContext);
			saveCompany(company);
			response.put("success", true);
			response.put("companyId", company.getId());
		}else{
			Map<String, String> validationMessages = new HashMap<String, String>();
			for (ConstraintViolation<ChildCompany> error : errors) {
				validationMessages.put(error.getPropertyPath().toString(), error.getMessage());
			}
			response.put("message", validationMessages);
			response.put("success", false);
		}
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/users/remove", produces = "application/json")
	public @ResponseBody Object companyUserRemove(@RequestParam Long companyid, @RequestParam Long userid) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("success", false);
		User user = User.find(userid);
		Company company = Company.find(companyid);
		if(!isUserAuthorized(company)){
		    response.put("message", "Permission denied");
			return response;
		}
		if (user == null){
		    response.put("message", "User could not be removed");
			return response;
		}
		if (AuthenticationService.currentActingUser().getId().equals(user.getId())) {
		    response.put("message", "Cannot delete own user");
			return response;
		}
		if (company == null) {
		    response.put("message", "Company not found");
			return response;
		}
		user.delete();
		response.put("success", true);
		response.put("message", "User removed");
		return response;
	}

	@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
	@RequestMapping(value = "/account/company/remove", produces = "application/json")
	public @ResponseBody Object companyRemove(@RequestParam Long companyid) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("success", false);
		User user = AuthenticationService.currentActingUser();
		Company company = Company.find(companyid);
		if(!isUserAuthorized(company)){
		    response.put("message", "Permission denied");
			return response;
		}
		if (company == null) {
		    response.put("message", "Company not found");
			return response;
		}
		if (user.getCompany().getId().equals(company.getId())) {
		    response.put("message", "Cannot remove own company");
			return response;
		}
		company.delete();
		response.put("success", true);
		response.put("message", "Company removed");
		return response;
	}
	
	private boolean isUserAuthorized(Company company) {
		User user = AuthenticationService.currentActingUser();
		Company userCompany = user.getCompany();
		if (userCompany.getId().equals(company.getId())) {
			return true;
		}
		Company parentCompany = company.getParentCompany();
		if (parentCompany != null && isUserAuthorized(parentCompany)) {
			return true;
		}
		return false;
	}

}

// Used to return a single company in JSON
class CompanyHashMap {

	private Address address;
	private Map<String, Object> company = new HashMap<String, Object>();

	public CompanyHashMap(Company company){
		this.company.put("id", company.getId());
		this.company.put("name", company.getName());
		this.company.put("websiteAddress", company.getWebsiteAddress());
		this.company.put("description", company.getDescription());
		this.company.put("phoneNumber", company.getPhoneNumber());
		this.address = company.getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress();
	}
	public Map<String, Object> getCompany() {
		return company;
	}

	public void setCompany(Map<String, Object> company) {
		this.company = company;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}

// Used as a backing object on the hidden company forms
class ChildCompany {

	@Valid
	private Company company;
	
	@Valid
	private Address address;
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

}

// Used as a backing object for the hidden user forms
class CompanyUser {

	private Long id;

	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Email
	private String confirmEmail;

	@NotNull
	@Size(min = 2, max = 255)
	private String firstName;

	@NotNull
	@Size(min = 2, max = 255)
	private String lastName;

	private Set<Role> roles = new HashSet<Role>();
	
	private boolean currentLoggedInUser = false;

	private boolean subscribed;
	
	public CompanyUser() {
	}

	public CompanyUser(User user) {
		setId(user.getId());
		setEmail(user.getEmail());
		if (user.getId() != null) {
			setConfirmEmail(user.getEmail());
		}
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setSubscribed(user.isSubscribed());
		addUserRoles(user);
		User currentUser = AuthenticationService.currentActingUser();
		setCurrentLoggedInUser(getId().equals(currentUser.getId()));
	}

	public void addUserRoles(User user) {
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			if (role == Role.ROLE_OUTLET || role == Role.ROLE_PUBLISHER || role == Role.ROLE_COMPANY_ADMIN) {
				addRole(role);
			}
		}
	}

	@AssertTrue(message = "Confirm email must be the same as your email.")
	public boolean isEmailValid() {
		return StringUtils.equals(email, confirmEmail);
	}

	@AssertTrue(message = "Email already in use.")
	public boolean isUsernameAvailable() {
		User user = User.findByUsername(confirmEmail);
		return (user == null || user.getId().equals(id)) ? true : false;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isCurrentLoggedInUser() {
		return currentLoggedInUser;
	}

	public void setCurrentLoggedInUser(boolean currentLoggedInUser) {
		this.currentLoggedInUser = currentLoggedInUser;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

}

class UserAccount {

	@Valid
	private User user;

	@Valid
	private Company company;

	@Valid
	private Address address;

	@NotNull(message = "You must represent either an outlet or a publisher")
	private List<Role> roles = new ArrayList<Role>();

	public UserAccount() {
	}

	public UserAccount(User user) {
		this.user = user;
		addUserRoles(user);
	}

	public void addUserRoles(User user) {
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			if (role == Role.ROLE_OUTLET || role == Role.ROLE_PUBLISHER) {
				addRole(role);
			}
		}
	}

	public void applyUserRoles() {
		List<Role> roles = getRoles();
		user.getRoles().remove(Role.ROLE_OUTLET);
		user.getRoles().remove(Role.ROLE_PUBLISHER);
		for (Role role : roles) {
			if (role == Role.ROLE_OUTLET || role == Role.ROLE_PUBLISHER) {
				user.addRole(role);
			}
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}

class ChangePassword {

	private String oldPassword;
	private String newPassword;
	private String confirmPassword;

	@AssertTrue(message = "Confirm password should be equal to new password.")
	public boolean matchPasswords() {
		return StringUtils.equals(newPassword, confirmPassword);
	}

	@Required
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Required
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Required
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}

class UpdateEmail {
	
	@NotEmpty
	@Email
	private String newEmail;
	
	@NotEmpty
	@Email
	private String confirmNewEmail;
	
	@AssertTrue(message = "Confirm email must be the same as your email.")
	public boolean isNewEmailValid() {
		return StringUtils.equals(getNewEmail(), getConfirmNewEmail());
	}
	
	@AssertTrue(message = "Email address already in use.")
	public boolean isEmailAvailable() {
		return (User.findByUsername(getNewEmail()) == null) ? true : false;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getConfirmNewEmail() {
		return confirmNewEmail;
	}

	public void setConfirmNewEmail(String confirmNewEmail) {
		this.confirmNewEmail = confirmNewEmail;
	}
	
}