package com.paperight.licence;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:/META-INF/spring/security-context.xml" })
public class LicenceTest {

	@Test
	public final void testFindByProductIdAndCompanyId() {
		List<Licence> licences = Licence.findByProductIdAndCompanyId(new Long(1), new Long(1), 1, 9999);
		Assert.assertNotNull(licences);
	}

}
