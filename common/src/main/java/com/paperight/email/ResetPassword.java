package com.paperight.email;

import com.paperight.user.User;

public class ResetPassword {
	
	public ResetPassword() {
		super();
	}
	
	public ResetPassword(User user, String redirectUrl) {
		this();
		this.setUser(user);
		this.setRedirectUrl(redirectUrl);
	}
	
	private User user;	
	private String redirectUrl;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
