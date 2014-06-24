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
	
	$('#layoutset').each(function(){
		var layout = $(this).find('input[name="layout"]');
		var layouts = $('.layout-type');
		if(layouts.length > 1){
			$('.layout-type').click(function(){
				$('.layout-type').removeClass('layout-selected');
				$(this).addClass('layout-selected');
				layout.val($(this).attr('id'));
				calculatePrintingCosts();
				return false;
			});
			$('.layout-type').hover(function(){ $(this).addClass('layout-hover'); }, function(){ $(this).removeClass('layout-hover'); });
		}else{
			layouts.each(function(){
				$(this).addClass('layout-selected');
				layout.val($(this).attr('id'));
			});
		};
	});
	
	function initialiseLayout() {
		$('.layout-type').removeClass('layout-selected');
		var elayout = $('.layout-type:first');
		elayout.addClass('layout-selected');
		$("#purchase-licence-form #layout").val(elayout.attr('id'));
		calculatePrintingCosts();
	};

	function calculatePrintingCosts() {
		var pageCount = Math.ceil(twoUpPageCount / 2);
		if ($("#purchase-licence-form #layout").val() == 'ONE_UP') {
			pageCount = Math.ceil(oneUpPageCount / 2);
		} else if ($("#purchase-licence-form #layout").val() == 'A5') {
			pageCount = Math.ceil(a5PageCount / 4);
		}
		var eQuantity = $("#purchase-licence-form #quantity"), quantity = parseInt(eQuantity.val());
		var printCosts = ((paperight.averagePrintingCost * pageCount) + paperight.averageBindingCost) * quantity;
		printCosts = printCosts.toFixed(2);
		var eOutletCharges = $("#purchase-licence-form #outletcharges");
		eOutletCharges.val(printCosts);
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
