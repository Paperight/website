<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="latest-transations">
	<table class="grid theme-b">
		<tr class="header">
			<td colspan="4"></td>
			<td class="center-column"><pr:snippet name="grid-credits-heading" group="licence-search" defaultValue="Credits"/></td>
			<td class="center-column" colspan="4"><pr:snippet name="grid-charged-to-customers-heading" group="licence-search" defaultValue="Charged to customers"/></td>
		</tr>
		<tr class="sub-header">
			<td class="left-column"><pr:snippet name="grid-product-heading" group="licence-search" defaultValue="Product"/></td>
			<td class="left-column"><pr:snippet name="grid-total-copies-heading" group="licence-search" defaultValue="Total&lt;br /&gt;Copies"/></td>
			<td class="left-column"><pr:snippet name="grid-date-heading" group="licence-search" defaultValue="Date"/></td>
			<td class="left-column"><pr:snippet name="grid-layout-heading" group="licence-search" defaultValue="Format"/></td>
			<td class="center-column"><pr:snippet name="grid-total-credits-heading" group="licence-search" defaultValue="Total&lt;br /&gt;credits used"/></td>
			<td class="center-column"><pr:snippet name="grid-total-licence-fees-heading" group="licence-search" defaultValue="Total&lt;br /&gt;licence fees"/></td>
			<td class="center-column"><pr:snippet name="grid-total-print-costs-heading" group="licence-search" defaultValue="Total&lt;br /&gt;print costs"/></td>
			<td class="center-column"><pr:snippet name="grid-total-charged-heading" group="licence-search" defaultValue="Total&lt;br /&gt;you charged"/></td>
			<td class="right-column">Invoice</td>
		</tr>
		<c:forEach items="${licences}" var="licence">
		<tr>
			<c:set var="licenceStatus" value="${licence.status}" />
			<c:set var="costInCredits" value="${licence.costInCredits}" />
			<c:set var="costInCurrency" value="${licence.costInCurrency}" />
			<c:set var="outletCharge" value="${licence.outletCharge}" />
			<c:if test="${licenceStatus eq 'CANCELLED'}">
				<c:set var="costInCredits" value="0" />
				<c:set var="costInCurrency" value="0" />
				<c:set var="outletCharge" value="0" />
			</c:if>
			<td class="left-column left-content-column">
				<div class="product-ui-tn-label">
					<a href="${ctx}/product/${licence.product.id}">
						<span class="img"><pr:image product="${licence.product}" width="60" height="60" /></span>
						<span class="label"><c:out value="${licence.product.title}" /></span>
					</a>
					<span class="customer">&nbsp;</span>
					<span class="customer">Customer:  <c:out value="${licence.customerFullName}" /></span>
					<c:if test="${not empty licence.customerPhoneNumber}">
					<span class="customer">Phone number:  <c:out value="${licence.customerPhoneNumber}" /></span>
					</c:if>
				</div>
				
			</td>
			<td class="left-column"><c:out value="${licence.numberOfCopies}" /></td>
			<td class="left-column"><joda:format value="${licence.transactionDate}" style="M-" /></td>
			<td class="left-column"><c:out value="${licence.pageLayout.displayName}" /></td>
			<td class="center-column"><fmt:formatNumber value="${costInCredits}" maxFractionDigits="2" /></td>
			<td class="center-column"><pr:price amount="${costInCurrency}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}" /></td>
			<td class="center-column"><pr:price amount="${outletCharge}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}" /></td>
			<td class="center-column"><pr:price amount="${costInCurrency + outletCharge}" currency="${licence.currencyCode}" displayCurrency="${licence.currencyCode}" /></td>
			<td class="right-column">
				<c:if test="${licence.status eq 'DOWNLOADED'}">
					<c:if test="${licence.invoiceState eq 'DOWNLOADED'}">
						<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-redownload-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Redownload invoice"/>" style="float:left;"><span>${licence.id}</span></a>
						<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-redownload-invoice" group="licence-search" escapeJavascript="true" defaultValue="Redownload invoice"/>"><span class="ui-icon ui-icon-refresh"></span></a>
					</c:if>
					<c:if test="${licence.invoiceState eq 'NEW'}">
						<a class="btn-licence-invoice" href="${licence.id}" title="<pr:snippet name="link-download-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Download invoice"/>" style="float:left;"><span>${licence.id}</span></a>
					</c:if>	
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
	<c:if test="${not empty licences}">
	<div id="link-panel">
		<a class="view-all" href="${ctx}/account/licences"><pr:snippet name="view-all-link" group="licence-search" defaultValue="view all"/></a>
	</div>
	</c:if>
</div>

<script type="text/javascript">

$(document).ready(function(){
		
	$('.btn-licence-invoice').on('click', function(){
		var button = $(this);
		var licenceId = button.attr('href');
		paperight.dialog('<p class="message"><pr:snippet name="dialog-licence-invoice-message" group="licence-search" escapeJavascript="true" defaultValue="Your invoice will download shortly. You may close this window at any time."/></p>', {
			title: "<pr:snippet name="dialog-licence-invoice-title" group="licence-search" escapeJavascript="true" defaultValue="Invoice"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-licence-invoice-button-ok" group="licence-search" escapeJavascript="true" defaultValue="OK"/>": function(){ 
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
