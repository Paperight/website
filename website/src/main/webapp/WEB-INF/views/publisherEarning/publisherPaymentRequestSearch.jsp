<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Publisher Payment Requests</h1>
<div class="form">
<form:form method="GET" modelAttribute="publisherPaymentRequestSearch" >
	<div style="width:40%; float:left; margin-right:30px;">
		<dl>
			<dt><pr:snippet name="search-status-label" group="publisher-payment-request" defaultValue="Status"/></dt>
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
		<td><pr:snippet name="grid-reference-heading" group="publisher-payment-request" defaultValue="Reference"/></td>
		<td><pr:snippet name="grid-rand-amount-heading" group="publisher-payment-request" defaultValue="ZAR Amount"/></td>
		<td><pr:snippet name="grid-dollar-amount-heading" group="publisher-payment-request" defaultValue="$ Amount"/></td>
		<td><pr:snippet name="grid-date-requested-heading" group="publisher-payment-request" defaultValue="Requested"/></td>
		<td><pr:snippet name="grid-status-heading" group="publisher-payment-request" defaultValue="Status"/></td>
		<td><pr:snippet name="grid-date-updated-heading" group="publisher-payment-request" defaultValue="Updated"/></td>
		<td class="right-column"></td>
	</tr>
	<c:forEach items="${publisherPaymentRequests}" var="publisherPaymentRequest">
	<spring:eval expression="publisherPaymentRequest.status == T(com.paperight.publisherearning.PublisherPaymentRequestStatus).PENDING" var="isPending" />
	<tr>
		<td>${publisherPaymentRequest.id}</td>
		<td>R${publisherPaymentRequest.randsAmount}</td>
		<td>$${publisherPaymentRequest.dollarsAmount}</td>
		<td><joda:format value="${publisherPaymentRequest.createdDate}" style="M-" /></td>
		<td>${publisherPaymentRequest.status.displayName}</td>
		<td><joda:format value="${publisherPaymentRequest.updatedDate}" style="M-" /></td>
		<c:choose>
			<c:when test="${isPending}">
				<td class="right-column"><a href="${publisherPaymentRequest.id}" id="cancel-publisher-payment-request"><pr:snippet name="grid-cancel-link" group="publisher-payment-request" defaultValue="Cancel"/></a></td>
			</c:when>
			<c:otherwise>
				<td class="right-column">&nbsp;</td>
			</c:otherwise>
		</c:choose>
	</tr>
	</c:forEach>
</table>
</c:if>

<script type="text/javascript">

$(document).ready(function(){
	
	$('#cancel-publisher-payment-request').click(function(){
		var publisherPaymentRequestId = $(this).attr('href');
		paperight.dialog('<p class="message"><pr:snippet name="dialog-cancel-payment-request-question" group="publisher-payment-request" escapeJavascript="true" defaultValue="Cancel this payment request?"/></p>', {
			title: '<pr:snippet name="dialog-cancel-payment-request-title" group="publisher-payment-request" escapeJavascript="true" defaultValue="Cancel payment request"/>', height: 165, model: true,
			buttons: {
				"<pr:snippet name="dialog-cancel-payment-request-button-no" group="publisher-payment-request" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-cancel-payment-request-button-yes" group="publisher-payment-request" escapeJavascript="true" defaultValue="Yes"/>": function(){
					var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-cancelling-payment-request-message-cancelling" group="publisher-payment-request" escapeJavascript="true" defaultValue="Submitting your request..."/></p>', {
						title: "<pr:snippet name="dialog-cancelling-payment-request-title" group="publisher-payment-request" escapeJavascript="true" defaultValue="Cancel publisher payment request"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
					});
					$(this).dialog("close");
					return paperight.sendCancelPublisherPaymentRequest(publisherPaymentRequestId, {
						success: function(data){
							modal.dialog('progressbar', {stop: true, value: 100});
							if(typeof data == "object"){
								modal.find(".message").text((data.result == false) ? data.message : "<pr:snippet name="dialog-cancelling-payment-request-message-cancelled" group="publisher-payment-request" escapeJavascript="true" defaultValue="Thank you! We've received your request to cancel your payment request"/>");
							};
							modal.dialog('addbutton', "<pr:snippet name="dialog-cancelling-payment-request-button-ok" group="publisher-payment-request" escapeJavascript="true" defaultValue="OK"/>", function(){
								$(this).dialog("close");
								var url = window.location.href;
								url = url.split("?")[0] + "?status=CANCELLATION_REQUESTED";
								window.location = url;
							});
						},
						error: function(){
							modal.find(".message").text("Error");
							modal.dialog('progressbar', {stop: true, value: 100});
							modal.dialog('addbutton', "<pr:snippet name="dialog-cancelling-payment-request-button-ok" group="publisher-payment-request" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
						}
					});
				}
			}
		});
		return false;
	});

});

</script>