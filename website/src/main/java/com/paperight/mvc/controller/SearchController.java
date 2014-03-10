package com.paperight.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paperight.mvc.util.Paginator;
import com.paperight.product.InvalidSearchException;
import com.paperight.product.Product;
import com.paperight.product.ThirdPartyProduct;
import com.paperight.product.amazon.AmazonProductService;
import com.paperight.search.ProductSearch;
import com.paperight.search.SearchResult;

@Controller
public class SearchController {
	
	@Autowired
	private AmazonProductService amazonProductService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@RequestParam("q") String searchTerm, @RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String start, Model model, HttpServletRequest request) throws Exception {
		int pageSize = 10;
		int pageNumber = 1;
		try {
			pageNumber = Integer.parseInt(start);
		} catch (Exception e) {
			
		}

		SearchResult<Product> searchResult = new SearchResult<Product>();
		try {
			searchResult = Product.search(searchTerm, pageNumber, pageSize);
		} catch (InvalidSearchException e) {
			
		}
		
		Paginator paginator = new Paginator(searchResult.getTotalResults(), pageSize, pageNumber, request);
		model.addAttribute("paginator", paginator);
		model.addAttribute("products", searchResult.getItems());
		model.addAttribute("searchterm", searchTerm);
		return "search";
	}
	
	@RequestMapping(value = "/search/amazon.html", method = RequestMethod.POST)
	public String searchAmazon(@RequestParam("q") String searchTerm, Model model, HttpServletRequest request) throws Exception {
		List<ThirdPartyProduct> amazonProducts = new ArrayList<ThirdPartyProduct>();
		try {
			if (!StringUtils.isBlank(searchTerm)) {
				amazonProducts = amazonProductService.search(searchTerm, 1);
			}			
		} catch (Exception e) {
			
		}
		if (!amazonProducts.isEmpty()) {
			model.addAttribute("amazonProducts", amazonProducts);
			return "search/amazon";
		} else {
			return "search/amazon/noresults";
		}
	}
	
	
	
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public String browse(@RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String start, Model model, HttpServletRequest request) throws Exception {
		return search("", start, model, request);
	}
	
	@RequestMapping(value = "/search/advanced", method = RequestMethod.GET)
	public String browse(ProductSearch productSearch, @RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String start, Model model, HttpServletRequest request) throws Exception {
		int pageSize = 10;
		int pageNumber = 1;
		try {
			pageNumber = Integer.parseInt(start);
		} catch (Exception e) {
			
		}
		SearchResult<Product> searchResult = Product.search(productSearch, pageNumber, pageSize);
		
		Paginator paginator = new Paginator(searchResult.getTotalResults(), pageSize, pageNumber, request);
		model.addAttribute("paginator", paginator);
		model.addAttribute("products", searchResult.getItems());
		model.addAttribute("productSearch", productSearch);
		return "search";
	}

}
