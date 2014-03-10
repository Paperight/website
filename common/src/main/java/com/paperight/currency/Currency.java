package com.paperight.currency;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class Currency implements Serializable, Comparable<Currency> {

	private static final long serialVersionUID = -6527186061596000880L;
	private String code;
	private String name;
	private BigDecimal rate;
	private String symbol = null;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getSymbol() {
		if( symbol == null ) {   
			synchronized( Currency.class ) {   
				if( symbol == null ) {
					java.util.Currency currency = java.util.Currency.getInstance(getCode());
					symbol = currency.getSymbol();
					if (StringUtils.equalsIgnoreCase(symbol, getCode())) {
						symbol = CurrencyService.defaultCurrencySymbol(getCode());
					}
				}
            }
        }
		return symbol;
	}

	@Override
	public int compareTo(Currency o) {
		return this.getName().compareTo(o.getName());
	}

}
