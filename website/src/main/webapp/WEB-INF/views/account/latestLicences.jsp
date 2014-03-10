<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="hasRole('ROLE_OUTLET')">
<h1><pr:snippet name="heading" group="latest-licences" defaultValue="Latest transactions"/></h1>
<%@ include file="/WEB-INF/views/licence/searchResult.jsp" %>
</sec:authorize>