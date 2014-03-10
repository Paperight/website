package com.paperight.publisherearning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.paperight.currency.Currency;

public class OverallPublisherEarningSummary {
	
	public class OverallPublisherEarningSummaryItem {
		
		private List<PublisherEarning> publisherEarnings = new ArrayList<PublisherEarning>();
		private Currency currency;
		
		public List<PublisherEarning> getPublisherEarnings() {
			return publisherEarnings;
		}
		
		public void setPublisherEarnings(List<PublisherEarning> publisherEarnings) {
			this.publisherEarnings = publisherEarnings;
		}
		
		private void addPublisherEarning(PublisherEarning publisherEarning) {
			getPublisherEarnings().add(publisherEarning);
		}

		public Currency getCurrency() {
			return currency;
		}

		public void setCurrency(Currency currency) {
			this.currency = currency;
		}
		
		public BigDecimal getPendingTotal() {
			return getTotalByPublisherEarningStatus(PublisherEarningStatus.PENDING);
		}
		
		public BigDecimal getRequestedTotal() {
			return getTotalByPublisherEarningStatus(PublisherEarningStatus.REQUESTED);
		}
		
		public BigDecimal getPaidTotal() {
			return getTotalByPublisherEarningStatus(PublisherEarningStatus.PAID);
		}
		
		public BigDecimal getTotal() {
			return getTotalByPublisherEarningStatus(null);
		}
		
		private BigDecimal getTotalByPublisherEarningStatus(PublisherEarningStatus status) {
			BigDecimal result = new BigDecimal(0);
			for (PublisherEarning publisherEarning : getPublisherEarnings()) {
				if (publisherEarning.getStatus() == status || status == null) {
					result = result.add(publisherEarning.getAmountInCurrency());
				}
			}
			return result;
		}
		
	}
	
	private List<OverallPublisherEarningSummaryItem> overallPublisherEarningSummaryItems = new ArrayList<OverallPublisherEarningSummaryItem>();

	public OverallPublisherEarningSummary(List<PublisherEarning> publisherEarnings) {
		for (PublisherEarning publisherEarning : publisherEarnings) {
			OverallPublisherEarningSummaryItem overallPublisherEarningSummaryItem = getPublisherEarningSummaryByCurrency(publisherEarning.getCurrency());
			if (overallPublisherEarningSummaryItem == null) {
				overallPublisherEarningSummaryItem = createOverallPublisherEarningSummaryItem(publisherEarning.getCurrency());				
			}
			overallPublisherEarningSummaryItem.addPublisherEarning(publisherEarning);
		}
		
	}
	
	private OverallPublisherEarningSummaryItem getPublisherEarningSummaryByCurrency(Currency currency) {
		for (OverallPublisherEarningSummaryItem overallPublisherEarningSummaryItem : getOverallPublisherEarningSummaryItems()) {
			if (StringUtils.endsWithIgnoreCase(overallPublisherEarningSummaryItem.getCurrency().getCode(), currency.getCode())) {
				return overallPublisherEarningSummaryItem;
			}
		}
		return null;
	}
	
	private OverallPublisherEarningSummaryItem createOverallPublisherEarningSummaryItem(Currency currency) {
		OverallPublisherEarningSummaryItem overallPublisherEarningSummaryItem = new OverallPublisherEarningSummaryItem();
		overallPublisherEarningSummaryItem.setCurrency(currency);
		getOverallPublisherEarningSummaryItems().add(overallPublisherEarningSummaryItem);
		return overallPublisherEarningSummaryItem;
	}

	public List<OverallPublisherEarningSummaryItem> getOverallPublisherEarningSummaryItems() {
		return overallPublisherEarningSummaryItems;
	}
	
}
