<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1><pr:snippet name="heading" group="account-information" defaultValue="Account Information"/></h1>
<div class="ui-panel ui-widget ui-widget-content ui-corner-all">
	<div class="column column-one">
		<dl>
			<dt><pr:snippet name="email" group="account-information" defaultValue="Email"/></dt>
			<dd><c:out value="${user.username}" /><br/><br/>
			<a href="${ctx}/account/email/update" class="button" ><pr:snippet name="change-email-button" group="account-information" defaultValue="Change email address"/></a></dd>
			<sec:authorize access="hasRole('ROLE_PUBLISHER')">
				<dt><pr:snippet name="publisher-payment-details" group="account-information" defaultValue="Publisher payment details"/></dt>
				<c:set var="publisherPaymentDetailsMethod" value="None" />
				<c:if test="${not empty publisherPaymentDetails.paymentMethod}">
					<c:set var="publisherPaymentDetailsMethod" value="${publisherPaymentDetails.paymentMethod.displayName}" />
				</c:if>
				<dd><pr:snippet name="payment-details" group="account-information" defaultValue="Payment method:"/>&nbsp;<span  style="text-transform: capitalize">${publisherPaymentDetailsMethod}</span><br/><br/>
				<a href="${ctx}/publisher-earnings/payment-details" class="button" ><pr:snippet name="change-payment-details-button" group="account-information" defaultValue="Change payment details"/></a>
				</dd>	
			</sec:authorize>
			<dt><pr:snippet name="business-name" group="account-information" defaultValue="Business name"/></dt>
			<dd><c:out value="${user.company.name}" /></dd>
			<dt><pr:snippet name="website" group="account-information" defaultValue="Website"/></dt>
			<dd><c:out value="${user.company.websiteAddress}" /></dd>
		</dl>
	</div>
	
	<div class="column column-two">
		<c:set var="gpsLocation" value="None" />
		<c:if test="${not empty user.company.gpsLocation}">
			<c:set var="gpsLocation" value = "${user.company.gpsLocation}" />
		</c:if>
		<dl>
			<dt><pr:snippet name="gpsLocation" group="account-information" defaultValue="GPS location"/></dt>
			<dd><c:out value="${gpsLocation}" /></dd>
		</dl>
		<sec:authorize access="principal.impersonatingUser">
			<c:set var="mapDisplay" value="N/A" />
			<c:if test="${not empty user.company.mapDisplay}">
				<c:set var="mapDisplay" value = "${user.company.mapDisplay}" />
			</c:if>
			<dl>
				<dt><pr:snippet name="mapDisplay" group="account-information" defaultValue="Map display"/></dt>
				<dd><c:out value="${mapDisplay}" /></dd>
			</dl>
		</sec:authorize>
		
		<c:forEach var="addressContext" items="${user.company.addressContexts}">
			<c:set var="address" value="${addressContext.address}" />
			<dl>
				<dt><pr:snippet name="address" group="account-information" defaultValue="Address"/></dt>
				<dd>
				<c:out value="${address.addressLine1}" /><br />
				<c:out value="${address.addressLine2}" /><br />
				<c:out value="${address.addressLine3}" /><br />
				<c:out value="${address.addressLine4}" /><br />
				<c:out value="${address.postalCode}" /><br />
				<c:out value="${address.country.name}" /><br />
				</dd>
			</dl>	
		</c:forEach>	
		<dl>
			<dt><pr:snippet name="phone-number" group="account-information" defaultValue="Phone number"/></dt>
			<dd><c:out value="${user.company.phoneNumber}" /></dd>
		</dl>	
		<dl>
			<dt><pr:snippet name="currency" group="account-information" defaultValue="Currency"/></dt>
			<dd><c:out value="${user.company.currency.name}" /></dd>
		</dl>
	</div>
	<div class="button-panel">
		<a href="${ctx}/account/update" class="button"><pr:snippet name="edit-details-button" group="account-information" defaultValue="Edit your details"/></a>
		<a href="${ctx}/account/changepassword" class="button"><pr:snippet name="change-password-button" group="account-information" defaultValue="Change password"/></a>
		<sec:authorize access="!principal.actingUser.verified">
		<a href="${ctx}/account/activate" class="button"><pr:snippet name="activate-account-button" group="account-information" defaultValue="Activate account"/></a>
		</sec:authorize>
	</div>
	<div class="button-panel button-panel-header">
		<c:if test="${showCloseUser eq true}">
		<a id="btn-close-account" href="${ctx}/account/close" class="button"><pr:snippet name="close-account-button" group="account-information" defaultValue="Close account"/></a>
		</c:if>
		<c:if test="${showCloseCompany eq true}">
		<a id="btn-close-company" href="${ctx}/account/company/close" class="button"><pr:snippet name="close-company-button" group="account-information" defaultValue="Close company"/></a>
		</c:if>
		<sec:authorize access="principal.impersonatingUser">
			<sec:authorize access="principal.actingUser.enabled">
			<c:if test="${showCloseUser eq true}">
			<a href="${ctx}/account/disable" class="button"><pr:snippet name="disable-account-button" group="account-information" defaultValue="Disable account"/></a>
			</c:if>
			<c:if test="${showCloseCompany eq true}">
			<a id="btn-disable-company" href="${ctx}/account/company/disable" class="button"><pr:snippet name="disable-company-button" group="account-information" defaultValue="Disable company"/></a>
			</c:if>
			</sec:authorize>
			<sec:authorize access="!principal.actingUser.enabled">
			<a href="${ctx}/account/enable" class="button"><pr:snippet name="enable-account-button" group="account-information" defaultValue="Enable account"/></a>
			<a href="${ctx}/account/company/enable" class="button"><pr:snippet name="enable-company-button" group="account-information" defaultValue="Enable company"/></a>
			</sec:authorize>
		</sec:authorize>
	</div>
</div>

<sec:authorize access="hasRole('ROLE_COMPANY_ADMIN')">
<h1><pr:snippet name="heading" group="company-management" defaultValue="Company Management"/></h1>
<div id="companymanagement" class="ui-htabs-container ui-helper-clearfix">
	<div class="ui-htabs ui-helper-clearfix">
		<ul class="ui-htabs-panel ui-htabs-panel-left ui-htabs-list companies">
			<li class="primary-company" data-companyid="${companyHeirachy.company.id}">
				<div class="thumbnail selected">
					<h3>${companyHeirachy.company.name}</h3>
					<p>${companyHeirachy.company.description}</p>
					<div class="thumbnail-tools">
						<button class="edit-company"><pr:snippet name="edit-company-button" group="company-management" defaultValue="Edit"/></button>
						<button class="add-company"><pr:snippet name="add-company-button" group="company-management" defaultValue="Add company"/></button>
						<button class="add-user"><pr:snippet name="add-user-button" group="company-management" defaultValue="Add user"/></button>
						<a class="delete-link delete-company" href="#"><pr:snippet name="delete-company-link" group="company-management" defaultValue="Delete"/></a>
					</div>
				</div>
				<c:if test="${not empty companyHeirachy.children}">
				<ul class="ui-htabs-list">
					<c:forEach var="firstChild" items="${companyHeirachy.children}">
					<li data-companyid="${firstChild.company.id}">
						<div class="company thumbnail">
							<h3>${firstChild.company.name}</h3>
							<p>${firstChild.company.description}</p>
						</div>
						<c:if test="${not empty firstChild.children}">
						<ul class="ui-htabs-list">
							<c:forEach var="secondChild" items="${firstChild.children}">
							<li data-companyid="${secondChild.company.id}">
								<div class="company thumbnail">
									<h3>${secondChild.company.name}</h3>
									<p>${secondChild.company.description}</p>
								</div>
							</li>
							</c:forEach>
						</ul>
						</c:if>
					</li>
					</c:forEach>
				</ul>
				</c:if>
			</li>
		</ul>
		<ul class="ui-htabs-panel ui-htabs-panel-right ui-htabs-list company-users">
			<c:forEach var="companyUser" items="${companyHeirachy.users}">
				<li data-companyid="${companyHeirachy.company.id}" data-userid="${companyUser.id}">
					<div class="thumbnail">
						<span>${companyUser.firstName}&nbsp;${companyUser.lastName}</span>
						<p><a href="mailto:${companyUser.email}">${companyUser.email}</a></p>
						<div class="thumbnail-tools">
							<button class="edit-user"><pr:snippet name="edit-button-link" group="company-management" defaultValue="Edit"/></button>
							<c:if test="${user.id != companyUser.id}">
							<a href="#" class="delete-user delete-link"><pr:snippet name="delete-user-link" group="company-management" defaultValue="Delete"/></a>
							</c:if>
						</div>
					</div>
				</li>				
			</c:forEach>
		</ul>
	</div>
</div>

<div class="form" id="companyUserForm" style="display: none; padding:10px; background: white; width: 400px;">
	<form:form commandName="companyUser" cssClass="no-validate" method="post">
		<div class="errors" style="display: none; margin-bottom:10px;"></div>
		<input type="hidden" name="companyId" id="companyId" />
		<form:hidden path="id" />
		<dl id="companyAdmin" >
			<dd class="checkbox">
				<label><form:checkbox path="roles" value="ROLE_COMPANY_ADMIN" />
				<pr:snippet name="company-administrator" group="edit-company-user-form" defaultValue="Company Administrator"/>
				<br />
				<span class="note"><pr:snippet name="company-administrator-note" group="edit-company-user-form" defaultValue="Company administrators can add, remove and update companies and users."/></span>
				</label>
			</dd>
		</dl>
		<br />
		<dl>
			<dt>
				<span><pr:snippet name="email" group="edit-company-user-form" defaultValue="Email address"/></span>
				<br /><span class="note"><pr:snippet name="email-note" group="edit-company-user-form" defaultValue=""/></span>
			</dt>
			<dd data-errors="usernameAvailable">
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="email" cssClass="email required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="confirm-email" group="edit-company-user-form" defaultValue="Confirm email address"/></span>
				<br /><span class="note"><pr:snippet name="confirm-email-note" group="edit-company-user-form" defaultValue=""/></span>
			</dt>
			<dd data-errors="emailValid">
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<input name="confirmEmail" id="confirmEmail" type="text" class="email required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="first-name" group="edit-company-user-form" defaultValue="First name"/></span>
				<br /><span class="note"><pr:snippet name="first-name-note" group="edit-company-user-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="firstName" cssClass="required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="last-name" group="edit-company-user-form" defaultValue="Last name"/></span>
				<br /><span class="note"><pr:snippet name="last-name-note" group="edit-company-user-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="lastName" cssClass="required" />
			</dd>
		</dl>
			
		<br />
			
		<h3><pr:snippet name="newsletterHeading" group="registration" defaultValue="Newsletter subscription"/></h3>
		<dl>
			<dd class="checkbox">
				<label><form:checkbox id="newsletterSubscription" path="subscribed" />
				&nbsp;<pr:snippet name="newsletter" group="registration" defaultValue="Subscribe to our newsletter"/></label>
			</dd>
		</dl>
		<br />
	</form:form>
</div>

<div class="form" id="childCompanyForm" style="display: none; padding:10px; background: white; width: 450px;">
	<form:form commandName="childCompany" cssClass="no-validate" method="post">
		<div class="errors" style="display: none; margin-bottom:10px;"></div>
		<input type="hidden" name="parentCompanyId" id="parentCompanyId" />
		<form:hidden path="company.id" />
		<form:hidden path="address.id" />
		<dl>
			<dt>
				<span><pr:snippet name="company-name" group="edit-company-form" defaultValue="Company name"/></span>
				<br /><span class="note"><pr:snippet name="company-name-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="company.name" cssClass="required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="company-description" group="edit-company-form" defaultValue="Company description"/></span>
				<br /><span class="note"><pr:snippet name="company-description-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:textarea path="company.description" cssClass="required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="website" group="edit-company-form" defaultValue="Website"/></span>
				<br /><span class="note"><pr:snippet name="website-note" group="edit-company-form" defaultValue="If you have a website. We may use this when we advertise to customers."/></span>
			</dt>
			<dd><form:input path="company.websiteAddress" cssClass="" /></dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="address-line-2" group="edit-company-form" defaultValue="Physical address line 1"/></span>
				<br /><span class="note"><pr:snippet name="address-line-1-note" group="edit-company-form" defaultValue="Outlets: if your address we accurate, we can include it on our outlets map."/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="address.addressLine1" cssClass="required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="address-line-2" group="edit-company-form" defaultValue="Physical address line 2"/></span>
				<br /><span class="note"><pr:snippet name="address-line-2-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd><form:input path="address.addressLine2" cssClass="" /></dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="suburb" group="edit-company-form" defaultValue="Suburb"/></span>
				<br /><span class="note"><pr:snippet name="suburb-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd><form:input path="address.addressLine3" cssClass="" /></dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="city-town" group="edit-company-form" defaultValue="City/town"/></span>
				<br /><span class="note"><pr:snippet name="city-town-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="address.addressLine4" cssClass="required" />
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="postal-code" group="edit-company-form" defaultValue="Postal code"/></span>
				<br /><span class="note"><pr:snippet name="postal-code-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
			<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
			<form:input path="address.postalCode" cssClass="required" /></dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="country" group="edit-company-form" defaultValue="Country"/></span>
				<br /><span class="note"><pr:snippet name="country-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:select path="address.countryCode" cssClass="required">
					<form:option value="" label="Select a country..." />
	    			<form:options items="${countries}" itemLabel="name" /> 
				</form:select>
			</dd>
		</dl>
		<dl>
			<dt>
				<span><pr:snippet name="phone-number" group="edit-company-form" defaultValue="Phone number"/></span>
				<br /><span class="note"><pr:snippet name="phone-number-note" group="edit-company-form" defaultValue=""/></span>
			</dt>
			<dd>
				<span class="require-message"><pr:snippet name="required" group="general" defaultValue="* Required"/></span>
				<form:input path="company.phoneNumber"  cssClass="required" minlength="10" />
			</dd>
		</dl>
	</form:form>
</div>

<script type="text/javascript">

$(document).ready(function(){
	
	var companiesUi = $('#companymanagement'),
	companyTools = companiesUi.find('.companies .thumbnail-tools'),
	usersUi = companiesUi.find('.company-users');

	var loadCompanyUsers = function(data){
		usersUi.css('opacity', 0.5);
		$.ajax({
			url: paperight.contextPath + '/account/company/'+ data.companyid +'/users.json',
			success: function(response){
				usersUi.css('opacity', 1).empty();
				if(typeof response != 'object' || response.success == false){
					return;
				};
				for(var i=0; i<response.data.length; ++i){
					var userIsCurrentLoggedInUser = response.data[i].currentLoggedInUser;
					usersUi.append(
						'<li data-companyid="'+ data.companyid +'" data-userid="'+ response.data[i].id +'">'+
							'<div class="thumbnail">'+
								'<span>'+ response.data[i].firstName +' '+ response.data[i].lastName +'</span>'+
								'<p><a href="mailto:'+ response.data[i].email +'">'+ response.data[i].email +'</a></p>'+
								'<div class="thumbnail-tools">'+
									'<button class="edit-user"><pr:snippet name="edit-button-link" group="company-management" defaultValue="Edit"/></button>' +
									((userIsCurrentLoggedInUser)?(''):('<a href="#" class="delete-user delete-link"><pr:snippet name="delete-user-link" group="company-management" defaultValue="Delete"/></a>')) +	
								'</div>'+
							'</div>'+
						'</li>'
					);
					usersUi.find('button').button();
				};
				sortUsers();
				if(response.data.length == 0){
					handleEmptyUserUi();
				};
				companiesUi.trigger('change');
			}
		});
	};
	
	var buildCompanyUi = function(company, primary){
		var children = '';
		for(var i=0;i<company.children.length;i++){
			children += buildCompanyUi(company.children[i], false);
		};
		var str = 
		'<li'+ ((primary == true) ? ' class="primary-company"':'') +' data-companyid="'+company.companyId+'">'+
			'<div class="thumbnail">'+
				'<h3>'+company.name+'</h3>'+
				'<p>'+company.description+'</p>'+
			'</div>'+
			((children == '') ? '' : 
			'<ul class="ui-htabs-list">'+
				children+
			'</ul>')+
		'</li>';
		return str;
	};
	var loadCompanies = function(added){
		$.ajax({
			url: paperight.contextPath + '/account/companies.json',
			success: function(data){
				if(data.success == true){
					companyTools.detach();
					companiesUi.find('.ui-htabs-panel-left').empty().html(buildCompanyUi(data.data, true));
					companiesUi.find('.ui-htabs-panel-left li[data-companyid="'+added.companyId+'"] .thumbnail:first').trigger('click');
					companiesUi.trigger('change');
					return false;
				}
			}
		});
	};
	var handleEmptyUserUi = function(){
		usersUi.append('<li class="empty-list"><p>No users</p></li>');
	};
	var sortUsers = function(){
		usersUi.find('li').sortElements(function(a, b){
			return $(a).data('user-id') < $(b).data('user-id');
		});
	};
	var handleModalError = function(modal, messages, form, validator){
		form = $(form);
		if(validator != undefined){
			validator.resetForm();
		}
		var message = '<label class="error">An error has occurred. Please try again.</label>';
		if(form != undefined && typeof messages == 'object'){
			if(typeof messages == 'object'){
				for(var n in messages){
					var errorContainer = form.find('[data-errors~="'+n+'"]');
					var input = form.find('[name="'+n+'"]');
					var errorMsg = '<label class="error">'+messages[n] +'</label>';
					if(errorContainer.get(0) != null){
						errorContainer.append(errorMsg);
					}else if(input.get(0) != null){
						input.after(errorMsg);
					}else{
						message += errorMsg;
					}
				}
			};
			form.find('.errors').show().html(message);
			modal.dialog("close");
		}else{
			modal.find(".message").html(message);
			modal.dialog('progressbar', {stop: true, value: 100});
			modal.dialog('addbutton', "Ok", function(){ $(this).dialog("close"); });
		}
	};
	var removeCompanyUser = function(el){
		paperight.dialog('<p class="message"><pr:snippet name="dialog-delete-company-user-question" group="company-management" escapeJavascript="true" defaultValue="Are you sure you want to remove this user?"/></p>', {
			title: "<pr:snippet name="dialog-delete-company-user-title" group="company-management" escapeJavascript="true" defaultValue="Delete company"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-delete-company-user-button-no" group="company-management" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-delete-company-user-button-yes" group="company-management" escapeJavascript="true" defaultValue="Yes"/>": function(){
					var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-deleting-company-user-message-deleting" group="company-management" escapeJavascript="true" defaultValue="Please wait..."/></p>', {
						title: "<pr:snippet name="dialog-deleting-company-user-title" group="company-management" escapeJavascript="true" defaultValue="Removing User"/>", 
						height: 180, 
						modal: true, 
						progressbar : { time: paperight.ajax.timeout, start: true }
					});
					return paperight.removeCompanyUser({
						data: {
							companyid: $(el).data('companyid'),
							userid: $(el).data('userid')
						},
						error: function(){
							handleModalError.call(this, modal);
						},
						success: function(data){
							if(data.success == true){
								modal.find(".message").text((data.success == false) ? data.message : "<pr:snippet name="dialog-deleting-company-user-message-deleted" group="company-management" escapeJavascript="true" defaultValue="User has been removed successfully"/>");
								modal.dialog('progressbar', {stop: true, value: 100});
								modal.dialog('addbutton', "<pr:snippet name="dialog-deleting-company-user-button-ok" group="company-management" escapeJavascript="true" defaultValue="OK"/>", function(){	$(this).dialog("close"); });
								$(this).dialog("close");
								el.fadeOut(500, function(){
									if(el.closest('ul').children().length == 1){
										handleEmptyUserUi();
									};
									el.remove();
									companiesUi.trigger('change');
								});
							}else{
								handleModalError.call(this, modal, data.message);
							}
						}
					});
				}
			}
		});
	};
	var removeCompany = function(el){
		paperight.dialog('<p class="message"><pr:snippet name="dialog-delete-company-question" group="company-management" escapeJavascript="true" defaultValue="Are you sure you want to remove this company?"/></p>', {
			title: "<pr:snippet name="dialog-delete-company-title" group="company-management" escapeJavascript="true" defaultValue="Delete company"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-delete-company-button-no" group="company-management" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-delete-company-button-yes" group="company-management" escapeJavascript="true" defaultValue="Yes"/>": function(){
					var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-deleting-company-message-deleting" group="company-management" escapeJavascript="true" defaultValue="Please wait..."/></p>', {
						title: "<pr:snippet name="dialog-deleting-company-title" group="company-management" escapeJavascript="true" defaultValue="Removing Company"/>", 
						height: 180, 
						modal: true, 
						progressbar : { time: paperight.ajax.timeout, start: true }
					});
					return paperight.removeCompany({
						data: {
							companyid: $(el).data('companyid')
						},
						error: function(){
							handleModalError.call(this, modal);
						},
						success: function(data){
							if(data.success == true){
								modal.find(".message").text((data.success == false) ? data.message : "<pr:snippet name="dialog-deleting-company-message-deleted" group="company-management" escapeJavascript="true" defaultValue="Company has been removed successfully"/>");
								modal.dialog('progressbar', {stop: true, value: 100});
								modal.dialog('addbutton', "<pr:snippet name="dialog-deleting-company-button-ok" group="company-management" escapeJavascript="true" defaultValue="OK"/>", function(){	$(this).dialog("close"); });
								$(this).dialog("close");
								el.fadeOut(500, function(){
									companyTools.detach();
									el.remove();
									companiesUi.find('.ui-htabs-panel-right').empty();
									handleEmptyUserUi();
									companiesUi.trigger('change');
									companiesUi.find('.ui-htabs-panel-left li:first .thumbnail:first').trigger('click');
								});
							}else{
								handleModalError.call(this, modal, data.message);
							}
						}
					});
				}
			}
		});
	};
	var addCompanyUser = function(el){
		companyUserFormValidator.resetForm();
		$('#companyUserForm').clearFormValues();
		$('#companyUserForm input#companyId').val(el.data('companyid'));
		$('#companyUserForm input#id').val("");
		$('#companyUserForm').dialog({
			title: '<pr:snippet name="title-new-user" group="edit-company-user-form" escapeJavascript="true" defaultValue="New User"/>',
			modal: true,
			width: 500,
			buttons: {
				"<pr:snippet name="button-cancel" group="edit-company-user-form" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="button-add" group="edit-company-user-form" escapeJavascript="true" defaultValue="Add"/>": function(){ $(this).find('form:first').trigger('submit'); }
			}
		});
	};
	var editCompanyUser = function(el){
		companyUserFormValidator.resetForm();
		$('#companyUserForm').clearFormValues();
		$('#companyUserForm input#companyId').val(el.data('companyid'));
		paperight.getCompanyUser({
			companyId: el.data('companyid'),
			userId: el.data('userid'),
			success: function(response){
				$('#companyUserForm').setFormValues(response.data);
				var currentLoggedInUser = response.data.currentLoggedInUser;
				if (currentLoggedInUser == true) {
					$('#companyUserForm #companyAdmin').css({ display: "none" });
				} else {
					$('#companyUserForm #companyAdmin').css({ display: "block" });
				}
				var subscribed = response.data.subscribed;
				if (subscribed) {
					$('#newsletterSubscription').attr('checked','checked');
				}
				$('#companyUserForm').dialog({
					title: '<pr:snippet name="title-edit-user" group="edit-company-user-form" escapeJavascript="true" defaultValue="Edit User"/>',
					modal: true,
					width: 500,
					buttons: {
						"<pr:snippet name="button-cancel" group="edit-company-user-form" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
						"<pr:snippet name="button-update" group="edit-company-user-form" escapeJavascript="true" defaultValue="Update"/>": function(){ $(this).find('form:first').trigger('submit'); }
					}
				});
			}
		});
	};
	var addCompany = function(el){
		companyFormValidator.resetForm();
		$('#childCompanyForm').clearFormValues();
		$('#childCompanyForm input#parentCompanyId').val(el.data('companyid'));
		$('#childCompanyForm input#company\\.id').val("");
		$('#childCompanyForm input#address\\.id').val("");
		$('#childCompanyForm').dialog({
			title: '<pr:snippet name="title-new-company" group="edit-company-form" escapeJavascript="true" defaultValue="New Company"/>',
			modal: true,
			width: 500,
			buttons: {
				"<pr:snippet name="button-cancel" group="edit-company-form" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="button-add" group="edit-company-form" escapeJavascript="true" defaultValue="Add"/>": function(){ $(this).find('form:first').trigger('submit'); }
			}
		});
	};
	var editCompany = function(el){
		companyFormValidator.resetForm();
		$('#childCompanyForm').clearFormValues();
		$('#childCompanyForm input#parentCompanyId').val(el.data('companyid'));
		paperight.getCompany({
			companyId: el.data('companyid'),
			async: false,
			success: function(response){
				$('#childCompanyForm').setFormValues(response.data);
				$('#childCompanyForm').dialog({
					title: '<pr:snippet name="title-edit-company" group="edit-company-form" escapeJavascript="true" defaultValue="Edit Company"/>',
					modal: true,
					width: 500,
					buttons: {
						"<pr:snippet name="button-cancel" group="edit-company-form" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
						"<pr:snippet name="button-update" group="edit-company-form" escapeJavascript="true" defaultValue="Update"/>": function(){ $(this).find('form:first').trigger('submit'); }
					}
				});
			}
		});
		
	};
	var companyFormValidator = $('#childCompanyForm form').validate({
		ignore: "",
		submitHandler: function(form){
			var data = $(form).getFormValues();
			var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-saving-message-saving" group="edit-company-form" escapeJavascript="true" defaultValue="Please wait..."/></p>', {
				title: "<pr:snippet name="dialog-saving-title" group="edit-company-form" escapeJavascript="true" defaultValue="Saving Company"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
			});
			return paperight.saveCompany({
				data: data,
				success: function(data){
					modal.dialog('progressbar', {stop: true, value: 100});
					if(typeof data == "object"){
						if(data.success == true){
							modal.find(".message").text((data.success == false) ? data.message : "<pr:snippet name="dialog-saving-message-saved" group="edit-company-form" escapeJavascript="true" defaultValue="Company saved successfully"/>");
							modal.dialog('addbutton', "<pr:snippet name="dialog-saving-button-ok" group="edit-company-form" escapeJavascript="true" defaultValue="Ok"/>", function(){	
								$('#childCompanyForm').dialog('close');
								console.log(data);
								loadCompanies(data);
								$(this).dialog("close"); 
							});
						}else{
							handleModalError.call(this, modal, data.message, form, companyFormValidator);
						}
					};
				},
				error: function(){
					handleModalError.call(this, modal);
				}
			});
			return false;
		}
	});
	var companyUserFormValidator = $('#companyUserForm form').validate({
		ignore: "",
		submitHandler: function(form){
			var formData = $(form).getFormValues();
			var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-saving-message-saving" group="edit-company-user-form" escapeJavascript="true" defaultValue="Please wait..."/></p>', {
				title: "Saving User", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
			});
			return paperight.saveCompanyUser({
				data: formData,
				success: function(data){
					modal.dialog('progressbar', {stop: true, value: 100});
					if(typeof data == "object"){
						if(data.success == true){
							var message = "<pr:snippet name="dialog-saving-message-saved-update-user" group="edit-company-user-form" escapeJavascript="true" defaultValue="User saved successfully"/>";
							if (data.newUser == true) {
								message = "<pr:snippet name="dialog-saving-message-saved-new-user" group="edit-company-user-form" escapeJavascript="true" defaultValue="User saved successfully. The new user will receive an email to set their password. Please make sure they check their email."/>";
							}
							modal.find(".message").text((data.success == false) ? data.message : message);
							modal.dialog('addbutton', "<pr:snippet name="dialog-saving-button-ok" group="edit-company-user-form" escapeJavascript="true" defaultValue="OK"/>", function(){	 
								$('#companyUserForm').dialog('close');
								loadCompanyUsers(companiesUi.find('.ui-htabs-panel-left li[data-companyid="'+ formData.companyId +'"]').data());
								$(this).dialog("close"); 
							});
						}else{
							handleModalError.call(this, modal, data.message, form, companyUserFormValidator);
						};
					}
				},
				error: function(){
					handleModalError.call(this, modal);
				}
			});
			return false;
		}
	});
	
	companiesUi.each(function(){
		
		var tabs = companiesUi.find('.ui-htabs'),
			leftPanel = companiesUi.find('.ui-htabs-panel-left'),
			rightPanel = companiesUi.find('.ui-htabs-panel-right');
	
		companiesUi.hTabs({
			select: function(data){
				companyTools.detach().appendTo(this);
				loadCompanyUsers(data);
				return false;
			}
		});
		
		leftPanel.on('click', '.delete-company', function(){
			removeCompany($(this).closest('li'));
			return false;
		});
		leftPanel.on('click', '.add-company', function(){
			addCompany($(this).closest('li'));
			return false;
		});
		leftPanel.on('click', '.edit-company', function(){
			editCompany($(this).closest('li'));
			return false;
		});
		leftPanel.on('click', '.add-user', function(){
			addCompanyUser($(this).closest('li'));
			return false;
		});
		rightPanel.on('click', '.delete-user', function(){
			removeCompanyUser($(this).closest('li'));
			return false;
		});
		rightPanel.on('click', '.edit-user', function(){
			editCompanyUser($(this).closest('li'));
			return false;
		});
	});
	
	sortUsers();
	
});

</script>

</sec:authorize>

<script type="text/javascript">

$(document).ready(function(){
	
	$('#btn-close-account').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message"><pr:snippet name="dialog-close-account-question" group="account-information" escapeJavascript="true" defaultValue="Are you sure you want to close your account?"/></p>', {
			title: "<pr:snippet name="dialog-close-account-title" group="account-information" escapeJavascript="true" defaultValue="Close account"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-close-account-button-no" group="account-information" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-close-account-button-yes" group="account-information" escapeJavascript="true" defaultValue="Yes"/>": function(){
					$(this).dialog("close");
					window.location.href = a.attr('href');
				}
			}
		});
		return false;
	});
	
	$('#btn-close-company').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message"><pr:snippet name="dialog-close-company-question" group="account-information" escapeJavascript="true" defaultValue="WARNING: Are you sure you want to close your company? This will close all users and child companies too."/></p>', {
			title: "<pr:snippet name="dialog-close-company-title" group="account-information" escapeJavascript="true" defaultValue="Warning"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-close-company-button-no" group="account-information" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-close-company-button-yes" group="account-information" escapeJavascript="true" defaultValue="Yes"/>": function(){
					$(this).dialog("close");
					window.location.href = a.attr('href');
				}
			}
		});
		return false;
	});
	
	$('#btn-disable-company').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message"><pr:snippet name="dialog-disable-company-question" group="account-information" escapeJavascript="true" defaultValue="WARNING: Are you sure you want to disable this company? This will disable all users and child companies too."/></p>', {
			title: "<pr:snippet name="dialog-disable-company-title" group="account-information" escapeJavascript="true" defaultValue="Warning"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-disable-company-button-no" group="account-information" escapeJavascript="true" defaultValue="No"/>":function(){ $(this).dialog("close"); },
				"<pr:snippet name="dialog-disable-company-button-yes" group="account-information" escapeJavascript="true" defaultValue="Yes"/>": function(){
					$(this).dialog("close");
					window.location.href = a.attr('href');
				}
			}
		});
		return false;
	});
	
});

</script>