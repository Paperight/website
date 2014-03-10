<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="content">
	<div class="wrapper">
		<div class="columns columns-two columns-two-left">
		
			<t:useAttribute id="headline" name="headline" ignore="true" />
			<c:if test="${not empty headline}"><h1>${headline}</h1></c:if>
			
			<div class="column-one column">
				<t:useAttribute id="list" name="columnOne" classname="java.util.List" />
				<c:forEach var="item" items="${list}">
					<t:insertAttribute value="${item}" flush="true" />
				</c:forEach>
			</div>
			<div class="column-two column">
				<t:useAttribute id="list" name="columnTwo" classname="java.util.List" />
				<c:forEach var="item" items="${list}">
					<t:insertAttribute value="${item}" flush="true" />
				</c:forEach>
			</div>
		</div>
	</div>
</div>