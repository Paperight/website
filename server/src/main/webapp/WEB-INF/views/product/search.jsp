<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Search Products</h1>
<div class="form">
	<form:form action="${ctx}/product/search" method="GET" modelAttribute="productSearch" >
	
		<div style="width:40%; float:left; margin-right:30px;">
			<dl>
				<dt>Identifier</dt>
				<dd><form:input path="identifier" />
				<form:errors path="identifier" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Title</dt>
				<dd><form:input path="title" />
				<form:errors path="title" cssClass="error" element="label" /></dd>
			</dl>
		</div>
		
		<div style="clear:both; width:100%;">
			<input type="submit" class="btn btn-paperight" value="Search" />
			<button value="${ctx}/product/search/rebuild-index" class="btn btn-paperight btn-rebuild-search-index" style="float:right; margin-right:10px;">
				<span>Rebuild Search Index</span>
			</button>
		</div>
		
	</form:form>
</div>

<c:if test="${not empty products}">
<br />
<div id="search-results">
	<table class="grid" id="search-results">
		<tr class="header">
			<td>Identifier</td>
			<td>Title</td>
			<td></td>
			<td></td>
		</tr>
		<c:forEach items="${products}" var="product">
		<tr>
			<td>${product.identifier}</td>
			<td>${product.title}</td>
			<td class="right-column"><a href="${ctx}/product/update/${product.id}">Edit</a></td>
			<td class="right-column"><a class="delete" href="${ctx}/product/delete/${product.id}">Delete</a></td>
		</tr>
		</c:forEach>
	</table>
</div>
</c:if>