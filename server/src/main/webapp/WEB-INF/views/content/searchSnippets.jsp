<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Snippets</h1>
<c:if test="${not empty snippetGroups}">
<table id="posters-grid" class="grid" style="width:100%">
	<thead>
		<tr class="header">
			<td>Group name</td>
			<td class="right-column"></td>
			<td class="right-column"></td>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${snippetGroups}" var="snippetGroup">
	<tr>
		<td>${snippetGroup}</td>
		<td class="right-column"><a href="${ctx}/snippets/update/${snippetGroup}">Edit</a></td>
	</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>