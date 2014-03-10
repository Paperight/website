<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="isAnonymous()">
<h1><pr:snippet name="notRegisteredHeading" group="loginForm" defaultValue="Not yet registered?"/></h1>
<div class="description"><pr:snippet name="notRegisteredText" group="loginForm" defaultValue="It's free to register with Paperight, and takes less than a minute. Once registered, you can get free books and documents to print, or buy credits for commercial books and documents."/></div>
<a href="${ctx}/register" class="button"><pr:snippet name="notRegisteredButton" group="loginForm" defaultValue="Register"/></a>
</sec:authorize>