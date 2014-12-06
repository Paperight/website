package com.paperight.mvc.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.paperight.content.Article;

@Controller
@SessionAttributes({"article"})
@PreAuthorize("hasPermission(#user, 'ARTICLES')")
public class ArticleController {

	private Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@InitBinder("article")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	@RequestMapping(value="/articles", method = RequestMethod.GET )
	public String searchArticles(Model model) {
		model.addAttribute("articles", Article.findLatestAll());
		return "articles";
	}
	
	@RequestMapping(value="/article/update/{articleId}", method = RequestMethod.GET )
	public String updateArticle(@PathVariable Long articleId, Model model) {
		Article article;
		if (articleId == null) {
			article = new Article();
		} else {
			article = Article.find(articleId);
		}
		model.addAttribute("article", article);
		return "article/update";
	}
	
	@RequestMapping(value = "/article/create", method = RequestMethod.GET)
	public String createArticle(Model model) {
		return updateArticle(null, model);
	}
	
	@RequestMapping(value = "/article/update", method = RequestMethod.POST)
	public String update(@ModelAttribute Article article, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "article/update";
		}
		article = createNewArticleRevision(article);
		article.merge();
		sessionStatus.setComplete();
		refreshWebsiteArticle(article.getName());
		return "redirect:/articles";
	}
	
	private Article createNewArticleRevision(Article article) {
		Article newArticle = null;
		try {
			newArticle = (Article) BeanUtils.cloneBean(article);
			newArticle.setId(null);
			newArticle.setCreatedDate(null);
			newArticle.setRevision(article.getRevision() + 1);
		} catch (Exception e) {
			e.printStackTrace();
			newArticle = article;
		}		
		return newArticle;
	}
	
	@Value("${base.url}/article/refresh/")
	private String reloadWebsiteArticleUrl;
	
	private void refreshWebsiteArticle(String name) {
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
			HttpGet httpget = new HttpGet(reloadWebsiteArticleUrl + name);
			httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send refresh article", e);
		}
	}
	
	@RequestMapping(value="/article/publish/{articleId}/{isPublished}")
	public String publishArticle(@PathVariable Long articleId, @PathVariable boolean isPublished) {
		Article article = Article.find(articleId);
		article.publishArticlesByName(article.getName(), isPublished);
		refreshWebsiteArticle(article.getName());
		return "redirect:/articles";
	}

}
