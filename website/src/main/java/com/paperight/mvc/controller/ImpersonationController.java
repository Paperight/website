package com.paperight.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paperight.authentication.AuthenticationService;
import com.paperight.content.ContentService;
import com.paperight.user.User;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN') and hasPermission(#user, 'IMPERSONATE_USER')")
public class ImpersonationController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value = "/impersonate", method = RequestMethod.GET)
	public String impersonateUser() {
		return "impersonate";
	}
	
	@RequestMapping(value = "/impersonate", method = RequestMethod.POST)
	public String forgotPassword(@RequestParam("username") String username, Model model) {
		if (!username.isEmpty()) {
			User user = User.findByUsername(username);
			if (user != null) {
				AuthenticationService.updateActingUser(user);
				return "redirect:/dashboard";
			} else {
				model.addAttribute("error", contentService.getSnippetValue("impersonate-no-user-found-error", "notifications", "Sorry, that email address isn't registered with us. Please check that you're logging in with the email address you registered with. If you haven't registered yet, do that now, it's free and quick.", false));
			}
		}
		return "impersonate";
	}
	
	@RequestMapping(value = "/impersonate/logout", method = RequestMethod.GET)
	public String impersonateUserLogout() {
		AuthenticationService.updateActingUser(null);
		return "redirect:/";
	}

}
