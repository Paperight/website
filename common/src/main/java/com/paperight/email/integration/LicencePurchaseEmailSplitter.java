package com.paperight.email.integration;

import java.util.ArrayList;
import java.util.List;

import com.paperight.email.LicencePurchaseEmail;
import com.paperight.licence.Licence;
import com.paperight.user.Company;
import com.paperight.user.User;

public class LicencePurchaseEmailSplitter {

	public List<LicencePurchaseEmail> split(Licence licence) {
		List<LicencePurchaseEmail> list = new ArrayList<LicencePurchaseEmail>();
		Company company = Company.find(licence.getCompany().getId());
		for (User user : company.getActiveCompanyAdmins()) {
			LicencePurchaseEmail licencePurchaseEmail = new LicencePurchaseEmail();
			licencePurchaseEmail.setLicence(licence);
			licencePurchaseEmail.setUser(user);
			list.add(licencePurchaseEmail);
		}
		return list;
	}
}
