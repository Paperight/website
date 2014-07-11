<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${paginator.pageCount > 1}">
<t:insertDefinition name="pagination">
	<t:putAttribute name="id" value="top-pagination" type="string" />
	<t:putAttribute name="paginator" value="${paginator}" />
</t:insertDefinition>
</c:if>
<table class="grid">
	<tr class="header">
		<td colspan="4"></td>
		<td class="center-column"><pr:snippet name="grid-credits-heading" group="product-detail-licences" defaultValue="Credits"/></td>
		<td class="center-column" colspan="3"><pr:snippet name="grid-charged-to-customers-heading" group="product-detail-licences" defaultValue="Charged to customers"/></td>
		<td class="right-column" colspan="2"></td>
	</tr>
	<tr class="sub-header">
		<td class="left-column"><pr:snippet name="grid-copies-heading" group="product-detail-licences" defaultValue="Copies"/></td>
		<td class="left-column"><pr:snippet name="grid-customer-heading" group="product-detail-licences" defaultValue="Customer"/></td>
		<td class="left-column"><pr:snippet name="grid-date-heading" group="product-detail-licences" defaultValue="Date"/></td>
		<td class="left-column"><pr:snippet name="grid-layout-heading" group="product-detail-licences" defaultValue="Layout"/></td>
		<td class="center-column"><pr:snippet name="grid-credits-heading" group="product-detail-licences" defaultValue="Total&lt;br /&gt;credits used"/></td>
		<td class="center-column"><pr:snippet name="grid-licence-fees-heading" group="product-detail-licences" defaultValue="Total&lt;br /&gt;licence fees"/></td>
		<td class="center-column"><pr:snippet name="grid-print-costs-heading" group="product-detail-licences" defaultValue="Total&lt;br /&gt;print costs"/></td>
		<td class="center-column"><pr:snippet name="grid-total-charged-heading" group="product-detail-licences" defaultValue="Total&lt;br /&gt;you charged"/></td>
		<td class="right-column"><pr:snippet name="grid-document-status-heading" group="product-detail-licences" defaultValue="Document status"/></td>
		<td class="right-column"><pr:snippet name="grid-document-invoice-heading" group="product-detail-licences" defaultValue="Invoice"/></td>
	</tr>
	<c:forEach items="${licences}" var="licence">
	<tr>
		<c:set var="licenceStatus" value="${licence.status}" />
		<c:set var="pageLayout" value="${licence.pageLayout}" />
		<c:set var="costInCredits" value="${licence.costInCredits}" />
		<c:set var="costInCurrency" value="${licence.costInCurrency}" />
		<c:set var="outletCharge" value="${licence.outletCharge}" />
		<c:if test="${licenceStatus eq 'CANCELLED'}">
			<c:set var="costInCredits" value="0" />
			<c:set var="costInCurrency" value="0" />
			<c:set var="outletCharge" value="0" />
		</c:if>
		<td class="left-column"><c:out value="${licence.numberOfCopies}" /></td>
		<td class="left-column"><span title="<c:out value="${licence.customerFullName}" />"><c:out value="${fn:substring(licence.customerFullName, 0, 25)}" /><c:if test="${fn:length(licence.customerFullName) > 25}">&nbsp;...</c:if></span></td>
		<td class="left-column"><joda:format value="${licence.transactionDate}" style="M-" /></td>
		<td class="left-column"><c:out value="${licence.pageLayout.displayName}" /></td>
		<td class="center-column"><fmt:formatNumber value="${costInCredits}" maxFractionDigits="2" /></td>
		<td class="center-column"><pr:price amount="${costInCurrency}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}"/></td>
		<td class="center-column"><pr:price amount="${outletCharge}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}"/></td>
		<td class="center-column"><pr:price amount="${costInCurrency + outletCharge}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}"/></td>
		<td class="right-column">
		<c:if test="${(licenceStatus eq 'NEW') || (licenceStatus eq 'GENERATED')}">
			<sec:authorize access="principal.user.verified">
			<c:if test="${(pageLayout ne 'PHOTOCOPY')}">
			<button value="${licence.id}" class="btn-download" title="approx. ${licence.fileSize} MB">
				<span class="ui-icon ui-icon-circle-arrow-s"></span>
				<span><pr:snippet name="download-button" group="product-detail-licences" defaultValue="Download"/></span>
			</button>
			<a href="${licence.id}" class="btn-cancel">
				<span><pr:snippet name="cancel-link" group="product-detail-licences" defaultValue="Cancel"/></span>
				<span class="ui-icon ui-icon-trash"></span>
			</a>
			</c:if>
			<c:if test="${(pageLayout eq 'PHOTOCOPY')}">
			<button value="${licence.id}" class="btn-print-licence">
                <span class="ui-icon ui-icon-circle-arrow-s"></span>
                <span><pr:snippet name="print-licence-button" group="product-detail-licences" defaultValue="Print licence"/></span>
            </button>
			</c:if>
			</sec:authorize>
			<sec:authorize access="!principal.user.verified">
			<button class="btn-activate">
				<span><pr:snippet name="confirm-email-button" group="product-detail-licences" defaultValue="Confirm email address"/></span>
			</button>
			</sec:authorize>
		</c:if>
		<c:if test="${licenceStatus eq 'DOWNLOADED'}">
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		<a href="${licence.id}" class="btn-reset">
			<span><pr:snippet name="reset-link" group="product-detail-licences" defaultValue="Reset"/></span>
			<span class="ui-icon ui-icon-refresh"></span>
		</a>	
		</sec:authorize>
		<pr:snippet name="downloaded" group="product-detail-licences" defaultValue="Downloaded"/>&nbsp;<a href="${ctx}/contactus"><pr:snippet name="feedback-link" group="product-detail-licences" defaultValue="(feedback)"/></a>
		</c:if>
		<c:if test="${licenceStatus eq 'CANCELLED'}"><pr:snippet name="cancelled" group="product-detail-licences" defaultValue="Cancelled"/>&nbsp;<a href="${ctx}/contactus"><pr:snippet name="feedback-link" group="product-detail-licences" defaultValue="(feedback)"/></a></c:if>
		</td>
		<td class="right-column">
				<c:if test="${licence.status eq 'DOWNLOADED'}">
					<c:if test="${licence.invoiceState eq 'NEW'}">
					<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-download-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Download invoice"/>">${licence.id}</a>
					</c:if>
					<c:if test="${licence.invoiceState eq 'DOWNLOADED'}">
					<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-redownload-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Redownload invoice"/>" style="float:left;"><span>${licence.id}</span></a>
					<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-redownload-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Redownload invoice"/>"><span class="ui-icon ui-icon-refresh"></span></a>
					</c:if>
				</c:if>
			</td>
	</tr>
	</c:forEach>
	<c:if test="${paginator.itemCount eq 0}">
	<tr>
		<td class="left-column" colspan="8"><pr:snippet name="no-licences" group="product-detail-licences" defaultValue="No licences"/></td>
	</tr>
	</c:if>
</table>
<c:if test="${paginator.pageCount > 1}">
<div class="list-tools">
<t:insertDefinition name="pagination">
	<t:putAttribute name="paginator" value="${paginator}" />
</t:insertDefinition>
</div>
</c:if>

<script type="text/javascript">

$(document).ready(function(){
		
	$('.btn-licence-invoice').on('click', function(){
		var button = $(this);
		var licenceId = button.attr('href');
		paperight.dialog('<p class="message"><pr:snippet name="dialog-licence-invoice-message" group="product-detail-licences" escapeJavascript="true" defaultValue="Your invoice will download shortly. You may close this window at any time."/></p>', {
			title: "<pr:snippet name="dialog-licence-invoice-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Invoice"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-licence-invoice-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>": function(){ 
					button.find('span').removeClass('ui-icon-link').addClass('ui-icon-refresh');
					$(this).dialog("close"); 
				}
			}
		});
		paperight.licenceInvoice(licenceId);
		return false;
	});
	
});

</script>
