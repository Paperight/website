package com.paperight.mvc.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paperight.authentication.AuthenticationService;
import com.paperight.country.CountryService;
import com.paperight.currency.CurrencyService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.user.Address;
import com.paperight.user.AddressContext;
import com.paperight.user.AddressContextType;
import com.paperight.user.Company;
import com.paperight.user.Role;
import com.paperight.user.User;

@Controller
@PreAuthorize("isAnonymous()")
public class RegistrationController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private EmailGateway emailGateway;

	@InitBinder("userRegistration")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("user.id", "user.roles");
	}

	@Transactional
	private void saveUser(User user, Company company, Address address) {
		AddressContext addressContext = new AddressContext();
		addressContext.setType(AddressContextType.DEFAULT_PRIMARY);
		addressContext.setAddress(address);
		company.setCredits(new BigDecimal(0));
		company.addAddressContext(addressContext);
		company.persist();

		user.addRole(Role.ROLE_USER);
		user.addRole(Role.ROLE_COMPANY_ADMIN);
		user.setCompany(company);
		user.setVerified(false);
		user.setEnabled(true);

		user.setUsername(user.getEmail());
		user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
		user.persist();
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, @RequestParam(value = "outlet", required = false) boolean outlet, @RequestParam(value = "publisher", required = false) boolean publisher) {
		User user = new User();
		if (outlet)
			user.addRole(Role.ROLE_OUTLET);
		if (publisher)
			user.addRole(Role.ROLE_PUBLISHER);
		UserRegistration userRegistration = new UserRegistration(user);
		model.addAttribute("userRegistration", userRegistration);
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("countries", countryService.getCountries());
		model.addAttribute("countryCurrencyMap", countryService.getCountryToCurrencyMappings());
		int captcha1 = (int)(Math.random() * ((9 - 0) + 1));
		int captcha2 = (int)(Math.random() * ((9 - 0) + 1));
		int captchaAnswer = captcha1 + captcha2;
		model.addAttribute("captcha1", captcha1);
		model.addAttribute("captcha2", captcha2);
		model.addAttribute("captchaAnswer", captchaAnswer);
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(Model model, @Valid UserRegistration userRegistration, BindingResult result) {
		User user = userRegistration.getUser();
		model.addAttribute("userRegistration", userRegistration);
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("countries", countryService.getCountries());
		int captcha1 = (int)(Math.random() * ((9 - 0) + 1));
		int captcha2 = (int)(Math.random() * ((9 - 0) + 1));
		int captchaAnswer = captcha1 + captcha2;
		model.addAttribute("captcha1", captcha1);
		model.addAttribute("captcha2", captcha2);
		model.addAttribute("captchaAnswer", captchaAnswer);
		if (result.hasErrors()) {
			return "register";
		}
		userRegistration.applyUserRoles();
		saveUser(user, userRegistration.getCompany(), userRegistration.getAddress());		
		authenticationService.authenticateUser(user);
		emailGateway.userRegistration(user);
		model.addAttribute("user", user);
		return "registration/complete";
	}

}

class UserRegistration {

	@Valid
	private User user;

	@Valid
	private String confirmPassword;

	@Valid
	private String confirmEmail;

	@Valid
	private Company company;

	@Valid
	private Address address;

	@NotNull(message = "You must represent either an outlet or a publisher")
	private List<Role> roles = new ArrayList<Role>();
	
	private int captcha1;
	private int captcha2;
	private int captchaYourAnswer;
	
	private String gllpLatitude = "-33.97883008368292";
	private String gllpLongitude = "18.463618755340576";
	

	public UserRegistration() {
		captcha1 = (int)(Math.random() * ((9 - 0) + 1));
		captcha2 = (int)(Math.random() * ((9 - 0) + 1));
	}

	public UserRegistration(User user) {
		super();
		this.user = user;
		addUserRoles(user);
	}

	@AssertTrue(message = "Confirm password must be the same as your password.")
	public boolean isPasswordValid() {
		return StringUtils.equals(getUser().getPassword(), getConfirmPassword());
	}

	@AssertTrue(message = "Confirm email must be the same as your email.")
	public boolean isEmailValid() {
		return StringUtils.equals(getUser().getEmail(), getConfirmEmail());
	}

	@AssertTrue(message = "Email already in use.")
	public boolean isUsernameAvailable() {
		return (User.findByUsername(confirmEmail) == null) ? true : false;
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public String getGllpLongitude() {
		return gllpLongitude;
	}

	public void setGllpLongitude(String gllpLongitude) {
		this.gllpLongitude = gllpLongitude;
	}

	public String getGllpLatitude() {
		return gllpLatitude;
	}

	public void setGllpLatitude(String gllpLatitude) {
		this.gllpLatitude = gllpLatitude;
	}

}
