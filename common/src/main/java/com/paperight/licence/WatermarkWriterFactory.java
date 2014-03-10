package com.paperight.licence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.paperight.pdf.ItextPDFWatermarkWriter;

@Service
public class WatermarkWriterFactory {

	private static Map<String, Class<? extends WatermarkWriter>> classes = new HashMap<String, Class<? extends WatermarkWriter>>();
	
	public static void register(String fileExtension, Class<? extends WatermarkWriter> clazz) {
		Collections.synchronizedMap(classes).put(fileExtension, clazz);
	}
	
	public static WatermarkWriter create(String fileExtension) throws Exception {
		Map<String, Class<? extends WatermarkWriter>> map = Collections.synchronizedMap(classes);
		Class<? extends WatermarkWriter> clazz = map.get(fileExtension);
		if (clazz != null) {
			Object object = clazz.newInstance();
			if (object instanceof WatermarkWriter) {
				return (WatermarkWriter) object;
			}
		}
		throw new Exception("No WatermarkWriter found for filetype " + fileExtension);
	}
	
	@PostConstruct
	public void postConstruct() {
		registerWatermarkWriters();
	}

	public void registerWatermarkWriters() {
		WatermarkWriterFactory.register("pdf", ItextPDFWatermarkWriter.class);
	}


	
}
