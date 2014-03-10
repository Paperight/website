package com.paperight.mvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.content.ContentService;
import com.paperight.content.Snippet;

@Configurable
public class SnippetTag extends SimpleTagSupport {
	
	@Autowired
	ContentService contentService;
	
	private String group;
	private String name;
	private String defaultValue;
	private boolean multiline = false;
	private boolean escapeJavascript = false;
	
	public void doTag() throws JspException, IOException {
		Snippet snippet = contentService.getSnippet(getName(), getGroup(), getDefaultValue(), isMultiline());
		if (snippet != null) {
			String value = getSnippetValue(snippet);
			getJspContext().getOut().write(value);		
		}
	}
	
	private String getSnippetValue(Snippet snippet) {
		String value = snippet.getValue();
		return escape(value);
		//return String.format("<div data-snippet-name=\"%s\" data-snippet-group=\"%s\">%s</div>", snippet.getName(), snippet.getGroupName(), );
	}
	
	private String escape(String value) {
		if (isEscapeJavascript()) {
			return StringEscapeUtils.escapeEcmaScript(value); 
		}
		return value;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		defaultValue = StringEscapeUtils.unescapeHtml4(defaultValue);
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isMultiline() {
		return multiline;
	}

	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}

	public boolean isEscapeJavascript() {
		return escapeJavascript;
	}

	public void setEscapeJavascript(boolean escapeJavascript) {
		this.escapeJavascript = escapeJavascript;
	}

}
