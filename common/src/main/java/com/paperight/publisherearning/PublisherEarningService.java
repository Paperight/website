package com.paperight.publisherearning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.Application;
import com.paperight.currency.CurrencyService;
import com.paperight.email.integration.EmailGateway;
import com.paperight.licence.Licence;
import com.paperight.product.Product;
import com.paperight.user.User;

@Service
public class PublisherEarningService {
	
	@Autowired
	private Application application;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private EmailGateway emailGateway;
	
	public boolean hasPendingEarnings(List<PublisherEarning> publisherEarnings) {
		for (PublisherEarning publisherEarning : publisherEarnings) {
			if (publisherEarning.getStatus() == PublisherEarningStatus.PENDING) {
				return true;
			}
		}
		return false;
	}
	
	public void recordPublisherEarning(Licence licence) {
		PublisherEarning publisherEarning = PublisherEarning.findByLicenceId(licence.getId());
		if (publisherEarning == null) {
			if (needsPublisherEarning(licence)) {
				publisherEarning = new PublisherEarning();
				publisherEarning.setLicence(licence);
				BigDecimal amount = calculatePublisherEarningAmount(licence);
				publisherEarning.setAmount(amount);
				publisherEarning.setPublisherEarningPercent(getPublisherEarningPercent(licence.getProduct()));
				BigDecimal amountInCurrency = currencyService.convert(amount, currencyService.getBaseCurrency(), licence.getCompany().getCurrency());
				publisherEarning.setAmountInCurrency(amountInCurrency);
				publisherEarning.setCurrencyCode(licence.getCompany().getCurrency().getCode());
				publisherEarning.setCompany(licence.getOwnerCompany());
				publisherEarning.persist();
			}
		}
	}
	
	private Integer getPublisherEarningPercent(Product product) {
		Integer percent = product.getPublisherEarningPercent();
		if (percent == null) {
			percent = application.getPublisherEarningPercent();
		}
		return percent;
	}
	
	private BigDecimal calculatePublisherEarningAmount(Licence licence) {
		Product product = licence.getProduct();
		double publisherEarningPercent = Double.valueOf(getPublisherEarningPercent(product)) / 100;
		BigDecimal amount = product.getLicenceFeeInDollars().multiply(BigDecimal.valueOf(publisherEarningPercent));
		amount = amount.multiply(new BigDecimal(licence.getNumberOfCopies())); 
		return amount;
	}
	
	private boolean needsPublisherEarning(Licence licence) {
		boolean result = false;
		if (licence.getOwnerCompany() != null) {
			result = true;
		}
		return result;
	}
	
	@Transactional
	public void requestPayment(List<PublisherEarning> publisherEarnings, User user) {
		if (!publisherEarnings.isEmpty()) {
			PublisherPaymentRequest publisherPaymentRequest = new PublisherPaymentRequest();
			publisherPaymentRequest.setCompany(user.getCompany());
			publisherPaymentRequest.setUser(user);
			publisherPaymentRequest.setPublisherEarnings(new HashSet<PublisherEarning>(publisherEarnings));
			changePublisherEarningsStatus(publisherEarnings, PublisherEarningStatus.REQUESTED);
			publisherPaymentRequest.persist();		
			emailGateway.publisherPaymentRequest(publisherPaymentRequest);
		}
	}
	
	
	
	@Transactional
	private void changePublisherEarningsStatus(List<PublisherEarning> publisherEarnings, PublisherEarningStatus status) {
		for (PublisherEarning publisherEarning : publisherEarnings) {
			publisherEarning.setStatus(status);
			publisherEarning.merge();
		}
	}
	
	@Transactional
	public void requestPaymentRequestCancellation(PublisherPaymentRequest publisherPaymentRequest) {
		publisherPaymentRequest.setStatus(PublisherPaymentRequestStatus.CANCELLATION_REQUESTED);
		publisherPaymentRequest.persist();
		emailGateway.cancelPublisherPaymentRequest(publisherPaymentRequest);
	}
	
	@Transactional
	public void completePublisherPaymentRequest(PublisherPaymentRequest publisherPaymentRequest) {
		changePublisherPaymentRequestState(publisherPaymentRequest, PublisherPaymentRequestStatus.PAID, PublisherEarningStatus.PAID);
	}
	
	@Transactional
	public void cancelPublisherPaymentRequest(PublisherPaymentRequest publisherPaymentRequest) {
		changePublisherPaymentRequestState(publisherPaymentRequest, PublisherPaymentRequestStatus.CANCELLED, PublisherEarningStatus.PENDING);
	}
	
	@Transactional
	private void changePublisherPaymentRequestState(PublisherPaymentRequest publisherPaymentRequest, PublisherPaymentRequestStatus status, PublisherEarningStatus publisherEarningsStatus) {
		publisherPaymentRequest.setStatus(status);
		List<PublisherEarning> publisherEarnings = new ArrayList<PublisherEarning>();
		publisherEarnings.addAll(publisherPaymentRequest.getPublisherEarnings());
		changePublisherEarningsStatus(publisherEarnings, publisherEarningsStatus);
		publisherPaymentRequest.persist();
	}

}
