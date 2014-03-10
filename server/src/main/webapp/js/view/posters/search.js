$(document).ready(function(){
		
	$('#posters-grid a.delete').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message">Are you sure you want to delete this poster?</p>', {
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
