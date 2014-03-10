<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="hasRole('ROLE_PUBLISHER')">
<h1><pr:snippet name="latest-earnings-heading" group="publisher-earnings" defaultValue="Latest earnings"/></h1>
<%@ include file="/WEB-INF/views/publisherEarning/searchResult.jsp" %>
</sec:authorize>