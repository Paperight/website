package com.paperight.licence;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class LicenceSearch {
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime fromDate;
	
	@DateTimeFormat(iso = ISO.DATE)
	private DateTime toDate;
	
	public DateTime getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(DateTime fromDate) {
		this.fromDate = fromDate.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	}
	
	public DateTime getToDate() {
		return toDate;
	}
	
	public void setToDate(DateTime toDate) {
		this.toDate = toDate.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
	}
}
