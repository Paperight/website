package com.paperight.mvc.controller;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paperight.authentication.AuthenticationService;
import com.paperight.credit.PaperightCreditService;
import com.paperight.credit.PaperightCreditTransaction;
import com.paperight.credit.PaperightCreditTransactionStatus;
import com.paperight.credit.PaymentMethod;
import com.paperight.email.integration.EmailGateway;
import com.paperight.paypal.PaypalResponse;
import com.paperight.paypal.PaypalService;
import com.paperight.user.Company;
import com.paperight.user.User;

@Controller
@PreAuthorize("hasRole('ROLE_OUTLET')")
public class TopUpController {

	@Autowired
	private PaperightCreditService paperightCreditService;
	
	@Autowired
	private PaypalService paypalService;
	
	@Autowired
	private EmailGateway emailGateway;
	
	@Value("${base.url}")
	private String applicationUrl;

	@RequestMapping(value = "/account/topup", method = RequestMethod.GET)
	public String topup(Model model) {
		model.addAttribute("topUp", new TopUp());
		return "account/topup";
	}

	@RequestMapping(value = "/account/topup", method = RequestMethod.POST)
	public String topup(Model model, @Valid TopUp topUp, BindingResult result) {

		if(result.hasErrors()){
			model.addAttribute("topUp", topUp);
			return "account/topup";
		}
		
		User user = AuthenticationService.currentActingUser();
		Company company = user.getCompany();

		PaperightCreditTransaction transaction = new PaperightCreditTransaction();
		transaction.setCompany(user.getCompany());
		transaction.setUser(user);
		transaction.setNumberOfCredits(topUp.getAmount());
		transaction.setAmount(paperightCreditService.paperightCreditsValueInCurrency(topUp.getAmount(), company.getCurrencyCode()));
		transaction.setStatus(PaperightCreditTransactionStatus.PENDING);
		transaction.setPaymentMethod(topUp.getPaymentMethod());
		transaction.setCurrency(company.getCurrencyCode());
		if (transaction.getPaymentMethod() == PaymentMethod.PAYPAL) {
			PaypalResponse response = paypalService.initiatePayment(transaction, applicationUrl + "/account/topup/paypal/complete?return=" + topUp.getReferer(), applicationUrl + "/account/topup/paypal/cancel?return=" + topUp.getReferer());
			if (response.isSuccessful()) {
				transaction.setTransactionReference(response.getToken());
				transaction.persist();
				return "redirect:" + response.getRedirectUrl();
			}
		} else {
			transaction.persist();
		}
		
		return "redirect:/account/payment/" + transaction.getId() + "?return=" + topUp.getReferer();
	}
	
	@RequestMapping(value = "/account/topup/paypal/complete", method = RequestMethod.GET)
	public String payPalComplete(Model model, @RequestParam("token") String token, @RequestParam("PayerID") String payerId, @RequestParam(value = "return", defaultValue = "/dashboard") String returnUrl) {
		PaperightCreditTransaction transaction = PaperightCreditTransaction.findByTransactionReference(token);
		if (transaction != null) {
			PaypalResponse response = paypalService.completePayment(transaction, token, payerId);
			if (response.isSuccessful()) {
				transaction.setPaymentReference(response.getTransactionId());
				paperightCreditService.authoriseTransaction(transaction);
				emailGateway.paperightCreditTransactionPaymentReceived(transaction);
				AuthenticationService.refreshActingUser();
			}
		}
		model.addAttribute("transaction", transaction);
		model.addAttribute("returnUrl", returnUrl);
		return "account/payment/paypal";
	}
	
	@RequestMapping(value = "/account/topup/paypal/cancel", method = RequestMethod.GET)
	public String payPalCancel(Model model, @RequestParam("token") String token, @RequestParam(value = "return", defaultValue = "/dashboard") String returnUrl) {
		PaperightCreditTransaction transaction = PaperightCreditTransaction.findByTransactionReference(token);
		if (transaction != null) {
			paperightCreditService.cancelTransaction(transaction);
		}
		model.addAttribute("returnUrl", returnUrl);
		return "account/payment/paypal/cancel";
		
	}
	
	@RequestMapping(value = "/account/payment/{transactionId}", method = RequestMethod.GET)
	public String confirmation(Model model, @PathVariable Long transactionId, @RequestParam(value = "return", defaultValue = "/dashboard") String returnUrl ) {
		User user = AuthenticationService.currentActingUser();
		PaperightCreditTransaction transaction = PaperightCreditTransaction.find(transactionId);
		if (transaction == null || !transaction.getUser().getId().equals(user.getId())) {
			return null;
		}
		model.addAttribute("transaction", transaction);
		model.addAttribute("returnUrl", returnUrl);
		return "account/payment";
	}

}

class TopUp {

	@NotNull
	@Digits(integer = 10, fraction = 2)
	@Min(value = 2)
	private BigDecimal amount;
	private PaymentMethod paymentMethod;
	private String referer;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

}
