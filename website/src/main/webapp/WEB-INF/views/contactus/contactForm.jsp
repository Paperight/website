<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1><pr:snippet name="heading" group="contactUsForm" defaultValue="Contact Us"/></h1>
<div class="description"><pr:snippet name="description" group="contactUsForm" defaultValue="Send us a message here. We'll get back to you as soon as possible."/></div>
<div class="form">
	<form:form commandName="contactMessage">
		<dl>
			<dt>
				<span><pr:snippet name="name" group="contactUsForm"	defaultValue="Name" /></span> <br />
				<span class="note"><pr:snippet name="nameNote" group="contactUsForm" defaultValue="" /></span>
			</dt>
			<dd>
				<form:input path="name" cssClass="required" />
				<form:errors path="name" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="email" group="contactUsForm" defaultValue="Email address" /></span> <br />
				<span class="note"><pr:snippet name="emailNote" group="contactUsForm" defaultValue="" /></span>
			</dt>
			<dd>
				<form:input path="email" cssClass="required" />
				<form:errors path="email" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="subject" group="contactUsForm"	defaultValue="Subject" /></span> <br />
				<span class="note"><pr:snippet name="subjectNote" group="contactUsForm" defaultValue="" /></span>
			</dt>
			<dd>
				<form:input path="subject" cssClass="required" />
				<form:errors path="subject" cssClass="error" element="label" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="message" group="contactUsForm"	defaultValue="Your message" /></span> <br />
				<span class="note"><pr:snippet name="messageNote" group="contactUsForm" defaultValue="" /></span>
			</dt>
			<dd>
				<form:textarea path="message" cssClass="required"></form:textarea>
				<form:errors path="message" cssClass="error" element="label" />
			</dd>
		</dl>

		<input type="submit" class="btn-send" value="<pr:snippet name="submitButton" group="contactUsForm" defaultValue="Send message"/>" />
	</form:form>
</div>