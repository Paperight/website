$(document).ready(function(){
	
	$('.btn-export').live("click", function(){
		var btn = $(this), form = btn.closest('form');
		if ($(form).valid()) {
			var href = btn.attr('value') + '?' + form.serialize();
			window.open(href);
		}
		return false;
	});
	
});
