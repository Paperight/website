<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${not empty publisherEarningSummaries}">
<div id="licences">
	<table class="grid theme-b">
		<tr class="header">
			<td>Product</td>
			<td class="center-column">Total transactions</td>
			<td class="center-column">Total amount</td>
		</tr>
		<c:forEach items="${publisherEarningSummaries}" var="publisherEarningSummary">
		<tr>
			<td class="left-column left-content-column">
				<div class="product-ui-tn-label">
					<a href="${ctx}/product/${publisherEarningSummary.productId}">
						<span class="img"><pr:image productId="${publisherEarningSummary.productId}" width="60" height="60" /></span>
						<span class="label"><c:out value="${publisherEarningSummary.productTitle}" /></span>
					</a>
				</div>
			</td>
			<td class="left-column"><c:out value="${publisherEarningSummary.numberOfTransactions}" /></td>
			<td class="center-column"><pr:price amount="${publisherEarningSummary.amountInCurrency}" currency="${publisherEarningSummary.currency.code}" displayCurrency="${publisherEarningSummary.currency.code}" /></td>
		</tr>
		</c:forEach>
	</table>
</div>
</c:if>