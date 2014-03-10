package com.paperight.mvc.util;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shaine
 *
 */
public class UrlUtils {
	
	private static final String GET_VAR_SEPARATOR = "&";
	
	public static String getQueryParams(HttpServletRequest request, String queryParamToIgnore) {
		return getQueryParams(request, new String[] { queryParamToIgnore });
	}

	public static String getQueryParams(HttpServletRequest request, String[] queryParamsToIgnore) {
		String result = "";
		for( Object key: request.getParameterMap().keySet() ){
			if (!Arrays.asList(queryParamsToIgnore).contains(key)) {
				if (StringUtils.isBlank(result)) {
					result = result + GET_VAR_SEPARATOR;
				}
				String[] value = request.getParameterValues((String) key);
				result = result + key + "=" + StringEscapeUtils.escapeHtml4(value[0]);
			}
		}
		return result;
	}
	
	public static String buildRequestUrl(HttpServletRequest r) {
        return buildRequestUrl(r.getServletPath(), r.getRequestURI(), r.getContextPath(), r.getPathInfo());
    }
	
	private static String buildRequestUrl(String servletPath, String requestURI, String contextPath, String pathInfo) {

        StringBuilder url = new StringBuilder();
        
        if (!StringUtils.isBlank(servletPath)) {
            url.append(servletPath);
            if (!StringUtils.isBlank(pathInfo)) {
                url.append(pathInfo);
            }
        } else {
            url.append(requestURI.substring(contextPath.length()));
        }

        return url.toString();
    }
	
}
