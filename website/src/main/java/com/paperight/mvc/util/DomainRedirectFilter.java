package com.paperight.mvc.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class DomainRedirectFilter extends OncePerRequestFilter {
	
	private String destinationDomain;
	private String sourceServletPath;

	@Override
	protected void doFilterInternal(HttpServletRequest request,	HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getServletPath();
		path = StringUtils.replace(path, getSourceServletPath(), "");
		if (request.getQueryString() != null) {
			path += '?' + request.getQueryString();
		}
		
		response.setHeader( "Location", getDestinationDomain() + path );
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader( "Connection", "close" );
	}

	public String getDestinationDomain() {
		return destinationDomain;
	}

	public void setDestinationDomain(String destinationDomain) {
		this.destinationDomain = destinationDomain;
	}

	public String getSourceServletPath() {
		return sourceServletPath;
	}

	public void setSourceServletPath(String sourceServletPath) {
		this.sourceServletPath = sourceServletPath;
	}

}
