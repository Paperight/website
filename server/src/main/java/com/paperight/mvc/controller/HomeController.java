package com.paperight.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	/**
	 * Loads a Map with various configuration used in the PaperightContext
	 * javascript object.
	 * 
	 * @param model
	 * @param request
	 * @return Model
	 */
	@RequestMapping(value = "/js/paperight.context.json*", method = RequestMethod.GET)
	public @ResponseBody Object contextJs(Model model, HttpServletRequest request) {
		model.addAttribute("contextPath", request.getContextPath());
		return model;
	}
	
}
