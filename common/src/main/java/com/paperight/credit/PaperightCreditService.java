package com.paperight.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.Application;
import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.user.Company;

@Service
public class PaperightCreditService {
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private Application application;
	
	@Transactional
	public void authoriseTransaction(PaperightCreditTransaction transaction) {
		transaction.setStatus(PaperightCreditTransactionStatus.PAID);
		transaction.merge();
		Company company = Company.find(transaction.getCompany().getId());
		addPaperightCreditsToCompany(company, transaction.getNumberOfCredits());
	}
	
	@Transactional
	public void cancelTransaction(PaperightCreditTransaction transaction) {
		transaction.setStatus(PaperightCreditTransactionStatus.CANCELLED);
		transaction.merge();
	}
	
	@Transactional
	public void addPaperightCreditsToCompany(Company company, BigDecimal numberOfCredits) {
		company.setCredits(company.getCredits().add(numberOfCredits));
		company.merge();
	}
	
	@Transactional 
	public void spendPaperightCredits(Company company, BigDecimal numberOfCredits) throws InsufficientCreditsException {
		company = Company.find(company.getId());
		if (company.getCredits().compareTo(numberOfCredits) == -1) {
			throw new InsufficientCreditsException();
		}
		company.setCredits(company.getCredits().subtract(numberOfCredits));
		company.merge();
	}
	
	@Transactional
	public void reimbersePaperightCredits(Company company, BigDecimal numberOfCredits) {
		company = Company.find(company.getId());
		addPaperightCreditsToCompany(company, numberOfCredits);
	}
	
	public BigDecimal paperightCreditsValueInCurrency(BigDecimal credits, String currencyCode) {
		BigDecimal amountInBaseCurrency = paperightCreditsInBaseCurrency(credits);
		Currency currency = currencyService.findByCode(currencyCode);
		BigDecimal currencyRate = BigDecimal.valueOf(1);
		if (currency != null) {
			currencyRate = currency.getRate();
		}
		return amountInBaseCurrency.multiply(currencyRate).setScale(2, RoundingMode.UP);
	}
	
	public BigDecimal paperightCreditsInBaseCurrency(BigDecimal credits) {
		return credits.divide(getPaperightCreditToBaseCurrencyRate()).setScale(2, RoundingMode.UP);
	}

	public BigDecimal getPaperightCreditToBaseCurrencyRate() {
		return application.getPaperightCreditToBaseCurrencyRate();
	}
	
	public BigDecimal paperightCreditsFromBaseCurrency(BigDecimal amount) {
		return amount.multiply(getPaperightCreditToBaseCurrencyRate()).setScale(2, RoundingMode.UP);
	}
	
}
