<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>User</h1>
<div class="form">
	<form:form action="${ctx}/user/update" method="POST" modelAttribute="userDto" >
		<sessionConversation:insertSessionConversationId attributeName="userDto"/>
		<div style="width:70%; float:left; margin-right:30px;">
			<dl>
				<dt>First Name</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:input path="user.firstName" cssClass="required" />
					<form:errors path="user.firstName" cssClass="error" element="label" />
					</dd>
			</dl>
			<dl>
				<dt>Last Name</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:input path="user.lastName" cssClass="required" />
					<form:errors path="user.lastName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>Email</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:input path="user.email" cssClass="required email" />
					<form:errors path="user.email" cssClass="error" element="label" />
					<form:errors path="usernameAvailable" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>Confirm email address</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:input path="confirmEmail" cssClass="email required" />
					<form:errors path="emailValid" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>Enabled</dt>
				<dd><form:checkbox path="user.enabled" />
				<form:errors path="user.enabled" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Permissions</dt>
				<dd>
					<c:forEach items="${allPermissions}" var="permission">
						<form:checkbox path="permissions" value="${permission}" label="${permission.displayName}" /><br /><br />
					</c:forEach>
				</dd>			
			</dl>
		</div>
		<div style="clear:both; width:100%;">
			<input class="btn btn-paperight" onclick="window.location.href = '${ctx}/user/search';" type="button" value="Cancel" />
			<input type="submit" class="btn btn-paperight" value="Update" />
		</div>
	</form:form>
</div>