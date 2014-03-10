<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${not empty productLicenceSummaries}">
<div id="licences">
	<table class="grid theme-b">
		<tr class="header">
			<td colspan="3"></td>
			<td class="center-column"><pr:snippet name="grid-credits-heading" group="licence-product-summary" defaultValue="Credits"/></td>
			<td class="center-column" colspan="3"><pr:snippet name="grid-charged-to-customers-heading" group="licence-product-summary" defaultValue="Charged to customers"/></td>
		</tr>
		<tr class="sub-header">
			<td class="left-column"><pr:snippet name="grid-product-heading" group="licence-product-summary" defaultValue="Product"/></td>
			<td class="left-column"><pr:snippet name="grid-total-transactions-heading" group="licence-product-summary" defaultValue="Total transactions"/></td>
			<td class="left-column"><pr:snippet name="grid-total-copies-heading" group="licence-product-summary" defaultValue="Total copies"/></td>
			<td class="center-column"><pr:snippet name="grid-total-credits-used-heading" group="licence-product-summary" defaultValue="Total credits used"/></td>
			<td class="center-column"><pr:snippet name="grid-total-licence-fees-heading" group="licence-product-summary" defaultValue="Total licence fees"/></td>
			<td class="center-column"><pr:snippet name="grid-total-print-costs-heading" group="licence-product-summary" defaultValue="Total print costs"/></td>
			<td class="center-column last-child"><pr:snippet name="grid-total-charged-heading" group="licence-product-summary" defaultValue="Total you charged"/></td>
		</tr>
		<c:forEach items="${productLicenceSummaries}" var="licenceSummary">
		<tr>
			<td class="left-column left-content-column">
				<div class="product-ui-tn-label">
					<a href="${ctx}/product/${licenceSummary.productId}">
						<span class="img"><pr:image productId="${licenceSummary.productId}" width="60" height="60" /></span>
						<span class="label"><c:out value="${licenceSummary.productTitle}" /></span>
					</a>
				</div>
			</td>
			<td class="left-column"><c:out value="${licenceSummary.numberOfTransactions}" /></td>
			<td class="left-column">${licenceSummary.numberOfCopies}</td>
			<td class="center-column"><pr:price showSymbol="false" showDecimal="false" amount="${licenceSummary.totalInCredits}" displayCurrency="USD" /></td>
			<td class="center-column"><pr:price amount="${licenceSummary.totalInCurrency}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}" /></td>
			<td class="center-column"><pr:price amount="${licenceSummary.totalOutletCharges}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}"/></td>
			<td class="center-column last-child"><pr:price amount="${licenceSummary.totalCharged}" currency="${licenceSummary.currencyCode}" displayCurrency="${licenceSummary.currencyCode}" /></td>
		</tr>
		</c:forEach>
	</table>
</div>
</c:if>