<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Publisher Payment Request</h1>
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Reference</td>
		<td>Company</td>
		<td>Status</td>
		<td>ZAR Amount</td>
		<td>$ Amount</td>
		<td class="right-column"></td>
		<td class="right-column"></td>
	</tr>
	<spring:eval expression="publisherPaymentRequest.status == T(com.paperight.publisherearning.PublisherPaymentRequestStatus).PENDING" var="isPending" />
	<spring:eval expression="publisherPaymentRequest.status == T(com.paperight.publisherearning.PublisherPaymentRequestStatus).CANCELLATION_REQUESTED" var="isCancellationRequested" />
	<tr>
		<td>${publisherPaymentRequest.id}</td>
		<td>${publisherPaymentRequest.company.name}</td>
		<td>${publisherPaymentRequest.status.displayName}</td>
		<td>R${publisherPaymentRequest.randsAmount}</td>
		<td>$${publisherPaymentRequest.dollarsAmount}</td>
		<c:choose>
			<c:when test="${isPending or isCancellationRequested}">
				<td class="right-column"><a href="${ctx}/publisher-payment-request/${publisherPaymentRequest.id}/cancel">Cancel</a></td>
				<td class="right-column"><a href="${ctx}/publisher-payment-request/${publisherPaymentRequest.id}/complete">Complete</a></td>
			</c:when>
			<c:otherwise>
				<td class="right-column">&nbsp;</td>
				<td class="right-column">&nbsp;</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Payment Method</td>
		<td>Payment Details</td>
	</tr>
	<tr>
		<td>${publisherPaymentDetails.paymentMethod}</td>
		<td>
		<c:choose>
			<c:when test="${publisherPaymentDetails.paymentMethod eq 'PAYPAL'}">
				${publisherPaymentDetails.paypalAddress}
			</c:when>
			<c:when test="${publisherPaymentDetails.paymentMethod eq 'BANK_ACCOUNT'}">
				Bank name: ${publisherPaymentDetails.bankName}<br />
				Account holder: ${publisherPaymentDetails.bankAccountHolder}<br />
				Account number: ${publisherPaymentDetails.bankAccountNumber}<br />
				Branch name: ${publisherPaymentDetails.bankBranchName}<br />
				Branch code: ${publisherPaymentDetails.bankBranchCode}
			</c:when>
		</c:choose>
		
		</td>
	</tr>
</table>