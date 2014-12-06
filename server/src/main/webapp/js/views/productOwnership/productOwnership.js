$(function(){
	
	var ownershipUi = $('#productownership'),
		companies = ownershipUi.find('#companySelect'),
		companySelect = companies.find('.selectlists select'),
		companySearch = companies.find('.selectfilters'),
		
		companyProducts = ownershipUi.find('#companyProducts'),
		companyProductSelect = companyProducts.find('.selectlists select'),
		companyProductSearch = companyProducts.find('.selectfilters'),
		
		orphanedProducts = ownershipUi.find('#orphanedProducts'),
		orphanedProductSelect = orphanedProducts.find('.selectlists select'),
		orphanedSearch = orphanedProducts.find('.selectfilters');
	
	var postBodyArgs = {
		type: 'POST',
		processData : false,
		contentType: 'application/json; charset=UTF-8'
	};
	
	var selectedCompanyId = null;
	
	var getSelectedItems = function(){
		var products = [];
		$(this).find('option:selected').each(function(){
			if(!$(this).is('.empty-list')){
				products.push($(this).data());
			};
		});
		return products;
	};
	
	var loadCompanyUi = function(companies){
		companySelect.empty();
		if(companies.success == true && companies.data.length > 0){
			for(var i=0; i<companies.data.length;++i){
				companySelect.append('<option data-companyid="'+ companies.data[i].id +'">'+ companies.data[i].name +' ('+ companies.data[i].companyAdminEmail +')</option>');
			}
		}else{
			companySelect.append('<option class="empty-list">No companies</option>');
		}
	};
	var searchCompanies = function(query){
		$('#company-search-loader').toggle();
		$.ajax($.extend({
			url : paperight.contextPath
					+ '/product/ownership/companies/search.json',
			type : 'POST',
			data : query,
			success : loadCompanyUi,
			complete : function() {
				$('#company-search-loader').toggle();
			}
		}, postBodyArgs));
	};
	
	var loadCompanyProductsUi = function(products){
		companyProductSelect.empty();
		if(products.success == true && products.data.length > 0){
			for(var i=0; i<products.data.length;++i){
				companyProductSelect.append('<option data-companyid="'+ selectedCompanyId +'" data-productid="'+ products.data[i].id +'">'+ products.data[i].title +' (' + products.data[i].identifier +')</option>');
			}
		}else{
			companyProductSelect.append('<option class="empty-list">No products</option>');
		}
	};
	var searchCompanyProducts = function(query, companyId){
		$('#company-product-search-loader').toggle();
		$.ajax($.extend({
			url : paperight.contextPath + '/product/ownership/' + companyId
					+ '/products/search.json',
			type : 'POST',
			data : query,
			success : loadCompanyProductsUi,
			complete : function() {
				$('#company-product-search-loader').toggle();
			}
		}, postBodyArgs));
	};
	
	var loadOrphanProductUi = function(products){
		orphanedProductSelect.empty();
		if(products.success == true && products.data.length > 0){
			for(var i=0; i<products.data.length;++i){
				orphanedProductSelect.append('<option data-productid="'+ products.data[i].id +'">'+ products.data[i].title +' (' + products.data[i].identifier +')</option>');
			}
		}else{
			orphanedProductSelect.append('<option class="empty-list">No products</option>');
		}
	};
	var searchOrphanProducts = function(query){
		$('#orphan-product-search-loader').toggle();
		$.ajax($.extend({
			url : paperight.contextPath
					+ '/product/ownership/orphan-products/search.json',
			type : 'POST',
			data : query,
			success: loadOrphanProductUi,
			complete : function() {
				$('#orphan-product-search-loader').toggle();
			}
		}, postBodyArgs));
	};
	
	var unassignProducts = function(ids){
		$.ajax($.extend({
			url: paperight.contextPath + '/product/ownership/unassign-products.json',
			data: JSON.stringify(ids),
			success: function(data){
				if(data){
					searchCompanyProducts('',selectedCompanyId);
					searchOrphanProducts('');
				}
			}
		}, postBodyArgs));
	};
	
	var assignProducts = function(ids){
		$.ajax($.extend({
			url: paperight.contextPath + '/product/ownership/'+ selectedCompanyId +'/assign-products.json',
			data: JSON.stringify(ids),
			success: function(data){
				if(data){
					searchCompanyProducts('',selectedCompanyId);
					searchOrphanProducts('');
				}
			}
		}, postBodyArgs));
	};
	

	companySelect.on('change', function(){
		var items = getSelectedItems.call(this);
		selectedCompanyId = (items.length > 0) ? items[0].companyid : null;
		searchCompanyProducts('',selectedCompanyId);
		return false;
	});
	companyProductSelect.on('change', function(){
		var items = getSelectedItems.call(this);
		return false;
	});
	orphanedProductSelect.on('change', function(){
		var items = getSelectedItems.call(this);
		return false;
	});
	companyProducts.find('.move-out').on('click', function(){
		var items = getSelectedItems.call(companyProductSelect);
		if(items.length > 0 ){
			var ids = [];
			for(var i=0; i<items.length; i++){
				ids.push(items[i].productid);
			};
			unassignProducts(ids);
		};
		return false;
	});
	orphanedProducts.find('.move-in').on('click', function(){
		var items = getSelectedItems.call(orphanedProductSelect);
		if(items.length > 0 && selectedCompanyId != null){
			var ids = [];
			for(var i=0; i<items.length; i++){
				ids.push(items[i].productid);
			};
			assignProducts(ids);
		};
		return false;
	});
	
	companySearch.find('form').on('submit', function(){
		searchCompanies($(this).find('input[type="text"]').val());
		return false;
	});
	companyProductSearch.find('form').on('submit', function(){
		searchCompanyProducts($(this).find('input[type="text"]').val(), selectedCompanyId);
		return false;
	});
	orphanedSearch.find('form').on('submit', function(){
		searchOrphanProducts($(this).find('input[type="text"]').val());
		return false;
	});
	
	selectedCompanyId = companySelect.children('option:first').attr('selected', 'selected').data('companyid');

});