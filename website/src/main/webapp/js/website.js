$(document).bind('loadui', function(e, element) {

    // Date
    $('input.date', this).datepicker({
        changeMonth : true,
        changeYear : true,
        dateFormat : 'yy-mm-dd'
    });

});