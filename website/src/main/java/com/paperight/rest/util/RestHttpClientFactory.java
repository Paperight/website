package com.paperight.rest.util;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

@Configurable
public class RestHttpClientFactory {

	@Value("${server.rest.host}")
	private String endpoint;
	
	@Autowired
	private ClientHttpRequestFactory httpClientFactory;
	
	private RestHttpClientFactory() {
		super();
	}
	
	public static ClientHttpRequest newInstance(String relativeUrl, HttpMethod method) throws IOException {
		RestHttpClientFactory restHttpClientFactory = new RestHttpClientFactory();
		return restHttpClientFactory.httpClientFactory.createRequest(URI.create(restHttpClientFactory.endpoint + relativeUrl), method);
	}
	
	public static ClientHttpResponse execute(String relativeUrl, HttpMethod method) throws IOException {
		ClientHttpRequest request = newInstance(relativeUrl, method);
		return request.execute();
	}
	
}
