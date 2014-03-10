<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${not empty error}">
<div class="notification error">
	<p>${error}</p>
</div>
</c:if>
<h1>Reopen your account?</h1>
<div class="description">Please fill in your email address and we'll send you a link to reopen your account.</div>
<div class="form">
	<form id="reopen-account-form" method="post">		
		<dl>
			<dt>Email Address</dt>
			<dd><input type="text" name="email" value="" class="required email" /></dd>
		</dl>
		<button type="submit">Reopen</button>
	</form>
</div>