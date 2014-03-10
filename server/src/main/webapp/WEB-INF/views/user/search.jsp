<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Users</h1>
<c:if test="${not empty users}">
<br />
<div id="search-results">
	<table class="grid" id="search-results">
		<tr class="header">
			<td style="width:100%;">Details</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<c:forEach items="${users}" var="user">
		<c:if test="${fn:contains(user.roles, 'ROLE_ADMIN')}">
		<tr data-userid="${user.id}">
			<td>
				<a href="${ctx}/user/update/${user.id}">${user.firstName}&nbsp;${user.lastName}</a><br />
				<span class="subtle">${user.email}</span>
			</td>
			<td>
				<c:if test="${user.enabled eq true}">Enabled</c:if>
				<c:if test="${user.enabled eq false}">Disabled</c:if>
			</td>
			<td>&nbsp;</td>
			<td class="right-column">
				<a href="${ctx}/user/update/${user.id}">Edit</a>
				&nbsp;
				<a class="delete" href="${ctx}/user/delete/${user.id}">Delete</a>
			</td>
		</tr>
		</c:if>
		</c:forEach>
	</table>
</div>
</c:if>