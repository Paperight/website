package com.paperight.dto.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.paperight.product.Product;
import com.paperight.user.Company;

public class ProductOwnershipCompanyDto {

	private Long id;
	private String name;
	private String companyAdminEmail;
	private Set<Product> products;
	
	public static final List<ProductOwnershipCompanyDto> buildDtos(List<Company> companies) {
		List<ProductOwnershipCompanyDto> dtos = new ArrayList<ProductOwnershipCompanyDto>();
		for (Company company : companies) {
			dtos.add(new ProductOwnershipCompanyDto(company));
		}
		return dtos;
	}
	
	public ProductOwnershipCompanyDto(Company company) {
		super();
		this.setId(company.getId());
		this.setName(company.getName());
		//this.setProducts(company.getProducts());
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

	public String getCompanyAdminEmail() {
		return companyAdminEmail;
	}

	public void setCompanyAdminEmail(String companyAdminEmail) {
		this.companyAdminEmail = companyAdminEmail;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
