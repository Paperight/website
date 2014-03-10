$(document).ready(function(){

	$('.btn-preview-conversion').live("click", function(){
		var btn = $(this);
		var filename = $(btn).attr('href');
		paperight.dialog('<p class="message">Preview will start shortly. You may close this window at any time.</p>', {
			title: "Preview", height: 145, modal: true,
			buttons: {
				"Ok": function(){ 
					$(this).dialog("close"); 
				}
			}
		});
		paperight.previewConversion({ data: {filename: filename} });
		return false;
	});
	
	$('.btn-delete-conversion').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message">Are you sure you want to delete this PDF?</p>', {
			buttons: {
				"No":function(){ $(this).dialog("close"); },
				"Yes": function(){
					$(this).dialog("close");
					window.location.href = a.attr('href');
				}
			}
		}, 'confirm');
		return false;
	});
	
});