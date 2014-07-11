<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="latest-transations">
	<table class="grid theme-b">
		<tr class="header">
			<td><pr:snippet name="grid-product-heading" group="publisher-earnings" defaultValue="Product"/></td>
			<td class="center-column"><pr:snippet name="grid-date-heading" group="publisher-earnings" defaultValue="Date"/></td>
			<td class="center-column"><pr:snippet name="grid-layout-heading" group="publisher-earnings" defaultValue="Format"/></td>
			<td class="center-column"><pr:snippet name="grid-amount-heading" group="publisher-earnings" defaultValue="Amount"/></td>
			<td class="center-column"><pr:snippet name="grid-status-heading" group="publisher-earnings" defaultValue="Status"/></td>
			<td class="right-column">Invoice</td>
		</tr>
		<c:forEach items="${publisherEarnings}" var="publisherEarning">
		<tr>
			<td class="left-column left-content-column">
				<div class="product-ui-tn-label">
					<a href="${ctx}/product/${publisherEarning.licence.product.id}">
						<span class="img"><pr:image product="${publisherEarning.licence.product}" width="60" height="60" /></span>
						<span class="label"><c:out value="${publisherEarning.licence.product.title}" /></span>
					</a>
				</div>
			</td>
			<td class="center-column"><joda:format value="${publisherEarning.createdDate}" style="M-" /></td>
			<td class="center-column">${publisherEarning.licence.pageLayout.displayName}</td>
			<td class="center-column"><pr:price amount="${publisherEarning.amountInCurrency}" currency="${publisherEarning.currencyCode}" displayCurrency="${publisherEarning.currencyCode}" /></td>
			<td class="center-column" style="text-transform: capitalize">${fn:toLowerCase(publisherEarning.status)}</td>
			<td class="right-column">
				<c:choose>
					<c:when test="${publisherEarning.invoiceState eq 'NEW'}">
						<a class="btn-earning-invoice" href="${publisherEarning.id}" title="<pr:snippet name="link-download-invoice" group="publisher-earnings" escapeJavascript="true" defaultValue="Download invoice"/>" style="float:left;"><span>${publisherEarning.licence.id}</span></a>
					</c:when>
					<c:when test="${publisherEarning.invoiceState eq 'DOWNLOADED'}">
						<a class="btn-earning-invoice" href="${publisherEarning.id}" title="<pr:snippet name="link-redownload-invoice" group="publisher-earnings" escapeJavascript="true" defaultValue="Redownload invoice"/>" style="float:left;"><span>${publisherEarning.licence.id}</span></a>
						<a class="btn-earning-invoice" href="${publisherEarning.id}" title="<pr:snippet name="link-redownload-invoice" group="publisher-earnings" escapeJavascript="true" defaultValue="Redownload invoice"/>"><span class="ui-icon ui-icon-refresh"></span></a>
					</c:when>
				</c:choose>
			</td>
		</tr>
		</c:forEach>
	</table>
	<div id="link-panel">
		<a class="view-all" href="${ctx}/account/publisher-earnings"><pr:snippet name="view-all-link" group="publisher-earnings" defaultValue="view all"/></a>
	</div>
</div>

<script type="text/javascript">

$(document).ready(function(){
		
	$('.btn-earning-invoice').on('click', function(){
		var button = $(this);
		var publisherEarningId = button.attr('href');
		paperight.dialog('<p class="message"><pr:snippet name="dialog-licence-invoice-message" group="publisher-earnings" escapeJavascript="true" defaultValue="Your invoice will download shortly. You may close this window at any time."/></p>', {
			title: "<pr:snippet name="dialog-licence-invoice-title" group="publisher-earnings" escapeJavascript="true" defaultValue="Invoice"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-licence-invoice-button-ok" group="publisher-earnings" escapeJavascript="true" defaultValue="OK"/>": function(){ 
					button.find('span').removeClass('ui-icon-link').addClass('ui-icon-refresh');
					$(this).dialog("close"); 
				}
			}
		});
		paperight.publisherEarningInvoice(publisherEarningId);
		return false;
	});
	
});

</script>
