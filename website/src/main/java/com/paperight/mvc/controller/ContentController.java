package com.paperight.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.paperight.content.Article;
import com.paperight.content.ContentService;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/article/{articleName}", method = RequestMethod.GET )
	public String getArticle(@PathVariable String articleName, Model model) {
		Article article = contentService.getArticle(articleName);
		if (article != null && article.isPublished()) {
			model.addAttribute("article", article);
			model.addAttribute("title", article.getTitle());
		} else {
			throw new ArticleNotFoundException();
		}
		return "content/article";
	}
	
	@RequestMapping(value="/terms/outlet", method = RequestMethod.GET )
	public String getOutletTerms(Model model) {
		return getArticle("outlet-terms", model);
	}
	
	@RequestMapping(value="/terms/publisher", method = RequestMethod.GET )
	public String getPublisherTerms(Model model) {
		return getArticle("publisher-terms", model);
	}
	
	@RequestMapping(value="/howto", method = RequestMethod.GET )
	public String getHowTo(Model model) {
		return getArticle("howto", model);
	}
	
	@RequestMapping(value="/privacy-policy", method = RequestMethod.GET )
	public String getPrivacyPolicy(Model model) {
		return getArticle("privacy-policy", model);
	}
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ArticleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -655956178485383710L;

}
