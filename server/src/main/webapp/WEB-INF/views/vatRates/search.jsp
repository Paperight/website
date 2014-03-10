<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Country VAT rates</h1>
<a id="btn-create" href="${ctx}/vat-rate/create" class="button">New VAT rate</a>
<br /><br />
<c:if test="${not empty vatRates}">
<table class="grid" style="width:100%">
	<thead>
		<tr class="header">
			<td>Country</td>
			<td>Rate 0</td>
			<td>Rate 1</td>
			<td>Rate 2</td>
			<td>Type</td>
			<td class="right-column"></td>
			<td class="right-column"></td>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${vatRates}" var="vatRate">
	<tr id="${vatRate.countryCode}">
		<td>${vatRate.country.name}</td>
		<td>${vatRate.rate0}%</td>
		<td>${vatRate.rate1}%</td>
		<td>${vatRate.rate2}%</td>
		<td>${vatRate.type}</td>
		<td class="right-column"><a href="${ctx}/vat-rate/update/${vatRate.countryCode}">Edit</a></td>
		<td class="right-column"><a class="delete" href="${ctx}/vat-rate/delete/${vatRate.countryCode}">Delete</a></td>
	</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>