<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="payment-confirmation-wrapper">
<pr:snippet name="text" group="payment-confirmation-paypal" multiline="true" defaultValue="&lt;h1&gt;PayPal payment confirmation&lt;/h1&gt;
&lt;p&gt;Thank you for your order! Your reference number for this order is &lt;strong&gt;[$transactionReference$]&lt;/strong&gt;&lt;/p&gt;

&lt;div class=&quot;ui-panel ui-widget ui-widget-content ui-corner-all&quot;&gt;
	&lt;h3&gt;Your order summary&lt;/h3&gt;
	&lt;div&gt;&lt;strong&gt;Reference: &lt;/strong&gt;&lt;span&gt;[$transactionReference$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Credits purchased: &lt;/strong&gt;&lt;span&gt;[$numberOfCredits$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Total charged: &lt;/strong&gt;&lt;span&gt;[$totalCharged$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Status: &lt;/strong&gt;&lt;span&gt;[$transactionStatus$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Date: &lt;/strong&gt;&lt;span&gt;[$transactionDate$]&lt;/span&gt;&lt;/div&gt;
&lt;/div&gt;

&lt;p&gt;If you have any questions about your purchase, call our team on +27 21 813 6390 or e-mail us at &lt;a href=&quot;mailto:team@paperight.com&quot;&gt;team@paperight.com&lt;/a&gt;&lt;/p&gt;" />
</div>
<p>
<button onclick="print();" class="ui-priority-primary">
	<span class="ui-icon ui-icon-print"></span>
	<span><pr:snippet name="button-print" group="payment-confirmation-paypal" defaultValue="Print"/></span>
</button>
<a class="button" href="${returnUrl}"><pr:snippet name="button-continue" group="payment-confirmation-paypal" defaultValue="Continue"/></a>
</p>

<script type="text/javascript">

$(document).ready(function(){
	var transactionReference = "${transaction.transactionReference}";
	var numberOfCredits = "<pr:price showSymbol="false" showDecimal="false" amount="${transaction.numberOfCredits}" displayCurrency="USD" />";
	var totalCharged = "<pr:price amount="${transaction.amount}" currency="${transaction.currency}" />";
	var transactionStatus = "${transaction.status.displayName}";
	var transactionDate = "<joda:format value="${transaction.createdDate}" style="MM" />";
	
	$("#payment-confirmation-wrapper *").replaceText( "[$transactionReference$]", transactionReference ); 
	$("#payment-confirmation-wrapper *").replaceText( "[$numberOfCredits$]", numberOfCredits );
	$("#payment-confirmation-wrapper *").replaceText( "[$totalCharged$]", totalCharged );
	$("#payment-confirmation-wrapper *").replaceText( "[$transactionStatus$]", transactionStatus );
	$("#payment-confirmation-wrapper *").replaceText( "[$transactionDate$]", transactionDate );

});
</script>