package com.paperight.content;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
	
	@Cacheable(value = "snippetGroups", key = "#group")
	public List<Snippet> getSnippetsByGroup(String group) {
		return Snippet.findByGroupName(group);
	}
	
	public Snippet getSnippet(String name, String group) {
		List<Snippet> snippets = getSnippetsByGroup(group);
		for (Snippet snippet : snippets) {
			if (StringUtils.equalsIgnoreCase(name, snippet.getName())) {
				return snippet;
			}
		}
		return null;
	}
	
	public Snippet getSnippet(String name, String group, String defaultValue, boolean multiline) {
		Snippet snippet = getSnippet(name, group);
		if (snippet == null) {
			snippet = createSnippet(name, group, defaultValue, multiline);
		}
		return snippet;
	}
	
	public String getSnippetValue(String name, String group, String defaultValue, boolean multiline) {
		return getSnippet(name, group, defaultValue, multiline).getValue();
	}
	
	private Snippet createSnippet(String name, String group, String defaultValue, boolean multiline) {
		Snippet snippet = new Snippet();
		snippet.setGroupName(group);
		snippet.setName(name);
		snippet.setValue(defaultValue);
		snippet.setMultiline(multiline);
		snippet.persist();
		refreshSnippetsCacheByGroup(group);
		return snippet;
	}
	
	@CacheEvict(value = "snippetGroups", key = "#groupName")
	public void refreshSnippetsCacheByGroup(String groupName) {
		//dummy method to clear cache
	}
	
	@CacheEvict(value = "articles", key = "#name")
	public void refreshArticleCacheByName(String name) {
		//dummy method to clear cache
	}
	
	@Cacheable(value = "articles", key = "#name")
	public Article getArticle(String name) {
		return Article.findLatestByName(name);
	}

}
