<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<t:useAttribute id="name" name="name" />
<div id="${name}">
	<div class="wrapper">
	<t:useAttribute id="list" name="items" classname="java.util.List" />
	<c:forEach var="item" items="${list}">
	<t:insertAttribute value="${item}" flush="true" />
	</c:forEach>
	</div>
</div>