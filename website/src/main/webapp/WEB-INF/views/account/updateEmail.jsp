<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h3><pr:snippet name="heading" group="update-email-form" defaultValue="Update Email"/></h3>
<div class="form">
	<form:form commandName="updateEmail" id="updateEmail" method="post">
		<dl>
			<dt>
				<span><pr:snippet name="new-email" group="update-email-form" defaultValue="New email address"/></span>
				<br /><span class="note"><pr:snippet name="new-email-note" group="update-email-form" defaultValue=""/></span>
			</dt>	
			<dd>
				<form:input path="newEmail" cssClass="email required" />
				<form:errors path="newEmail" cssClass="error" element="label" />
				<form:errors path="emailAvailable" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>	
			<dt>
				<span><pr:snippet name="confirm-new-email" group="update-email-form" defaultValue="Confirm new email address"/></span>
				<br /><span class="note"><pr:snippet name="confirm-new-email-note" group="update-email-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:input path="confirmNewEmail" cssClass="email required" />
				<form:errors path="newEmailValid" cssClass="error" element="label" />
			</dd>
		</dl>
		<input type="submit" class="button" value="<pr:snippet name="button" group="update-email-form" defaultValue="Update"/>" />
	</form:form>
</div>