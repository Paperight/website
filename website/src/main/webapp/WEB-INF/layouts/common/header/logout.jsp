<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="isAuthenticated()">
<t:importAttribute name="user" />
<t:importAttribute name="actingUser" />

<sec:authorize access="!principal.impersonatingUser">
<div id="logout-panel">
	<div id="welcome-message"><pr:snippet name="welcome-message-pre" group="loggedInHeader" defaultValue="Welcome back,"/>&nbsp;<span title="<sec:authentication property="principal.username"/>"><sec:authentication property="principal.user.fullName"/></span><pr:snippet name="welcome-message-post" group="loggedInHeader" defaultValue="."/> 
	&nbsp; <a href="${ctx}/dashboard"><pr:snippet name="dashboardLink" group="loggedInHeader" defaultValue="Dashboard"/></a><span> | </span><a href="${ctx}/account"><pr:snippet name="profileLink" group="loggedInHeader" defaultValue="Profile"/></a><span> | </span><a href="${ctx}/logout"><pr:snippet name="logOutLink" group="loggedInHeader" defaultValue="Log out"/></a><sec:authorize access="hasRole('ROLE_ADMIN') and hasPermission(#user, 'IMPERSONATE_USER')"><span> | </span><a href="${ctx}/impersonate"><pr:snippet name="impersonateUserLink" group="loggedInHeader" defaultValue="Log in as a different user"/></a></sec:authorize></div>
	<sec:authorize access="hasRole('ROLE_OUTLET')">
	<div id="topup-panel">
		<div class="inner">
			<div id="details">
				<div id="credits"><pr:snippet name="credits-pre" group="topupPanel" defaultValue="You have"/>&nbsp;<em class="credits"><pr:price showDecimal="true" showSymbol="false" amount="${user.company.credits}" displayCurrency="USD" /></em>&nbsp;<pr:snippet name="credits-post" group="topupPanel" defaultValue="credits"/></div>
				<div id="value"><pr:snippet name="approx-currency-pre" group="topupPanel" defaultValue="( That's about"/>&nbsp;<em class="credits-home-currency"><pr:price amount="${user.company.credits}" displayCurrency="${user.company.currency}" /></em>&nbsp;<pr:snippet name="approx-currency-post" group="topupPanel" defaultValue=")"/></div>
			</div>
			<a id="btn-topup" class="button" href="${ctx}/account/topup"><pr:snippet name="topupButton" group="topupPanel" defaultValue="Top up"/></a>
		</div>
	</div>
	</sec:authorize>
</div>
</sec:authorize>

<sec:authorize access="principal.impersonatingUser">
<div id="logout-panel">
	<div id="welcome-message">
		<pr:snippet name="welcome-message-pre" group="loggedInHeader" defaultValue="Welcome back,"/>&nbsp;<span title="<sec:authentication property="principal.username"/>"><sec:authentication property="principal.user.fullName"/></span><pr:snippet name="welcome-message-post" group="loggedInHeader" defaultValue="."/> 
		&nbsp; <a href="${ctx}/dashboard"><pr:snippet name="dashboardLink" group="loggedInHeader" defaultValue="Dashboard"/></a>
		<span> | </span>
		<a href="${ctx}/account"><pr:snippet name="profileLink" group="loggedInHeader" defaultValue="Profile"/></a>
		<span> | </span>
		<a href="${ctx}/logout"><pr:snippet name="logOutLink" group="loggedInHeader" defaultValue="Log out"/></a>
		<span> | </span>
		<a href="${ctx}/impersonate/logout"><pr:snippet name="logOutImpersonateLink" group="loggedInHeader" defaultValue="Log out of"/>&nbsp;<sec:authentication property="principal.actingUser.fullName"/></a>
	</div>
	<sec:authorize access="hasRole('ROLE_OUTLET')">
	<div id="logout-panel-impersonate">
		<div id="impersonate-message"><pr:snippet name="impersonateWarningMessage" group="loggedInHeader" defaultValue="You are logged in as someone else, please make sure you are logged into the correct account."/></div>
		<div id="topup-panel" class="warn">
			<div class="inner">
				<div id="details">
					<div id="credits"><pr:snippet name="credits-impersonate-name-pre" group="topupPanel" defaultValue=""/>&nbsp;<sec:authentication property="principal.actingUser.firstName"/>&nbsp;<sec:authentication property="principal.actingUser.lastName"/>&nbsp;<pr:snippet name="credits-impersonate-name-post" group="topupPanel" defaultValue="has"/>&nbsp;<pr:snippet name="credits-impersonate-pre" group="topupPanel" defaultValue=""/><em class="credits"><pr:price showDecimal="true" showSymbol="false" amount="${actingUser.company.credits}" displayCurrency="USD" /></em><pr:snippet name="credits-impersonate-post" group="topupPanel" defaultValue="credits"/></div>
					<div id="value"><pr:snippet name="approx-currency-pre" group="topupPanel" defaultValue="( That's about"/>&nbsp;<em class="credits-home-currency"><pr:price amount="${actingUser.company.credits}" displayCurrency="${actingUser.company.currency}" /></em><pr:snippet name="approx-currency-post" group="topupPanel" defaultValue=")"/></div>
				</div>
				<a id="btn-topup" class="button" href="${ctx}/account/topup"><pr:snippet name="topupButton" group="topupPanel" defaultValue="Top up"/></a>
			</div>
		</div>
	</div>
	</sec:authorize>
</div>
</sec:authorize>

<script type="text/javascript">
paperight.bind('update', function(){
	$('#logout-panel .credits').html(accounting.formatMoney(paperight.credits, "")).effect("highlight", {}, 3000);
	$('#logout-panel .credits-home-currency').html(accounting.formatMoney(paperight.credits * accounting.settings.currency.rate)).effect("highlight", {}, 3000);
});
</script>
</sec:authorize>