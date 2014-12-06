package com.paperight.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
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
	public @ResponseBody Object contextJs(HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("contextPath", request.getContextPath());
		return response;
	}
	
}
