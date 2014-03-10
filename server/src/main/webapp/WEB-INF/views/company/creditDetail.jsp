<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>Credit detail</h1>
<div class="ui-helper-clearfix">
	<div style="width: 100%; float: left; margin-right: 30px;">
		<dl style="padding-top:10px;">
			<dt>Company</dt>
			<dd>
				<label class="company-credit-detail">${companyCreditRecord.company.name}</label>
			</dd>
		</dl>
		<dl style="padding-top:10px;">
			<dt>Credits</dt>
			<dd>
				<label  class="company-credit-detail">${companyCreditRecord.credits}</label>
			</dd>
		</dl>
		<dl style="padding-top:10px;">
			<dt>Created By</dt>
			<dd>
				<label class="company-credit-detail">${companyCreditRecord.user.fullName}</label>
			</dd>
		</dl>
		<dl style="padding-top:10px;">
			<dt>Created Date</dt>
			<dd>
				<label class="company-credit-detail"><joda:format value="${companyCreditRecord.createdDate}" pattern="dd MMMM YYYY" /></label>
			</dd>
		</dl>
		<dl style="padding-top:10px;">
			<dt>Note</dt>
			<dd>
				<label class="company-credit-detail-note">${companyCreditRecord.note}</label>
			</dd>
		</dl>
	</div>
</div>
