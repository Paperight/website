<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="suggestion-form">
	<h3><pr:snippet name="heading" group="suggestBookWidget" defaultValue="Suggest a book"/></h3>
	<p><pr:snippet name="description" group="suggestBookWidget" defaultValue="Can't find the title you're after? Let us know and we'll look into it for you."/></p>
	<form id="suggest-book-form" class="no-validate" action="" method="post">
		<ul>
			<li><input class="required clearfocus noplaceholder" type="text" id="name" name="name" value="" /></li>
			<li><input class="email required clearfocus noplaceholder" type="text" id="email" name="email" value="" /></li>
		    <li><input class="required_group clearfocus " type="text" id="title" name="title" value="" /></li>
			<li><input class="required_group clearfocus " type="text" id="author" name="author" value="" /></li>
			<li><input class="required_group clearfocus " type="text" id="additionalInformation" name="additionalInformation" value="" /></li>
	  	</ul>
	  	<button type="submit" class="btn-suggest-book ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false" name="register"><span class="ui-button-text"><pr:snippet name="button" group="suggestBookWidget" defaultValue="Suggest Book"/></span></button>
	</form>
</div>
<script type="text/javascript">

$(document).ready(function(){
	
	resetForm();
	
	function resetForm(){
		
		$("#suggest-book-form #name").val("");
		$("#suggest-book-form #email").val("");
		$("#suggest-book-form #title").val("");
		$("#suggest-book-form #author").val("");
		$("#suggest-book-form #additionalInformation").val("");
		
		$("#suggest-book-form #name").DefaultValue("<pr:snippet name="name" group="suggestBookWidget" defaultValue="Your Name"/>");
		$("#suggest-book-form #email").DefaultValue("<pr:snippet name="email" group="suggestBookWidget" defaultValue="Email Address"/>");
		$("#suggest-book-form #title").DefaultValue("<pr:snippet name="bookTitle" group="suggestBookWidget" defaultValue="Book Title"/>");
		$("#suggest-book-form #author").DefaultValue("<pr:snippet name="author" group="suggestBookWidget" defaultValue="Author"/>");
		$("#suggest-book-form #additionalInformation").DefaultValue("<pr:snippet name="isbn" group="suggestBookWidget" defaultValue="ISBN / EAN"/>");
		
	};
	
	var suggestBook = function(form){
		
		var modal = paperight.dialog('<p class="message"><pr:snippet name="dialogSubmitting" group="suggestBookWidget" escapeJavascript="true" defaultValue="Submitting your suggestion..."/></p>', {
			title: "<pr:snippet name="dialogTitle" group="suggestBookWidget" escapeJavascript="true" defaultValue="Suggest a book"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
		});
		
		var data = $(form).getFormValues();
		return paperight.sendSuggestBook({
			data: data,
			success: function(data){
				modal.dialog('progressbar', {stop: true, value: 100});
				if(typeof data == "object"){
					modal.find(".message").text((data.result == false) ? data.message : "<pr:snippet name="dialogSuccess" group="suggestBookWidget" escapeJavascript="true" defaultValue="Thank you! We've received your suggestion and will look into it."/>");
					resetForm();
				};
				modal.dialog('addbutton', "<pr:snippet name="ok-button" group="suggestBookWidget" escapeJavascript="true" defaultValue="OK"/>", function(){
					$(this).dialog("close");
				});
			},
			error: function(){
				modal.find(".message").text("<pr:snippet name="dialogError" group="suggestBookWidget" defaultValue="Error"/>");
				modal.dialog('progressbar', {stop: true, value: 100});
				modal.dialog('addbutton', "<pr:snippet name="ok-button" group="suggestBookWidget" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
			}
		});
	};
	
	$.validator.addClassRules("required_group", {
	    require_from_group: [1,".required_group"]
	});
	
	$('#suggest-book-form').validate({
		ignore: "",
		submitHandler: function(form){
			try{
				suggestBook(form);
			}catch(e){
				console.error(e);
			}
			return false;
		}
	});
	
	
});

</script>
