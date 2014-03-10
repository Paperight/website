package com.paperight.user;
import java.util.ArrayList;
import java.util.List;

public class CompanyHeirachy {

	private Company company;
	private List<User> users;

	private List<CompanyHeirachy> children = new ArrayList<CompanyHeirachy>();

	public CompanyHeirachy(Company company) {
		this.company = company;
		this.users = User.findByCompany(this.company.getId());
		List<Company> companies = Company.findByParentCompany(this.company.getId());
		for (Company childCompany : companies) {
			if(childCompany.isDeleted() == false){
				this.children.add(new CompanyHeirachy(childCompany));
			}
		}
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<CompanyHeirachy> getChildren() {
		return children;
	}

	public void setChildren(List<CompanyHeirachy> children) {
		this.children = children;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}