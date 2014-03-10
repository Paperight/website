package com.paperight.email.integration;

import org.springframework.integration.annotation.Gateway;

import com.paperight.credit.PaperightCreditTransaction;
import com.paperight.email.ContactMessage;
import com.paperight.email.ResetPassword;
import com.paperight.email.SuggestBook;
import com.paperight.email.UpdateEmail;
import com.paperight.licence.Licence;
import com.paperight.publisherearning.PublisherPaymentRequest;
import com.paperight.user.Company;
import com.paperight.user.User;

public interface EmailGateway {
	
	@Gateway(requestChannel="userRegistationChannel")
	public void userRegistration(final User user);
	
	@Gateway(requestChannel="activateUserChannel")
	public void activateUser(final User user);
	
	@Gateway(requestChannel="contactUsChannel")
	public void contactUs(final ContactMessage contactMessage);
	
	@Gateway(requestChannel="resetPasswordChannel")
	public void resetPassword(final ResetPassword resetPassword);
	
	@Gateway(requestChannel="paperightCreditTransactionPaymentReceivedChannel")
	public void paperightCreditTransactionPaymentReceived(final PaperightCreditTransaction transaction);
	
	@Gateway(requestChannel="updateEmailChannel")
	public void updateEmail(final UpdateEmail updateEmail);
	
	@Gateway(requestChannel="suggestBookChannel")
	public void suggestBook(final SuggestBook suggestBook);

	@Gateway(requestChannel="newUserChannel")
	public void newUser(final User user);
	
	@Gateway(requestChannel="closeUserChannel")
	public void closeUser(final User user);
	
	@Gateway(requestChannel="reopenUserChannel")
	public void reopenUser(final User user);

	@Gateway(requestChannel="publisherPaymentRequestEmailChannel")
	public void publisherPaymentRequest(final PublisherPaymentRequest publisherPaymentRequest);
	
	@Gateway(requestChannel="cancelPublisherPaymentRequestEmailChannel")
	public void cancelPublisherPaymentRequest(final PublisherPaymentRequest publisherPaymentRequest);

	@Gateway(requestChannel="licencePurchaseEmailChannel")
	public void licencePurchaseComplete(Licence licence);
	
	@Gateway(requestChannel="paidPublisherPaymentRequestEmailChannel")
	public void paidPublisherPaymentRequest(final PublisherPaymentRequest publisherPaymentRequest);
	
	@Gateway(requestChannel="publisherEarningReminderEmailChannel")
	public void publisherEarningReminder(final Company company);

}
