<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1><pr:snippet name="heading" group="publisher-earning-generate-statement" defaultValue="Get statement"/></h1>
<div class="form ui-helper-clearfix" style="padding: 0 0 30px">
	<form id="publisher-earning-generate-statement-form" class="no-validate" action="" method="POST"  >
		<div id="publisher-earning-generate-statement-form-panel">
			<div id="field">
				<label for="fromDate"><pr:snippet name="from-date-label" group="publisher-earning-generate-statement" defaultValue="From date"/></label> 
				<input type="" name="fromDate" class="required date search-input"/>
			</div>
			<div id="field">
				<label for="toDate"><pr:snippet name="to-date-label" group="publisher-earning-generate-statement" defaultValue="To date"/></label> 
				<input type="" name="toDate" class="required date search-input" />
			</div>
		</div>
		<input type="submit" class="button-big" value="<pr:snippet name="button" group="publisher-earning-generate-statement" defaultValue="Generate statement"/>" />
	</form>
</div>

<script type="text/javascript">

$(function() {
	
	$('#publisher-earning-generate-statement-form').validate({
		ignore: "",
		submitHandler: function(form){
			try{
				generatePublisherStatement(form);
			}catch(e){
				console.error(e);
			}
			return false;
		}
	});
	
	var generatePublisherStatement = function(form){
		
		paperight.dialog('<p class="message"><pr:snippet name="dialog-generate-statement-message" group="publisher-earning-generate-statement" escapeJavascript="true" defaultValue="Your statement will download shortly. You may close this window at any time."/></p>', {
			title: "<pr:snippet name="dialog-generate-statement-title" group="publisher-earning-generate-statement" escapeJavascript="true" defaultValue="Generate statement"/>", height: 145, modal: true,
			buttons: {
				"<pr:snippet name="dialog-generate-statement-button-ok" group="publisher-earning-generate-statement" escapeJavascript="true" defaultValue="OK"/>": function(){ 
					$(this).dialog("close"); 
				}
			}
		});
		var data = $(form).serialize();
		paperight.generatePublisherStatement({data: data});
		return false;
		
	};
	
});

</script>