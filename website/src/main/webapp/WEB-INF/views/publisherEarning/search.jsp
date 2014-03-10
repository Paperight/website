<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<div class="form ui-helper-clearfix" style="padding: 0 0 30px">
	<form:form id="publisher-earning-search-form" method="get" action="${ctx}/publisher-earnings/search" modelAttribute="publisherEarningSearch" >
		<div id="publisher-earning-search-form-panel">
			<div id="field">
				<label for="Status"><pr:snippet name="status-label" group="publisher-earning-search-panel" defaultValue="Status"/></label> 
				<form:select path="status" cssStyle="text-transform: capitalize;" class="search-input">
					<form:option value="">Any</form:option>
					<form:options itemLabel="displayName" />
				</form:select>
				<form:errors path="status" cssClass="error" element="label" />
			</div>
			<div id="field">
				<label for="fromDate"><pr:snippet name="from-date-label" group="publisher-earning-search-panel" defaultValue="From date"/></label> 
				<form:input type="" path="fromDate" class="date search-input"/>
			</div>
			<div id="field">
				<label for="toDate"><pr:snippet name="to-date-label" group="publisher-earning-search-panel" defaultValue="To date"/></label> 
				<form:input type="" path="toDate" class="date search-input" />
			</div>
		</div>
		<input type="submit" class="button-big" value="<pr:snippet name="search-button" group="publisher-earning-search-panel" defaultValue="Filter earnings"/>" />
	</form:form>
</div>