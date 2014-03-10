<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Product Imports</h1>
<div class="form">
<form:form method="GET" modelAttribute="importSearch" >
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
<c:if test="${not empty importExecutions}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td style="width:100px">Date</td>
		<td>Filename</td>
		<td>Status</td>
		<td class="right-column"></td>
	</tr>
	<c:forEach items="${importExecutions}" var="importExecution">
	<tr>
		<td><joda:format value="${importExecution.createdDate}" style="MM" /></td>
		<td>${importExecution.filename}</td>
		<td>${importExecution.status}</td>
		<spring:eval expression="importExecution.status == T(com.paperight.product.ImportStatus).COMPLETE" var="isComplete" />
		<td class="right-column"><c:if test="${not isComplete}"><a href="${ctx}/product/import//${importExecution.id}/errors">View Errors</a></c:if></td>
	</tr>
	</c:forEach>
</table>
</c:if>