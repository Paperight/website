package com.paperight.pdf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class PdfExecutorFactory {

	public enum PdfExecutorType {
		DOCRAPTOR_TEST("Docraptor - Test Account"),
		DOCRAPTOR("DocRaptor"),
		PRINCE("Prince XML");
		
		private String displayName;
		
		private PdfExecutorType(String displayName) {
			this.setDisplayName(displayName);
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
	}

	private static Map<PdfExecutorType, Class<? extends PdfExecutor>> executors = new HashMap<PdfExecutorType, Class<? extends PdfExecutor>>();
	
	public static void register(PdfExecutorType executorType, Class<? extends PdfExecutor> clazz) {
		Collections.synchronizedMap(executors).put(executorType, clazz);
	}
	
	public static PdfExecutor create(PdfExecutorType executorType) throws Exception {
		Map<PdfExecutorType, Class<? extends PdfExecutor>> map = Collections.synchronizedMap(executors);
		Class<? extends PdfExecutor> clazz = map.get(executorType);
		if (clazz != null) {
			Object object = clazz.newInstance();
			if (object instanceof PrincePdfExecutor) {
				return (PrincePdfExecutor) object;
			} else if (object instanceof DocRaptorExecutor) {
				DocRaptorExecutor executor = (DocRaptorExecutor) object;
				if (executorType.equals(PdfExecutorType.DOCRAPTOR_TEST)) {
					executor.setTest(true);
				}
				return executor;
			}
		}
		throw new Exception("No PdfExecutor found for executor type " + executorType);
	}
	
	@PostConstruct
	public void postConstruct() {
		registerPdfExecutors();
	}

	public void registerPdfExecutors() {
		PdfExecutorFactory.register(PdfExecutorType.PRINCE, PrincePdfExecutor.class);
		PdfExecutorFactory.register(PdfExecutorType.DOCRAPTOR, DocRaptorExecutor.class);
		PdfExecutorFactory.register(PdfExecutorType.DOCRAPTOR_TEST, DocRaptorExecutor.class);
	}

}
