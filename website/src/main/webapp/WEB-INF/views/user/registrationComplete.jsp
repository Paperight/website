<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="registration-complete-wrapper">
<pr:snippet name="text" group="registration-complete" multiline="true" defaultValue="&lt;h1&gt;Registration successful&lt;/h1&gt;
&lt;h3&gt;Thank you, [$userFirstName$], your registration is almost complete&lt;/h3&gt;
&lt;div class=&quot;form&quot;&gt;
	To activate your account, you must still confirm your email address.&lt;br /&gt;&lt;br /&gt;
	&lt;ul&gt;
		&lt;li&gt;You will receive an email from Paperight with an account-activation link.&lt;/li&gt;
		&lt;li&gt;Please check your email now, including your spam folders.&lt;/li&gt;
		&lt;li&gt;You must click the link in that email to confirm your email address.&lt;/li&gt;
	&lt;/ul&gt;
	&lt;p&gt;
		If you do not confirm your email by clicking the activation link, you will not be able to download documents.&lt;br /&gt;&lt;br /&gt;
		&lt;a href=&quot;/dashboard&quot;&gt;Click here to go to your dashboard&lt;/a&gt;
	&lt;/p&gt;
&lt;/div&gt;
" />
</div>
<script type="text/javascript">

$(document).ready(function(){
	var userFirstName = "${user.firstName}";
	
	$("#registration-complete-wrapper *").replaceText( "[$userFirstName$]", userFirstName ); 
});
</script>