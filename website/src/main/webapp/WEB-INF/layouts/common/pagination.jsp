<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<t:importAttribute name="paginator" ignore="true"  />
<t:importAttribute name="id" ignore="true"  />
<c:if test="${paginator.pageCount > 0}">
<div <c:if test="${not empty id}">id="${id}" </c:if>class="pagination">
	<ul>
		<c:if test="${!paginator.firstPage}">
		<li class="previous"><a target="_self" href="${ctx}${paginator.url}${paginator.page-1}">&lt; <pr:snippet name="paginatorPrevious" group="searchResults" defaultValue="Previous" /></a></li>
		</c:if>
		<c:if test="${paginator.firstPage}">
		<li class="previous disabled"><a>&lt; <pr:snippet name="paginatorPrevious" group="searchResults" defaultValue="Previous" /></a></li>
		</c:if>
		<c:forEach var="i" begin="${paginator.firstLinkedPage}" end="${paginator.lastLinkedPage}">
		<li class="<c:if test="${paginator.page eq i+1}">selected</c:if>"><a target="_self" href="${ctx}${paginator.url}${i+1}">${i+1}</a></li>
		</c:forEach>
		<c:if test="${!paginator.lastPage}">
		<li class="next"><a target="_self" href="${ctx}${paginator.url}${paginator.page+1}"><pr:snippet name="paginatorNext" group="searchResults" defaultValue="Next" /> &gt;</a></li>
		</c:if>
		<c:if test="${paginator.lastPage}">
		<li class="next disabled"><a><pr:snippet name="paginatorNext" group="searchResults" defaultValue="Next" /> &gt;</a></li>
		</c:if>
	</ul>
</div>
</c:if>