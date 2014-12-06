package com.paperight.mvc.controller;

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.paperight.product.Poster;
import com.paperight.theme.PaperightTheme;
import com.paperight.theme.ThemeService;

@Controller
@SessionAttributes({"poster"})
@PreAuthorize("hasPermission(#user, 'POSTERS')")
public class PosterController {
	
	private Logger logger = LoggerFactory.getLogger(PosterController.class);
	
	@Autowired
	private ThemeService themeService;
	
	@RequestMapping(value="/posters", method = RequestMethod.GET )
	public String search(@ModelAttribute PaperightTheme theme, Model model) {
		if (theme.getId() == null) {
			// Get default theme
			theme = themeService.getDefaultTheme();
		}
		model.addAttribute("theme", theme);
		model.addAttribute("themes", PaperightTheme.findAll());
		model.addAttribute("posters", Poster.findPostersOrderedByThemeId(theme.getId()));
		return "posters";
	}
	
	@InitBinder("poster")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	
	@RequestMapping(value = "/poster/update/{posterId}", method = RequestMethod.GET)
	public String update(@PathVariable Long posterId, Model model) {
		Poster poster;
		if (posterId == null) {
			poster = new Poster();
			poster.setTheme(themeService.getDefaultTheme());
		} else {
			poster = Poster.find(posterId);
		}
		List<PaperightTheme> themes = PaperightTheme.findAll();
		model.addAttribute("poster", poster);
		model.addAttribute("themes", themes);
		return "poster/update";
	}
	
	@RequestMapping(value = "/poster/create", method = RequestMethod.GET)
	public String create(Model model) {
		return update(null, model);
	}
	
	@RequestMapping(value = "/poster/update", method = RequestMethod.POST)
	public String update(@ModelAttribute Poster poster, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "poster/update";
		}
		PaperightTheme theme = PaperightTheme.find(poster.getTheme().getId());
		poster.setTheme(theme);
		poster.merge();
		refreshWebsitePosters();
		sessionStatus.setComplete();
		return "redirect:/posters";
	}
	
	@RequestMapping(value = "/poster/delete/{posterId}", method = RequestMethod.GET)
	public String delete(@PathVariable Long posterId) {
		Poster poster = Poster.find(posterId);
		if (poster != null) {
			poster.remove();
			refreshWebsitePosters();
		}
		return "redirect:/posters";
	}
	
	@RequestMapping(value = "/posters/reorder", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Object reorder(@RequestBody Map<String, Integer> map) {
		Poster.updateOrder(map);
		refreshWebsitePosters();
		return true;
	}
	
	@Value("${base.url}/posters/refresh")
	private String reloadWebsitePostersUrl;
	
	private void refreshWebsitePosters() {
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
			HttpGet httpget = new HttpGet(reloadWebsitePostersUrl);
			httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send refresh posters", e);
		}
	}

}
