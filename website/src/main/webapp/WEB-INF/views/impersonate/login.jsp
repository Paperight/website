<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>

<c:if test="${not empty param.error and param.error eq 'login_failed'}">
<div class="notification error">
	<p>Your login attempt was not successful, try again.</p>
</div>
</c:if>

<h3>Impersonate User</h3>
<div class="form">
	<form name="login-form" id="login-form" action="${ctx}/impersonate" method="post">
		<dl>
			<dt>Email address</dt>
			<dd><input type="text" name="username" value="" class="required" /></dd>
		</dl>
		<input type="submit" class="button" value="Impersonate" />
	</form>
</div>
