package com.paperight.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.DefaultRedirectStrategy;

public class RedirectUrlRedirectStrategy extends DefaultRedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String redirectUrl = request.getParameter("redirectUrl");
		if (!StringUtils.contains(url, "redirectUrl=")) {
			if (StringUtils.contains(url, "?") ) {
				url = url + "&";
			} else {
				url = url + "?";
			}
			url = url + "redirectUrl=" + redirectUrl;
		}
		super.sendRedirect(request, response, url);
	}
	
}
