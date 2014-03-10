<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1><pr:snippet name="heading" group="top-up-form" defaultValue="Top-up"/></h1>
<h3><pr:snippet name="sub-heading" group="top-up-form" defaultValue="Buy more credits"/></h3>
<div class="form">
	<form:form commandName="topUp" id="topupForm" method="post">
		<pr:referer/>
		<dl>
			<dt><pr:snippet name="amount" group="top-up-form" defaultValue="Amount &lt;small&gt;(You can buy a minimum of 2 credits)&lt;/small&gt;"/></dt>
			<dd>
				<form:input path="amount" cssClass="required price" />
				<form:errors path="amount" cssClass="error" element="label" />
				<br />
				<small><pr:snippet name="amount-about" group="top-up-form" defaultValue="That's about"/>&nbsp;<em class="price"><pr:price amount="${amount}" /></em></small>
			</dd>
		</dl>
	
		<h4><pr:snippet name="payment-method" group="top-up-form" defaultValue="How do you want to pay?"/></h4>
		<dl>
			<dd class="checkbox">
				<label><form:radiobutton path="paymentMethod" value="EFT" autocomplete="off" />&nbsp;
				<pr:snippet name="payment-method-eft" group="top-up-form" defaultValue="Bank Transfer"/></label>
			</dd>
		</dl>
		<dl>
			<dd class="checkbox">
				<label><form:radiobutton path="paymentMethod" value="PAYPAL" autocomplete="off" />&nbsp;
				<pr:snippet name="payment-method-paypal" group="top-up-form" defaultValue="Pay with a credit card (processed by PayPal)"/></label>
			</dd>
		</dl>
		<br />
		
		<div style="clear:both; width:100%;">
			<button type="submit"><pr:snippet name="button" group="top-up-form" defaultValue="Pay now"/></button>
		</div>
		
	</form:form>
</div>