package com.paperight.licence;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resources/resource-context.xml", "classpath:/META-INF/spring/services/persistence-context.xml" })
public class LicenceServiceTest {
	
	@Autowired
	LicenceService licenceService;

	@Test
	public final void testGenerateFile() throws Exception {
		//licenceService.generateFile(licence);
		fail("Not yet implemented");
	}

}
