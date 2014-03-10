package com.paperight.publisherearning;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class PublisherEarningSearch {
	
	private PublisherEarningStatus status;
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime fromDate;
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime toDate;
	
	public PublisherEarningStatus getStatus() {
		return status;
	}
	
	public void setStatus(PublisherEarningStatus status) {
		this.status = status;
	}

	public DateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(DateTime fromDate) {
		this.fromDate = fromDate;
	}

	public DateTime getToDate() {
		return toDate;
	}

	public void setToDate(DateTime toDate) {
		if (toDate != null) {
			toDate = toDate.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue();
		}
		this.toDate = toDate;
	}

}
