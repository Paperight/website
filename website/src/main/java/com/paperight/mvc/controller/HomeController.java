package com.paperight.mvc.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paperight.authentication.AuthenticationService;
import com.paperight.content.Article;
import com.paperight.content.ContentService;
import com.paperight.credit.PaperightCreditService;
import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.product.Poster;
import com.paperight.theme.ThemeService;
import com.paperight.user.User;

@Controller
public class HomeController {

	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private PaperightCreditService paperightCreditService;

	@Autowired
	private ThemeService themeService;

	@RequestMapping(value = "/")
	public String home(Model model) {
	    Article article = contentService.getArticle("home");
		//List<Poster> posters = loadPosters();
		model.addAttribute("article", article);
		return "home";
	}
	
	@Cacheable(value = "orderedPosters")
	private List<Poster> loadPosters() {
		return Poster.findPostersOrderedByThemeId(themeService.getCurrentTheme().getId());
	}
	
	/** 
	 * Called by server after editing posters.
	 * Clears cached posters
	 */
	@RequestMapping(value = "/posters/refresh", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object refreshPosters() {
		Response response = new Response();
		try {
			clearPosterCache();
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}
	
	@CacheEvict(value = "orderedPosters", allEntries = true)
	private void clearPosterCache() {
		//dummy method to evict posters cache
	}
	
	@RequestMapping(value = "/application/settings/refresh", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object refreshApplicationSettings() {
		Response response = new Response();
		try {
			clearApplicationSettingsCache();
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}
	
	@CacheEvict(value = "applicationSettings", allEntries = true)
	private void clearApplicationSettingsCache() {
		//dummy method to evict posters cache
	}

	/** 
	 * Called by server after editing snippets.
	 * Clears cached snippets
	 */
	@RequestMapping(value = "/snippets/refresh/{groupName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object refreshSnippets(@PathVariable String groupName) {
		Response response = new Response();
		try {
			contentService.refreshSnippetsCacheByGroup(groupName);
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}

	/** 
	 * Called by server after editing an article.
	 * Clears cached article
	 */
	@RequestMapping(value = "/article/refresh/{name}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object refreshArticle(@PathVariable String name) {
		Response response = new Response();
		try {
			contentService.refreshArticleCacheByName(name);
			response.setStatus(ResponseStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage = errorMessage + ": " + e.getCause().getMessage(); 
			}
			response.setResponseObject(errorMessage);
		}
		return response;
	}

	/**
	 * Loads a Map with various configuration used in the PaperightContext
	 * javascript object.
	 * 
	 * @param model
	 * @param request
	 * @return Model
	 */
	@RequestMapping(value = "/js/paperight.context.json*", method = RequestMethod.GET)
	public @ResponseBody Object contextJs(HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    
		User user = AuthenticationService.currentActingUser();
		Currency currency = (user != null) ? user.getCompany().getCurrency() : currencyService.getDefaultCurrency();
		response.put("contextPath", request.getContextPath());
		response.put("currency", getCurrencyMap(currency));
		if (user != null) {
		    response.put("credits", user.getCompany().getCredits());
		    response.put("averagePrintingCost", user.getCompany().getAveragePrintingCost());
		    response.put("averageBindingCost", user.getCompany().getAverageBindingCost());
		} else {
		    response.put("credits", 0);
		    response.put("averagePrintingCost", 0);
		    response.put("averageBindingCost", 0);
		}
		
	    return response;
	}

	private Map<String, Object> getCurrencyMap(Currency currency) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("symbol", currency.getSymbol());
		BigDecimal rate = currency.getRate().divide(paperightCreditService.getPaperightCreditToBaseCurrencyRate());
		map.put("rate", rate);
		return map;
	}

}