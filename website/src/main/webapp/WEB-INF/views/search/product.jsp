<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<t:importAttribute name="product" />
<c:url var="url" value="/product/${product.id}/${product.displayName}" />
<c:url var="imgUrl" value="/img/product-pic.png" />
<div class="product">
	<div class="image-panel">
		<a href="${url}">
			<pr:image product="${product}" width="100" height="130" />
		</a>
	</div>
	<div class="details">
		<h2><a href="${url}"><c:out value="${product.title}" /></a></h2>
		<c:if test="${not empty product.subTitle}">
		<h3><c:out value="${product.subTitle}" /></h3>
		</c:if>
		<dl>
			<c:if test="${not empty product.primaryCreators}">
			<dt><pr:snippet name="primaryCreators" group="searchResult" defaultValue="Primary Creators:" /></dt>
			<dd><c:out value="${product.primaryCreators}" /></dd>
			</c:if>
			<c:if test="${not empty product.publisher}">
			<dt><pr:snippet name="publisher" group="searchResult" defaultValue="Publisher/owner:" /></dt>
			<dd><c:out value="${product.publisher}" /></dd>
			</c:if>
			<c:if test="${not empty product.publicationDate}">
			<dt><pr:snippet name="publicationDate" group="searchResult" defaultValue="First published:" /></dt>
			<dd><joda:format value="${product.publicationDate}" pattern="MMMM yyyy" /></dd>
			</c:if>
			<c:set var="identifierType" value="ISBN" />
			<c:if test="${not empty product.identifierType}">
				<c:set var="identifierType" value="${product.identifierType}" />
			</c:if>
			<c:if test="${not empty product.identifier}">
			<dt>${identifierType}:</dt>
			<dd><c:out value="${product.identifier}" /></dd>
			</c:if>
			<c:choose>
				<c:when test="${product.twoUpPageExtent gt 0 && not empty product.twoUpFilename}">
					<dt><pr:snippet name="pages" group="searchResult" defaultValue="Pages:" /></dt>
					<dd><c:out value="${product.twoUpPageExtent}" /> A4 pages (two-up)</dd>
				</c:when>
				<c:when test="${product.oneUpPageExtent gt 0 && not empty product.oneUpFilename}">
					<dt><pr:snippet name="pages" group="searchResult" defaultValue="Pages:" /></dt>
					<dd><c:out value="${product.oneUpPageExtent}" /> A4 pages (one-up)</dd>
				</c:when>
				<c:when test="${product.a5PageExtent gt 0 && not empty product.a5Filename}">
					<dt><pr:snippet name="pages" group="searchResult" defaultValue="Pages:" /></dt>
					<dd><c:out value="${product.a5PageExtent}" /> A5 pages (one-up)</dd>
				</c:when>
			</c:choose>
		</dl>
		<div class="description">
			<p><c:out value="${fn:substring(product.shortDescription, 0, 250)}" /><c:if test="${fn:length(product.shortDescription) > 250}">&nbsp;...</c:if></p>
		</div>
	</div>
</div>
<div class="pricing">
	<div class="inner">
		<div class="ui-helper-clearfix">
            <c:set var="printoutAvailableIconClass" value="ui-icon-closethick" />
            <c:if test="${product.canPrint}">
                <c:set var="printoutAvailableIconClass" value="ui-icon-check" />
            </c:if>
			<div>
				<span style="float: left; clear: both; font-size: 12px;"><pr:snippet name="printoutAvailable" group="searchResult" defaultValue="Print-out available" /></span>
				<span class="ui-icon ${printoutAvailableIconClass}" style="float: left; clear: right;"></span>
			</div>
			<c:set var="canPhotocopyIconClass" value="ui-icon-closethick" />
			<c:if test="${product.canPhotocopy}">
			    <c:set var="canPhotocopyIconClass" value="ui-icon-check" />
			</c:if>
			<div style="padding-bottom: 50px">
				<span style="float: left; clear: both; font-size: 12px;"><pr:snippet name="canPhotocopy" group="searchResult" defaultValue="Can photocopy legally" /></span>
				<span class="ui-icon ${canPhotocopyIconClass}" style="float: left; clear: right;"></span>
			</div>
		</div>
		<sec:authorize access="hasRole('ROLE_OUTLET')">
			<c:choose>
				<c:when test="${product.availabilityStatus eq 'ON_SALE'}">
					<c:choose>
						<c:when test="${product.licenceFeeInCredits.unscaledValue() ne 0}">
							<div class="credit">
								<em>${product.licenceFeeInCredits}</em>
								<pr:snippet name="creditsText" group="searchResult"
									defaultValue="credits per copy-licence" />
							</div>
							<div class="value">
								<pr:snippet name="creditsAboutText" group="searchResult"
									defaultValue="About" />
								&nbsp;<em><pr:price amount="${product.licenceFeeInDollars}" /></em>
							</div>
						</c:when>
						<c:otherwise>
							<div class="credit">
								<em>Free</em>
							</div>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<div class="credit">
						<em style="font-size: 26px;"><pr:snippet
								name="unavailable-text" group="searchResult"
								defaultValue="Unavailable" /></em>
					</div>
				</c:otherwise>
			</c:choose>
		</sec:authorize>
		<a class="button btn-view-product" href="${url}"><pr:snippet
				name="viewProductButton" group="searchResult"
				defaultValue="View product" /></a>
	</div>
</div>
