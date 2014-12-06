<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>Company Credits</h1>
<div id="companySearch" class="ui-helper-clearfix">

	<div id="companySelect">
		<div style="margin: 0;">
			<div class="selectfilters">
				<form>
					<input type="text" placeholder="Search companies..." />
					<button class="btn btn-paperight" id="company-search" type="submit">&raquo;</button>
					<img id="company-search-loader" style="display: none"
						src="${ctx}/img/ajax-loader.gif" width="32" height="32" />
				</form>
			</div>
			<div class="selectlists">
				<select id="companiesList" size="15" style="width: 100%;">
					<c:forEach var="company" items="${companies}">
						<option data-companyid="${company.id}"
							data-companycredits="${company.credits}">${company.name} (${company.companyAdminEmail})</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>

	<div id="companyDetails" class="company-credit-details">
		<div>
			<h3>Company Details</h3>
			<h4 id="company-name"></h4>
			<div>
				<div>
					<div class="balances">
						<label id="company-credits-current-label">Current Credits Balance</label> 
						<label id="company-credits-current" class="credits"></label>
					</div>
					<div class="balances">
						<label id="company-credits-updated-label">New Credits Balance</label> 
						<label id="company-credits-updated" class="credits"></label>
					</div>
				</div>
				<div class="form">
					<form:form id="companyCreditRecord"
						method="POST" modelAttribute="companyCreditRecord">
						<div style="width: 100%; float: left; margin-right: 30px;">
							<dl>
								<dt>Credits</dt>
								<dd><form:input type="text" id="credits-input" path="credits" cssClass="required number" />
								<form:errors path="credits" cssClass="error" element="label" /></dd>
							</dl>
							<dl>
								<dt>Note</dt>
								<dd><form:textarea id="note-input" path="note" cssClass="required" />
								<form:errors path="note" cssClass="error" element="label" /></dd>
							</dl>
							<div style="clear: both; width: 100%; padding-top: 10px;">
								<input class="btn btn-paperight" type="submit" value="Save" />
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#credits-input').val("");
		$('#note-input').val("");
	});
	$(function() {

		var ownershipUi = $('#companySearch'), companies = ownershipUi
				.find('#companySelect'), companySelect = companies
				.find('.selectlists select'), companySearch = companies
				.find('.selectfilters');

		var selectedCompanyId;
		
		var postBodyArgs = {
			type : 'POST',
			processData : false,
			contentType : 'application/json; charset=UTF-8'
		};

		var loadCompanyUi = function(companies) {
			companySelect.empty();
			if (companies.success == true && companies.data.length > 0) {
				for ( var i = 0; i < companies.data.length; ++i) {
					companySelect
							.append('<option data-companyid="'+ companies.data[i].id + '" data-companycredits="'+ companies.data[i].credits +'">'
									+ companies.data[i].name  +' ('+ companies.data[i].companyAdminEmail +')</option>');
				}
			} else {
				companySelect
						.append('<option class="empty-list">No companies</option>');
			}
		};
		var searchCompanies = function(query) {
			$('#company-search-loader').toggle();
			$.ajax($.extend({
				url : paperight.contextPath + '/company/search.json',
				type : 'POST',
				data : query,
				success : loadCompanyUi,
				complete : function() {
					$('#company-search-loader').toggle();
					$('#companyDetails').hide();
				}
			}, postBodyArgs));
		};

		var populateCompanyDetails = function(currentCredits) {
			companyCreditValues(currentCredits);
		};

		companySelect.on('change', function() {
							var companyCredits = $(this).find(":selected").data().companycredits;
							selectedCompanyId = $(this).find(":selected").data().companyid; 
							populateCompanyDetails(companyCredits);
							$('#companyDetails').show();
							$("#companyCreditRecord").attr("action", paperight.contextPath + "/company/credit/update/" + selectedCompanyId);
							$('#company-name').text($(this).find(":selected").text());
							return false;
						});

		companySearch.find('form').on('submit', function() {
			searchCompanies($(this).find('input[type="text"]').val());
			return false;
		});

		selectedCompanyId = companySelect.children('option:first').attr(
				'selected', 'selected').data('companyid');
		populateCompanyDetails(companySelect.children('option:first').attr(
				'selected', 'selected').data('companyid'));

	});
	
	$('#credits-input').on('input', function() {
		try {
			var currentCredits = parseFloat($('#company-credits-current').text());
			companyCreditValues(currentCredits);
		} catch (err) {
			
		}
	});
	
	var companyCreditValues = function(currentCredits) {
		var additionalCredits = parseFloat($('#credits-input').val());
		var updatedCredits = currentCredits;
		if (!isNaN(additionalCredits)) {
			updatedCredits = currentCredits + additionalCredits;
		}
		$('#company-credits-current').text(currentCredits.toFixed(2));
		$('#company-credits-updated').text(updatedCredits.toFixed(2));
	};
</script>