package com.paperight.licence;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:/META-INF/spring/security-context.xml" })
public class LicenceSummaryTest {

	@Test
	public final void testFindLicenceSummaries() {
		List<LicenceSummary> licenceSummaries = LicenceSummary.findByCompanyId(new Long(1));
		Assert.assertNotNull(licenceSummaries);
	}
	
}
