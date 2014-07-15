(function($){
	
	var PaperightContext = function(options){
		if(typeof options == "object"){
			this.load(options);
		}
		return this;
	};
	$.extend(PaperightContext.prototype, {
		
		contextPath: '',
		ajax: { timeout: 10000 },
		credits: 0,
		averagePrintingCost: 0,
		averageBindingCost: 0,
		number: { 
			precision: 0, 
			thousand: ",", 
			decimal: "."	
		},
		currency: { 
			rate: 1, 
			precision: 2, 
			symbol: "", 
			format: "%s%v", 
			thousand: ",",  
			decimal: "." 
		},
		
		bind: function(event, fn){
			$(this).bind(event, fn);
		},
		
		trigger: function(event, params){
			$(this).triggerHandler(event, params);
		},
		

		reload: function(){
			var self = this;
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'GET', url: this.contextPath + '/js/paperight.context.json' }, {
				success: function(data){
					self.load(data);
					self.trigger('update');
				}
			}));
		},
		
		load: function(options){
			$.extend(true, this, options);
			window.accounting.settings = {
				number: this.number, 
				currency: this.currency
			};
			return this;
		},
		
		cancelLicence: function(licenceId, params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'GET', url: this.contextPath + '/licence/' + licenceId + '/cancel' }, params));
			return this;
		},
		
		resetLicence: function(licenceId, params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'GET', url: this.contextPath + '/licence/' + licenceId + '/reset' }, params));
			return this;
		},
		
		purchaseLicence: function(params){
			if(typeof params == 'object' && typeof params['data'] == 'object' && !params['data']['outletcharges']){
				params.data.outletcharges = 0;
			};
			$.ajax($.extend({ timeout: this.ajax.timeout, type: 'POST', url: this.contextPath + '/licence/purchase' }, params));
			return this;
		},
		
		topup: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, type: 'POST', url: this.contextPath + '/account/topup' }, params));
			return this;
		},
		
		generateLicence: function(licenceId, params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/licence/'+ licenceId +'/generate' }, params));
			return this;
		},
		
		generatePublisherStatement: function(params){
			window.open(this.contextPath + '/publisher-earnings/statement?' + params.data, '_self');
			return this;
		},
		
		generateOutletStatement: function(params){
			window.open(this.contextPath + '/licence/statement?' + params.data, '_self');
			return this;
		},
		
		downloadLicence: function(licenceId, params){
			window.open(this.contextPath + '/licence/'+ licenceId +'/download', '_self');
			return this;
		},
		
		previewProduct: function(params){
			window.open(this.contextPath + '/licence/preview?' + $.param(params.data), '_self');
			return this;
		},
		
		licenceInvoice: function(licenceId) {
			window.open(this.contextPath + '/licence/' + licenceId + '/invoice', '_self');
			return this;
		},
		
		publisherEarningInvoice: function(publisherEarningId) {
			window.open(this.contextPath + '/publisher-earnings/' + publisherEarningId + '/invoice', '_self');
			return this;
		},
		
		previewConversion: function(params){
			window.open(this.contextPath + '/pdf/preview?' + $.param(params.data), '_self');
			return this;
		},
		
		sendActivateUser: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/activate' }, params));
			return this;
		},
		
		sendSuggestBook: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/suggest-book' }, params));
			return this;
		},
		
		saveCompanyUser: function(params){
			if(params.data.id == undefined || params.data.id == null | params.data.id == ''){
				$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/users/save' }, params));
			}else{
				$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/users/save/' + params.data.id }, params));
			}
		},

		saveCompany: function(params){
			if(params.data['company.id'] == undefined || params.data['company.id'] == null | params.data['company.id'] == ''){
				$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/save' }, params));
			}else{
				$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/save/' + params.data['company.id'] }, params));
			}
		},
		
		removeCompanyUser: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/users/remove' }, params));
		},
		
		removeCompany: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/account/company/remove' }, params));
		},
		
		getCompanyUser: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'GET', url: this.contextPath + '/account/company/'+ params.companyId +'/'+ params.userId +'.json' }, params));
		},
		
		getCompany: function(params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'GET', url: this.contextPath + '/account/company/'+ params.companyId +'.json' }, params));
		},

		sendCancelPublisherPaymentRequest: function(publisherPaymentRequestId, params){
			$.ajax($.extend({ timeout: this.ajax.timeout, cache: false, type: 'POST', url: this.contextPath + '/publisher-earnings/request-payment/cancel/' + publisherPaymentRequestId }, params));
			return this;
		},
		
		loadView: function(view, params){
			view = $(view);
			var url = view.data('view-id');
			if(url == null){
				url = view.attr('view-id');
				view.data('view-id', url);
			}
			$.ajax($.extend({ 
				success: function(html){
					html = $(html);
					html.find('a[href^="\/"][target="_self"]').click(function(e){
						view.data('view-id', $(this).attr('href'));
						paperight.loadView(view);
						return false;
					});
					view.addClass('ui-view-loaded').clearQueue().stop().html(html);
					$(document).triggerHandler('loadui', view);
					view.show();
				},
				cache: false,
				timeout: this.ajax.timeout, 
				type: 'POST', 
				url: url, 
			}, params));
			return this;
		},
		
		resetView: function(view, params){
			view = $(view);
			if(!view.hasClass('ui-view-loaded'))
				return;
			view.data('view-id', null);
			paperight.loadView(view);
			return this;
		}
		
	});

	window.paperight = new PaperightContext();

})(jQuery);
