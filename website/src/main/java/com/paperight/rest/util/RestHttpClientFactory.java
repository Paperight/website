package com.paperight.rest.util;

import java.io.IOException;
import java.net.URI;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configurable
public class RestHttpClientFactory {

    @Value("${server.rest.host}")
    private String endpoint;

    @Value("${server.rest.username}")
    private String username;

    @Value("${server.rest.password}")
    private String password;

    //@Autowired
    private ClientHttpRequestFactory httpClientFactory;

    private RestHttpClientFactory() {
        super();
        
        //DefaultHttpClient httpClient = new DefaultHttpClient();
    }

    public static ClientHttpRequest newInstance(String relativeUrl, HttpMethod method) throws IOException {
        RestHttpClientFactory restHttpClientFactory = new RestHttpClientFactory();
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(restHttpClientFactory.username, restHttpClientFactory.password));
        
        HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

        restHttpClientFactory.httpClientFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return restHttpClientFactory.httpClientFactory.createRequest(URI.create(restHttpClientFactory.endpoint + relativeUrl), method);
    }

    public static ClientHttpResponse execute(String relativeUrl, HttpMethod method) throws IOException {
        ClientHttpRequest request = newInstance(relativeUrl, method);
        return request.execute();
    }

}
