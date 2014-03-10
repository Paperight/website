(function($){

	// H-Tabs
	$.fn.hTabs = function(options){
		var settings = $.extend({
			select: $.noop,
		}, options);
		return this.each(function(){
			var self = $(this),
				tabs = self.find('.ui-htabs'),
				leftPanel = self.find('.ui-htabs-panel-left'),
				rightPanel = self.find('.ui-htabs-panel-right');
			var autoHeight = function(){
				self.find('.ui-htabs-panel')
				.css({height: 'auto'}) // auto height to make the UI adjust itself automatically, then read the height
				.css({height: self.height()});
			};
			leftPanel.on({
				mouseenter: function(){
					$(this).addClass('hover');
				},
				mouseleave: function(){
					$(this).removeClass('hover');
				},
				click: function(){
					tabs.find('.thumbnail.selected').removeClass('selected');
					$(this).addClass('selected');
					options.select.call(this, $(this).closest('li').data());
					return false;
				}
			}, '.thumbnail');
			self.on('change', function(){
				autoHeight();
			});
			autoHeight();
		});
	};
	
})(jQuery);