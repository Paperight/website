<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="hasRole('ROLE_OUTLET')">
<h1><pr:snippet name="heading" group="licence-summary" defaultValue="Licences"/></h1>
<%@ include file="/WEB-INF/views/licence/search.jsp" %>
<div id="licences">
	<table class="grid">
		<tr class="header">
			<td colspan="7"><pr:snippet name="grid-grand-totals-heading" group="licence-summary" defaultValue="Grand totals"/></td>
		</tr>
		<tr class="sub-header">
			<td class="left-column"><pr:snippet name="grid-transactions-heading" group="licence-summary" defaultValue="Transactions"/></td>
			<td class="left-column"><pr:snippet name="grid-copies-heading" group="licence-summary" defaultValue="Copies"/></td>
			<td class="left-column"><pr:snippet name="grid-credits-heading" group="licence-summary" defaultValue="Credits"/></td>
			<td class="left-column"><pr:snippet name="grid-currency-heading" group="licence-summary" defaultValue="Currency"/></td>
			<td class="center-column"><pr:snippet name="grid-licence-fees-charged-heading" group="licence-summary" defaultValue="Licence fees charged"/></td>
			<td class="center-column"><pr:snippet name="grid-printing-costs-charged-heading" group="licence-summary" defaultValue="Printing costs charged"/></td>
			<td class="center-column last-child"><pr:snippet name="grid-total-charged-heading" group="licence-summary" defaultValue="Total charged to customers"/></td>
		</tr>
		<c:forEach items="${licenceSummaries}" var="licenceSummary">
		<tr>
			<td class="left-column">${licenceSummary.numberOfTransactions}</td>
			<td class="left-column">${licenceSummary.numberOfCopies}</td>
			<td class="left-column"><pr:price showSymbol="false" showDecimal="false" amount="${licenceSummary.totalInCredits}" displayCurrency="USD" /></td>
			<td class="left-column">${licenceSummary.currencyCode}</td>
			<td class="center-column"><pr:price amount="${licenceSummary.totalInCurrency}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}" /></td>
			<td class="center-column"><pr:price amount="${licenceSummary.totalOutletCharges}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}"/></td>
			<td class="center-column last-child"><pr:price amount="${licenceSummary.totalCharged}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}" /></td>
		</tr>
		</c:forEach>
	</table>
</div>
<%@ include file="/WEB-INF/views/licence/generateStatement.jsp" %>
</sec:authorize>