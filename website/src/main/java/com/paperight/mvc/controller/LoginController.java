package com.paperight.mvc.controller;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.authentication.AuthenticationService;
import com.paperight.content.ContentService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.user.Role;
import com.paperight.user.User;
import com.paperight.user.UserService;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentService contentService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailGateway emailGateway;
	
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public String activate(@RequestParam("email") String email, @RequestParam("hash") String hash) {
		User user = userService.activateUser(email, hash);
		if (user != null) {
			AuthenticationService.updateActingUser(user);
		}
		return "account/activated";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public String forgotPassword(Model model) throws Exception {
		return "forgot/password";
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public String forgotPassword(@RequestParam("email") String email, @RequestParam(value = "redirectUrl", defaultValue = "") String redirectUrl, Model model) {
		if (!email.isEmpty()) {
			User user = User.findByUsername(email);
			if (user != null) {
				emailGateway.resetPassword(new com.paperight.email.ResetPassword(user, redirectUrl));
				model.addAttribute("notificationType", "success");
				model.addAttribute("notificationMessage", contentService.getSnippetValue("forgot-password-reset-email-sent", "notifications", "Password reset request successful! An email has been sent.", false));
				return "password/sent";
			} else {
				model.addAttribute("error", contentService.getSnippetValue("forgot-password-no-user-found-error", "notifications", "Sorry, that email address isn't registered with us. Please check that you're logging in with the email address you registered with. If you haven't registered yet, do that now, it's free and quick.", false));
			}
		}
		return "forgot/password";
	}
	
	@RequestMapping(value = "/password/reset", method = RequestMethod.GET)
	public String resetPassword(@RequestParam("email") String email, @RequestParam("hash") String hash, Model model, RedirectAttributes redirectAttributes) {
		if (!email.isEmpty()) {
			if (userService.isValidResetPasswordAttempt(email, hash)) {
				ResetPassword resetPassword = new ResetPassword();
				resetPassword.setEmail(email);
				resetPassword.setHash(hash);
				model.addAttribute("resetPassword", resetPassword);
				return "password/reset";
			}
		}
		redirectAttributes.addFlashAttribute("notificationType", "error");
		redirectAttributes.addFlashAttribute("notificationMessage", contentService.getSnippetValue("password-reset-invalid-request", "notifications", "Invalid request to reset password", false));
		return "redirect:/forgotpassword";
	}
	
	@RequestMapping(value = "/password/reset", method = RequestMethod.POST)
	public String resetPassword(@Valid ResetPassword resetPassword, BindingResult result, Model model, RedirectAttributes redirectAttributes, BindingResult binding) {
		if (result.hasErrors()) {
			return "password/reset";
		}
		if (userService.isValidResetPasswordAttempt(resetPassword.getEmail(), resetPassword.getHash())) {
			User user = User.findByUsername(resetPassword.getEmail());
			user.setPassword(passwordEncoder.encodePassword(resetPassword.getNewPassword(), null));
			user.setVerified(true);
			user.merge();
			model.addAttribute("notificationType", "success");
			model.addAttribute("notificationMessage", contentService.getSnippetValue("password-reset-success", "notifications", "Password reset successful. You can now log in with your username and new password.", false));
			return "password/reset/success";
		}
		redirectAttributes.addFlashAttribute("notificationType", "error");
		redirectAttributes.addFlashAttribute("notificationMessage", contentService.getSnippetValue("password-reset-invalid-request", "notifications", "Invalid request to reset password", false));
		return "redirect:/forgotpassword";
	}
	
	@RequestMapping(value = "/user/reload/{userId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object reloadUser(@PathVariable Long userId) {
		Response response = new Response();
		try {
			AuthenticationService.reloadUser(userId);
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}
	
	@RequestMapping(value = "/company/reload/{companyId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object reloadCompany(@PathVariable Long companyId) {
		Response response = new Response();
		try {
			AuthenticationService.reloadCompany(companyId);
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}
	
	@RequestMapping(value = "/reopen/request", method = RequestMethod.GET)
	public String reopen(Model model) {
		return "reopen";
	}
	
	@RequestMapping(value = "/reopen/request", method = RequestMethod.POST)
	public String reopen(@RequestParam("email") String email, Model model) {
		if (!email.isEmpty()) {
			User user = User.findByUsername(email);
			if (user != null) {
				emailGateway.reopenUser(user);
				model.addAttribute("notificationType", "success");
				model.addAttribute("notificationMessage", contentService.getSnippetValue("reopen-account-email-sent", "notifications", "An email has been sent to you with instructions on how to reopen your account.", false));
				return "reopen/sent";
			} else {
				model.addAttribute("error", contentService.getSnippetValue("reopen-account-request-no-user-found-error", "notifications", "Sorry, that email address isn't registered with us. Please check that you're logging in with the email address you registered with. If you haven't registered yet, do that now, it's free and quick.", false));
			}
		}
		return "reopen";
	}
	
	@RequestMapping(value = "/reopen", method = RequestMethod.GET)
	public String reopenUser(@RequestParam("email") String email, @RequestParam("hash") String hash, Model model, RedirectAttributes redirectAttributes) {
		if (userService.isValidReopenUserAttempt(email, hash)) {
			User user = User.findByUsername(email);
			user.setClosed(false);
			if (user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
				user.getCompany().reopen();
			}
			user.merge();
			model.addAttribute("notificationType", "success");
			model.addAttribute("notificationMessage", contentService.getSnippetValue("reopen-account-success", "notifications", "Account reopened successfully. You can now log in with your username and password.", false));
			return "reopen/success";
		}
		redirectAttributes.addFlashAttribute("notificationType", "error");
		redirectAttributes.addFlashAttribute("notificationMessage", contentService.getSnippetValue("reopen-account-invalid-request", "notifications", "Invalid request to reopen account", false));
		return "redirect:/reopen/request";
	}

}

class ResetPassword {
	
	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	private String hash;
	
	@Valid
	@Pattern(regexp = "^[A-Za-z0-9_]+", message = "Not a valid password. Only alpha-numeric and underscore characters allowed.")
	@Size(min = 6, max = 50, message = "We need your account to be more secure. Please use 6 or more characters.")
	private String newPassword;
	
	private String confirmNewPassword;
	
	private String redirectUrl;
	
	@AssertTrue(message = "Confirm password must be the same as your password.")
	public boolean isNewPasswordValid() {
		return StringUtils.equals(getNewPassword(), getConfirmNewPassword());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
}
