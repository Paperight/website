String.prototype.trim = function() { return this.replace(/^\s+|\s+$/g,''); };
String.prototype.ltrim = function() { return this.replace(/^\s+/,''); };
String.prototype.rtrim = function() { return this.replace(/\s+$/,''); };

//this is to fix a bug in current version of additional-methods-1.10.0
$.validator.addMethod("require_from_group", function(value, element, options) {
	  var numberRequired = options[0];
	  var selector = options[1];
	  var fields = $(selector, element.form);
	  var filled_fields = fields.filter(function() {
	    // it's more clear to compare with empty string
	    return $(this).val() != ""; 
	  });
	  var empty_fields = fields.not(filled_fields);
	  // we will mark only first empty field as invalid
	  if (filled_fields.length < numberRequired && empty_fields[0] == element) {
	    return false;
	  }
	  return true;
	// {0} below is the 0th item in the options field
	}, $.format("Please fill out at least {0} of these fields."));

$.validator.addMethod( 'noplaceholder', function (value, element) {  return value !== element.defaultValue; }, 'This field is required.' );
$.validator.addMethod( 'price', function (value, element) { if(value.trim() == '') return true; else return /^([1-9]{1}[0-9]{0,2}(\,[0-9]{3})*(\.[0-9]{0,2})?|[1-9]{1}[0-9]{0,}(\.[0-9]{0,2})?|0(\.[0-9]{0,2})?|(\.[0-9]{1,2})?)$/i.test(value); }, 'Please only enter a price in numbers, e.g. 0.50' );

$.validator.addMethod('passwordminlength', $.validator.methods.minlength, $.format("We need your account to be more secure. Please use {0} or more characters."));

$.validator.addMethod(
        "passwordregex",
        function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "Not a valid password. Spaces are not allowed."
);

$.validator.addMethod(
        "pagerangeregex",
        function(value, element) {
            var pageRangeRegex = new RegExp("^(\\d+|\\d+-\\d+\\s*)(,\\s*?(\\d+|\\d+-\\d+)\\s*)*$");
            var pageRangePercentRegex = new RegExp("^(\\d+%)$");
            var isValidPageRange = pageRangeRegex.test(value);
            var isValidPageRangePercent = pageRangePercentRegex.test(value);
            return this.optional(element) || isValidPageRange || isValidPageRangePercent;
        },
        "Not a valid page range. Only values like \"1-4, 6, 9-12\" or \"20%\" allowed."
);

$.validator.addMethod(
        "fileextensionregex",
        function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "Not a valid file."
);

$.validator.addClassRules('password', {
	required: true,
	passwordregex: "^[\\S]+$",
	passwordminlength: 6
});

$.validator.addClassRules('pagerange', {
	required: false,
	pagerangeregex: "",
});

$.validator.addClassRules('epub', {
	required: true,
	fileextensionregex: "^.*\.(epub)$",
});
$.validator.addClassRules('imageFile', {
	fileextensionregex: "^.*\.(jpg|png)$",
});
$.validator.addClassRules('pdfFile', {
	required: false,
	fileextensionregex: "^.*\.(pdf)$",
});

$.validator.methods["date"] = function (value, element) {
	try { 
		var d = new Date();
		return this.optional(element) || !/Invalid|NaN/.test(new Date(d.toLocaleDateString(value)).toString());
	} catch (error) {
		return this.optional(element) || !/Invalid|NaN/.test(new Date(value));
	}
};

$.fn.center = function () { return this.css('position','absolute').css('top', (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + 'px').css('left', (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + 'px'); };
$.fn.getFormValues = function(){ var formvals = {}; $.each($('input[type="text"],input[type="password"],input[type="hidden"],textarea,:checked,select',this).serializeArray(),function(i,obj){ formvals[obj.name] = obj.value; }); return formvals; };
$.fn.clearFormValues = function(){
	return this.each(function(){
		$(this).find('input[type="text"],select,textarea').val('');
		$(this).find('input[type="checkbox"],input[type="radio"]').attr('checked', false);
	});
};
(function(){
	var applyObjValues = function(form, name, obj){
		for(var i in obj){
			if(typeof obj[i] == 'object'){
				applyObjValues(form, name + '\\.' + i, obj[i]);
				continue;
			}
			var e = $(form).find('[name="'+name+'"]');
			if(e.attr('type') == 'checkbox' || e.attr('type') == 'radio'){
				$(form).find('input[value="'+obj[i]+'"]').prop('checked', true);
				continue;
			}
			applyStringValues(form, name + '\\.' + i, obj[i]);
		}
	};
	var applyStringValues = function(form, name, str){
		var input = $(form).find('#'+name);
		input.val(str);
	};
	$.fn.setFormValues = function(values){
		for(var i in values){
			if(typeof values[i] == 'object'){
				applyObjValues(this, i, values[i]);
				continue;
			}
			applyStringValues(this, i, values[i]);
			continue;
		}
	};
})();


$(document).bind('loadui', function(e, element){
	
	// Element can be passed in, or uses the current context
	element = $(element);
	if(element.get(0) == null){
		element = $(this);
	}
	
	// Buttons
	$('button,a.button,input.button,input[type="submit"]', this).each(function(){
		if(false == $(this).hasClass('ui-widget')){
			$(this).button();
		};
	});
	$('.buttonset input[type="radio"]', this).button({icons: {primary:'ui-icon-radio-on'}}).bind('change.toggleRadio', function(){
		$(this).closest('.ui-buttonset').find('.ui-button').each(function(){
			var b = $(this), actv = 'ui-state-active', ico = '.ui-button-icon-primary', on = 'ui-icon-bullet', off = 'ui-icon-radio-on';
			b.find(ico).each(function(){ var icon = $(this); if(b.hasClass(actv)){ icon.removeClass(off).addClass(on); }else{ icon.addClass(off).removeClass(on); }});
		});
	});
	$('.buttonset', this).buttonset();
	

	// Make sure all content editors are synced with their elements before running validation
	$('form', this).bind('submit', function(){
		if(typeof CKEDITOR == 'object'){
			for(var i in CKEDITOR.instances){
				CKEDITOR.instances[i].updateElement();
			}
		}
	});

	// Validation
	$('form:not(.no-validate)', this).each(function(){
		$(this).validate({
			ignore: '',
			messages: {email: "Not valid!"}
		});
	});
	
	// Input Helpers 
	$('input.clearfocus', this).bind('focus', function(){
		if($(this).data('initval') == null){
			$(this).data('initval', $(this).val());
			$(this).toggleClass('focused');
		}
		var initval = $(this).data('initval');
		if(initval == $(this).val()){
			$(this).val('');
		}
	});	
	$('input.clearfocus', this).bind('blur', function(){
		var initval = $(this).data('initval');
		if( $(this).val() == ''){
			$(this).val(initval).toggleClass('focused');
		}
	});
	
	$('.tooltip').each(function(){
		var tooltip = $(this);
		tooltip.hover(
			function(){ 
				tooltip.addClass('tooltip-hover'); 
			}, 
			function(){ 
				tooltip.removeClass('tooltip-hover'); 
			}
		);
	});

});

function roleOutletClick() {
	if ($('#roleOutlet').attr("checked") == "checked" || $('#roleOutlet').attr("checked") == true){
		$('#averagePrintingCostContainer').show();
	} else {
		$('#averagePrintingCostContainer').hide();
	}	
}

function vatRegisteredClick(checkbox, container, input) {
	if (checkbox.attr("checked") == "checked" || checkbox.attr("checked") == true){
		input.addClass("required");
		container.show();
	} else {
		input.removeClass("required");
		container.hide();
	}	
}

$(document).ready(function(){
	// Notification
	$("div.notification", this).notification({container: "#header .wrapper .notifications", autoClose: 10000});
	
	// Load UI
	$(this).triggerHandler('loadui');
	
	// Initial views
	$('.ui-view:not(.ui-view-loaded)', this).each(function(){
		paperight.loadView(this);
	});
	  
	//var documentTitle = document.title;
	//documentTitle = documentTitle.replace("Paperight : ",""); 
	//getBreadcrumb(document, document.location, documentTitle);
});
