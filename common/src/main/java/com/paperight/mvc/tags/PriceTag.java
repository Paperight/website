package com.paperight.mvc.tags;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.authentication.AuthenticationService;
import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.user.User;

@Configurable
public class PriceTag extends SimpleTagSupport {

	private BigDecimal amount;
	private Object currency;
	private Object displayCurrency;
	private boolean showSymbol = true;
	private boolean showDecimal = false;
	
	@Autowired
	private CurrencyService currencyService;

	public void doTag() throws JspException, IOException {

		Currency oCurrency = getCurrency(currency);
		Currency oDisplayCurrency = getDisplayCurrency(displayCurrency);

		if (oCurrency == null || oDisplayCurrency == null) {
			throw new JspException();
		}

		String price = "";
		if (!StringUtils.equalsIgnoreCase(oDisplayCurrency.getCode(), oCurrency.getCode())) {
			price = buildPriceTag(oDisplayCurrency, currencyService.convert(amount, oCurrency, oDisplayCurrency).setScale(2, RoundingMode.UP));
		} else {
			price = buildPriceTag(oCurrency, amount.setScale(2, RoundingMode.UP));
		}

		// write out price
		try {
			getJspContext().getOut().write(price);
		} catch (IOException e) {
			throw new JspException();
		}

		return;
	}

	private Currency getDisplayCurrency(Object oCurrency) {
		if (oCurrency == null || oCurrency instanceof String) {
			String currency = (String) oCurrency;
			if (StringUtils.isEmpty(currency)) {
				return getDefaultDisplayCurrency();
			} else {
				return currencyService.findByCode(currency);
			}
		}
		return (Currency) oCurrency;
	}

	private Currency getCurrency(Object oCurrency) {
		if (oCurrency == null || oCurrency instanceof String) {
			String currency = (java.lang.String) oCurrency;
			if (StringUtils.isEmpty(currency)) {
				return currencyService.getBaseCurrency();
			} else {
				return currencyService.findByCode(currency);
			}
		}
		return (Currency) oCurrency;
	}

	private Currency getDefaultDisplayCurrency() {
		User user = AuthenticationService.currentActingUser();
		if (user != null) {
			return user.getCompany().getCurrency();
		} else {
			return currencyService.getDefaultCurrency();
		}
	}

	public String buildPriceTag(Currency currency, BigDecimal amount) {
		BigDecimal rounded = amount.setScale(0, RoundingMode.HALF_UP);
		boolean hasDecimal = (amount.compareTo(rounded) == 0) ? false : true;
		String out = ((!showSymbol) ? "" : currency.getSymbol());
		out += new DecimalFormat((showDecimal || hasDecimal) ? "#,##0.00" : "#,##0").format(amount);
		return out;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Object getCurrency() {
		return currency;
	}

	public void setCurrency(Object currency) {
		this.currency = currency;
	}

	public Object getDisplayCurrency() {
		return displayCurrency;
	}

	public void setDisplayCurrency(Object displayCurrency) {
		this.displayCurrency = displayCurrency;
	}

	public boolean isShowSymbol() {
		return showSymbol;
	}

	public void setShowSymbol(boolean showSymbol) {
		this.showSymbol = showSymbol;
	}

	public boolean isShowDecimal() {
		return showDecimal;
	}

	public void setShowDecimal(boolean showDecimal) {
		this.showDecimal = showDecimal;
	}
}