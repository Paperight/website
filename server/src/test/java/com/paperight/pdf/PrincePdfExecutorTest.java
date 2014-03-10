package com.paperight.pdf;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/org/springframework/batch/admin/web/resources/webapp-config.xml", "classpath:META-INF/spring/security-context.xml", "classpath:/META-INF/spring/resource-context.xml", "classpath:META-INF/spring/basic-context.xml", "classpath:META-INF/spring/batch/jobs/product-import.xml", "classpath:META-INF/spring/persistence-context.xml" })
public class PrincePdfExecutorTest {

	@Test
	public final void testRemote() throws IOException, InterruptedException {
		test("http://www.gutenberg.org/files/841/841-h/841-h.htm");
	}
	
	@Test
	public final void testLocal() throws IOException, InterruptedException {
		test("c:/as/844-h.htm");
	}
	
	private void test(String htmlLocation) throws IOException, InterruptedException {
		PrincePdfExecutor princePdfExecutor = new PrincePdfExecutor();
//		File file = princePdfExecutor.execute(htmlLocation, "test.css");
//		System.out.println(file.getPath());
	}

}
