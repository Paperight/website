package com.paperight.product.amazon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.stereotype.Service;

import com.paperight.product.ThirdPartyProduct;

@Service
public class AmazonProductService {
	
	private Logger logger = LoggerFactory.getLogger(AmazonProductService.class);
	
	/*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
	@Value("${amazon.access.key.id}")
    private String accessKeyId;

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
	@Value("${amazon.secret.key}")
    private String secretKey;
    
	@Value("${amazon.associate.id}")
    private String associateId;
    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";
        
    private Unmarshaller createUnmarshaller() throws Exception {
    	CastorMarshaller unmarshaller = new CastorMarshaller();
    	unmarshaller.setMappingLocations(new Resource[] {new ClassPathResource("/castor/amazonProductSearchResponse.xml"), new ClassPathResource("/castor/amazonProduct.xml")});
    	unmarshaller.setIgnoreExtraElements(true);
    	unmarshaller.afterPropertiesSet();
		return unmarshaller;
	}

    @Cacheable(value = "amazonSearch")
	public List<ThirdPartyProduct> search(String searchTerms, int pageNumber) throws Exception {
		List<ThirdPartyProduct> products = new ArrayList<ThirdPartyProduct>();
		try {	    	
	    	AmazonSignedRequestsHelper helper = AmazonSignedRequestsHelper.getInstance(ENDPOINT, accessKeyId, secretKey);
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemSearch");
	        params.put("Keywords", searchTerms);
	        params.put("SearchIndex", "Books");
	        params.put("ResponseGroup", "Medium");
	        params.put("AssociateTag", associateId);
	        params.put("ItemPage", "" + pageNumber);
	        
	        String requestUrl = helper.sign(params);
	        
	        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
    			HttpGet httpget = new HttpGet(requestUrl);
    			HttpResponse response = httpclient.execute(httpget);
    			HttpEntity entity = response.getEntity();
    			if (entity != null) {
    				try (InputStream instream = entity.getContent()){
    					products = buildProducts(instream);
    				} catch (RuntimeException ex) {
    					//httpget.abort();
    					throw ex;
    				}
    			}
	        }
	    	return products;
		} catch (Exception exception) {
			logger.error("Error executing Amazon search", exception);
			return products;
		}
    }

	private List<ThirdPartyProduct> buildProducts(InputStream inputStream) throws Exception {
		Unmarshaller unmarshaller = createUnmarshaller();
		StreamSource source = new StreamSource(inputStream);
		AmazonProductSearchResponse amazonProductSearchResponse = (AmazonProductSearchResponse) unmarshaller.unmarshal(source);
		return amazonProductSearchResponse.getProducts();
	}

}
