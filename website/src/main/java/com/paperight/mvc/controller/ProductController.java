package com.paperight.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.paperight.authentication.AuthenticationService;
import com.paperight.currency.CurrencyService;
import com.paperight.geo.AvailabilityService;
import com.paperight.licence.Licence;
import com.paperight.mvc.util.Paginator;
import com.paperight.product.Product;
import com.paperight.publisherearning.PublisherEarning;
import com.paperight.user.Company;
import com.paperight.user.Company.MapDisplay;
import com.paperight.user.User;

@SessionAttributes({"product"})
@Controller
public class ProductController {

	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private AvailabilityService availabilityService;

	@PreAuthorize("hasRole('ROLE_OUTLET')")
	@RequestMapping(method = RequestMethod.POST, value = "/product/{productId}/licences.html", produces = "text/html")
	public String productLicences(@PathVariable Long productId, @RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String start, Model model, HttpServletRequest request) throws Exception {
		User user = AuthenticationService.currentActingUser();
		Product product = Product.find(productId);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		if (user != null) {
			Company company = user.getCompany();
			if (company == null) {
				throw new CompanyNotFoundException();
			}
			int pageSize = 5;
			int pageNumber = 1;
			try {
				pageNumber = Integer.parseInt(start);
			} catch (Exception e) {
			}
			List<Licence> licences = Licence.findByProductIdAndCompanyId(product.getId(), company.getId(), pageNumber, pageSize);
			long licenceCount = Licence.countByProductIdAndCompanyId(product.getId(), company.getId());
			Paginator paginator = new Paginator((int) licenceCount, pageSize, pageNumber, request);
			model.addAttribute("paginator", paginator);
			model.addAttribute("licences", licences);
		}
		return "licence/list";
	}
	
	@PreAuthorize("hasRole('ROLE_PUBLISHER')")
	@RequestMapping(method = RequestMethod.POST, value = "/product/{productId}/publisher-earnings.html", produces = "text/html")
	public String productEarnings(@PathVariable Long productId, @RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String start, Model model, HttpServletRequest request) throws Exception {
		User user = AuthenticationService.currentActingUser();
		Product product = Product.find(productId);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		if (user != null) {
			Company company = user.getCompany();
			if (company == null) {
				throw new CompanyNotFoundException();
			}
			int pageSize = 5;
			int pageNumber = 1;
			try {
				pageNumber = Integer.parseInt(start);
			} catch (Exception e) {
			}
			List<PublisherEarning> publisherEarnings = PublisherEarning.findByCompanyIdAndProductId(company.getId(), product.getId(), pageNumber, pageSize);
			long earningCount = PublisherEarning.countByProductIdAndCompanyId(product.getId(), company.getId());
			Paginator paginator = new Paginator((int) earningCount, pageSize, pageNumber, request);
			model.addAttribute("paginator", paginator);
			model.addAttribute("publisherEarnings", publisherEarnings);
		}
		return "publisher-earnings/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/product/{productId}", "/product/{productId}/{additionalText}" })
	public String productDetail(@PathVariable Long productId, Model model, HttpServletRequest request) {
		Product product = Product.find(productId);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		model.addAttribute("title", product.getTitle());
		model.addAttribute("product", product);
		model.addAttribute("defaultCurrency", currencyService.getDefaultCurrency());
		model.addAttribute("available", availabilityService.isAvailable(product, request));
		User user = AuthenticationService.currentActingUser();
		Company company = null;
		if (user != null) {
			company = user.getCompany();
			if (company == null) {
				throw new CompanyNotFoundException();
			}
			model.addAttribute("company", company);
		}
		model.addAttribute("restrictedPremium", restrictedPremium(product, company));
		return "product/detail";
	}
	
	private boolean restrictedPremium(Product product, Company company) {
	    boolean result = product.isPremium();
	    if (company != null) {
	        boolean premiumCompany = company.getMapDisplay().equals(MapDisplay.FEATURE);
	        if (result && premiumCompany) {
	            result = false;
	        }
	    }
	    return result;
	}
	
	@InitBinder("product")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/product/update/{productId}", method = RequestMethod.GET)
	public String update(@PathVariable Long productId, Model model) {
		Product product;
		if (productId == null) {
			product = new Product();
		} else {
			product = Product.find(productId);
		}
		model.addAttribute("product", product);
		return "product/update";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public String update(@ModelAttribute Product product, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "product/update";
		}
		product = product.merge();
		sessionStatus.setComplete();
		return "redirect:/product/" + product.getId();
	}
	
	@Value("${jacket.image.file.folder}")
	private String jacketImageFolder;
	
	@RequestMapping(method = RequestMethod.GET, value = "/product/{productId}/jacket-image")
	public void downloadJacketImage(@PathVariable Long productId, HttpServletResponse response) throws Exception {
		Product product = Product.find(productId);
		if (product != null) {
			File file = new File(FilenameUtils.concat(jacketImageFolder, product.getJacketImageFilename()));
			if (file.exists()) {
				InputStream inputStream = new FileInputStream(file);
				try {
					String filename = URLEncoder.encode(product.getTitle(), "UTF-8");
					response.setContentType("image/jpeg");
					response.setContentLength(new Long(file.length()).intValue());
					response.addHeader("content-disposition", "attachment; filename=" + filename + ".jpg");
					IOUtils.copy(inputStream, response.getOutputStream());
					response.flushBuffer();
				} finally {
					inputStream.close();
				}
			}
		}
	}
	
	

}

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
class CompanyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}
