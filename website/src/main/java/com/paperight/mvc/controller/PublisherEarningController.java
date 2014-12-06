package com.paperight.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paperight.authentication.AuthenticationService;
import com.paperight.licence.LicenceInvoiceService;
import com.paperight.mvc.util.Paginator;
import com.paperight.publisherearning.OverallPublisherEarningSummary;
import com.paperight.publisherearning.PublisherEarning;
import com.paperight.publisherearning.PublisherEarning.InvoiceState;
import com.paperight.publisherearning.PublisherEarningSearch;
import com.paperight.publisherearning.PublisherEarningService;
import com.paperight.publisherearning.PublisherEarningStatementService;
import com.paperight.publisherearning.PublisherEarningStatus;
import com.paperight.publisherearning.PublisherEarningSummary;
import com.paperight.publisherearning.PublisherPaymentRequest;
import com.paperight.publisherearning.PublisherPaymentRequestStatus;
import com.paperight.user.PublisherPaymentDetails;
import com.paperight.user.User;

@Controller
@PreAuthorize("isAuthenticated() and hasRole('ROLE_PUBLISHER')")
@SessionAttributes({"publisherPaymentDetails", "publisherEarningSearchFlash"})
public class PublisherEarningController {
	
	@Autowired
	private PublisherEarningService publisherEarningService;
	
	@Autowired
	private PublisherEarningStatementService publisherEarningStatementService; 
	
	@Autowired
	private LicenceInvoiceService licenceInvoiceService;

	@RequestMapping(value = "/account/publisher-earnings", method = RequestMethod.GET)
	public String publisherEarnings(@RequestParam(value = Paginator.GET_VAR_PAGE_NUMBER_NAME, defaultValue = "1") String page, Model model, HttpServletRequest request) {
		User user = AuthenticationService.currentActingUser();
		List<PublisherEarning> publisherEarnings = PublisherEarning.findByCompanyId(user.getCompany().getId());
		OverallPublisherEarningSummary overallPublisherEarningSummary = new OverallPublisherEarningSummary(publisherEarnings);
		List<PublisherEarningSummary> publisherEarningSummaries = PublisherEarningSummary.findByCompanyIdGroupedByProduct(user.getCompany().getId());
		model.addAttribute("overallPublisherEarningSummary", overallPublisherEarningSummary);
		model.addAttribute("publisherEarningSummaries", publisherEarningSummaries);
		model.addAttribute("publisherEarningSearch", new PublisherEarningSearch());
		return "account/publisher-earnings";
	}
	
	@RequestMapping(value = "/publisher-earnings/search", method = RequestMethod.GET)
	public String searchPublisherEarnings(@ModelAttribute PublisherEarningSearch publisherEarningSearch, Model model) {
		User user = AuthenticationService.currentActingUser();
		List<PublisherEarning> publisherEarnings;
		if (publisherEarningSearch.getStatus() != null) {
			publisherEarnings = PublisherEarning.findByCompanyIdAndStatus(user.getCompany().getId(), publisherEarningSearch.getStatus(), publisherEarningSearch.getFromDate(), publisherEarningSearch.getToDate());
		} else {
			publisherEarnings = PublisherEarning.findByCompanyId(user.getCompany().getId(), publisherEarningSearch.getFromDate(), publisherEarningSearch.getToDate());
		}
		model.addAttribute("publisherEarnings", publisherEarnings);
		model.addAttribute("hasPendingEarnings", publisherEarningService.hasPendingEarnings(publisherEarnings));
		model.addAttribute("publisherEarningSearch", publisherEarningSearch);
		return "publisher-earnings/search";
	}
	
	@RequestMapping(value = "/publisher-earnings/request-payment", method = RequestMethod.POST)
	public String requestPayment(@ModelAttribute PublisherEarningSearch publisherEarningSearch, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		User user = AuthenticationService.currentActingUser();
		PublisherPaymentDetails publisherPaymentDetails = PublisherPaymentDetails.findByCompanyId(user.getCompany().getId());
		if (publisherPaymentDetails == null) {
			redirectAttributes.addFlashAttribute("publisherEarningSearchFlash", publisherEarningSearch);
			return "redirect:/publisher-earnings/payment-details";
		}
		requestPayment(publisherEarningSearch, user);
		return "redirect:/publisher-earnings/payment-requests";
	}
	
	private void requestPayment(PublisherEarningSearch publisherEarningSearch, User user) {
		List<PublisherEarning> publisherEarnings = PublisherEarning.findByCompanyIdAndStatus(user.getCompany().getId(), PublisherEarningStatus.PENDING, publisherEarningSearch.getFromDate(), publisherEarningSearch.getToDate());
		if (!publisherEarnings.isEmpty()) {
			publisherEarningService.requestPayment(publisherEarnings, user);
		}
	}
	
	@RequestMapping(value = "/publisher-earnings/request-payment/cancel/{id}", method = RequestMethod.POST)
	public @ResponseBody Object suggestBook(@PathVariable(value = "id") Long publisherPaymentRequestId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = AuthenticationService.currentActingUser();
			PublisherPaymentRequest publisherPaymentRequest = PublisherPaymentRequest.findByCompanyId(publisherPaymentRequestId, user.getCompany().getId());
			if (publisherPaymentRequest != null) {
				publisherEarningService.requestPaymentRequestCancellation(publisherPaymentRequest);
			}
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
		}
		return map;
	}
	
	@RequestMapping(value = "/publisher-earnings/payment-requests", method = RequestMethod.GET)
	public String paymentRequests(@ModelAttribute PublisherPaymentRequestSearch publisherPaymentRequestSearch, Model model) {
		User user = AuthenticationService.currentActingUser();
		model.addAttribute("publisherPaymentRequests", PublisherPaymentRequest.findByCompanyIdAndStatus(user.getCompany().getId(), publisherPaymentRequestSearch.getStatus()));
		return "publisher-payment-requests/search";
	}

	
	@InitBinder("publisherPaymentDetails")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
		binder.setDisallowedFields("company.id");
	}
	
	@RequestMapping(value = "/publisher-earnings/payment-details", method = RequestMethod.GET)
	public String paymentRequestDetails(Model model) {
		User user = AuthenticationService.currentActingUser();
		PublisherPaymentDetails publisherPaymentDetails = PublisherPaymentDetails.findByCompanyId(user.getCompany().getId());
		if (publisherPaymentDetails == null) {
			publisherPaymentDetails = new PublisherPaymentDetails();
			publisherPaymentDetails.setCompany(user.getCompany());
		}
		PublisherEarningSearch publisherEarningSearch = (PublisherEarningSearch) model.asMap().get("publisherEarningSearchFlash");
		if (publisherEarningSearch != null) {
			model.addAttribute("publisherEarningSearchFlash", publisherEarningSearch);
		}
		model.addAttribute("publisherPaymentDetails", publisherPaymentDetails);
		return "publisher-payment-details";
	}
	
	@RequestMapping(value = "/publisher-earnings/payment-details", method = RequestMethod.POST)
	public String paymentRequestDetails(@ModelAttribute PublisherPaymentDetails publisherPaymentDetails, Model model, BindingResult result, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("publisherPaymentDetails", publisherPaymentDetails);
			return "publisher-payment-details";
		}
		publisherPaymentDetails.merge();
		sessionStatus.setComplete();
		User user = AuthenticationService.currentActingUser();
		PublisherEarningSearch publisherEarningSearch = (PublisherEarningSearch) model.asMap().get("publisherEarningSearchFlash");
		if (publisherEarningSearch != null) {
			requestPayment(publisherEarningSearch, user);
			return "redirect:/publisher-earnings/payment-requests";
		}
		return "redirect:/account";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/publisher-earnings/statement")
	public void statement(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime toDate, HttpServletResponse response) throws Exception {
		User user = AuthenticationService.currentActingUser();
		response.setContentType("application/pdf");
		String filename = "statement_" + fromDate.toString("yyyy-MM-dd") + "_to_" + toDate.toString("yyyy-MM-dd") + ".pdf";
		response.addHeader("content-disposition", "attachment; filename=" + filename);
		publisherEarningStatementService.generatePublisherStatement(user.getCompany().getId(), fromDate, toDate, response.getOutputStream());
		response.flushBuffer();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/publisher-earnings/{id}/invoice")
	public  @ResponseBody Object licenceInvoice(@PathVariable("id") long id, HttpServletResponse response) throws Exception {
	    Map<String, Object> result = new HashMap<>();
		User user = AuthenticationService.currentActingUser();
		PublisherEarning publisherEarning = PublisherEarning.findByIdAndCompanyId(id, user.getCompany().getId());
		if (publisherEarning != null) {
			response.setContentType("application/pdf");
			response.addHeader("content-disposition", "attachment; filename=invoice_" + publisherEarning.getLicence().getId() + ".pdf");
			licenceInvoiceService.generateOutletInvoice(publisherEarning.getLicence(), response.getOutputStream());
			response.flushBuffer();
			publisherEarning.setInvoiceState(InvoiceState.DOWNLOADED);
			publisherEarning.merge();
			result.put("result", true);
			result.put("message", "");
		}
		return result;
	}
	
		
}

class PublisherPaymentRequestSearch {
	
	private PublisherPaymentRequestStatus status = PublisherPaymentRequestStatus.PENDING;

	public PublisherPaymentRequestStatus getStatus() {
		return status;
	}

	public void setStatus(PublisherPaymentRequestStatus status) {
		this.status = status;
	}
	
}
