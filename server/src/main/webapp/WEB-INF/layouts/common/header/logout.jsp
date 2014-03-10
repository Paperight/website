<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="isAuthenticated()">
<t:importAttribute name="user" />
<div id="logout-panel">
	<div id="welcome-message">Welcome back! <span title="<sec:authentication property="principal.username"/>"><sec:authentication property="principal.user.fullName"/></span></div>
</div>

</sec:authorize>