package com.paperight.credit;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.paperight.user.Company;
import com.paperight.user.User;

public class PaperightCreditTransaction implements Serializable {

	private static final long serialVersionUID = -2804242674067933027L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private String transactionReference;
	private PaperightCreditTransactionStatus status = PaperightCreditTransactionStatus.PENDING;
	private BigDecimal numberOfCredits;
	private BigDecimal amount;
	private String currency;
	private Company company;
	private User user;
	private PaymentMethod paymentMethod;
	private String paymentReference;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public DateTime getCreatedDate() {
		return createdDate;
	}
	
	@SuppressWarnings("unused")
	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	@SuppressWarnings("unused")
	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public PaperightCreditTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(PaperightCreditTransactionStatus status) {
		this.status = status;
	}

	public BigDecimal getNumberOfCredits() {
		return numberOfCredits;
	}

	public void setNumberOfCredits(BigDecimal numberOfCredits) {
		this.numberOfCredits = numberOfCredits;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}


}
