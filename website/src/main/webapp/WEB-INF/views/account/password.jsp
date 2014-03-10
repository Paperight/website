<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h3><pr:snippet name="heading" group="change-password-form" defaultValue="Change your password"/></h3>
<div class="form">
	<form:form commandName="changePassword" id="passwordform" method="post">
		<dl>
			<dt>
				<span><pr:snippet name="old-password" group="change-password-form" defaultValue="Old password"/></span>
				<br /><span class="note"><pr:snippet name="old-password-note" group="change-password-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:password path="oldPassword" cssClass="required" />
				<form:errors path="oldPassword" cssClass="error" element="label" />
			</dd>
			<dt>
				<span><pr:snippet name="new-password" group="change-password-form" defaultValue="New password"/></span>
				<br /><span class="note"><pr:snippet name="new-password-note" group="change-password-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:password path="newPassword" cssClass="required password" />
				<form:errors path="newPassword" cssClass="error" element="label" />
			</dd>
			<dt>
				<span><pr:snippet name="confirm-new-password" group="change-password-form" defaultValue="Confirm new password"/></span>
				<br /><span class="note"><pr:snippet name="confirm-new-password-note" group="change-password-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:password path="confirmPassword" cssClass="required" />
				<form:errors path="confirmPassword" cssClass="error" element="label" />
			</dd>
		</dl>
		<button type="submit"><pr:snippet name="button" group="change-password-form" defaultValue="Save new password"/></button>
	</form:form>
</div>
