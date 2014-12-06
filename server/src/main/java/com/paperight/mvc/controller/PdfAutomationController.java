package com.paperight.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.paperight.Application;
import com.paperight.licence.PageLayout;
import com.paperight.pdf.PdfAutomationService;
import com.paperight.pdf.PdfConversion;
import com.paperight.pdf.PdfExecutorFactory.PdfExecutorType;
import com.paperight.pdf.PdfUtils;
import com.paperight.product.Product;
import com.paperight.search.SearchResult;
import com.paperight.utils.ContentType;
import com.paperight.utils.StringComparators;

@Controller
@SessionAttributes({"toProduct"})
@RequestMapping(value = "/pdf")
public class PdfAutomationController {
	
	private Logger logger = LoggerFactory.getLogger(PdfAutomationController.class);
	
	@Autowired
	private PdfAutomationService pdfAutomationService;
	
	@Autowired
	private Application application;
	
	@Value("${html.pdf.css.folder}")
	private String htmlToPdfCssFolder;
	
	@Value("${jacket.image.file.folder}")
	private String jacketImageFolder;	
	
	@Value("${pdf.file.folder}")
	private String pdfFileFolder;

	@RequestMapping(value = "/epub", method = RequestMethod.GET)
	public String productImport(Model model) {
		model.addAttribute("epubFileUpload", new EpubFileUpload());
		return "pdf/epub";
	}
	
	@RequestMapping(value = "/epub", method = RequestMethod.POST)
	public String productImport(@Valid EpubFileUpload epubFileUpload, BindingResult result) {
		if (result.hasErrors()) {
			return "pdf/epub";
		}
		if (!epubFileUpload.getFile().isEmpty()) {
			try {
				File newPdf = pdfAutomationService.epubToPdf(epubFileUpload.getFile());
				try {
					//PdfConversion pdfConversion = pdfAutomationService.createPaperightPdfs(newPdf, epubFileUpload.getLayout());
					//pdfConversion.setOriginalFilename(epubFileUpload.getFile().getOriginalFilename());
					//pdfConversion.persist();
					return "pdf/epub";
					//return "redirect:/pdf/product/" + pdfConversion.getId();
				} finally {
					FileUtils.deleteQuietly(newPdf);
				}
			} catch (Throwable e) {
				result.reject(e.getMessage());
				return "pdf/epub";
			}
       } else {
           return "pdf/epub";
       }
	}
	
	@RequestMapping(value = "/html", method = RequestMethod.GET)
	public String htmlToPdf(Model model) throws Exception {
		HtmlConversion htmlConversion = new HtmlConversion();
		htmlConversion.setPdfConversion(application.getDefaultPdfConversion());
		htmlConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
		model.addAttribute("htmlConversion", htmlConversion);
		model.addAttribute("cssFiles", loadCssFiles());
		model.addAttribute("pageLayouts", PageLayout.values());
		model.addAttribute("pdfConversions", PdfExecutorType.values());
		return "pdf/html";
	}
	
	private List<String> loadCssFiles() throws IOException {
		File folder = new File(htmlToPdfCssFolder);
		if (!folder.isDirectory()) {
			throw new IOException("Invalid CSS File Folder: " + htmlToPdfCssFolder);
		}
		List<String> folderList = Arrays.asList(folder.list());
		List<String> cssList = new ArrayList<String>();
		for (String fileName : folderList) {
			if (fileName.endsWith(".css")) {
				if (!fileName.contains(A5_CSS_FILE)) {
					cssList.add(fileName);
				}
			}
		}
		return cssList;
	}
	
	private static final String A5_CSS_FILE = "a5.css";
	
	@RequestMapping(value = "/html", method = RequestMethod.POST)
	public String htmlToPdf(@Valid HtmlConversion htmlConversion, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			htmlConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
			model.addAttribute("htmlConversion", htmlConversion);
			model.addAttribute("cssFiles", loadCssFiles());
			model.addAttribute("pageLayouts", PageLayout.values());
			return "pdf/html";
		}
		try {
			List<String> cssFilesList = new ArrayList<String>();
			cssFilesList.add(htmlConversion.getCss());
			File newPdf = null;
			if (htmlConversion.getLayouts() == null || htmlConversion.getLayouts().contains(PageLayout.ONE_UP) || htmlConversion.getLayouts().contains(PageLayout.TWO_UP)) {
				newPdf = pdfAutomationService.htmlToPdf(htmlConversion.getHtml(), cssFilesList, htmlConversion.getPdfConversion());
			}
			File newA5Pdf = null;
			if (htmlConversion.getLayouts() == null || htmlConversion.getLayouts().contains(PageLayout.A5)) {
				cssFilesList.add(A5_CSS_FILE);
				newA5Pdf = pdfAutomationService.htmlToPdf(htmlConversion.getHtml(), cssFilesList, htmlConversion.getPdfConversion());
			}
			
			try {
				PdfConversion pdfConversion = pdfAutomationService.createPaperightPdfs(newPdf, newA5Pdf, htmlConversion.getLayouts(), null);
				pdfConversion.setOriginalFilename(htmlConversion.getHtml());
				pdfConversion.persist();
				String redirect = "redirect:/pdf/product/" + pdfConversion.getId();
				if (htmlConversion.isAmendExistingProduct() && htmlConversion.getExistingProductId() != null) {
					redirect = redirect + "/existing-product/" + htmlConversion.getExistingProductId();
				}
				return redirect;
			} finally {
				FileUtils.deleteQuietly(newA5Pdf);
				FileUtils.deleteQuietly(newPdf);
			}
		} catch (Throwable e) {
			// result.reject(e.getMessage());
			htmlConversion.setPdfConversion(application.getDefaultPdfConversion());
			htmlConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
			model.addAttribute("htmlConversion", htmlConversion);
			model.addAttribute("cssFiles", loadCssFiles());
			model.addAttribute("pageLayouts", PageLayout.values());
			model.addAttribute("pdfConversions", PdfExecutorType.values());
			model.addAttribute("notificationType", "error");
			model.addAttribute("notificationMessage", e.getMessage());
			return "pdf/html";
		}
	}
	
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	public String existingPdfToPdf(Model model) throws Exception {
		ExistingPdfConversion existingPdfConversion = new ExistingPdfConversion();
		existingPdfConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
		model.addAttribute("existingPdfConversion", existingPdfConversion);
		model.addAttribute("pageLayouts", PageLayout.values());
		return "pdf/pdf";
	}
	
	@RequestMapping(value = "/pdf", method = RequestMethod.POST)
	public String existingPdfToPdf(@Valid ExistingPdfConversion existingPdfConversion, BindingResult result, Model model) {
		if (result.hasErrors()) {
			existingPdfConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
			model.addAttribute("existingPdfConversion", existingPdfConversion);
			model.addAttribute("pageLayouts", PageLayout.values());
			return "pdf/pdf";
		}
		try {

			File newPdf = null;
			File newA5Pdf = null;
			if (existingPdfConversion.getLayouts().contains(PageLayout.A5)) {
				newA5Pdf = saveExistingPdf(existingPdfConversion.getExistingPdf());
			} else {
				newPdf = saveExistingPdf(existingPdfConversion.getExistingPdf());
			}
			
			try {
				PdfConversion pdfConversion = pdfAutomationService.createPaperightPdfs(newPdf, newA5Pdf, existingPdfConversion.getLayouts(), null);
				pdfConversion.setOriginalFilename(existingPdfConversion.getExistingPdf().getOriginalFilename());
				pdfConversion.persist();
				String redirect = "redirect:/pdf/product/" + pdfConversion.getId();
				if (existingPdfConversion.isAmendExistingProduct() && existingPdfConversion.getExistingProductId() != null) {
					redirect = redirect + "/existing-product/" + existingPdfConversion.getExistingProductId();
				}
				return redirect;
			} finally {
				FileUtils.deleteQuietly(newA5Pdf);
				FileUtils.deleteQuietly(newPdf);
			}
		} catch (Throwable e) {
			// result.reject(e.getMessage());
			existingPdfConversion.setLayouts(Arrays.asList(new PageLayout[] { PageLayout.ONE_UP, PageLayout.TWO_UP }));
			model.addAttribute("existingPdfConversion", existingPdfConversion);
			model.addAttribute("pageLayouts", PageLayout.values());
			model.addAttribute("notificationType", "error");
			model.addAttribute("notificationMessage", e.getMessage());
			return "pdf/pdf";
		}
	}

	private File saveExistingPdf(MultipartFile existingPdfFile) throws Exception {
		String filename = existingPdfFile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(filename);
		filename = FilenameUtils.getBaseName(filename) + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + "." + extension;
		filename = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), filename);
		logger.debug("saving existing pdf file " + existingPdfFile.getOriginalFilename() + " to " + filename);
		File file = new File(filename);
		FileUtils.writeByteArrayToFile(file, existingPdfFile.getBytes());
		return file;
	}
	
	@InitBinder("toProduct")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("pdfConversion.id", "pdfConversion.originalFilename", "pdfConversion.oneUpFilename", "pdfConversion.twoUpFilename", "product.oneUpFilename", "product.twoUpFilename");
	}
	
	@RequestMapping(value = "/product/{pdfConversionId}", method = RequestMethod.GET)
	public String toProduct(@PathVariable Long pdfConversionId, Model model) throws Exception {
		return internalToProduct(pdfConversionId, null, model);
	}
	
	@RequestMapping(value = "/product/{pdfConversionId}/existing-product/{existingProductId} ", method = RequestMethod.GET)
	public String toProduct(@PathVariable Long pdfConversionId, @PathVariable Long existingProductId, Model model) throws Exception {
		return internalToProduct(pdfConversionId, Product.find(existingProductId), model);
	}
	
	protected String internalToProduct(Long pdfConversionId, Product existingProduct, Model model) throws Exception {
		PdfConversion pdfConversion = PdfConversion.find(pdfConversionId);
		ToProduct toProduct = new ToProduct();
		buildProductDetails(existingProduct, pdfConversion);
		toProduct.setPdfConversion(pdfConversion);
		toProduct.setProduct(existingProduct);
		model.addAttribute("canDeleteLayout", canDeleteLayout(pdfConversion));
		model.addAttribute("toProduct", toProduct);
		model.addAttribute("pdfConversion", pdfConversion);
		return "pdf/product";
	}
	private boolean canDeleteLayout(PdfConversion pdfConversion) {
		int existingLayoutCount = 0;
		if (!StringUtils.isBlank(pdfConversion.getOneUpFilename())) {
			existingLayoutCount = existingLayoutCount + 1;
		}
		if (!StringUtils.isBlank(pdfConversion.getTwoUpFilename())) {
			existingLayoutCount = existingLayoutCount + 1;
		}
		if (!StringUtils.isBlank(pdfConversion.getA5Filename())) {
			existingLayoutCount = existingLayoutCount + 1;
		}
		if (existingLayoutCount > 1) {
			return true;
		} else {
			return false;
		}
	}

	private void buildProductDetails(Product product, PdfConversion pdfConversion) throws Exception {
		try {
			if (!StringUtils.isBlank(pdfConversion.getOneUpFilename())) {
				String filename = FilenameUtils.concat(pdfFileFolder, pdfConversion.getOneUpFilename());
				int pageExtent = PdfUtils.countPages(filename);
				product.setOneUpPageExtent(pageExtent);
			}
			if (!StringUtils.isBlank(pdfConversion.getTwoUpFilename())) {
				String filename = FilenameUtils.concat(pdfFileFolder, pdfConversion.getTwoUpFilename());
				int pageExtent = PdfUtils.countPages(filename);
				product.setTwoUpPageExtent(pageExtent);
			}
			if (!StringUtils.isBlank(pdfConversion.getA5Filename())) {
				String filename = FilenameUtils.concat(pdfFileFolder, pdfConversion.getA5Filename());
				int pageExtent = PdfUtils.countPages(filename);
				product.setA5PageExtent(pageExtent);
			}
		} catch (Exception e) {
			//do nothing;
		}
	}
	
	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public String update(@ModelAttribute ToProduct toProduct, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "pdf/product";
		}
		Product product = toProduct.getProduct();
		if (toProduct.getJacketImage() != null && !toProduct.getJacketImage().isEmpty()) {
			String jacketImageFilename;
			try {
				jacketImageFilename = saveJacketImageFile(toProduct.getJacketImage());
			} catch (Exception e) {
				result.reject(e.getMessage());
				return "pdf/product";
			}
			product.setJacketImageFilename(FilenameUtils.getName(jacketImageFilename));
		}
		PdfConversion pdfConversion = toProduct.getPdfConversion();
		product = product.merge();
		pdfConversion.setProduct(product);
		pdfConversion.merge();
		sessionStatus.setComplete();
		return "redirect:/product/update/" + product.getId();
	}
	
	private String saveJacketImageFile(MultipartFile jacketImageFile) throws Exception {
		String filename = jacketImageFile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(filename);
		filename = FilenameUtils.getBaseName(filename) + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + "." + extension;
		filename = FilenameUtils.concat(jacketImageFolder, filename);
		logger.info("saving uploaded file " + jacketImageFile.getOriginalFilename() + " to " + filename);
		File file = new File(filename);
		FileUtils.writeByteArrayToFile(file, jacketImageFile.getBytes());
		return filename;
	}
	
	@RequestMapping(value = "/product/{pdfConversionId}/cancel", method = RequestMethod.GET)
	public String cancelConversion(@PathVariable Long pdfConversionId, Model model) throws Exception {
		PdfConversion pdfConversion = PdfConversion.find(pdfConversionId);
		if (!StringUtils.isBlank(pdfConversion.getOneUpFilename())) {
			File file = new File(FilenameUtils.concat(pdfFileFolder, pdfConversion.getOneUpFilename()));
			FileUtils.deleteQuietly(file);
		}
		if (!StringUtils.isBlank(pdfConversion.getTwoUpFilename())) {
			File file = new File(FilenameUtils.concat(pdfFileFolder, pdfConversion.getTwoUpFilename()));
			FileUtils.deleteQuietly(file);
		}
		if (!StringUtils.isBlank(pdfConversion.getA5Filename())) {
			File file = new File(FilenameUtils.concat(pdfFileFolder, pdfConversion.getA5Filename()));
			FileUtils.deleteQuietly(file);
		}
		pdfConversion.remove();
		return "redirect:/pdf/html";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/preview")
	public void conversionFilePreview(@RequestParam("filename") String filename, HttpServletResponse response) throws Exception {
	    File file;
	    if (StringUtils.isBlank(FilenameUtils.getPath(filename))) {
	        file = new File(FilenameUtils.concat(pdfFileFolder, filename));
	    } else {
	        file = new File(filename);
	    }
		  
		InputStream inputStream = new FileInputStream(file);
		try {
			response.setContentType("application/pdf");
			response.setContentLength(new Long(file.length()).intValue());
			response.addHeader("content-disposition", "attachment; filename=" + filename + ".pdf");
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} finally {
			inputStream.close();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/product/{pdfConversionId}/delete/{filename}/{layout}/")
	public String deleteConversionFile(@PathVariable Long pdfConversionId, @PathVariable("filename") String filename, @PathVariable("layout") PageLayout layout, HttpServletResponse response) throws Exception {
		PdfConversion pdfConversion = PdfConversion.find(pdfConversionId);
		File file = new File(FilenameUtils.concat(pdfFileFolder, filename));
		if (file.delete()) {
			if (layout == PageLayout.ONE_UP) {
				pdfConversion.setOneUpFilename("");
			} else if (layout == PageLayout.TWO_UP) {
				pdfConversion.setTwoUpFilename("");
			} else {
				pdfConversion.setA5Filename("");
			}
			pdfConversion.merge();
		}
		return "redirect:/pdf/product/" + pdfConversion.getId();
	}
	
	@RequestMapping(value = "/products/search.json", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Object searchProducts(@RequestBody String searchString, Model model) {
		List<Product> products = searchProducts(searchString);
		Map<String, Object> response = new HashMap<>();
		response.put("data",  products);
		response.put("success", true);
		return response;
	}
	
	private List<Product> searchProducts(String searchString) {
		List<Product> products = new ArrayList<Product>();
		if (!StringUtils.isBlank(searchString)) {
			SearchResult<Product> searchResult = new SearchResult<Product>();
			try {
				searchResult = Product.search(searchString, 1, 999999);
				products = searchResult.getItems();
				sortProducts(products);
			} catch (Exception e) {
			}
		}
		return products;
	}
	
	private void sortProducts(List<Product> products) {
		Collections.sort(products, new Comparator<Product>() {

			@Override
			public int compare(Product product, Product otherProduct) {
				return StringComparators.compareNaturalIgnoreCaseAscii(StringUtils.defaultIfBlank(product.getTitle(), ""), StringUtils.defaultIfBlank(otherProduct.getTitle(), ""));
			}
			
		});
	}
}

class EpubFileUpload {
	
	private MultipartFile file;
	private PageLayout layout;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	private static final String[] VALID_EXTENSIONS = { "epub" };
	
	@AssertTrue(message = "Only 'epub' files allowed")
	public boolean isExtensionValid() {
		String filename = getFile().getOriginalFilename();
		String extension = FilenameUtils.getExtension(filename);
		boolean result = false;
		for (String validExtension : VALID_EXTENSIONS) {
			if (StringUtils.equalsIgnoreCase(extension, validExtension)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public PageLayout getLayout() {
		return layout;
	}

	public void setLayout(PageLayout layout) {
		this.layout = layout;
	}
	
}

class HtmlConversion {
	
	private String html;
	private String css;
	private List<PageLayout> layouts;
	private boolean amendExistingProduct;
	private Long existingProductId;
	private PdfExecutorType pdfConversion;
	
	public List<PageLayout> getLayouts() {
		return layouts;
	}

	public void setLayouts(List<PageLayout> layouts) {
		this.layouts = layouts;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public boolean isAmendExistingProduct() {
		return amendExistingProduct;
	}

	public void setAmendExistingProduct(boolean amendExistingProduct) {
		this.amendExistingProduct = amendExistingProduct;
	}

	public Long getExistingProductId() {
		return existingProductId;
	}

	public void setExistingProductId(Long existingProductId) {
		this.existingProductId = existingProductId;
	}

	public PdfExecutorType getPdfConversion() {
		return pdfConversion;
	}

	public void setPdfConversion(PdfExecutorType pdfConversion) {
		this.pdfConversion = pdfConversion;
	}

}

class ToProduct {
	
	private PdfConversion pdfConversion;
	private Product product;
	private MultipartFile jacketImage;
	
	public PdfConversion getPdfConversion() {
		return pdfConversion;
	}
	
	public void setPdfConversion(PdfConversion pdfConversion) {
		this.pdfConversion = pdfConversion;
		setProductFilenames();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		setProductFilenames();
	}
	
	private void setProductFilenames() {
		if (getProduct() != null && getPdfConversion() != null) {
			if (!StringUtils.isBlank(getPdfConversion().getOneUpFilename())) {
				getProduct().setOneUpFilename(getPdfConversion().getOneUpFilename());
			}
			if (!StringUtils.isBlank(getPdfConversion().getTwoUpFilename())) {
				getProduct().setTwoUpFilename(getPdfConversion().getTwoUpFilename());
			}
			if (!StringUtils.isBlank(getPdfConversion().getA5Filename())) {
				getProduct().setA5Filename(getPdfConversion().getA5Filename());
			}
		}
	}

	public MultipartFile getJacketImage() {
		return jacketImage;
	}

	public void setJacketImage(MultipartFile jacketImage) {
		this.jacketImage = jacketImage;
	}
	
	private static final String[] VALID_JACKET_IMAGE_EXTENSIONS = { "jpg", "png" };
	
	@AssertTrue(message = "Only 'jpg' and '.png' files allowed")
	public boolean isJacketImageExtensionValid() {
		if (getJacketImage() != null) {
			String filename = getJacketImage().getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);
			boolean result = false;
			for (String validExtension : VALID_JACKET_IMAGE_EXTENSIONS) {
				if (StringUtils.equalsIgnoreCase(extension, validExtension)) {
					result = true;
					break;
				}
			}
			return result;
		}
		return true;
	}
}

class ExistingPdfConversion {

	@ContentType(value = { "application/pdf", "application/x-pdf" }, message = "Only '.pdf' files allowed")
	private MultipartFile existingPdf;
	private List<PageLayout> layouts;
	private boolean amendExistingProduct;
	private Long existingProductId;
	
	public List<PageLayout> getLayouts() {
		return layouts;
	}

	public void setLayouts(List<PageLayout> layouts) {
		this.layouts = layouts;
	}

	public boolean isAmendExistingProduct() {
		return amendExistingProduct;
	}

	public void setAmendExistingProduct(boolean amendExistingProduct) {
		this.amendExistingProduct = amendExistingProduct;
	}

	public Long getExistingProductId() {
		return existingProductId;
	}

	public void setExistingProductId(Long existingProductId) {
		this.existingProductId = existingProductId;
	}

	public MultipartFile getExistingPdf() {
		return existingPdf;
	}

	public void setExistingPdf(MultipartFile existingPdf) {
		this.existingPdf = existingPdf;
	}
	
//	private static final String[] VALID_EXTENSION = { "pdf" };
//	
//	@AssertTrue(message = "Only '.pdf' files allowed")
//	public boolean isPdfExtensionValid() {
//		if (getExistingPdf() != null) {
//			String filename = getExistingPdf().getOriginalFilename();
//			String extension = FilenameUtils.getExtension(filename);
//			boolean result = false;
//			for (String validExtension : VALID_EXTENSION) {
//				if (StringUtils.equalsIgnoreCase(extension, validExtension)) {
//					result = true;
//					break;
//				}
//			}
//			return result;
//		}
//		return true;
//	}

}
