<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>Product Ownership</h1>
<div id="productownership" class="ui-helper-clearfix">

	<div id="companySelect">
		<div style="margin: 0;">
			<div class="selectfilters">
				<form>
					<input type="text" placeholder="Search companies..." />
					<button class="btn btn-paperight" id="company-search" type="submit">&raquo;</button>
					<img id="company-search-loader" style="display:none" src="${ctx}/img/ajax-loader.gif" width="32" height="32" />
				</form>
			</div>
			<div class="selectlists">
				<select id="companiesList" size="15" style="width: 100%;">
					<c:forEach var="company" items="${companies}">
						<option data-companyid="${company.id}">${company.name}
							(${company.companyAdminEmail})</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>

	<div id="companyProducts">
		<div>
			<h3>Assigned Products</h3>
			<div class="selectfilters">
				<form>
					<div>
					<input type="text" placeholder="Search products..." />
					<button class="btn btn-paperight" type="submit">&raquo;</button>
					<img id="company-product-search-loader" style="display:none" src="${ctx}/img/ajax-loader.gif" width="32" height="32" />
					</div>
				</form>
			</div>
			<div class="selectlists">
				<select size="15" style="width: 100%;" multiple="multiple">
					<c:forEach var="company" items="${companies}" end="0">
						<c:forEach var="product" items="${company.products}">
							<option class="slim" data-companyid="${company.id}"	data-productid="${product.id}">${product.title} (${product.identifier})</option>
						</c:forEach>
						<c:if test="${empty company.products}">
							<option class="empty-list">No products</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<button class="btn btn-paperight move-out" style="width: 100%;">Unassign &raquo;</button>
			<div class="ui-helper-clearfix">
				<small>*Note: products and orphaned products support multiple select.</small>
			</div>
		</div>
	</div>

	<div id="orphanedProducts">
		<div>
			<h3>Unassigned Products</h3>
			<div class="selectfilters">
				<form>
					<input type="text" placeholder="Search products..." />
					<button class="btn btn-paperight" type="submit">&raquo;</button>
					<img id="orphan-product-search-loader" style="display:none" src="${ctx}/img/ajax-loader.gif" width="32" height="32" />
				</form>
			</div>
			<div class="selectlists">
				<select size="15" style="width: 100%;" multiple="multiple">
					<c:forEach var="product" items="${orphanProducts}">
						<option data-productid="${product.id}">${product.title}	(${product.identifier})</option>
					</c:forEach>
					<c:if test="${empty orphanProducts}">
						<option class="empty-list">No products</option>
					</c:if>
				</select>
			</div>
			<button class="btn btn-paperight move-in" style="width: 100%;">&laquo; Assign</button>
			<div class="ui-helper-clearfix">
				<small>*Note: products and orphaned products support multiple select.</small>
			</div>
		</div>
	</div>

</div>