(function($){

	// Grid view
	paperight.grid = function(id, options){
		this.id = id;
		this.options = options;
		this.element = $(this.id).find('.grid');
		return this;
	};
	
})(jQuery);