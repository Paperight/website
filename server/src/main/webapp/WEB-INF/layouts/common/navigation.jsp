<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="navigators" class="navigation">
	<sec:authorize access="hasPermission(#user, 'CONFIGURATION')">
	<h3>Application</h3>
	<ul>
		<li><a href="${ctx}/application/config">Configuration</a></li>
	</ul>
	</sec:authorize>
	<sec:authorize access="hasPermission(#user, 'EDIT_PAPERIGHT_STAFF')">
	<h3>Admin Users</h3>
	<ul>
		<li><a href="${ctx}/user/create">New User</a></li>
		<li><a href="${ctx}/user/search">Edit Users</a></li>
	</ul>
	</sec:authorize>
	<h3>Products</h3>
	<ul>
		<li><a href="${ctx}/pdf/html">New HTML Conversion</a></li>
		<li><a href="${ctx}/pdf/pdf">Existing PDF Conversion</a></li>
<%-- 		<li><a href="${ctx}/pdf/epub">New ePub Conversion</a></li> --%>
		<li><a href="${ctx}/product/create">New Product</a></li>
		<li><a href="${ctx}/product/search">Edit Products</a></li>
		<sec:authorize access="hasPermission(#user, 'PRODUCT_OWNERSHIP')">
		<li><a href="${ctx}/product/ownership">Product Ownership</a></li>
		</sec:authorize>
	</ul>
	<sec:authorize access="hasPermission(#user, 'EDIT_COMPANY_CREDITS')">
	<h3>Company Credits</h3>
	<ul>
		<li><a href="${ctx}/company/credits">Company Credits</a></li>
	</ul>
	</sec:authorize>
	<sec:authorize access="hasPermission(#user, 'POSTERS')">
	<h3>Posters</h3>
	<ul>
		<li><a href="${ctx}/poster/create">New Poster</a></li>
		<li><a href="${ctx}/posters">Edit Posters</a></li>
	</ul>
	</sec:authorize>
	<h3>Product Import</h3>
	<ul>
		<li><a href="${ctx}/product/import">New Import</a></li>
		<li><a href="${ctx}/product/import/search">History</a></li>
	</ul>
	<h3>Bulk Import</h3>
    <ul>
        <li><a href="${ctx}/bulk/upload">New Import</a></li>
        <li><a href="${ctx}/bulk">History</a></li>
    </ul>
	<h3>Transactions</h3>
	<ul>
		<sec:authorize access="hasPermission(#user, 'AUTHORISE_TOPUPS')">
		<li><a href="${ctx}/transaction/credit/search">Credit Transactions</a></li>
		</sec:authorize>
		<li><a href="${ctx}/licence/search">Licences</a></li>
		<sec:authorize access="hasPermission(#user, 'PROCESS_PUBLISHER_PAYMENT_REQUESTS')">
		<li><a href="${ctx}/publisher-payment-requests/search">Publisher Payment Requests</a></li>
		</sec:authorize>
		<sec:authorize access="hasPermission(#user, 'EDIT_VAT_RATES')">
		<li><a href="${ctx}/vat-rates">Country VAT Rates</a></li>
		</sec:authorize>
	</ul>
	<h3>Content</h3>
	<ul>
		<sec:authorize access="hasPermission(#user, 'SNIPPETS')">
		<li><a href="${ctx}/snippets">Snippets</a></li>
		</sec:authorize>
		<sec:authorize access="hasPermission(#user, 'ARTICLES')">
		<li><a href="${ctx}/articles">Articles</a></li>
		</sec:authorize>
	</ul>
</div>