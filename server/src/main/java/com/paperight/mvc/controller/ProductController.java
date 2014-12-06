package com.paperight.mvc.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.paperight.FileUpload;
import com.paperight.ProductImportService;
import com.paperight.product.ImportExecution;
import com.paperight.product.ImportStatus;
import com.paperight.product.Product;
import com.paperight.search.SearchIndexUtils;

@Controller
@SessionAttributes({"product"})
public class ProductController {
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Value("${product.file.upload.folder}")
	private String productFileUploadFolder;
	
	@Autowired
	private ProductImportService productImportService;
	
	@RequestMapping(value = "/product/import", method = RequestMethod.GET)
	public String productImport() {
		return "product/import";
	}

	@RequestMapping(value = "/product/import", method = RequestMethod.POST)
	public String productImport(FileUpload fileUpload, BindingResult result) {
		if (!fileUpload.getFile().isEmpty()) {
			try {
				String filename = saveFile(fileUpload.getFile());
				productImportService.importProductsFromFile(filename);
			} catch (Throwable e) {
				result.reject(e.getMessage());
				return "product/import";
			}
           return "product/import/complete";
       } else {
           return "product/import";
       }
	}
	
	@RequestMapping(value="/product/import/{importExecutionId}/errors", method = {RequestMethod.GET} )
	public String importExecutionErrors(@PathVariable Long importExecutionId, Model model) {
		ImportExecution importExecution = ImportExecution.find(importExecutionId);
		if (importExecution == null) {
			return "redirect:/product/import/search";
		}
		model.addAttribute("importExecution", importExecution);
		return "product/import/errors";
	}
	
	@RequestMapping(value="/product/import/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String search(@ModelAttribute ImportExecutionSearch importSearch, Model model) {
		List<ImportExecution> importExecutions = searchImports(importSearch);
		model.addAttribute("importSearch", importSearch);
		model.addAttribute("importExecutions", importExecutions);
		return "product/import/search";
	}
	
	private List<ImportExecution> searchImports(ImportExecutionSearch importSearch) {
		return ImportExecution.findByStatus(importSearch.getStatus());
	}

	private String saveFile(MultipartFile uploadFile) throws Exception {
		String filename = uploadFile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(filename);
		filename = FilenameUtils.getBaseName(filename) + "_" + DateTime.now().toString("yyyyMMdd_HHmmss") + "." + extension + ".import";
		filename = FilenameUtils.concat(productFileUploadFolder, filename);
		logger.info("saving uploaded file " + uploadFile.getOriginalFilename() + " to " + filename);
		File file = new File(filename);
		FileUtils.writeByteArrayToFile(file, uploadFile.getBytes());
		return filename;
	}
	
	@InitBinder("product")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	
	@RequestMapping(value = "/product/update/{productId}", method = RequestMethod.GET)
	public String update(@PathVariable Long productId, Model model) {
		Product product;
		if (productId == null) {
			product = new Product();
		} else {
			product = Product.find(productId);
		}
		model.addAttribute("product", product);
		return "product/update";
	}
	
	@RequestMapping(value = "/product/create", method = RequestMethod.GET)
	public String create(Model model) {
		return update(null, model);
	}
	
	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public String update(@ModelAttribute Product product, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "product/update";
		}
		product = product.merge();
		sessionStatus.setComplete();
		return "redirect:/product/update/" + product.getId();
	}
	
	@RequestMapping(value = "/product/delete/{productId}", method = RequestMethod.GET)
	public String delete(@PathVariable Long productId) {
		Product product = Product.find(productId);
		if (product != null) {
			product.remove();
		}
		return "redirect:/product/search";
	}
	
	@RequestMapping(value="/product/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String search(@ModelAttribute ProductSearch productSearch, @RequestParam(value = "s", defaultValue = "1") String start, Model model) throws Exception {
		int pageSize = 10;
		int pageNumber = 1;
		try {
			pageNumber = Integer.parseInt(start);
		} catch (Exception e) {
			
		}
		List<Product> products = searchProducts(productSearch, pageNumber, pageSize);
		if (products == null) {
			products = Product.findEntries((pageNumber - 1) * pageSize, pageSize);			
		}
		
		model.addAttribute("productSearch", productSearch);
		model.addAttribute("products", products);
		return "product/search";
	}
	
	private List<Product> searchProducts(ProductSearch productSearch, int pageNumber, int pageSize) {
		return Product.search(productSearch.getIdentifier(), productSearch.getTitle(), pageNumber, pageSize);
	}
	
	@RequestMapping(value = "/product/search/rebuild-index", method = RequestMethod.GET)
	public String rebuildSearchIndex() throws InterruptedException {
		SearchIndexUtils.rebuildSearchIndex();
		return "redirect:/product/search";
	}
	
	@RequestMapping(value="/") 
	public String home() {
		return "redirect:/product/search";
	}

}

class ProductSearch {
	
	private String identifier;
	private String title;
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}

class ImportExecutionSearch {
	
	private ImportStatus status;

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}

}
