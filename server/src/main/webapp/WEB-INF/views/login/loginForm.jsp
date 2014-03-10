<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>

<c:if test="${not empty param.error and param.error eq 'login_failed'}">
<div class="notification error">
	<p>Your login attempt was not successful, try again.</p>
</div>
</c:if>
<c:if test="${not empty param.error and param.error eq 'disabled'}">
<div class="notification error">
	<p>Sorry, your account is disabled. Please <a href="${ctx}/contactus">contact us</a> to find out about reactivating your account.</p>
</div>
</c:if>

<h3>Already registered?</h3>
<div class="form">
	<form name="login-form" id="login-form" action="${ctx}/login" method="post">
		<dl>
			<dt>Email address</dt>
			<dd><input type="text" name="username" value="" class="required" /></dd>
			<dt>Password</dt>
			<dd><input type="password" name="password" value="" class="required" /></dd>
			<dd class="checkbox">
				<input type="checkbox" id="rememberme" name="_spring_security_remember_me" />
				<label for="rememberme">Remember me</label>
			</dd>
		</dl>
		<input type="submit" class="button" value="Log in" />
	</form>
</div>