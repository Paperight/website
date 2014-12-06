<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:set var="id"><t:insertAttribute name="id" ignore="true" /></c:set>
<c:if test="${empty title}"><c:set var="title"><t:insertAttribute name="title" ignore="true" /></c:set></c:if>
<spring:message code="application_name" text="" var="applicationName"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="google-site-verification" content="3iGxUqEm-hqess2cmA6wfzN6WjIDGAq1lTs7FUDa7so" />
<title>${applicationName}: ${title}</title>
<t:useAttribute id="links" name="links" classname="java.util.List" ignore="true" />
<c:forEach var="href" items="${links}">
<c:choose>
<c:when test="${fn:substring(href,0,2) eq '//'}">
<link href="<c:out value="${href}" />" media="screen" rel="stylesheet" type="text/css" />
</c:when>
<c:otherwise>
<link href="<c:url value="${href}" />" media="screen" rel="stylesheet" type="text/css" />
</c:otherwise>
</c:choose>
</c:forEach>
<style type="text/css">
	<c:set var="themeName"><spring:theme code="name"/></c:set>
	<pr:snippet name="${themeName}" group="themes-css" multiline="true" defaultValue=""/>
</style>
<t:useAttribute id="print" name="print" classname="java.util.List" ignore="true" />
<c:forEach var="href" items="${print}">
<c:choose>
<c:when test="${fn:substring(href,0,2) eq '//'}">
<link href="<c:out value="${href}" />" media="print" rel="stylesheet" type="text/css" />
</c:when>
<c:otherwise>
<link href="<c:url value="${href}" />" media="print" rel="stylesheet" type="text/css" />
</c:otherwise>
</c:choose>
</c:forEach>
<t:useAttribute id="scripts" name="scripts" classname="java.util.List" ignore="true" />
<c:forEach var="src" items="${scripts}">
<c:choose>
<c:when test="${fn:substring(src,0,2) eq '//'}">
<script type="text/javascript" src="<c:out value="${src}" />"></script>
</c:when>
<c:otherwise>
<script type="text/javascript" src="<c:url value="${src}" />"></script>
</c:otherwise>
</c:choose>
</c:forEach>

</head>
<body class="${id}">
<t:insertAttribute name="header" ignore="true" />
<t:insertAttribute name="body" ignore="true" /> 
<t:insertAttribute name="deck" ignore="true" /> 
<t:insertAttribute name="footer" ignore="true" />
<t:insertAttribute name="notifications" ignore="true" />
<script  type="text/javascript" >

$.validator.messages.required = "<pr:snippet name="required" group="validation" escapeJavascript="true" defaultValue="This field is required"/>";

</script>
</body>
</html>