jQuery.fn.DefaultValue = function(text){	
    return this.each(function(){
		//Store reference to input field
		var $input_obj = jQuery(this);
		
		//See if text is supplied or contained in label property
		var default_val = text || $input_obj.attr("label");
					
		//Make sure we're dealing with text-based form fields
		switch(this.type){
			case 'text':
			case 'password':
			case 'textarea':
				break;
			default:
				return;
		}
		
		//Set value initially if none are specified
        if($input_obj.val() =='') {
			$input_obj.val(default_val);
		} else {
			//Other value exists - ignore
			return;
		}
		
		//Remove values on focus
		$input_obj.bind('focus', function() {
			if($input_obj.val() ==default_val || $input_obj.val() =='')
				$input_obj.val('');
		});
		
		//Place values back on blur
		$input_obj.bind('blur',function() {
			if($input_obj.val() == default_val || $input_obj.val() == '')
				$input_obj.val(default_val);
		});
		
		//Capture parent form submission
		//Remove field values that are still default
		$input_obj.parents("form").each(function() {
			//Bind parent form submit
			jQuery(this).bind('submit beforeValidation',function() {
				if($input_obj.val() == default_val && jQuery.fn.DefaultValue.settings.clear_defaults_on_submit == true) {
					$input_obj.val('');
				}
			});
		});
    });
};

jQuery.fn.DefaultValue.settings = {
	'clear_defaults_on_submit':true
}