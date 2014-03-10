<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div class="navigation">
	<ul>
		<li><a href="${ctx}/contactus"><pr:snippet name="contactUsLink" group="footer" defaultValue="Contact us"/></a></li>
		<li><a href="${ctx}/about"><pr:snippet name="aboutUsLink" group="footer" defaultValue="About us"/></a></li>
		<li><a href="${ctx}/terms/outlet"><pr:snippet name="outletTermsLink" group="footer" defaultValue="Outlet terms and conditions"/></a></li>
		<li><a href="${ctx}/terms/publisher"><pr:snippet name="publisherTermsLink" group="footer" defaultValue="Publisher terms and conditions"/></a></li>
		<li><a href="http://blog.paperight.com" target="_blank"><pr:snippet name="blogLink" group="footer" defaultValue="Read our blog"/></a></li>
		<li><a href="${ctx}/newsletter/subscribe"><pr:snippet name="subscribeNewsletterLink" group="footer" defaultValue="Subscribe to our newsletter"/></a></li>
		<li><a href="${ctx}/privacy-policy"><pr:snippet name="privacyPolicyLink" group="footer" defaultValue="Privacy policy"/></a></li>
	</ul>
</div>
