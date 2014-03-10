<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<t:useAttribute id="type" name="type" ignore="true" />
<t:useAttribute id="message" name="message" ignore="true" />
<c:if test="${not empty notificationType}">
	<c:set var="type" value="${notificationType}" />
</c:if>
<c:if test="${not empty notificationMessage}">
	<c:set var="message" value="${notificationMessage}" />
</c:if>
<div class="notification ${type}"><p>${message}</p></div>