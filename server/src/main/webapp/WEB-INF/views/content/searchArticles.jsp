<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Articles</h1>
<c:if test="${not empty articles}">
<table id="posters-grid" class="grid" style="width:100%">
	<thead>
		<tr class="header">
			<td>Name</td>
			<td>Title</td>
			<td class="right-column">Revision</td>
			<td class="right-column">Published</td>
			<td class="right-column"></td>
			<td class="right-column"></td>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${articles}" var="article">
	<tr>
		<td>${article.name}</td>
		<td>${article.title}</td>
		<td class="right-column">${article.revision}</td>
		<td class="right-column">${article.published}</td>
		<td class="right-column"><a href="${ctx}/article/update/${article.id}">Edit</a></td>
		<c:choose>
			<c:when test="${article.published}">
				<td class="right-column"><a href="${ctx}/article/publish/${article.id}/false">Unpublish</a></td>
			</c:when>
			<c:otherwise>
				<td class="right-column"><a href="${ctx}/article/publish/${article.id}/true">Publish</a></td>
			</c:otherwise>
		</c:choose>
	</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>