package com.paperight.mvc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paperight.email.ContactMessage;
import com.paperight.email.integration.EmailGateway;

@Controller
public class ContactUsController {
	
	@Autowired
	private EmailGateway emailGateway;

	@RequestMapping(value = "/contactus", method = RequestMethod.GET)
	public String contactUs(Model model) {
		model.addAttribute("contactMessage", new ContactMessage());
		return "contact";
	}

	@RequestMapping(value = "/contactus", method = RequestMethod.POST)
	public String contactUs(@Valid ContactMessage contactMessage, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "contact";
		}
		emailGateway.contactUs(contactMessage);
		return "contact/success";
	}
}

