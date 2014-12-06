$(document).ready(function(){
		
	$('#posters-grid a.delete').click(function(){
		var a = $(this);
		paperight.dialog('<p class="message">Are you sure you want to delete this poster?</p>', {
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
                        window.location.href = a.attr('href');
                        $(this).dialog("close");  
                    }, 
                    class:"btn btn-paperight", 
                },
            ]
		}, 'confirm');
		return false;
	});
	
	
	
});
