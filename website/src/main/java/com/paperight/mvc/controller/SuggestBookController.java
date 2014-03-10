package com.paperight.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paperight.email.SuggestBook;
import com.paperight.email.integration.EmailGateway;

@Controller
public class SuggestBookController {
	
	@Autowired
	private EmailGateway emailGateway;
	
	@RequestMapping(value = "/suggest-book", method = RequestMethod.POST)
	public @ResponseBody Object suggestBook(SuggestBook suggestBook) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			emailGateway.suggestBook(suggestBook);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
		}
		return map;
	}

}

