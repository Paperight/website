<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>

<c:if test="${not empty param.error and param.error eq 'login_failed'}">
<div class="notification error">
	<p><pr:snippet name="login-failed" group="notifications" defaultValue="Your login attempt was not successful, try again."/></p>
</div>
</c:if>
<c:if test="${not empty param.error and param.error eq 'disabled'}">
<div class="notification error">
	<p><pr:snippet name="account-disabled" group="notifications" defaultValue="Sorry, your account is disabled. Please &lt;a href=&quot;/contactus&quot;&gt;contact us&lt;/a&gt; to find out about reactivating your account."/></p>
</div>
</c:if>
<c:if test="${not empty param.error and param.error eq 'closed'}">
<div class="notification error">
	<p><pr:snippet name="account-closed" group="notifications" defaultValue="Sorry, your account is closed. &lt;a href=&quot;/reopen/request&quot;&gt;Click here&lt;/a&gt; to reopen your account."/></p>
</div>
</c:if>

<h1><pr:snippet name="alreadyRegisteredHeading" group="loginForm" defaultValue="Already registered?"/></h1>
<div class="form">
	<form name="login-form" id="login-form" action="${ctx}/login" method="post">
		<input type="hidden" name="redirectUrl" />
		<dl>
			<dt><pr:snippet name="email" group="loginForm" defaultValue="Email address"/></dt>
			<dd><input type="text" name="username" value="" class="required" /></dd>
			<dt><pr:snippet name="password" group="loginForm" defaultValue="Password"/></dt>
			<dd><input type="password" name="password" value="" class="required" /></dd>
			<dd class="checkbox">
				<input type="checkbox" id="rememberme" name="_spring_security_remember_me" />
				<label for="rememberme"><pr:snippet name="rememberMe" group="loginForm" defaultValue="Remember me"/></label> | <a id="forgot-password" href="${ctx}/forgotpassword"><pr:snippet name="forgotPasswordLink" group="loginForm" defaultValue="Forgot your password?"/></a>
			</dd>
		</dl>
		<input id="login" type="submit" class="button" value="<pr:snippet name="loginButton" group="loginForm" defaultValue="Log in"/>" />
	</form>
</div>
<script type="text/javascript">
	var redirectUrl = $.url().param('redirectUrl');
	$('input[name="redirectUrl"]').attr('value', redirectUrl);
	$('#forgot-password').attr('href', $('#forgot-password').attr('href') + '?redirectUrl=' + encodeURIComponent(redirectUrl));
</script>