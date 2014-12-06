package com.paperight.mvc.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paperight.credit.PaperightCreditService;
import com.paperight.credit.PaperightCreditTransaction;
import com.paperight.credit.PaperightCreditTransactionStatus;
import com.paperight.email.integration.EmailGateway;

@Controller
@PreAuthorize("hasPermission(#user, 'AUTHORISE_TOPUPS')")
public class PaperightCreditTransactionController {
	
	private Logger logger = LoggerFactory.getLogger(PaperightCreditTransactionController.class);
	
	@Autowired
	private PaperightCreditService paperightCreditService;
	
	@Autowired
	private EmailGateway emailGateway;
	
	@RequestMapping(value="/transaction/credit/search", method = {RequestMethod.GET, RequestMethod.POST} )
	public String search(@ModelAttribute PaperightCreditTransactionSearch transactionSearch, Model model) {
		List<PaperightCreditTransaction> transactions = searchTransactions(transactionSearch);
		model.addAttribute("transactionSearch", transactionSearch);
		model.addAttribute("transactions", transactions);
		return "transaction/credit/search";
	}

	private List<PaperightCreditTransaction> searchTransactions(PaperightCreditTransactionSearch transactionSearch) {
		if (transactionSearch.getId() == null) {
			return PaperightCreditTransaction.findByStatus(transactionSearch.getStatus());
		} else {
			return PaperightCreditTransaction.search(transactionSearch.getLongId(), transactionSearch.getStatus());
		}
	}
	
	@RequestMapping(value="/transaction/credit/{id}/{action}")
	public String authorise(@PathVariable Long id, @PathVariable String action) {
		PaperightCreditTransaction transaction = PaperightCreditTransaction.find(id);
		if (StringUtils.equalsIgnoreCase(action, "authorise")) {
			paperightCreditService.authoriseTransaction(transaction);
			sendReloadUser(transaction);
			emailGateway.paperightCreditTransactionPaymentReceived(transaction);
		} else if (StringUtils.equalsIgnoreCase(action, "cancel")) {
			paperightCreditService.cancelTransaction(transaction);
		}
		return "redirect:/transaction/credit/search";
	}
	
	@Value("${base.url}/user/reload/")
	private String reloadUserUrl;
	
	private void sendReloadUser(PaperightCreditTransaction transaction) {
		Long userId = transaction.getUser().getId();
		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
			HttpGet httpget = new HttpGet(reloadUserUrl + userId);
			httpclient.execute(httpget);
		} catch (Exception e) {
			logger.error("unable to send reloadUser request for userId " + userId, e);
		}
	}

}

class PaperightCreditTransactionSearch {
	
	private String id;
	private PaperightCreditTransactionStatus status = PaperightCreditTransactionStatus.PENDING;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Long getLongId() {
		try {
			return Long.parseLong(getId());
		} catch (Exception e) {
			return null;
		}
	}

	public PaperightCreditTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(PaperightCreditTransactionStatus status) {
		this.status = status;
	} 
	
}
