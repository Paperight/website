$(function(){

	var notification = function(e, options){
		var self = this;
		self.element = e;
		self.options = $.extend({
			autoClose: 0, 
			container: null, 
			events: [{click: function(e){ self.close(); }}] 
		}, options);
		self.container = $(self.options.container).append(self.element.detach());
		$(self.options.events).each(function(){ 
			for(n in this){ self.element.bind(n + "." + self.namespace, this[n]); } 
		});
		return self.show();
	};
	var notificationAutoCloseReset = function(notification, newId){
		newId = (newId == undefined) ? null : newId;
		if(notification.timeoutId > 0){
			clearTimeout( notification.timeoutId );
			notification.timeoutId = newId;
		};
		return notification;
	};
	
	$.extend(notification.prototype, {

		namespace: "notification",
		timeoutId: null,
		element: null,
		container: null,
		options: null,
		
		show: function(){
			var self = this, e = this.element;
			e.css({top:-50, opacity:0.5}).animate({ opacity: 1, top: '+=50', easing: "easeInOutExpo" }, 600, function() {
				if(self.options.autoClose > 0){
					var timeoutId = setTimeout(function(){ self.close(); }, self.options.autoClose);
					notificationAutoCloseReset(self, timeoutId);
				};
			});
			return this;
		},
		
		close: function(){
			var self = this, e = this.element;
			notificationAutoCloseReset(self);
			e.css({top:0, opacity:1}).animate({ opacity: 0, top: '-=50', easing: "easeOutInExpo" }, 800, function() {
				self.remove();
			});
			return this;
		},
		
		remove: function(){
			this.element.remove();
			return this;
		}
		
	});
	
	$.fn.notification = function(){
		var options = null, method = null;
		if(arguments.length > 1 && typeof arguments[0] == "string"){
			method = arguments[0];
			options = arguments[1];
		};
		if(arguments.length == 1 && typeof arguments[0] == "object"){
			options = arguments[0];
		};
		return this.each(function(){
			var self = $(this), notificationObj = null;
			if(method != null){
				notificationObj = self.data(notification.namespace);
				return notificationObj[notificationObj](options);
			};
			notificationObj = new notification(self, options);
			self.data(notification.namespace, notificationObj);
		});
	};
	
});