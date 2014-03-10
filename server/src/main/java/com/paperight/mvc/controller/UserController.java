package com.paperight.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.authentication.AuthenticationService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.user.Permission;
import com.paperight.user.Role;
import com.paperight.user.User;

@Controller
@SessionAttributes({"userDto", "userDto.user"})
@PreAuthorize("hasPermission(#user, 'EDIT_PAPERIGHT_STAFF')")
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailGateway emailGateway;
	
	@InitBinder("userDto")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("user.id");
	}
	
	@RequestMapping(value="/user/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String searchUsers(@RequestParam(value = "s", defaultValue = "1") String start, Model model) throws Exception {
		int pageSize = 250;
		int pageNumber = 1;
		try {
			pageNumber = Integer.parseInt(start);
		} catch (Exception e) {
		}
		List<User> users = User.findByRole(Role.ROLE_ADMIN, (pageNumber-1)*pageSize, pageSize, false);
		model.addAttribute("users", users);
		return "user/search";
	}
	
	@RequestMapping(value = "/user/disable/{userId}", method = RequestMethod.GET)
	public String disableUser(@PathVariable Long userId, Model model) {
		User user = User.find(userId);
		user.setEnabled(false);
		user.merge();
		return "redirect:/user/search/";
	}
	
	@RequestMapping(value = "/user/update/{userId}", method = RequestMethod.GET)
	public String update(@PathVariable Long userId, Model model) {
		User user;
		if (userId == null) {
			user = new User();
		} else {
			user = User.find(userId);
		}
		UserDto userDto = new UserDto(user);
		model.addAttribute("userDto", userDto);
		model.addAttribute("allPermissions", Permission.values());
		return "user/update";
	}
	
	@RequestMapping(value = "/user/create", method = RequestMethod.GET)
	public String create(Model model) {	
		return update(null, model);
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public String update(@Valid UserDto userDto, BindingResult result, Model model, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "user/update";
		}
		userDto.applyUserPermissions();
		User user = userDto.getUser();		
		user.setUsername(user.getEmail());
		boolean newUser = false;
		if (user.getId() == null ) {
			newUser = true;
			for (Role role : Role.values()) {
				user.addRole(role);
			}
			User currentUser = AuthenticationService.currentActingUser();
			user.setCompany(currentUser.getCompany());
			user.setPassword(passwordEncoder.encodePassword(user.getEmail(), "TEMP_SALT"));
		}
		user = user.merge();
		if (newUser) {
			emailGateway.newUser(user);
		}
		sessionStatus.setComplete();
		redirectAttributes.addFlashAttribute("notificationType", "success");
		if (newUser) {
			redirectAttributes.addFlashAttribute("notificationMessage", "New user created.");
		} else {
			redirectAttributes.addFlashAttribute("notificationMessage", "User updated.");
		}
		return "redirect:/user/update/" + user.getId();
	}
		
	@RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.GET)
	public String delete(@PathVariable Long userId) {
		User user = User.find(userId);
		user.setUsername(String.valueOf(user.getId()) + "_DELETED");
		user.setDeleted(true);
		user.merge();
		return "redirect:/user/search";
	}
	
}

class UserDto {

	@Valid
	private User user;
	
	private String currentEmail;
	
	@Valid
	private String confirmEmail;

	private List<Permission> permissions = new ArrayList<Permission>();
	
	public UserDto() {
		super();
	}

	public UserDto(User user) {
		super();
		setUser(user);
		setCurrentEmail(user.getEmail());
		setConfirmEmail(user.getEmail());
		addUserPermissions(user);
	}
	
	@AssertTrue(message = "Confirm email must be the same as your email.")
	public boolean isEmailValid() {
		return StringUtils.equals(getUser().getEmail(), getConfirmEmail());
	}

	@AssertTrue(message = "Email already in use.")
	public boolean isUsernameAvailable() {
		boolean mustCheckUsername = true;
		if (!StringUtils.isBlank(currentEmail) && StringUtils.equalsIgnoreCase(getCurrentEmail(), user.getEmail())) {
			mustCheckUsername = false;
		}
		boolean usernameAvailable = true;
		if (mustCheckUsername) {
			usernameAvailable = User.findByUsername(user.getEmail()) == null;
		}
		return usernameAvailable;
	}

	public void addUserPermissions(User user) {
		getPermissions().addAll(user.getPermissions());
	}

	public void applyUserPermissions() {
		user.getPermissions().clear();
		if (getPermissions() != null) {
			user.getPermissions().addAll(getPermissions());
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void addPermission(Permission permission) {
		permissions.add(permission);
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getCurrentEmail() {
		return currentEmail;
	}

	public void setCurrentEmail(String currentEmail) {
		this.currentEmail = currentEmail;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

}