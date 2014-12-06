$(document).ready(function(){
		
	$('.btn-rebuild-search-index').live("click", function(){
		var btn = $(this);
		paperight.dialog('<p class="message">This will perform a full product search reindex! <br /><br /> Are you sure you wish to continue?</p>', {
			title: "Warning", height: 165,
			buttons: [
                {
                    text: "No", 
                    click: function () { 
                        $(this).dialog("close");  
                    }, 
                    class:"btn btn-paperight", 
                },
                {
                    text: "Yes", 
                    click: function () {
                        window.location.href = btn.attr('value');
                        $(this).dialog("close");  
                    }, 
                    class:"btn btn-paperight", 
                },
            ]
		});
		return false;
	});
	
	$('#search-results a.delete').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message">Are you sure you want to delete this product?</p>', {
		    buttons: [
                {
                    text: "No", 
                    click: function () { 
                        $(this).dialog("close");  
                    }, 
                    class:"btn btn-paperight", 
                },
                {
                    text: "Yes", 
                    click: function () {
                        window.location.href = a.attr('value');
                        $(this).dialog("close");  
                    }, 
                    class:"btn btn-paperight", 
                },
            ]
		}, 'confirm');
		return false;
	});
	
	
	
});
