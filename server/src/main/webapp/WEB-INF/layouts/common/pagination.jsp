<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<t:importAttribute name="paginator" ignore="true"  />
<t:importAttribute name="id" ignore="true"  />
<c:if test="${paginator.pageCount > 0}">
<div <c:if test="${not empty id}">id="${id}" </c:if>class="pagination">
	<ul>
		<c:if test="${!paginator.firstPage}">
		<li class="previous"><a href="#">&lt; Previous</a></li>
		</c:if>
		<c:if test="${paginator.firstPage}">
		<li class="previous disabled"><a href="#">&lt; Previous</a></li>
		</c:if>
		<c:forEach var="i" begin="${paginator.firstLinkedPage}" end="${paginator.lastLinkedPage}">
		<li class="<c:if test="${paginator.page eq i}">selected</c:if>"><a href="#">${i+1}</a></li>
		</c:forEach>
		<c:if test="${!paginator.lastPage}">
		<li class="next"><a href="#">Next &gt;</a></li>
		</c:if>
		<c:if test="${paginator.lastPage}">
		<li class="next disabled"><a href="#">Next &gt;</a></li>
		</c:if>
	</ul>
</div>
</c:if>