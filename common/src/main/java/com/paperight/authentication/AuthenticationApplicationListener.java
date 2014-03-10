package com.paperight.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.paperight.authentication.AuthenticationLog.AuthenticationLogContext;
import com.paperight.user.User;

@Component
public class AuthenticationApplicationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		User user = AuthenticationService.currentActingUser();
		if (user != null) {
			AuthenticationLogContext authenticationLogContext = getAuthenticationLogContext(event);
			if (authenticationLogContext != null) {
				AuthenticationService.createLog(user, authenticationLogContext);
			}
		}
	}
	
	private AuthenticationLogContext getAuthenticationLogContext(InteractiveAuthenticationSuccessEvent event) {
		return eventGeneratedByClassToAuthenticationLogContextMap.get(event.getGeneratedBy());
	}
	
	private static Map<Class<?>, AuthenticationLogContext> eventGeneratedByClassToAuthenticationLogContextMap = new HashMap<Class<?>, AuthenticationLog.AuthenticationLogContext>();
	
	static {
		eventGeneratedByClassToAuthenticationLogContextMap.put(RememberMeAuthenticationFilter.class, AuthenticationLogContext.COOKIE);
		eventGeneratedByClassToAuthenticationLogContextMap.put(UsernamePasswordAuthenticationFilter.class, AuthenticationLogContext.FORM);
	}

}
