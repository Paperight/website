package com.paperight.mvc.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class RefererTag extends SimpleTagSupport {
	
	private String fieldName = "referer";
	
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
		String referer = request.getHeader("referer");
		getJspContext().getOut().write("<input name=\"" + getFieldName() + "\" type=\"hidden\" value=\"" + referer + "\"/>");
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
