package com.paperight.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class DocRaptorExecutor extends PdfExecutor {

	private static final String ENCODING = "UTF-8";
	private static final String TAG_HTML_HEAD = "<head>";
	private static final String TAG_CSS_STYLE_BEGIN = "<style type=\"text/css\">";
	private static final String TAG_CSS_STYLE_END = "</style>";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${docraptor.api.url}")
	private String docRaptorApiUrl;

	@Value("${docraptor.api.key}")
	private String docRaptorApiKey;

	@Value("${html.pdf.css.folder:none}")
	private String htmlToPdfCssFolder;

	@Value("${html.folder:none}")
	private String htmlFolder;

	private boolean test = false;

	@Override
	public void execute(InputStream html, OutputStream out) 
			throws Exception {

		String data = convertInputStreamToData(html);

		InputStream input = execute(data);
		IOUtils.copy(input, out);
		input.close();
	}
	
	@Override
	public File execute(String htmlLocation, List<String> cssFiles)
			throws Exception {

		String fileName = getNewPdfFilename(htmlLocation);
		String data = "";

		if (!StringUtils.startsWith(htmlLocation, "http")) {
			htmlLocation = FilenameUtils.concat(htmlFolder, htmlLocation);
			File f = new File(htmlLocation);
		    byte[] buf = new byte[4096];
		    String encoding = null;
		    if(!f.exists()) { 
				throw new IOException("File '" + htmlLocation + "' not found");
			}
			InputStream inputStream = new FileInputStream(f);

			int nread;
			UniversalDetector universalDetector = new UniversalDetector(null);
			while ((nread = inputStream.read(buf)) > 0 && !universalDetector.isDone()) {
				universalDetector.handleData(buf, 0, nread);
			}
			universalDetector.dataEnd();
			encoding = universalDetector.getDetectedCharset();
			if (encoding != null) {
				System.out.println("Detected encoding = " + encoding);
			} else {
				System.out.println("No encoding detected.");
			}
			universalDetector.reset();
			
			String document = convertInputStreamToHtmlString(new FileInputStream(f));
			inputStream.close();
			try {
				for (String cssFile : cssFiles) {
					document = appendCss(document, cssFile);
				}		
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			data = convertStringToData(document, !StringUtils.isBlank(encoding) ? encoding : ENCODING);
		} else {
			try {
				/*
				String document = readHtmlFromLocation(htmlLocation);

				for (String cssFile : cssFiles) {
					document = appendCss(document, cssFile);
				}		
				data = convertStringToData(document);
				*/
				data = "doc[document_url]=" + htmlLocation;
				data += "&doc[name]=" + fileName;
				data += "&doc[document_type]=pdf";
				data += "&doc[test]=" + isTest();
				data += "&doc[strict]=none";
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		File file = new File(fileName);
		InputStream input = execute(data);
		FileUtils.copyInputStreamToFile(input, file);
		input.close();
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	private String convertInputStreamToHtmlString(InputStream inputStream) 
			throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer, ENCODING);
		String html = writer.toString();	
		return html;
	}

	private String convertInputStreamToData(InputStream in) 
			throws IOException, InterruptedException {
		String document = convertInputStreamToHtmlString(in);
		String data = convertStringToData(document);
		return data;
	}

	private String convertStringToData(String document) 
			throws IOException, InterruptedException {
		return convertStringToData(document, ENCODING);
	}

	private String convertStringToData(String document, String encoding) 
			throws IOException, InterruptedException {

		document = URLEncoder.encode(document, encoding);

		String fileName = getNewPdfFilename("");

		String data = "doc[document_content]=" + document;
		data += "&doc[name]=" + fileName;
		data += "&doc[document_type]=pdf";
		data += "&doc[test]=" + isTest();
		data += "&doc[strict]=none";
		return data;
	}

	private InputStream execute(String data) 
			throws Exception  {

		String url = docRaptorApiUrl + "?user_credentials=" + docRaptorApiKey;
		String agent = "Mozilla/4.0";
		String type = "application/x-www-form-urlencoded";

		byte[] encodedData = data.getBytes(ENCODING);

		HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("User-Agent", agent);
		conn.setRequestProperty("Content-Type", type);
		conn.setRequestProperty("Content-Length", Integer.toString(encodedData.length));

		OutputStream os = conn.getOutputStream();
		os.write(encodedData);
		os.flush();

		InputStream responseStream = null;
		try {
			responseStream = conn.getInputStream();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return responseStream;
	}

	@SuppressWarnings("unused")
	private String readHtmlFromLocation(String location) throws Exception {
		URL url = new URL(location);
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	private String appendCss(String document, String cssFile) throws Exception {
		File file = new File(htmlToPdfCssFolder, cssFile);
		if (file.exists()) {
			InputStream in = new FileInputStream(file);
			String css = convertInputStreamToHtmlString(in);

			css = TAG_CSS_STYLE_BEGIN + css + TAG_CSS_STYLE_END;

			document = StringUtils.replace(document, TAG_HTML_HEAD, TAG_HTML_HEAD + css);
		}
		return document;
	}
}
