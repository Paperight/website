package com.paperight.mvc.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paperight.content.Snippet;

@Controller
@PreAuthorize("hasPermission(#user, 'SNIPPETS')")
public class SnippetController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/snippets", method = RequestMethod.GET )
	public String searchSnippets(Model model) {
		model.addAttribute("snippetGroups", Snippet.findGroups());
		return "snippets";
	}
	
	private static final String SNIPPET_ID_FIELD_PREFIX = "snippet_id_";
	
	@RequestMapping(value="/snippets/update/{groupName}", method = RequestMethod.GET )
	public String updateSnippets(@PathVariable("groupName") String groupName, Model model) {
		model.addAttribute("snippetGroup", groupName);
		model.addAttribute("snippets", Snippet.findByGroupName(groupName));
		model.addAttribute("snippet_id_field_prefix", SNIPPET_ID_FIELD_PREFIX);
		return "snippets/update";
	}
	
	@RequestMapping(value="/snippets/update/{groupName}", method = RequestMethod.POST )
	public String updateSnippets(@PathVariable("groupName") String groupName, HttpServletRequest request, Model model) {
		List<Snippet> snippets = Snippet.findByGroupName(groupName);
		updateSnippetValuesFromRequest(snippets, request);
		refreshWebsiteSnippets(groupName);
		return "redirect:/snippets";
	}
	
	private void updateSnippetValuesFromRequest(List<Snippet> snippets, HttpServletRequest request) {
		for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			if (key.startsWith(SNIPPET_ID_FIELD_PREFIX)) {
				String[] keyValue = (String[]) entry.getValue();
				String snippetValue = keyValue[0];
				Long snippetId = Long.parseLong(key.replace(SNIPPET_ID_FIELD_PREFIX, ""));
				for (Snippet snippet : snippets) {
					if (snippet.getId().intValue() == snippetId.intValue()) {
						snippet.setValue(snippetValue);
						snippet.persist();
						break;
					}
				}
				
			}
		}	
	}
	
	@Value("${base.url}/snippets/refresh/")
	private String reloadWebsiteSnippetsUrl;
	
	private void refreshWebsiteSnippets(String groupName) {
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
			HttpGet httpget = new HttpGet(reloadWebsiteSnippetsUrl + groupName);
			httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send refresh snippets", e);
		}
	}

}
