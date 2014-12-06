$(document).bind('loadui', function(e, element) {

    $('input.date').datetimepicker({
        pickTime : false
    });
    
    $('.btn-preview-pdf').live("click", function(){
        var filename = $(this).data('filename');
        paperight.dialog('<p class="message">Preview will start shortly. You may close this window at any time.</p>', {
            title: "Preview", height: 160, modal: true,
            buttons: [
                {
                    text: "OK", 
                    click: function () { $(this).dialog("close");  }, 
                    class:"btn btn-paperight", 
                }
            ]
        });
        paperight.previewConversion({ data: {filename: filename} });
        return false;
    });
    
    //disables auto focus for jquery ui dialogs, when then causes bootstrap buttons to have hover state
    $.ui.dialog.prototype._focusTabbable = function(){};

});