<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="hasRole('ROLE_PUBLISHER')">
<h1><pr:snippet name="heading" group="publisherEarningSummary" defaultValue="Earnings summary"/></h1>
<%@ include file="/WEB-INF/views/publisherEarning/search.jsp" %>
<%@ include file="/WEB-INF/views/publisherEarning/requestPayment.jsp" %>
<div id="licences">
	<table class="grid">
		<tr class="header">
			<td><pr:snippet name="earnings-pending-header" group="publisherEarningSummary" defaultValue="Earnings pending"/></td>
			<td><pr:snippet name="earnings-requested-header" group="publisherEarningSummary" defaultValue="Earnings requested"/></td>
			<td><pr:snippet name="earnings-paid-header" group="publisherEarningSummary" defaultValue="Earnings paid"/></td>
			<td><pr:snippet name="earnings-total-header" group="publisherEarningSummary" defaultValue="Earnings total"/></td>
		</tr>
		<c:forEach items="${overallPublisherEarningSummary.overallPublisherEarningSummaryItems}" var="overallPublisherEarningSummaryItem">
		<tr>
			<td class="left-column"><pr:price amount="${overallPublisherEarningSummaryItem.pendingTotal}" currency="${overallPublisherEarningSummaryItem.currency.code}" displayCurrency="${overallPublisherEarningSummaryItem.currency.code}" /></td>
			<td class="left-column"><pr:price amount="${overallPublisherEarningSummaryItem.requestedTotal}" currency="${overallPublisherEarningSummaryItem.currency.code}" displayCurrency="${overallPublisherEarningSummaryItem.currency.code}" /></td>
			<td class="left-column"><pr:price amount="${overallPublisherEarningSummaryItem.paidTotal}" currency="${overallPublisherEarningSummaryItem.currency.code}" displayCurrency="${overallPublisherEarningSummaryItem.currency.code}" /></td>
			<td class="left-column"><pr:price amount="${overallPublisherEarningSummaryItem.total}" currency="${overallPublisherEarningSummaryItem.currency.code}" displayCurrency="${overallPublisherEarningSummaryItem.currency.code}" /></td>
		</tr>
		</c:forEach>
	</table>
</div>
<%@ include file="/WEB-INF/views/publisherEarning/generateStatement.jsp" %>
</sec:authorize>