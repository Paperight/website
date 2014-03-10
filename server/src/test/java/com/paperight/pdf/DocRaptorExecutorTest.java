package com.paperight.pdf;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/org/springframework/batch/admin/web/resources/webapp-config.xml", "classpath:META-INF/spring/security-context.xml", "classpath:/META-INF/spring/resource-context.xml", "classpath:META-INF/spring/basic-context.xml", "classpath:META-INF/spring/batch/jobs/product-import.xml", "classpath:META-INF/spring/persistence-context.xml" })
public class DocRaptorExecutorTest {

	@Test
	public final void testRemote() throws Exception {
		test("http://www.gutenberg.org/files/841/841-h/841-h.htm");
	}

	private void test(String htmlLocation) throws Exception {
		DocRaptorExecutor docRaptorExecutor = new DocRaptorExecutor();
		File file = docRaptorExecutor.execute(htmlLocation, null);
		System.out.println(file.getPath());
	}
	
}
