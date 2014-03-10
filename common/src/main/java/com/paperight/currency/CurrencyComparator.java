package com.paperight.currency;

import java.util.Comparator;
import java.util.Map;

public class CurrencyComparator implements Comparator<String> {

	Map<String, Currency> base;

	public CurrencyComparator(Map<String, Currency> base) {
		this.base = base;
	}

	public int compare(String a, String b) {
		Currency currency1 = base.get(a);
		Currency currency2 = base.get(b);
		return currency1.compareTo(currency2);
	}
	
}
