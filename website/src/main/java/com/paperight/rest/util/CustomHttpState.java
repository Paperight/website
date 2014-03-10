package com.paperight.rest.util;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.auth.AuthScope;

public class CustomHttpState extends HttpState {

	/**
     * Set credentials property.
     *
     * @param credentials
     * @see #setCredentials(org.apache.commons.httpclient.auth.AuthScope, org.apache.commons.httpclient.Credentials)
     */
    public void setCredentials(final Credentials credentials) {
        super.setCredentials(AuthScope.ANY, credentials);
    }

	
}
