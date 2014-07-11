<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<c:if test="${paginator.pageCount > 1}">
<t:insertDefinition name="pagination">
	<t:putAttribute name="id" value="top-pagination" type="string" />
	<t:putAttribute name="paginator" value="${paginator}" />
</t:insertDefinition>
</c:if>
<table class="grid">
	<tr class="header">
		<td class="center-column"><pr:snippet name="grid-heading-date" group="product-detail-publisher-earnings" defaultValue="Date"/></td>
		<td class="center-column"><pr:snippet name="grid-heading-copies" group="product-detail-publisher-earnings" defaultValue="Copies"/></td>
		<td class="center-column"><pr:snippet name="grid-heading-layout" group="product-detail-publisher-earnings" defaultValue="Format"/></td>
		<td class="center-column"><pr:snippet name="grid-heading-amount" group="product-detail-publisher-earnings" defaultValue="Amount"/></td>
		<td class="center-column"><pr:snippet name="grid-heading-status" group="product-detail-publisher-earnings" defaultValue="Status"/></td>
		<td class="right-column"><pr:snippet name="grid-heading-invoice" group="product-detail-publisher-earnings" defaultValue="Invoice"/></td>
	</tr>
	<c:forEach items="${publisherEarnings}" var="publisherEarning">
	<tr>
		<td class="center-column"><joda:format value="${publisherEarning.createdDate}" style="M-" /></td>
		<td class="center-column">${publisherEarning.licence.numberOfCopies}</td>
		<td class="center-column">${publisherEarning.licence.pageLayout.displayName}</td>
		<td class="center-column"><pr:price amount="${publisherEarning.amountInCurrency}" currency="${publisherEarning.currencyCode}" displayCurrency="${publisherEarning.currencyCode}" /></td>
		<td class="center-column" style="text-transform: capitalize">${fn:toLowerCase(publisherEarning.status)}</td>
		<td class="right-column">
			<c:choose>
				<c:when test="${publisherEarning.invoiceState eq 'DOWNLOADED'}">
					<a class="btn-earning-invoice" href="${publisherEarning.id}" title="<pr:snippet name="link-redownload-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Redownload invoice"/>" style="float:left;"><span>${publisherEarning.licence.id}</span></a>
					<a href="${publisherEarning.id}" class="btn-earning-invoice btn-earning-invoice-redownload" title="<pr:snippet name="link-redownload-invoice" group="product-detail-publisher-earnings" escapeJavascript="true" defaultValue="Redownload invoice"/>" >
						<span style="float:right;" class="ui-icon ui-icon-refresh"></span>
					</a>
				</c:when>
				<c:when test="${publisherEarning.invoiceState eq 'NEW'}">
					<a class="btn-earning-invoice" href="${publisherEarning.id}" title="<pr:snippet name="link-download-invoice" group="product-detail-licences" escapeJavascript="true" defaultValue="Download invoice"/>" style="float:left;"><span>${publisherEarning.licence.id}</span></a>
				</c:when>
			</c:choose>
		</td>
	</tr>
	</c:forEach>
	<c:if test="${paginator.itemCount eq 0}">
	<tr>
		<td class="left-column" colspan="8"><pr:snippet name="no-earnings" group="product-detail-earnings" defaultValue="No earnings"/></td>
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
		
	$('.btn-earning-invoice').on('click', function(){
		var button = $(this);
		var publisherEarningId = button.attr('href');
		paperight.dialog('<p class="message"><pr:snippet name="dialog-licence-invoice-message" group="product-detail-earnings" escapeJavascript="true" defaultValue="Your invoice will download shortly. You may close this window at any time."/></p>', {
			title: "<pr:snippet name="dialog-licence-invoice-title" group="product-detail-earnings" escapeJavascript="true" defaultValue="Invoice"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-licence-invoice-button-ok" group="product-detail-earnings" escapeJavascript="true" defaultValue="OK"/>": function(){ 
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
