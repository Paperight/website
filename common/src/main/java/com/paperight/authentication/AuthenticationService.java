package com.paperight.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.authentication.AuthenticationLog.AuthenticationLogContext;
import com.paperight.user.Role;
import com.paperight.user.User;

@Service("authenticationService")
public class AuthenticationService implements UserDetailsService {

	private static Log log = LogFactory.getLog(AuthenticationService.class);
	private static Set<Long> reloadUserIds = new HashSet<Long>();
	private static Set<Long> reloadCompanyIds = new HashSet<Long>();

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = User.findByUsername(username);
			log.debug("Loading user [" + username + "]");
			if (user == null) {
				throw new UsernameNotFoundException("Username " + username + " not found");
			}
			return new UserDetailsImpl(user);
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static User currentAuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			UserDetailsImpl userDetails = (UserDetailsImpl) principal;
			return userDetails.getUser();
		}
		return null;
	}
	

	public static void updateAuthenticatedUser(User user) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			UserDetailsImpl userDetails = new UserDetailsImpl(user);
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
		}
	}

	public void authenticateUser(User user) {
		UserDetails userDetails = loadUserByUsername(user.getUsername());
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
	}

	public static void refreshAuthenticatedUser() {
		User user = currentAuthenticatedUser();
		if (user != null) {
			user = User.find(user.getId());
			updateAuthenticatedUser(user);
		}
	}
	
	private static User internalCurrentActingUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			UserDetailsImpl userDetails = (UserDetailsImpl) principal;
			User user = userDetails.getActingUser();
			return user;
		}
		return null;
	}
	
	public static User currentActingUser() {
		User user = internalCurrentActingUser();
		if (mustReloadUser(user) || mustReloadCompany(user)) {
			refreshActingUser();
			userReloaded(user);
			companyReloaded(user);
			user = internalCurrentActingUser();
		}
		return user;
	}
	

	public static void updateActingUser(User user) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			User currentUser = currentAuthenticatedUser();
			if (user == null) {
				user = currentUser;
			}
			UserDetailsImpl userDetails;
			if (currentUser.getId().equals(user.getId())) {
				userDetails = new UserDetailsImpl(user);
			} else {
				userDetails = new UserDetailsImpl(currentUser);
			}
			userDetails.setActingUser(user);
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
		}
	}
	
	public static void refreshActingUser() {
		User user = internalCurrentActingUser();
		if (user != null) {
			user = User.find(user.getId());
			updateActingUser(user);
		}
	}

	private static Set<Long> getReloadUserIds() {
		return reloadUserIds;
	}

	private static Set<Long> getReloadCompanyIds() {
		return reloadCompanyIds;
	}

	public static void reloadUser(Long userId) {
		getReloadUserIds().add(userId);
	}

	public static void reloadCompany(Long companyId) {
		getReloadCompanyIds().add(companyId);
	}
	
	private static boolean mustReloadUser(User user) {
		if (user != null) {
			return getReloadUserIds().contains(user.getId());
		} else {
			return false;
		}
		
	}
	
	private static boolean mustReloadCompany(User user) {
		if (user != null && user.getCompany() != null) {
			return getReloadCompanyIds().contains(user.getCompany().getId());
		} else {
			return false;
		}
		
	}
	
	private static void userReloaded(User user) {
		if (user != null) {
			getReloadUserIds().remove(user.getId());
		}
	}
	
	private static void companyReloaded(User user) {
		if (user != null && user.getCompany() != null ) {
			getReloadCompanyIds().remove(user.getCompany().getId());
		}
	}
	
	public static boolean isImpersonatingUser() {
		return currentAuthenticatedUser().getId().equals(currentActingUser().getId()) ? false : true;
	}

	public static void createLog(User user, AuthenticationLogContext context) {
		AuthenticationLog logEntry = new AuthenticationLog();
		logEntry.setUser(user);
		logEntry.setContext(context);
		logEntry.persist();
	}
}

class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 7599083537931020723L;
	private com.paperight.user.User user;
	private com.paperight.user.User actingUser = null;

	public UserDetailsImpl(com.paperight.user.User user) {
		super(user.getUsername(), user.getPassword(), (user.isEnabled() && !user.isDeleted() && !user.getCompany().isDeleted()), true, true, !user.isClosed(), getAuthorities(user.getRoles()));
		setUser(user);
	}

	public com.paperight.user.User getUser() {
		return user;
	}

	public void setUser(com.paperight.user.User user) {
		this.user = user;
	}

	private static List<GrantedAuthority> getAuthorities(Set<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.name()));
		}
		return authorities;
	}
	
	public com.paperight.user.User getActingUser() {
		if (actingUser == null) {
			return user;
		}
		return actingUser;
	}
	
	public void setActingUser(com.paperight.user.User user) {
		this.actingUser = user;
	}
	
	public boolean isImpersonatingUser() {
		return getUser().getId().equals(getActingUser().getId()) ? false : true;
	}
}