package com.paperight.dto.company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.paperight.user.Company;

public class CreditCompanyDto {

	private Long id;
	private String name;
	private BigDecimal credits;
	private String companyAdminEmail;
	
	public static final List<CreditCompanyDto> buildDtos(List<Company> companies) {
		List<CreditCompanyDto> dtos = new ArrayList<CreditCompanyDto>();
		for (Company company : companies) {
			dtos.add(new CreditCompanyDto(company));
		}
		return dtos;
	}
	
	public CreditCompanyDto(Company company) {
		super();
		this.setId(company.getId());
		this.setName(company.getName());
		this.setCredits(company.getCredits());
		if (!company.getCompanyAdmins().isEmpty()) {
			this.setCompanyAdminEmail(company.getCompanyAdmins().get(0).getEmail());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCredits() {
		return credits;
	}

	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}

	public String getCompanyAdminEmail() {
		return companyAdminEmail;
	}

	public void setCompanyAdminEmail(String companyAdminEmail) {
		this.companyAdminEmail = companyAdminEmail;
	}

}
