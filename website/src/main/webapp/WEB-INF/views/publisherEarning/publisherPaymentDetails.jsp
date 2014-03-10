<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1><pr:snippet name="heading" group="publisher-payment-details-form" defaultValue="Publisher Payment Details"/></h1>
<div class="form">
<form:form id="publisherPaymentDetailsForm" method="POST" modelAttribute="publisherPaymentDetails" >
	<sessionConversation:insertSessionConversationId attributeName="publisherPaymentDetails"/>
	<sessionConversation:insertSessionConversationId attributeName="publisherEarningSearchFlash"/>
	<div style="width:100%; float:left; margin-right:30px;">
		<dl>
			<dt>
				<span><pr:snippet name="payment-method" group="publisher-payment-details-form" defaultValue="Payment method"/></span>
				<br /><span class="note"><pr:snippet name="payment-method-note" group="publisher-payment-details-form" defaultValue=""/></span>
			</dt>
			<dd>
				<form:select path="paymentMethod" cssClass="required">
					<form:options itemLabel="displayName" />
				</form:select>
				<form:errors path="paymentMethod" cssClass="error" element="label" />
			</dd>
		</dl>
		<div id="BANK_ACCOUNT">
			<dl>
				<dt>
					<span><pr:snippet name="bank-name" group="publisher-payment-details-form" defaultValue="Bank name"/></span>
					<br /><span class="note"><pr:snippet name="bank-name-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="bankName" cssClass="required" />
					<form:errors path="bankName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="account-holder" group="publisher-payment-details-form" defaultValue="Account holder"/></span>
					<br /><span class="note"><pr:snippet name="account-holder-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="bankAccountHolder" cssClass="required" />
					<form:errors path="bankAccountHolder" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="account-number" group="publisher-payment-details-form" defaultValue="Account number"/></span>
					<br /><span class="note"><pr:snippet name="account-number-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="bankAccountNumber" cssClass="number required" />
					<form:errors path="bankAccountNumber" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="branch-name" group="publisher-payment-details-form" defaultValue="Branch name"/></span>
					<br /><span class="note"><pr:snippet name="branch-name-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="bankBranchName" cssClass="required" />
					<form:errors path="bankBranchName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="branch-code" group="publisher-payment-details-form" defaultValue="Branch code"/></span>
					<br /><span class="note"><pr:snippet name="branch-code-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="bankBranchCode" cssClass="number required" />
					<form:errors path="bankBranchCode" cssClass="error" element="label" />
				</dd>
			</dl>
		</div>
		<div id="PAYPAL">
			<dl>
				<dt>
					<span><pr:snippet name="paypal-address" group="publisher-payment-details-form" defaultValue="Paypal Address"/></span>
					<br /><span class="note"><pr:snippet name="paypal-address-note" group="publisher-payment-details-form" defaultValue=""/></span>
				</dt>
				<dd>
					<form:input path="paypalAddress" cssClass="email required" />
					<form:errors path="paypalAddress" cssClass="error" element="label" />
				</dd>
			</dl>
		</div>
	</div>
	<div style="clear:both; width:100%;">
		<input type="submit" class="button-big" value="Update" />
	</div>
</form:form>
</div>
<script>
$(document).ready(function(){
	$('#publisherPaymentDetailsForm #paymentMethod').trigger('change');
});

$('#publisherPaymentDetailsForm #paymentMethod').change(function() {
	$('#BANK_ACCOUNT, #PAYPAL').hide();
	$('#' + $(this).find('option:selected').attr('value')).show();
	var selectValue = $('#publisherPaymentDetailsForm #paymentMethod').val();
	
	$('#BANK_ACCOUNT input[type="text"]').removeClass('required');
	$('#PAYPAL #paypalAddress').removeClass('required');
	
	$(this).addClass('layout-selected');
	if (selectValue == 'BANK_ACCOUNT') {
		$('#BANK_ACCOUNT input[type="text"]').addClass('required');
	} else {
		$('#PAYPAL #paypalAddress').addClass('required');
	}
});
</script>