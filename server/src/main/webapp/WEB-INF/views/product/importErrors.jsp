<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Import Execution</h1>
<c:if test="${not empty importExecution}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Date</td>
		<td>Filename</td>
		<td class="right-column">Status</td>
	</tr>
	<tr>
		<td><joda:format value="${importExecution.createdDate}" style="MM" /></td>
		<td>${importExecution.filename}</td>
		<td>${importExecution.status}</td>
	</tr>
</table>
</c:if>
<br />
<h2>Errors</h2>
<c:if test="${not empty importExecution.errors}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Error</td>
		<td>Line Content</td>
	</tr>
	<c:forEach items="${importExecution.errors}" var="error">
	<tr>
		<td>${error.error}</td>
		<td>${error.lineContent}</td>
	</tr>
	</c:forEach>
</table>
</c:if>