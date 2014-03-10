<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<c:if test="${hasPendingEarnings}">
<div class="form ui-helper-clearfix" style="padding: 0 0 30px">
	<form:form id="request-payment-form" method="post" action="${ctx}/publisher-earnings/request-payment" modelAttribute="publisherEarningSearch">
		<form:hidden path="fromDate" />
		<form:hidden path="toDate" />
		<button id="btn-request-payment" class="ui-state-active"><pr:snippet name="request-payment-button" group="publisher-earnings" defaultValue="Request payment for 'Pending' items"/></button>
	</form:form>
</div>
<script>

$('#request-payment-form input').hover(function(){$(this).removeClass('ui-state-active');},function(){$(this).addClass('ui-state-active');});

$('#btn-request-payment').live("click", function(){
	paperight.dialog('<p class="message"><pr:snippet name="dialog-request-payment-question" group="publisher-earnings" escapeJavascript="true" defaultValue="Are you sure you want to request payment?"/></p>', {
		title: "<pr:snippet name="dialog-request-payment-title" group="publisher-earnings" escapeJavascript="true" defaultValue="Warning"/>", height: 165,
		buttons: {
			"<pr:snippet name="dialog-request-payment-button-no" group="publisher-earnings" escapeJavascript="true" defaultValue="No"/>":function(){ 
				$(this).dialog("close");
				return false;
			},
			"<pr:snippet name="dialog-request-payment-button-yes" group="publisher-earnings" escapeJavascript="true" defaultValue="Yes"/>": function(){
				$(this).dialog("close");
				$('#request-payment-form').submit();
				return true;
			}
		}
	});
	return false;
});

</script>
</c:if>