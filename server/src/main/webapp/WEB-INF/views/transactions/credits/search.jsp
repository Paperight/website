<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Credit Transactions</h1>
<div class="form">
<form:form method="GET" modelAttribute="transactionSearch" >
	<div style="width:40%; float:left; margin-right:30px;">
		<dl>
			<dt>Status</dt>
			<dd>
				<form:select path="status" onchange="$(this).closest('form').submit();">
					<form:option value="">Any</form:option>
					<form:options />
				</form:select>
				<form:errors path="status" cssClass="error" element="label" />
			</dd>
		</dl>
	</div>
</form:form>
</div>
<c:if test="${not empty transactions}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Reference</td>
		<td>Company</td>
		<td>Status</td>
		<td>Credits</td>
		<td>Currency</td>
		<td>Amount</td>
		<td>Payment Method</td>
		<td class="right-column"></td>
		<td class="right-column"></td>
	</tr>
	<c:forEach items="${transactions}" var="transaction">
	<spring:eval expression="transaction.status == T(com.paperight.credit.PaperightCreditTransactionStatus).PENDING" var="isPending" />
	<tr>
		<td>${transaction.transactionReference}</td>
		<td>${transaction.company.name}</td>
		<td>${transaction.status}</td>
		<td>${transaction.numberOfCredits}</td>
		<td>${transaction.currency}</td>
		<td>${transaction.amount}</td>
		<td>${transaction.paymentMethod}</td>
		<c:choose>
			<c:when test="${isPending}">
				<td class="right-column"><a href="${ctx}/transaction/credit/${transaction.id}/cancel">Cancel</a></td>
				<td class="right-column"><a href="${ctx}/transaction/credit/${transaction.id}/authorise">Authorise</a></td>
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