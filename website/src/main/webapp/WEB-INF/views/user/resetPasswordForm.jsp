<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h3><pr:snippet name="heading" group="reset-password-form" defaultValue="Password Reset"/></h3>
<div class="form">
	<form:form commandName="resetPassword" id="resetPassword" method="post">
		<form:hidden path="redirectUrl"/>
		<dl>
			<dt>
				<span><pr:snippet name="new-password" group="reset-password-form" defaultValue="New password"/></span>
				<br /><span class="note"><pr:snippet name="new-password-note" group="reset-password-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:password path="newPassword" cssClass="required clearfocus noplaceholder password" />
				<form:errors path="newPassword" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="confirm-new-password" group="reset-password-form" defaultValue="Confirm password"/></span>
				<br /><span class="note"><pr:snippet name="confirm-new-password-note" group="reset-password-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:password path="confirmNewPassword" cssClass="required clearfocus noplaceholder" />
				<form:errors path="newPasswordValid" cssClass="error" element="label" />
			</dd>
		</dl>
		<input type="submit" class="button" value="<pr:snippet name="button" group="reset-password-form" defaultValue="Reset"/>" />
	</form:form>
</div>
<script type="text/javascript">
	var redirectUrl = $.url().param('redirectUrl');
	if (redirectUrl != '') {
		$('input[name="redirectUrl"]').attr('value', redirectUrl);
	}
</script>