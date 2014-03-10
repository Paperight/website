package com.paperight.mvc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.DataOutputStream;

@Component
public class JsonpFilter implements Filter {

	private static String[] CALLBACKS = { "cb", "callback" };
	private static Log log = LogFactory.getLog(JsonpFilter.class);

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		@SuppressWarnings("unchecked")
		Map<String, String[]> parms = httpRequest.getParameterMap();
		String callbackValue = null;
		for (String cb : CALLBACKS) {
			if (parms.containsKey(cb)) {
				callbackValue = parms.get(cb)[0];
				break;
			}
		}
		if (callbackValue != null) {
			if (log.isDebugEnabled()) {
				log.debug("Wrapping response with JSONP callback '" + callbackValue + "'");
			}
			OutputStream out = httpResponse.getOutputStream();
			GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);
			chain.doFilter(request, wrapper);
			out.write(new String(callbackValue + "(").getBytes());
			out.write(wrapper.getData());
			out.write(new String(");").getBytes());
			wrapper.setContentType("text/javascript;charset=UTF-8");
			out.close();
		} else {
			chain.doFilter(request, response);
		}
	}

	class GenericResponseWrapper extends HttpServletResponseWrapper {

		private ByteArrayOutputStream output;
		private int contentLength;
		private String contentType;

		public GenericResponseWrapper(HttpServletResponse response) {
			super(response);

			output = new ByteArrayOutputStream();
		}

		public byte[] getData() {
			return output.toByteArray();
		}

		public ServletOutputStream getOutputStream() {
			return new FilterServletOutputStream(output);
		}

		public PrintWriter getWriter() {
			return new PrintWriter(getOutputStream(), true);
		}

		public void setContentLength(int length) {
			this.contentLength = length;
			super.setContentLength(length);
		}

		public int getContentLength() {
			return contentLength;
		}

		public void setContentType(String type) {
			this.contentType = type;
			super.setContentType(type);
		}

		public String getContentType() {
			return contentType;
		}
	}

	class FilterServletOutputStream extends ServletOutputStream {

		private DataOutputStream stream;

		public FilterServletOutputStream(OutputStream output) {
			stream = new DataOutputStream(output);
		}

		public void write(int b) throws IOException {
			stream.write(b);
		}

		public void write(byte[] b) throws IOException {
			stream.write(b);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			stream.write(b, off, len);
		}

	}

	public void destroy() {
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
