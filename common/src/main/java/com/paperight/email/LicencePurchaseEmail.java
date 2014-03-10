package com.paperight.email;

import com.paperight.licence.Licence;
import com.paperight.user.User;

public class LicencePurchaseEmail {

	private User user;
	private Licence licence;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Licence getLicence() {
		return licence;
	}
	
	public void setLicence(Licence licence) {
		this.licence = licence;
	}
	
	
}
