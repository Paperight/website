<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${fn:length(amazonProducts) gt 0}">
<div id="amazon-results" class="ui-helper-clearfix">
<c:forEach items="${amazonProducts}" var="product" begin="0" end="7">
	<c:url var="url" value="${product.detailPageUrl}" />
	<div class="amazon-result">
		<div class="image-panel" style="text-align:center; height: 180px;">
			<a href="${url}">
				<pr:image product="${product}" width="100" height="130" customImageUrl="${product.imageUrl}" />
			</a>
		</div>
		<div class="amazon-details" style="width:185px">
			<div class="title"><a title="${product.title}" href="${url}"><c:out value="${product.title}" /></a></div>
			<c:if test="${not empty product.primaryCreators}">
			<div class="author">by <c:out value="${product.primaryCreators}" /></div>
			</c:if>
			<c:choose>
			<c:when test="${product.licenceFeeInDollars.unscaledValue() gt 0}">
			<div class="price"><pr:price showSymbol="true" showDecimal="true" amount="${product.licenceFeeInDollars}" displayCurrency="USD" /></div>
			</c:when>
			<c:otherwise>
			<div class="price">Unknown</div>
			</c:otherwise>
			</c:choose>
		</div>
	</div>
</c:forEach>
</div>
</c:if>