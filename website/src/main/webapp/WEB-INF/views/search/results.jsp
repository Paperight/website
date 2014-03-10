<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="search-details">
	<c:if test="${empty searchterm}">
		<h1><pr:snippet name="results-no-search-term" group="searchResults" defaultValue="Results" /></h1>
	</c:if>
	<c:if test="${not empty searchterm}">
	<h1><pr:snippet name="results-search-term" group="searchResults" defaultValue="Results for" />&nbsp;<em>&quot;<c:out value="${searchterm}" />&quot;</em>
	</h1>
	</c:if>
	<div class="description"><pr:snippet name="item-count-pre" group="searchResults" defaultValue="Found" />&nbsp;<em>${paginator.itemCount}</em>&nbsp;<pr:snippet name="item-count-post" group="searchResults" defaultValue="results" /></div>
	<div class="description"><pr:snippet name="current-page-pre" group="searchResults" defaultValue="Page" />&nbsp;<em>${paginator.page}</em>&nbsp;<pr:snippet name="current-page-post" group="searchResults" defaultValue="of" />&nbsp;<em>${paginator.pageCount}</em></div>
</div>
<div id="results-list-panel">
	<c:if test="${paginator.pageSize > 1}">
	<t:insertDefinition name="pagination">
		<t:putAttribute name="id" value="top-pagination" type="string" />
		<t:putAttribute name="paginator" value="${paginator}" />
	</t:insertDefinition>
	</c:if>
	
	<c:if test="${paginator.itemCount eq 0}">
		<div id="results-list-empty" class="ui-helper-clearfix">
			<pr:snippet name="no-results" group="searchResults" multiline="true" defaultValue="&lt;p&gt;Your search did not match any products.&lt;/p&gt;
&lt;p&gt;Suggestions:&lt;/p&gt;
&lt;ul&gt;
	&lt;li&gt;Make sure all words are spelled correctly.&lt;/li&gt;
	&lt;li&gt;Try different keywords.&lt;/li&gt;
	&lt;li&gt;Try more general keywords.&lt;/li&gt;
&lt;/ul&gt;" />
		</div>
	</c:if>
	
	<c:if test="${paginator.itemCount > 0}">
	<ul id="results-list">
	<c:forEach items="${products}" var="product" varStatus="status">
		<c:set var="i" value="" />
		<c:if test="${status.count %2 == 0}"><c:set var="i" value="alternate" /></c:if>
		<li class="${i}">
			<t:insertDefinition name="search/result/product">
				<t:putAttribute name="product" value="${product}" />
			</t:insertDefinition>
		</li>
	</c:forEach>
	</ul>
	</c:if>
	
	<c:if test="${paginator.pageSize > 1}">
	<t:insertDefinition name="pagination">
		<t:putAttribute name="paginator" value="${paginator}" />
	</t:insertDefinition>
	</c:if>
	

	<c:if test="${paginator.page == 1}">
	<h3 id="amazon-header"><pr:snippet name="amazon-header" group="searchResults" defaultValue="If we don't have what you're looking for, you might want to order it from Amazon:" /></h3>
	<c:set var="q" value="${pageContext.request.queryString}" />
	<div id="amazon-loader" style="text-align:center; display:block" width="100%"><img src="${ctx}/img/ajax-loader.gif" /> </div>
	<div id="amazon-products" class="ui-view" view-id="${ctx}/search/amazon.html?${q}"></div>
	</c:if>
	
</div>
<script type="text/javascript" >

$('#amazon-products').ajaxStart(function() {
    $('#amazon-loader').show();
}).ajaxComplete(function() {
	 $('#amazon-loader').hide();
});

</script>