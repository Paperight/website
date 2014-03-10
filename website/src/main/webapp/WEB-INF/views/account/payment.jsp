<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="payment-confirmation-wrapper">
<pr:snippet name="text" group="payment-confirmation" multiline="true" defaultValue="&lt;h1&gt;Payment confirmation&lt;/h1&gt;
&lt;p&gt;Thank you for your order! Your reference number for this order is &lt;strong&gt;[$transactionReference$]&lt;/strong&gt;&lt;/p&gt;

&lt;div class=&quot;ui-panel ui-widget ui-widget-content ui-corner-all&quot;&gt;
	&lt;h3&gt;Your order summary&lt;/h3&gt;
	&lt;div&gt;&lt;strong&gt;Reference: &lt;/strong&gt;&lt;span&gt;[$transactionReference$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Credits purchased: &lt;/strong&gt;&lt;span&gt;[$numberOfCredits$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Total charged: &lt;/strong&gt;&lt;span&gt;[$totalCharged$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Status: &lt;/strong&gt;&lt;span&gt;[$transactionStatus$]&lt;/span&gt;&lt;/div&gt;
	&lt;div&gt;&lt;strong&gt;Date: &lt;/strong&gt;&lt;span&gt;[$transactionDate$]&lt;/span&gt;&lt;/div&gt;
&lt;/div&gt;


&lt;h2&gt;You have chosen to pay by bank transfer. Please pay [$totalCharged$] into our account as soon as possible.&lt;/h2&gt;
&lt;div&gt;&lt;strong&gt;Account Name: &lt;/strong&gt;&lt;span&gt;Paperight (Pty) Ltd&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;Account number: &lt;/strong&gt;&lt;span&gt;62357315425&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;Bank: &lt;/strong&gt;&lt;span&gt;First National Bank&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;Branch: &lt;/strong&gt;&lt;span&gt;Claremont, 215 Main Road, Claremont, Cape Town 7708, South Africa&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;Branch code: &lt;/strong&gt;&lt;span&gt;200109&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;Account type: &lt;/strong&gt;&lt;span&gt;Business cheque/current account&lt;/span&gt;&lt;/div&gt;
&lt;div&gt;&lt;strong&gt;SWIFT code for international payments: &lt;/strong&gt;&lt;span&gt;FIRNZAJJ&lt;/span&gt;&lt;/div&gt;

&lt;p&gt;Payments via PayPal can be sent to paypal@paperight.com.&lt;/p&gt;

&lt;p&gt;&lt;strong&gt;Please use [$transactionReference$] as the reference on the payment.&lt;/strong&gt;&lt;/p&gt;

&lt;p&gt;If you have any questions about your purchase, call our team on +27 21 671 1278 or e-mail us at &lt;a href=&quot;mailto:team@paperight.com&quot;&gt;team@paperight.com&lt;/a&gt;&lt;/p&gt;

&lt;p&gt;&lt;strong&gt;Disclaimer:&lt;/strong&gt; We will add your credits to your account as soon as we have received the full payment in our account. Depending on your bank, this may take up to a few days. Contact us if you have questions.&lt;/p&gt;
" />
</div>

<p id="payment-buttons">
<button onclick="print();" class="ui-priority-primary ui-print-button">
	<span class="ui-icon ui-icon-print"></span>
	<span><pr:snippet name="button-print" group="payment-confirmation" defaultValue="Print"/></span>
</button>
<a class="button" href="${returnUrl}"><pr:snippet name="button-continue" group="payment-confirmation" defaultValue="Continue"/></a>
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