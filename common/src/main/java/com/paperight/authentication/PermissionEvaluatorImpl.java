package com.paperight.authentication;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.paperight.user.Permission;

public class PermissionEvaluatorImpl implements PermissionEvaluator {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public boolean hasPermission(Authentication authentication,	Object targetDomainObject, Object permission) {
		boolean hasPermission = false;
		if (authentication != null && permission instanceof String) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetailsImpl) {
				UserDetailsImpl userDetails = (UserDetailsImpl) principal;
				hasPermission = userDetails.getUser().hasPermission(Permission.valueOf((String) permission));
			}
		}
		if (!hasPermission) {
			logger.debug("Denying user " + authentication.getName() + " permission '" + permission + "' on object " + targetDomainObject);
		}
		return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication,	Serializable targetId, String targetType, Object permission) {
		throw new RuntimeException("Id and Class permissions are not supperted by this application");
	}

}
