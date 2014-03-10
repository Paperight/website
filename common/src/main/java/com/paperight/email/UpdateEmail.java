package com.paperight.email;

import com.paperight.user.User;

public class UpdateEmail {
	
	public UpdateEmail() {
		super();
	}
	
	public UpdateEmail(User user, String oldEmail) {
		this();
		this.setUser(user);
		this.setOldEmail(oldEmail);
	}
	
	private User user;	
	private String oldEmail;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
	}

}
