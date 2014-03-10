package com.paperight.product.amazon;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paperight.product.ThirdPartyProduct;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:/META-INF/spring/security-context.xml" })
public class AmazonProductServiceTest {
	
	@Autowired
	AmazonProductService amazonProductService;

	@Test
	public final void test() throws Exception {
		List<ThirdPartyProduct> products = amazonProductService.search("harry potter", 1);
		System.out.println("Title: " + products.get(0));
		fail("Not yet implemented");
	}

}
