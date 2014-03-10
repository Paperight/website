$(function(){
	var eAmount = $('#topupForm input[name="amount"]');
	var ePrice = $('#topupForm .price');
	eAmount.bind('keyup change', function(){
		var amount = accounting.unformat(eAmount.val());
		ePrice.html(accounting.formatMoney((amount*accounting.settings.currency.rate)));
	});
});