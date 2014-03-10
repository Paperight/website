<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div class="form ui-helper-clearfix" style="padding: 0 0 30px">
	<form id="licence-search-form" method="post" action="${ctx}/licences/search">
		<div id="licence-search-form-panel">
			<div id="field">
				<label for="customerFirstName"><pr:snippet name="first-name-label" group="licence-search-form" defaultValue="Customer first name"/></label>
				<input id="customerFirstName" name="customerFirstName" class="required_group search-input"/>
			</div>
			<div id="field">
				<label for="customerLastName"><pr:snippet name="last-name-label" group="licence-search-form" defaultValue="Customer last name"/></label>
				<input id="customerLastName" name="customerLastName" class="required_group search-input" />
			</div>
			<div id="field">
				<label for="customerPhoneNumber"><pr:snippet name="phone-number-label" group="licence-search-form" defaultValue="Customer phone number"/></label>
				<input id="customerPhoneNumber" name="customerPhoneNumber" class="required_group search-input" />
			</div>
		</div>
		<input type="submit" class="button-big" value="<pr:snippet name="button" group="licence-search-form" defaultValue="Search licences"/>" />
	</form>
</div>
<script type="text/javascript">
	$.validator.addClassRules("required_group", {
	    require_from_group: [1,".required_group"]
	});
</script>