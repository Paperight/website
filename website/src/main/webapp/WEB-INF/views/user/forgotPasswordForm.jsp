<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${not empty error}">
<div class="notification error">
	<p>${error}</p>
</div>
</c:if>
<h1><pr:snippet name="heading" group="forgotPasswordForm" defaultValue="Forgot your Password?"/></h1>
<div class="description"><pr:snippet name="description" group="forgotPasswordForm" defaultValue="Please fill in your email address and we'll send you a link to reset your password."/></div>
<div class="form">
	<form id="forgotpassword-form" method="post">
		
		<dl>
			<dt><pr:snippet name="email" group="forgotPasswordForm" defaultValue="Email Address"/></dt>
			<dd><input type="text" name="email" value="" class="required email" /></dd>
		</dl>
		<button type="submit"><pr:snippet name="button" group="forgotPasswordForm" defaultValue="Recover"/></button>
	</form>
</div>
