<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Licences</h1>
<div class="form">
<form:form method="GET" modelAttribute="licenceSearch" >
	<div style="width:310px; float:left; margin-right:30px; margin-bottom:30px;">
		<div style="margin-bottom: 10px;">
			<a id="btn-download-all-licences" class="btn btn-paperight" href="${ctx}/licence/invoices"><pr:snippet name="downloadAllLicencesButton" group="topupPanel" defaultValue="Download licences"/></a>
			<a id="btn-download-all-earnings" class="btn btn-paperight" href="${ctx}/publisher-earnings/invoices"><pr:snippet name="downloadAllEarningsButton" group="topupPanel" defaultValue="Download earnings"/></a>
		</div>
		<dl>
			<dt>From Date</dt>
			<dd>
				<form:input path="fromDate" cssClass="date required" data-date-format="YYYY-MM-DD" />
				<form:errors path="fromDate" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>To Date</dt>
			<dd>
				<form:input path="toDate" cssClass="date required" data-date-format="YYYY-MM-DD" />
				<form:errors path="toDate" cssClass="error" element="label" />
			</dd>
		</dl>
		<div style="clear:both; width:100%;">
	        <input type="submit" class="btn btn-paperight" value="Search" />
	        <button value="${ctx}/licence/export" class="btn btn-paperight btn-export" style="float:right; margin-right:10px;">
	            <span>Export</span>
	        </button>
	    </div>
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
		<td>Format</td>
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