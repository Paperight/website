<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Account Information</h1>
<h3>Update your account details</h3>
<div class="form">
	<form:form commandName="userAccount" id="registration" method="post">
		<sessionConversation:insertSessionConversationId attributeName="userAccount"/>
		<form:errors />
		<div style="width:100%; float:left; margin-right:30px;">
			<sec:authorize access="hasRole('ROLE_COMPANY_ADMIN')">
			<dl>
				<dd class="checkbox">
					<label><form:checkbox id="roleOutlet" path="roles" value="ROLE_OUTLET" />
					&nbsp;<pr:snippet name="outlet" group="profileUpdate" defaultValue="I represent an Outlet: "/><a href="${ctx}/terms/outlet" target="_blank"><pr:snippet name="outletLink" group="profileUpdate" defaultValue="Read outlet terms & conditions"/></a>
					<br />
					<span class="note"><pr:snippet name="outletNote" group="profileUpdate" defaultValue="Outlets (like copy shops and schools) use Paperight to find and print books for their customers."/></span>
					</label>
				</dd>
			</dl>
			<br />
			<dl>
				<dd class="checkbox">
					<label><form:checkbox id="rolePublisher" path="roles" value="ROLE_PUBLISHER" />
					&nbsp;<pr:snippet name="publisher" group="profileUpdate" defaultValue="I represent a Publisher: "/><a href="${ctx}/terms/publisher" target="_blank"><pr:snippet name="publisherLink" group="profileUpdate" defaultValue="Read publisher terms & conditions"/></a>
					<br />
					<span class="note"><pr:snippet name="publisherNote" group="profileUpdate" defaultValue="Publishers (rights holders like authors and publishing companies) contribute books to Paperight for outlets to print."/></span>
					</label>
					<form:errors path="roles" cssClass="error" element="label" />
				</dd>
			</dl>
			<br />
			</sec:authorize>
			<dl>
				<dt>
					<span><pr:snippet name="firstName" group="profileUpdate" defaultValue="First name"/></span>
					<br /><span class="note"><pr:snippet name="firstNameNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
				<form:input path="user.firstName" cssClass="required" />
				<form:errors path="user.firstName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="lastName" group="profileUpdate" defaultValue="Last name"/></span>
					<br /><span class="note"><pr:snippet name="lastNameNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd><form:input path="user.lastName" cssClass="required" />
				<form:errors path="user.firstName" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="companyName" group="profileUpdate" defaultValue="Business name"/></span>
					<br /><span class="note"><pr:snippet name="companyNameNote" group="profileUpdate" defaultValue="We may display this to customers."/></span>
				</dt>
				<dd>
				<form:input path="company.name" cssClass="required" />
				<form:errors path="company.name" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dd class="checkbox">
					<label><form:checkbox path="company.vatRegistered" />
					&nbsp;<span><pr:snippet name="vatRegistered" group="profileUpdate" defaultValue="VAT registered?"/></span>
					<br />
					<span class="note"><pr:snippet name="vatRegisteredNote" group="profileUpdate" defaultValue=""/></span>
					</label>
				</dd>
			</dl>
			<dl id="vatRegistrationNumberContainer">
				<dt>
					<span><pr:snippet name="vatRegistrationNumber" group="profileUpdate" defaultValue="VAT registration number"/></span>
					<br /><span class="note"><pr:snippet name="vatRegistrationNumberNote" group="profileUpdate" defaultValue="If you are registered for VAT in your country, we'll include these details on your invoices."/></span>
				</dt>
				<dd><form:input path="company.vatRegistrationNumber" cssClass="" />
				<form:errors path="company.vatRegistrationNumber" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl id="vatDateOfLiabilityContainer">
				<dt>
					<span><pr:snippet name="vatDateOfLiability" group="profileUpdate" defaultValue="VAT date of liability"/></span>
					<br /><span class="note"><pr:snippet name="vatDateOfLiabilityNote" group="profileUpdate" defaultValue="The date on which you became liable to pay VAT."/></span>
				</dt>
				<dd><form:input path="company.vatDateOfLiability" cssClass="date" />
				<form:errors path="company.vatDateOfLiability" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="websiteAddress" group="profileUpdate" defaultValue="Website"/></span>
					<br /><span class="note"><pr:snippet name="websiteAddressNote" group="profileUpdate" defaultValue="If you have a website. We may use this when we advertise to customers."/></span>
				</dt>
				<dd><form:input path="company.websiteAddress" cssClass="" />
				<form:errors path="company.websiteAddress" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="companyDescription" group="profileUpdate" defaultValue="Describe your business in one sentence"/></span>
					<br /><span class="note"><pr:snippet name="companyDescriptionNote" group="profileUpdate" defaultValue="We may use this description when promoting you to customers."/></span>
				</dt>
				<dd>
				<form:textarea path="company.description" cssClass="required" />
				<form:errors path="company.description" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine1" group="profileUpdate" defaultValue="Physical address line 1"/></span>
					<br /><span class="note"><pr:snippet name="addressLine1Note" group="profileUpdate" defaultValue="Outlets: if your address is accurate, we can include it on our outlets map."/></span>
				</dt>
				<dd>
				<form:input path="address.addressLine1" cssClass="required" />
				<form:errors path="address.addressLine1" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine2" group="profileUpdate" defaultValue="Physical address line 2"/></span>
					<br /><span class="note"><pr:snippet name="addressLine2Note" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd><form:input path="address.addressLine2" cssClass="" />
				<form:errors path="address.addressLine2" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine3" group="profileUpdate" defaultValue="Suburb"/></span>
					<br /><span class="note"><pr:snippet name="addressLine3Note" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd><form:input path="address.addressLine3" cssClass="" />
				<form:errors path="address.addressLine3" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="addressLine4" group="profileUpdate" defaultValue="City/town"/></span>
					<br /><span class="note"><pr:snippet name="addressLine4Note" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
				<form:input path="address.addressLine4" cssClass="required" />
				<form:errors path="address.addressLine4" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="postalCode" group="profileUpdate" defaultValue="Postal code"/></span>
					<br /><span class="note"><pr:snippet name="postalCodeNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
				<form:input path="address.postalCode" cssClass="required" />
				<form:errors path="address.postalCode" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="country" group="profileUpdate" defaultValue="Country"/></span>
					<br /><span class="note"><pr:snippet name="countryNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
					<form:select path="address.countryCode" cssClass="required">
						<form:option value="" label="Select a country..." />
		    			<form:options items="${countries}" itemLabel="name" /> 
					</form:select>
					<form:errors path="address.countryCode" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="gpsLocation" group="profileUpdate" defaultValue="GPS location"/></span>
					<br /><span class="note"><pr:snippet name="gpsLocationNote" group="profileUpdate" defaultValue="Double-click the map to drop a marker where your outlet is. Please be very accurate: this will help customers find you easily."/></span>
				</dt>
				<dd>
				<div class="gllpLatlonPicker" style="width:200px">
					<button type="button" class="gllpSearchButton button"><pr:snippet name="gpsLocationSearchButton" group="profileUpdate" defaultValue="Find my location from my address"/></button><br /><br />
					<input type="hidden" class="gllpSearchField">
				    <div class="gllpMap">Google Maps</div>
				    <input type="hidden" id="gllpLatitude" class="gllpLatitude" value="-33.97883008368292" />
				    <input type="hidden" id="gllpLongitude" class="gllpLongitude" value="18.463618755340576" />
		    		<input type="hidden" id="gllpZoom" class="gllpZoom" value="16"/>
		    		<input type="button" class="gllpUpdateButton" style="display:none;">
				</div>
				<form:hidden path="company.gpsLocation" cssClass="" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="phoneNumber" group="profileUpdate" defaultValue="Phone number"/></span>
					<br /><span class="note"><pr:snippet name="phoneNumberNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
				<form:input path="company.phoneNumber"  cssClass="required" minlength="10" />
				<form:errors path="company.phoneNumber" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span><pr:snippet name="currency" group="profileUpdate" defaultValue="What currency do you use?"/></span>
					<br /><span class="note"><pr:snippet name="currencyNote" group="profileUpdate" defaultValue="All prices on the site will be converted into your currency according to daily exchange rates."/></span>
				</dt>
				<dd>
					<form:select path="company.currencyCode" cssClass="required">
						<form:option value="" label="Select a currency..." />
		    			<form:options items="${currencies}" itemLabel="name" /> 
					</form:select>
					<form:errors path="company.currencyCode" cssClass="error" element="label" /></dd>
			</dl>
			<div id="averagePrintingCostContainer">
				<dl>
					<dt>
						<span><pr:snippet name="averagePrintingCost" group="profileUpdate" defaultValue="Average price per double-sided A4 sheet"/></span>
						<br /><span class="note"><pr:snippet name="averagePrintingCostNote" group="profileUpdate" defaultValue="Lets us suggest a total price for you to charge your customers for each sale."/></span>
					</dt>
					<dd>
					<form:input id="averagePrintingCost" path="company.averagePrintingCost"  cssClass="price required" />
					<form:errors path="company.averagePrintingCost" cssClass="error" element="label" /></dd>
				</dl>
				<dl>
	                <dt>
	                    <span><pr:snippet name="averageBindingCost" group="profileUpdate" defaultValue="Average price for binding and colour cover"/></span>
	                    <br /><span class="note"><pr:snippet name="averageBindingCostNote" group="profileUpdate" defaultValue="We'll include this in your customer's suggested price."/></span>
	                </dt>
	                <dd>
	                <form:input id="averageBindingCost" path="company.averageBindingCost"  cssClass="price required" />
	                <form:errors path="company.averageBindingCost" cssClass="error" element="label" /></dd>
	            </dl>
            </div>
			<sec:authorize access="principal.impersonatingUser">
			<dl>
				<dt>
					<span><pr:snippet name="mapDisplay" group="profileUpdate" defaultValue="Map display"/></span>
					<br /><span class="note"><pr:snippet name="mapDisplayNote" group="profileUpdate" defaultValue=""/></span>
				</dt>
				<dd>
					<form:select path="company.mapDisplay" cssClass="">
						<form:option value="" label="" />
		    			<form:options items="${mapDisplay}"/> 
					</form:select>
					<form:errors path="company.mapDisplay" cssClass="error" element="label" />
				</dd>
			</dl>
			</sec:authorize>
		</div>
		
		
		<br />
		<div style="width:100%; clear:both;float:left;">
			<h3><pr:snippet name="newsletterHeading" group="profileUpdate" defaultValue="Newsletter subscription"/></h3>
			<dl>
				<dd class="checkbox">
					<label><form:checkbox path="user.subscribed" />
					&nbsp;<pr:snippet name="newsletter" group="profileUpdate" defaultValue="Subscribe to our newsletter"/></label>
				</dd>
			</dl>
			<br />
		</div>
		
		<div style="clear:both; width:100%;">
			<button id="updateProfile" type="submit"><pr:snippet name="updateButton" group="profileUpdate" defaultValue="Save details"/></button>
		</div>
	</form:form>
</div>
<script type="text/javascript" language="javascript">
$(document).ready(function(){ 
	roleOutletClick();
	$('#roleOutlet').click(function(){
		roleOutletClick();
	});
	
	extractLatLon();
		
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

function extractLatLon() {
	var gpsLocation = $('#company\\.gpsLocation').val();
	if (gpsLocation !== '') {
		var gpsLocationArray = gpsLocation.split(',');
		$('.gllpLatitude').val(gpsLocationArray[0]);
		$('.gllpLongitude').val(gpsLocationArray[1]);
		$('.gllpUpdateButton').click();
	}
}
</script>	
	
