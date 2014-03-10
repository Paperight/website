$(document).ready(function(){

	toggleParams();
	
	$('input:radio[name=htmlLocation][value="Http"]').attr('checked', true);
	
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
		if (this.checked && ($('#pdfConverter').val() == 'DOCRAPTOR_TEST' || $('#pdfConverter').val() == 'DOCRAPTOR') && $('input:radio[name=htmlLocation][value="Http"]').is(':checked')) {
			if (this.value == 'A5') {	       		
				$('input[name="layouts"][value!="A5"]').attr("checked",false);
	        } else {
	        	$('input[name="layouts"][value="A5"]').attr("checked",false);
	          }
	      }
	});

	$('input[name="htmlLocation"]').live("click", function() {
		if (this.value == 'Http') {	    
			if ($('#pdfConverter').val() == 'DOCRAPTOR_TEST'
			|| $('#pdfConverter').val() == 'DOCRAPTOR') {
				$('#cssSelector').removeClass('required');
				$('#cssSelectorContainer').hide();
				$('input[name="layouts"][value!="A5"]').attr("checked",true);
	        	$('input[name="layouts"][value="A5"]').attr("checked",false);
			} else if ($('#pdfConverter').val() == 'PRINCE') {
				$('#cssSelector').addClass('required');
				$('#cssSelectorContainer').show();
				$('input[name="layouts"][value!="A5"]').attr("checked",true);
	        	$('input[name="layouts"][value="A5"]').attr("checked",true);
			}
		} else if (this.value == 'File') {
			$('#cssSelector').addClass('required');
			$('#cssSelectorContainer').show();
			$('input[name="layouts"][value!="A5"]').attr("checked",true);
        	$('input[name="layouts"][value="A5"]').attr("checked",true);
		}
	});
	
	$('#pdfConverter').change(function() {
		toggleParams();
	});

	function toggleParams() {
		if ($('#pdfConverter').val() == 'DOCRAPTOR_TEST'
			|| $('#pdfConverter').val() == 'DOCRAPTOR') {
			$('#pdfConversionNote').show();
			$('#cssSelector').removeClass('required');
			$('#cssSelectorContainer').hide();
			$('input[name="layouts"][value!="A5"]').attr("checked",true);
			if($('input:radio[name=htmlLocation][value="Http"]')) {
				$('input[name="layouts"][value="A5"]').attr("checked",false);
			} else {
				$('input[name="layouts"][value="A5"]').attr("checked",true);
			}
		} else if ($('#pdfConverter').val() == 'PRINCE') {
			$('#pdfConversionNote').hide();
			$('#cssSelector').addClass('required');
			$('#cssSelectorContainer').show();
			$('input[name="layouts"][value!="A5"]').attr("checked",true);
        	$('input[name="layouts"][value="A5"]').attr("checked",true);
		}
    	$('input:radio[name=htmlLocation][value="Http"]').attr('checked', true);
	}
});