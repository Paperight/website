$(document).ready(function(){
	
	var products = $('#existingProduct'),
	productSelect = products.find('.selectlists select');

	var postBodyArgs = {
		type: 'POST',
		processData : false,
		contentType: 'application/json; charset=UTF-8'
	};
	
	var loadProductUi = function(products){
		productSelect.empty();
		if(products.success == true && products.data.length > 0){
			for(var i=0; i<products.data.length;++i){
				productSelect.append('<option value="'+ products.data[i].id +'">'+ products.data[i].title +' ('+ products.data[i].identifier +')</option>');
			}
		}else{
			productSelect.append('<option class="empty-list">No products</option>');
		}
	};
	
	var searchProducts = function(query){
		$.ajax($.extend({url: paperight.contextPath + '/pdf/products/search.json', type: 'POST', data: query, success: loadProductUi}, postBodyArgs));
	};
	
	$('#searchProducts').live("click", function() {
		searchProducts($('#productSearchQuery').val());
		return false;
	});

	$('#amendExistingProduct').live("click", function() {
	    if (this.checked) {
	        $('#existingProduct').show();
	    }
	    else {
	        $('#existingProduct').hide();
	    }
	});
	
	$('input[name="layouts"]').live("click", function() {
		if (this.checked) {
			if (this.value == 'A5') {	       		
				$('input[name="layouts"][value!="A5"]').attr("checked",false);
	        } else {
	        	$('input[name="layouts"][value="A5"]').attr("checked",false);
	          }
	      }
	});
	
});