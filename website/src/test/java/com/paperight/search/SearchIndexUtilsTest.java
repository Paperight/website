package com.paperight.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/resource-context.xml", "classpath:/META-INF/spring/persistence-context.xml", "classpath:/META-INF/spring/security-context.xml" })
public class SearchIndexUtilsTest {

	@Test
	public final void testRebuildSearchIndex() throws InterruptedException {
		SearchIndexUtils.rebuildSearchIndex();
	}

}
