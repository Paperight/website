<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1><pr:snippet name="heading" group="registration" defaultValue="Registration"/></h1>
<div class="form">
	<form:form commandName="userRegistration" id="registration" method="post">
					
		<div style="width:100%; float:left; margin-right:30px;">
			<dl>
				<dd class="checkbox">
					<label><form:checkbox id="roleOutlet" path="roles" value="ROLE_OUTLET" />
					&nbsp;<pr:snippet name="outlet" group="registration" defaultValue="I represent an Outlet: "/><a href="${ctx}/terms/outlet" target="_blank"><pr:snippet name="outletLink" group="registration" defaultValue="Read outlet terms & conditions"/></a>
					<br />
					<span class="note"><pr:snippet name="outletNote" group="registration" defaultValue="Outlets (like copy shops and schools) use Paperight to find and print books for their customers."/></span>
					</label>
				</dd>
			</dl>
			<br />
			<dl>
				<dd class="checkbox">
					<label><form:checkbox id="rolePublisher" path="roles" value="ROLE_PUBLISHER" />
					&nbsp;<pr:snippet name="publisher" group="registration" defaultValue="I represent a Publisher: "/><a href="${ctx}/terms/publisher" target="_blank"><pr:snippet name="publisherLink" group="registration" defaultValue="Read publisher terms & conditions"/></a>
					<br />
					<span class="note"><pr:snippet name="publisherNote" group="registration" defaultValue="Publishers (rights holders like authors and publishing companies) contribute books to Paperight for outlets to print."/></span>
					</label>
					<form:errors path="roles" cssClass="error" element="label" />
				</dd>
			</dl>
			
			<br /><br />
			<dl>
				<dt>
					<span><pr:snippet name="email" group="registration" defaultValue="Email address"/></span>
					<br /><span class="note"><pr:snippet name="emailNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="user.email" cssClass="email required" />
				<form:errors path="user.email" cssClass="error" element="label" />
				<form:errors path="usernameAvailable" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="confirmEmail" group="registration" defaultValue="Confirm email address"/></span>
					<br /><span class="note"><pr:snippet name="confirmEmailNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="confirmEmail" cssClass="email required" />
				<form:errors path="emailValid" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="firstName" group="registration" defaultValue="First name"/></span>
					<br /><span class="note"><pr:snippet name="firstNameNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="user.firstName" cssClass="required" />
				<form:errors path="user.firstName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="lastName" group="registration" defaultValue="Last name"/></span>
					<br /><span class="note"><pr:snippet name="lastNameNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="user.lastName" cssClass="required" />
				<form:errors path="user.firstName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="password" group="registration" defaultValue="Password"/></span>
					<br /><span class="note"><pr:snippet name="passwordNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:password path="user.password" cssClass="required password" />
				<form:errors path="user.password" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="confirmPassword" group="registration" defaultValue="Confirm password"/></span>
					<br /><span class="note"><pr:snippet name="confirmPasswordNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:password path="confirmPassword" cssClass="required" />
				<form:errors path="passwordValid" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="companyName" group="registration" defaultValue="Business name"/></span>
					<br /><span class="note"><pr:snippet name="companyNameNote" group="registration" defaultValue="We may display this to customers."/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="company.name" cssClass="required" />
				<form:errors path="company.name" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dd class="checkbox">
					<label><form:checkbox path="company.vatRegistered" />
					&nbsp;<span><pr:snippet name="vatRegistered" group="registration" defaultValue="VAT registered?"/></span>
					<br />
					<span class="note"><pr:snippet name="vatRegisteredNote" group="registration" defaultValue=""/></span>
					</label>
				</dd>
			</dl>
			<dl id="vatRegistrationNumberContainer">
				<dt>
					<span><pr:snippet name="vatRegistrationNumber" group="registration" defaultValue="VAT registration number"/></span>
					<br /><span class="note"><pr:snippet name="vatRegistrationNumberNote" group="registration" defaultValue="If you are registered for VAT in your country, we'll include these details on your invoices."/></span>
				</dt>
				<dd><form:input path="company.vatRegistrationNumber" cssClass="" />
				<form:errors path="company.vatRegistrationNumber" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl id="vatDateOfLiabilityContainer">
				<dt>
					<span><pr:snippet name="vatDateOfLiability" group="registration" defaultValue="VAT date of liability"/></span>
					<br /><span class="note"><pr:snippet name="vatDateOfLiabilityNote" group="registration" defaultValue="The date on which you became liable to pay VAT."/></span>
				</dt>
				<dd><form:input path="company.vatDateOfLiability" cssClass="date" />
				<form:errors path="company.vatDateOfLiability" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="websiteAddress" group="registration" defaultValue="Website"/></span>
					<br /><span class="note"><pr:snippet name="websiteAddressNote" group="registration" defaultValue="If you have a website. We may use this when we advertise to customers."/></span>
				</dt>
				<dd><form:input path="company.websiteAddress" cssClass="" />
				<form:errors path="company.websiteAddress" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="companyDescription" group="registration" defaultValue="Describe your business in one sentence"/></span>
					<br /><span class="note"><pr:snippet name="companyDescriptionNote" group="registration" defaultValue="We may use this description when promoting you to customers."/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:textarea path="company.description" cssClass="required" />
				<form:errors path="company.description" cssClass="error" element="label" /></dd>
			</dl>			
			<dl>
				<dt>
					<span><pr:snippet name="addressLine1" group="registration" defaultValue="Physical address line 1"/></span>
					<br /><span class="note"><pr:snippet name="addressLine1Note" group="registration" defaultValue="Outlets: if your address is accurate, we can include it on our outlets map."/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="address.addressLine1" cssClass="required" />
				<form:errors path="address.addressLine1" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine2" group="registration" defaultValue="Physical address line 2"/></span>
					<br /><span class="note"><pr:snippet name="addressLine2Note" group="registration" defaultValue=""/></span>
				</dt>
				<dd><form:input path="address.addressLine2" cssClass="" />
				<form:errors path="address.addressLine2" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine3" group="registration" defaultValue="Suburb"/></span>
					<br /><span class="note"><pr:snippet name="addressLine3Note" group="registration" defaultValue=""/></span>
				</dt>
				<dd><form:input path="address.addressLine3" cssClass="" />
				<form:errors path="address.addressLine3" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine4" group="registration" defaultValue="City/town"/></span>
					<br /><span class="note"><pr:snippet name="addressLine4Note" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="address.addressLine4" cssClass="required" />
				<form:errors path="address.addressLine4" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="postalCode" group="registration" defaultValue="Postal code"/></span>
					<br /><span class="note"><pr:snippet name="postalCodeNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="address.postalCode" cssClass="required" />
				<form:errors path="address.postalCode" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="country" group="registration" defaultValue="Country"/></span>
					<br /><span class="note"><pr:snippet name="countryNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:select path="address.countryCode" cssClass="required">
						<form:option value="" label="Select a country..." />
		    			<form:options items="${countries}" itemLabel="name" /> 
					</form:select>
					<form:errors path="address.countryCode" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="gpsLocation" group="registration" defaultValue="GPS location"/></span>
					<br /><span class="note"><pr:snippet name="gpsLocationNote" group="registration" defaultValue="Double-click the map to drop a marker where your outlet is. Please be very accurate: this will help customers find you easily."/></span>
				</dt>
				<dd>
				<div class="gllpLatlonPicker" style="width:200px">
					<button type="button" class="gllpSearchButton button"><pr:snippet name="gpsLocationSearchButton" group="registration" defaultValue="Find my location from my address"/></button><br /><br />
					<input type="hidden" class="gllpSearchField">
				    <div class="gllpMap">Google Maps</div>
				    <form:hidden path="gllpLatitude" cssClass="gllpLatitude" />
				    <form:hidden path="gllpLongitude" cssClass="gllpLongitude" />
		    		<input type="hidden" id="gllpZoom" class="gllpZoom" value="16"/>
				</div>
				<form:hidden path="company.gpsLocation" cssClass="" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="phoneNumber" group="registration" defaultValue="Phone number"/></span>
					<br /><span class="note"><pr:snippet name="phoneNumberNote" group="registration" defaultValue=""/></span>
				</dt>
				<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="company.phoneNumber"  cssClass="required" minlength="10" />
				<form:errors path="company.phoneNumber" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="currency" group="registration" defaultValue="Currency"/></span>
					<br /><span class="note"><pr:snippet name="currencyNote" group="registration" defaultValue="All prices on the site will be converted into your currency according to daily exchange rates."/></span>
				</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<form:select path="company.currencyCode" cssClass="required">
						<form:option value="" label="Select a currency..." />
		    			<form:options items="${currencies}" itemLabel="name" /> 
					</form:select>
					<form:errors path="company.currencyCode" cssClass="error" element="label" />
				</dd>
			</dl>
			<div id="averagePrintingCostContainer">
				<dl>
					<dt>
						<span><pr:snippet name="averagePrintingCost" group="registration" defaultValue="Average price per double-sided A4 sheet"/></span>
						<br /><span class="note"><pr:snippet name="averagePrintingCostNote" group="registration" defaultValue="Lets us suggest a total price for you to charge your customers for each sale."/></span>
					</dt>
					<dd>
					<form:input id="averagePrintingCost" path="company.averagePrintingCost"  cssClass="price" />
					<form:errors path="company.averagePrintingCost" cssClass="error" element="label" /></dd>
				</dl>
				<dl>
	                <dt>
	                    <span><pr:snippet name="averageBindingCost" group="registration" defaultValue="Average price for binding and colour cover"/></span>
	                    <br /><span class="note"><pr:snippet name="averageBindingCostNote" group="registration" defaultValue="We'll include this in your customer's suggested price."/></span>
	                </dt>
	                <dd>
	                <form:input id="averageBindingCost" path="company.averageBindingCost"  cssClass="price" />
	                <form:errors path="company.averageBindingCost" cssClass="error" element="label" /></dd>
	            </dl>
            </div>
			<dl>
				<dt>
					<span><pr:snippet name="captchaYourAnswer" group="registration" defaultValue="Please answer this question: "/>${captcha1} + ${captcha2} =</span>
					<br /><span class="note"><pr:snippet name="captchaYourAnswerNote" group="registration" defaultValue="This assures us that you're a person, and your registration isn't spam."/></span>
				</dt>
				<dd>
					<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
					<input type="text" id="captchaYourAnswer" cssClass="required" />
					<input type="hidden" id="captchaTheAnswer" value="${captchaAnswer}" />
				</dd>
			</dl>
			
			<br />
			
			<h3><pr:snippet name="newsletterHeading" group="registration" defaultValue="Newsletter subscription"/></h3>
			<dl>
				<dd class="checkbox">
					<label><form:checkbox path="user.subscribed" />
					&nbsp;<pr:snippet name="newsletter" group="registration" defaultValue="Subscribe to our newsletter"/></label>
				</dd>
			</dl>
			<br />
			
		</div>
	
		<div style="clear:both; width:100%;">
			<input id="register" type="submit" class="button-big" value="<pr:snippet name="registerButton" group="registration" defaultValue="Register"/>" />
		</div>
	</form:form>
</div>
<script type="text/javascript">
	var countryToCurrencyMap = {  
        <c:forEach items="${countryCurrencyMap}" var="item" varStatus="loop">  
          ${item.key}: '${item.value}' ${not loop.last ? ',' : ''}  
        </c:forEach>  
      }; 
      
	$('#address\\.countryCode').change(function(){
		var currencies = $('#company\\.currencyCode'), countries = $(this);
		currencies.val( countryToCurrencyMap[countries.val()] );
	});

$(document).ready(function(){
	
	roleOutletClick();
	$('#roleOutlet').click(function(){
		roleOutletClick();
	});
	
	var vatRegistrationNumberContainer = $('#vatRegistrationNumberContainer');
	var vatDateOfLiabilityContainer = $('#vatDateOfLiabilityContainer');
	var checkbox = $('[name="company\\.vatRegistered"]');
	var vatNumberInput = $('#company\\.vatRegistrationNumber');
	var vatDateOfLiabilityInput = $('#company\\.vatDateOfLiability');
	vatRegisteredClick(checkbox, vatRegistrationNumberContainer, vatNumberInput);
	vatRegisteredClick(checkbox, vatDateOfLiabilityContainer, vatDateOfLiabilityInput);
	checkbox.click(function(){
		vatRegisteredClick(checkbox, vatRegistrationNumberContainer, vatNumberInput);
		vatRegisteredClick(checkbox, vatDateOfLiabilityContainer, vatDateOfLiabilityInput);
	});
	
});

$(document).bind("location_changed", function(event, object) {
	var gpsLocation = $('.gllpLatitude').val() + ',' + $('.gllpLongitude').val();
	$('#company\\.gpsLocation').val(gpsLocation);
});

$("#registration").validate({
    ignore: '.gllpLatlonPicker *'
});

$('.gllpSearchButton').bind('click', function() {
	extractAddress();
});

function extractAddress() {
	var flatAddress = $('#address\\.addressLine1').val() + "," + $('#address\\.addressLine2').val() + "," + $('#address\\.addressLine3').val() + "," + $('#address\\.addressLine4').val();
	$('.gllpSearchField').val(flatAddress);
}

</script>	
	
