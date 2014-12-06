<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>Company Credits</h1>
<div class="ui-helper-clearfix">
	<form action="${ctx}/company/search">
		<div style="clear: both; width: 100%;">
			<input class="btn btn-paperight" type="submit" value="New Credit Record">
		</div>
	</form>
</div>

<c:if test="${not empty companyCreditRecords}">
	<br />
	<div id="search-results">
		<table class="grid" id="search-results">
			<tr class="header">
				<td>Company</td>
				<td>Credits</td>
				<td>Date Created</td>
				<td></td>
			</tr>
			<c:forEach items="${companyCreditRecords}" var="companyCreditRecord">
				<tr>
					<td>${companyCreditRecord.company.name}</td>
					<td>${companyCreditRecord.credits}</td>
					<td><joda:format value="${companyCreditRecord.createdDate}" pattern="dd MMMM YYYY" /></td>
					<td class="right-column"><a
						href="${ctx}/company/credits/detail/${companyCreditRecord.id}">View Details</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</c:if>