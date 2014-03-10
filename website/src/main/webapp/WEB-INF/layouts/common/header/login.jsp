<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="isAnonymous()">
<div id="logged-out-panel">
	<div class="inner">
		<div class="span span-first" id="logged-out-customers">
			<span class="title"><pr:snippet name="findOutletButtonHeading" group="loginPanel" defaultValue="Customers"/></span>
			<button class="ui-state-active"><pr:snippet name="findOutletButton" group="loginPanel" defaultValue="Find your closest outlet"/></button>
		</div>
		
		<div class="span span-last" id="logged-out-outlets">
			<span class="title"><pr:snippet name="loginButtonHeading" group="loginPanel" defaultValue="Outlets / Publishers"/></span>
			<button><pr:snippet name="loginButton" group="loginPanel" defaultValue="Business Login"/></button>
			<a href="${ctx}/register"><pr:snippet name="registerLink" group="loginPanel" defaultValue="Register your business"/></a>
		</div>
	</div>
</div>
<script>
$('#logged-out-customers button').button({icons: {primary: "icon_store_locator"}}).hover(function(){$(this).removeClass('ui-state-active');},function(){$(this).addClass('ui-state-active');}).click(function(){
	window.location.href = '${ctx}/outlets';
});
$('#logged-out-outlets button').button({icons: {primary: "icon_login"}}).click(function(){
	window.location.href = '${ctx}/login/?redirectUrl=' + encodeURIComponent(document.URL);
});
</script>
</sec:authorize>