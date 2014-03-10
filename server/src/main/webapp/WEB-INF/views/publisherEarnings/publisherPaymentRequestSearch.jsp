<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Publisher Payment Requests</h1>
<div class="form">
<form:form method="GET" modelAttribute="publisherPaymentRequestSearch" >
	<div style="width:40%; float:left; margin-right:30px;">
		<dl>
			<dt>Status</dt>
			<dd>
				<form:select path="status" onchange="$(this).closest('form').submit();">
					<form:option value="">Any</form:option>
					<form:options itemLabel="displayName" />
				</form:select>
				<form:errors path="status" cssClass="error" element="label" />
			</dd>
		</dl>
	</div>
</form:form>
</div>
<c:if test="${not empty publisherPaymentRequests}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Reference</td>
		<td>Company</td>
		<td>Status</td>
		<td>ZAR Amount</td>
		<td>$ Amount</td>
		<td class="right-column"></td>
		<td class="right-column"></td>
		<td class="right-column"></td>
	</tr>
	<c:forEach items="${publisherPaymentRequests}" var="publisherPaymentRequest">
	<spring:eval expression="publisherPaymentRequest.status == T(com.paperight.publisherearning.PublisherPaymentRequestStatus).PENDING" var="isPending" />
	<spring:eval expression="publisherPaymentRequest.status == T(com.paperight.publisherearning.PublisherPaymentRequestStatus).CANCELLATION_REQUESTED" var="isCancellationRequested" />
	<tr>
		<td>${publisherPaymentRequest.id}</td>
		<td>${publisherPaymentRequest.company.name}</td>
		<td>${publisherPaymentRequest.status.displayName}</td>
		<td>R${publisherPaymentRequest.randsAmount}</td>
		<td>$${publisherPaymentRequest.dollarsAmount}</td>
		<td class="right-column"><a href="${ctx}/publisher-payment-request/${publisherPaymentRequest.id}">View Details</a></td>
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
	</c:forEach>
</table>
</c:if>