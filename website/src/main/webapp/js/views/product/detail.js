var oneUpPageCount = 0, twoUpPageCount = 0, a5PageCount = 0;

$(document).ready(function(){

	initialiseLayout();
	
	resetForm();
	
	function updateLicenceTotals(e){
		var eQuantity = e.find('#quantity'), quantity = parseInt(eQuantity.val());
		var eOutletCharges = e.find('input[name="outletcharges"]'), outletcharges = accounting.unformat(eOutletCharges.val());
		if(!quantity){ quantity = 1; eQuantity.val(quantity); };
		if(!outletcharges){	outletcharges = 0; }
		var sellingPrice = accounting.unformat(e.find('input[name="productPrice"]').val()), totalSellingPrice = sellingPrice*quantity, approxTotal = totalSellingPrice * accounting.settings.currency.rate;
		e.find(".pricing #creditamount").html(accounting.formatMoney(totalSellingPrice, "", ((totalSellingPrice % 1 == 0) ? 0 : 2)));
		e.find(".pricing #approxselling").html(accounting.formatMoney(approxTotal));
		e.find(".pricing #sellingprice").html(accounting.formatMoney(approxTotal + outletcharges));
	};
		
	$('#purchase-licence-form').bind("change", function(){
		updateLicenceTotals($(this));
	});
	
	$("#purchase-licence-form #outletcharges").keyup(function(){
		$(this).closest("form").triggerHandler("change");
	});
	
	
	
	$('.btn-minus').bind('click', function (){
		if($('#quantity').val() != 1){
			$('#quantity').val((parseFloat ($('#quantity').val())-1));
			calculatePrintingCosts();
			//$('#purchase-licence-form').triggerHandler('change');
		}
	});
	
	$('.btn-plus').bind('click', function (){
		$('#quantity').val(parseFloat ($('#quantity').val())+1);
		calculatePrintingCosts();
		//$('#purchase-licence-form').triggerHandler('change');
	});
	
	$('.disable-overlay').fadeTo(0, 0.25).click(function(){
		paperight.dialog('<p class="message">You must be logged in to use the product calculator.</p>', {
			title: "Please log in", height: 145, modal: true,
			buttons: {
				"Ok": function(){ $(this).dialog("close"); }
			}
		});
		return false;
	}).find(':input').attr('disabled', true);
		
	$('input:radio[name="layout"]').click(function(){
		calculatePrintingCosts();
	});
	
	function initialiseLayout() {
		var firstLayout = $('input:radio[name="layout"]:first');
		firstLayout.attr("checked", true);
		if ($('input:radio[name="layout"]').length === 1) {
			//firstLayout.attr('disabled', true);
			var label = firstLayout.nextAll('label').first();
			label.addClass('disabled');
		}
		calculatePrintingCosts();
	};

	function calculatePrintingCosts() {
		var selectedLayout = $('input:radio[name="layout"]:checked').val();
		var pageCount = Math.ceil(twoUpPageCount / 2);
		if (selectedLayout === 'ONE_UP') {
			pageCount = Math.ceil(oneUpPageCount / 2);
		} else if (selectedLayout === 'A5') {
			pageCount = Math.ceil(a5PageCount / 4);
		}
		var eOutletCharges = $("#purchase-licence-form #outletcharges");
		if (selectedLayout === 'PHOTOCOPY') {
		    eOutletCharges.val("");
		} else {
		    var eQuantity = $("#purchase-licence-form #quantity"), quantity = parseInt(eQuantity.val());
		    var printCosts = ((paperight.averagePrintingCost * pageCount) + paperight.averageBindingCost) * quantity;
		    printCosts = printCosts.toFixed(2);
		    eOutletCharges.val(printCosts);
		}
		updateLicenceTotals(eOutletCharges.closest("form"));
	};
	
	$('.btn-login').live("click", function(){
		var btn = $(this);
		paperight.dialog('<p class="message">Download will start shortly. You may close this window at any time.</p>', {
			title: "Download", height: 145, modal: true,
			buttons: {
				"Ok": function(){ 
					paperight.loadView(btn.closest('.ui-view'));
					$(this).dialog("close"); 
				}
			}
		});
		paperight.downloadLicence(btn.attr('value'));
		return false;
	});	
	
});

function resetForm(){

	$("#firstName").val("");
	$("#lastName").val("");
	$("#phoneNumber").val("");
	$("#quantity").val(1);
	
	$("#firstName").DefaultValue("First name (will appear on document)");
	$("#lastName").DefaultValue("Last name (will appear on document)");
	$("#phoneNumber").DefaultValue("Phone number (will appear on document)");
	
};
