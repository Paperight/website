package com.paperight.product;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/security-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:META-INF/spring/email-context.xml" })
public class ProductTest {

	@Test
	public final void testSearch() throws Exception {
		List<Product> products = Product.search("88420668710");
		Assert.assertTrue(!products.isEmpty());
	}
	
	@Test
	public final void testInsertProduct() throws Exception {
		Product product = new Product();
		product.setIdentifier(DateTime.now().toString("yyyyMMddhhmmss"));
		product.setIdentifierType("ean");
		product.setTitle("title");
		product.setLicenceFeeInDollars(new BigDecimal(3));
		product.setPublisher("Paperight");
		product.persist();
		//List<Product> products = Product.search("88420668710");
		Assert.assertTrue(product.getId() != null);
	}

}
