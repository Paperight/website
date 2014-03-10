$(document).ready(function(){
		
	$('.btn-rebuild-search-index').live("click", function(){
		var btn = $(this);
		paperight.dialog('<p class="message">This will perform a full product search reindex! <br /><br /> Are you sure you wish to continue?</p>', {
			title: "Warning", height: 165,
			buttons: {
				"No":function(){ $(this).dialog("close"); },
				"Yes": function(){
					$(this).dialog("close");
					window.location.href = btn.attr('value');
				}
			}
		});
		return false;
	});
	
	$('#search-results a.delete').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message">Are you sure you want to delete this product?</p>', {
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
