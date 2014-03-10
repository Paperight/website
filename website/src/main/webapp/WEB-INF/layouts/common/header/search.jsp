<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div class="notifications"></div>
<div id="search-panel">
	<form id="search-form" method="get" action="${ctx}/search">
		<div class="input-panel">
			<c:if test="${empty searchterm}">
			<input name="q" id="searchterm" type="text" value="<pr:snippet name="text" group="searchBar" defaultValue="Search for books or documents (e.g. by Title / Author / Keyword / ISBN)"/>" class="clearfocus" />
			</c:if>
			<c:if test="${not empty searchterm}">
			<input name="q" id="searchterm" type="text" value="<c:out value="${searchterm}" />" />
			</c:if>
		</div>
		<div class="btn-panel">
			<button class="button-big" id="search-button" type="submit"><pr:snippet name="button" group="searchBar" defaultValue="search"/></button>
		</div>
	</form>
</div>
