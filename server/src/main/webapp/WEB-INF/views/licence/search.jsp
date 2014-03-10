<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Licences</h1>
<div class="form">
<form:form method="GET" modelAttribute="licenceSearch" >
	<div style="width:40%; float:left; margin-right:30px;">
		<dl>
			<dt>From Date</dt>
			<dd>
				<form:input path="fromDate" cssClass="date required" />
				<form:errors path="fromDate" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>To Date</dt>
			<dd>
				<form:input path="toDate" cssClass="date required" />
				<form:errors path="toDate" cssClass="error" element="label" />
			</dd>
		</dl>
	</div>
	<div style="clear:both; width:100%;">
		<input type="submit" value="Search" />
		<button value="${ctx}/licence/export" class="btn-export" style="float:right; margin-right:10px;">
			<span class="ui-icon ui-icon-circle-arrow-s"></span>
			<span>Export</span>
		</button>
	</div>
</form:form>
</div>
<c:if test="${not empty licences}">
<table class="grid" style="width:100%">
	<tr class="header">
		<td>Rightsholder</td>
		<td>Date</td>
		<td>Title</td>
		<td>Outlet Name</td>
		<td>Layout</td>
		<td>Number Copies</td>
		<td>Status</td>
	</tr>
	<c:forEach items="${licences}" var="licence">
	<tr>
		<td>${licence.product.publisher}</td>
		<td><joda:format value="${licence.transactionDate}" style="MM" /></td>
		<td>${licence.product.title}</td>
		<td>${licence.company.name}</td>
		<td>${licence.pageLayout.displayName}</td>
		<td>${licence.numberOfCopies}</td>
		<td>${licence.status}</td>
	</tr>
	</c:forEach>
</table>
</c:if>