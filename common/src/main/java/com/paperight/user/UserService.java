package com.paperight.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User activateUser(String email, String hash) {
		String hashToCompare = buildUserActivationHash(email);
		if (StringUtils.equals(hash, hashToCompare)) {
			User user = User.findByUsername(email);
			if (user != null) {
				user.setVerified(true);
				user.persist();
				return user;
			}
		}
		return null;
	}

	public String buildUserActivationHash(final User user) {
		return buildUserActivationHash(user.getEmail());
	}
	
	private static final String ACTIVATE_SALT = "gfd56#%hgfFG";
	
	public String buildUserActivationHash(final String emailAddress) {
		return passwordEncoder.encodePassword(emailAddress, ACTIVATE_SALT);
	}
	
	public String buildResetPasswordHash(final User user) {
		return buildResetPasswordHash(user.getEmail());
	}
	
	private static final String RESET_PASSWORD_SALT = "Kf5G90!_hgrU";
	
	public String buildResetPasswordHash(final String emailAddress) {
		return passwordEncoder.encodePassword(emailAddress, RESET_PASSWORD_SALT);
	}
	
	public boolean isValidResetPasswordAttempt(final String email, final String hash) {
		String hashToCompare = buildResetPasswordHash(email);
		if (StringUtils.equals(hash, hashToCompare)) {
			return true;
		}
		return false;
	}
	
	public boolean isValidReopenUserAttempt(final String email, final String hash) {
		return isValidResetPasswordAttempt(email, hash);
	}
	

}
