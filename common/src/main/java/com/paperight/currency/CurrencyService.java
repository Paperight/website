package com.paperight.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.paperight.Application;
import com.paperight.user.Company;

@Service
public class CurrencyService implements Serializable {

	private static final long serialVersionUID = -5659192203747428570L;

	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	transient private Application application;

	@Cacheable("currencies")
	public Map<String, Currency> getCurrencies() {
		try {
			return currencyDao.loadCurrencies();
		} catch (Exception e) {
		}
		return null;
	}

	public Currency findByCode(String code) {
		return getCurrencies().get(code);
	}

	public static String defaultCurrencySymbol(String currencyCode) {
		return DefaultCurrencySymbolDao.getDefaultSymbol(currencyCode);
	}

	public BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
		BigDecimal conversionRate = getBaseCurrency().getRate().divide(fromCurrency.getRate(), 6, RoundingMode.UP);
		BigDecimal exchangeRate = toCurrency.getRate().multiply(conversionRate);
		return exchangeRate.multiply(amount);
	}

	@Cacheable(value = "defaultCurrencies", key = "'baseCurrency'")
	public Currency getBaseCurrency() {
		return findByCode(application.getPaperightCreditBaseCurrencyCode());
	}

	@Cacheable(value = "defaultCurrencies", key = "'defaultCurrency'")
	public Currency getDefaultCurrency() {
		return findByCode(application.getDefaultCurrencyCode());
	}
	
	private static final String INVOICE_CURRENCY_CODE = "ZAR";
	
	@Cacheable(value = "defaultCurrencies", key = "'invoiceCurrency'")
	public Currency getInvoiceCurrency() {
		return findByCode(INVOICE_CURRENCY_CODE);
	}
	
	public BigDecimal backCalculateVat(BigDecimal grossAmount, BigDecimal vatRatePercentage) {
		BigDecimal vatRateDecimal = vatRatePercentage.divide(BigDecimal.valueOf(100));  //eg VAT decimal for 14%: 14/100 = 0.14
		BigDecimal varRateFraction = vatRateDecimal.divide(BigDecimal.valueOf(1).add(vatRateDecimal), 10, RoundingMode.HALF_UP);   //eg VAT fraction for 14%: 0.14/1.14
		return grossAmount.multiply(varRateFraction).setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal percentageValue(BigDecimal amount, Integer percent) {
		double percentDecimal = Double.valueOf(percent) / 100;
		return amount.multiply(BigDecimal.valueOf(percentDecimal));
	}
	
	public boolean isLiableForVat(Company company, DateTime transactionDate) {
		boolean result = false;
		if (company != null) {
			result = (company.isVatRegistered() && transactionDate.isAfter(company.getVatDateOfLiability()));
		}
		return result;
	}

}