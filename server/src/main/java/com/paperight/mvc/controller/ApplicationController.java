package com.paperight.mvc.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.Application;
import com.paperight.currency.CurrencyService;
import com.paperight.pdf.PdfExecutorFactory.PdfExecutorType;

@Controller
@SessionAttributes({"application"})
@PreAuthorize("hasPermission(#user, 'CONFIGURATION')")
public class ApplicationController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Application application;
	
	@Autowired
	private CurrencyService currencyService;
	
	@RequestMapping(value = "/application/config", method = RequestMethod.GET)
	public String update(Model model) {
		model.addAttribute("application", application);
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("pdfConversions", PdfExecutorType.values());
		return "application/config";
	}
	
	@RequestMapping(value = "/application/config", method = RequestMethod.POST)
	public String update(@ModelAttribute Application application, BindingResult result, Model model, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "application/config";
		}
		refreshWebsiteApplicationSettings();
		sessionStatus.setComplete();
		redirectAttributes.addFlashAttribute("notificationType", "success");
		redirectAttributes.addFlashAttribute("notificationMessage", "Application configuration updated");
		return "redirect:/application/config";
	}
	
	@Value("${base.url}/application/settings/refresh")
	private String reloadWebsiteApplicationSettingsUrl;
	
	private void refreshWebsiteApplicationSettings() {
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
	        HttpGet httpget = new HttpGet(reloadWebsiteApplicationSettingsUrl);
	        httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send application settings refresh", e);
		}
	}

}
