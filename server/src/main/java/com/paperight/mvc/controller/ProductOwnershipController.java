package com.paperight.mvc.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paperight.dto.company.ProductOwnershipCompanyDto;
import com.paperight.product.Product;
import com.paperight.search.SearchResult;
import com.paperight.user.Company;
import com.paperight.utils.StringComparators;

@Controller
@PreAuthorize("hasPermission(#user, 'PRODUCT_OWNERSHIP')")
public class ProductOwnershipController {
	
	@RequestMapping(value = "/product/ownership", method = RequestMethod.GET)
	public String view(Model model) {
		List<Company> companies = Company.findAll();
		Company.sortCompanies(companies);
		List<Product> orphanProducts = Product.findByCompanyId(null);
		sortProducts(orphanProducts);
		model.addAttribute("orphanProducts", orphanProducts);
		model.addAttribute("companies", ProductOwnershipCompanyDto.buildDtos(companies));
		return "product/ownership";
	}
	
	@RequestMapping(value = "/product/ownership/companies/search.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object searchCompanies(@RequestBody String searchString, Model model) {
		List<Company> companies;
		if (StringUtils.isBlank(searchString)) {
			companies = Company.findAll();
		} else {
			companies = Company.findByNameOrUserEmail(searchString);
		}
		Company.sortCompanies(companies);
		Map<String, Object> response = new HashMap<>();
		response.put("data", ProductOwnershipCompanyDto.buildDtos(companies));
		response.put("success", true);
		return response;
	}
	
	@RequestMapping(value = "/product/ownership/{companyId}/products/search.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object searchCompanyProducts(@RequestBody String searchString, Model model, @PathVariable Long companyId) {
		List<Product> products = searchProducts(searchString, companyId);
        Map<String, Object> response = new HashMap<>();
        response.put("data", products);
        response.put("success", true);
		return response;
	}
	
	@RequestMapping(value = "/product/ownership/orphan-products/search.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object searchOrphanProducts(@RequestBody String searchString) {
		List<Product> products = searchProducts(searchString, null);
        Map<String, Object> response = new HashMap<>();
        response.put("data",  products);
        response.put("success", true);
		return response;
	}
	
	private List<Product> searchProducts(String searchString, Long companyId) {
		List<Product> products;
		if (!StringUtils.isBlank(searchString)) {
			SearchResult<Product> searchResult = new SearchResult<Product>();
			try {
				searchResult = Product.search(searchString, 1, 999999);
			} catch (Exception e) {
			}
			products = filterByCompanyId(searchResult, companyId);
		} else {
			products = Product.findByCompanyId(companyId);
		}
		sortProducts(products);
		return products;
	}
	
	private List<Product> filterByCompanyId(SearchResult<Product> searchResult, Long companyId) {
		List<Product> products = new ArrayList<Product>();
		for (Product product : searchResult.getItems()) {
			if (companyId == null) {
				if (product.getOwnerCompany() == null) {	
					products.add(product);
				}
			} else {
				if (product.getOwnerCompany() != null && product.getOwnerCompany().getId().equals(companyId)) {
					products.add(product);
				}
			}
		}
		return products;
	}
	
	private void sortProducts(List<Product> products) {
		Collections.sort(products, new Comparator<Product>() {

			@Override
			public int compare(Product product, Product otherProduct) {
				return StringComparators.compareNaturalIgnoreCaseAscii(StringUtils.defaultIfBlank(product.getTitle(), ""), StringUtils.defaultIfBlank(otherProduct.getTitle(), ""));
			}
			
		});
	}

	@RequestMapping(value = "/product/ownership/{companyId}/assign-products.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object assignProducts(@PathVariable Long companyId, @RequestBody List<Integer> productIds, Model model) {
		Company company = Company.find(companyId);
		if (company != null) {
			boolean publisherInactive = false;
			if (company.isClosed() || company.isDisabled() || company.isDeleted()) {
				publisherInactive = true;
			}
			Product.updateOwnerCompany(companyId, intsToLongs(productIds), publisherInactive);
			return true;
		} else {
			return false;
		}
	}
	
	@RequestMapping(value = "/product/ownership/unassign-products.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object assignProducts(@RequestBody List<Integer> productIds, Model model) {
		Product.unassignOwnerCompany(intsToLongs(productIds));
		return true;
	}
	
	private List<Long> intsToLongs(List<Integer> ints){
		List<Long> longs = new ArrayList<Long>();
		for (Integer intId : ints) {
			longs.add(Long.valueOf(intId));
		}
		return longs;
	}

}