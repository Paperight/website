(function($){

	paperight.dialog = function(content, options, conf){
		if(this['inst'] != null){
			this.inst.remove();
		}
		this.inst = $('<div class="dialog" />').append(content).appendTo('body');
		$.extend(options, ((conf != null && typeof paperight.dialog.conf[conf] == "object") ? paperight.dialog.conf[conf] : paperight.dialog.conf.basic));
		this.inst.dialog(options).parent().css({position:(options.fixedPosition) ? "fixed" : "absolute"});
		if(typeof options['progressbar'] == 'object'){
			this.inst.dialog('progressbar', options['progressbar']);
		};
		return this.inst.dialog('open');
	};
	paperight.dialog.conf = {
		basic: {autoOpen: false, fixedPosition: true},
		confirm: {title: 'Confirmation', autoOpen: false, resizable: false, height: 120, modal: true, fixedPosition: true}
	};

	$.extend($.ui.dialog.prototype, {
		'progressbar': function(params) {
			var options = $.extend({id: "progressbar", time: 0, stop:false, start: false, value: 0}, params);
			var progressbar = this.element.find('#' + options.id);
			if(progressbar.get(0) == null){
				progressbar = $('<div id="' + options.id + '" />').appendTo(this.element);
			}
			progressbar.progressbar({value: options.value});
			if(options.stop){
				progressbar.find(".ui-progressbar-value").stop(true, true);
			}else if(options.start){
				progressbar.progressbar({value: 1}).find(".ui-progressbar-value").animate({width: 100+"%"}, options.time);
			}
	    }
	});
	$.extend($.ui.dialog.prototype, {
		'addbutton': function(buttonName, func) {
			var buttons = this.element.dialog('option', 'buttons');
	        buttons[buttonName] = func;
	        this.element.dialog('option', 'buttons', buttons);
	    }
	});
	$.extend($.ui.dialog.prototype, {
	    'removebutton': function(buttonName) {
	        var buttons = this.element.dialog('option', 'buttons');
	        delete buttons[buttonName];
	        this.element.dialog('option', 'buttons', buttons);
	    }
	});

})(jQuery);