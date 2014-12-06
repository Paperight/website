<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:set var="themeDisabled" value="false" />
<c:if test="${not empty poster.id}" >
	<c:set var="themeDisabled" value="true" />
</c:if>
    
<h1>Update poster</h1>
<div class="form">
	<form:form action="${ctx}/poster/update" method="POST" modelAttribute="poster" >
	
		<sessionConversation:insertSessionConversationId attributeName="poster"/>
	
		<div style="width:40%; float:left; margin-right:30px;">
			<dl>
				<dt>Theme</dt>
 				<dd><form:select path="theme.id" items="${themes}" itemLabel="name" itemValue="id" cssClass="required" disabled="${themeDisabled}" /></dd>
			</dl>
			<dl>
				<dt>Title</dt>
				<dd><form:input path="title" cssClass="required" />
			</dl>
			<dl>
				<dt>Image URL</dt>
				<dd><form:input path="imageUrl" cssClass="required" />
				<form:errors path="imageUrl" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Link URL</dt>
				<dd><form:input path="linkUrl" cssClass="required" />
				<form:errors path="linkUrl" cssClass="error" element="label" /></dd>
			</dl>
			
		</div>
		
		<div style="clear:both; width:100%;">
			<input type="submit" class="btn btn-paperight" value="Update" />
		</div>
	</form:form>
</div>