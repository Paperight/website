$(function(){

	$('form#updateEmail #confirmNewEmail').rules("add", {equalTo: '#newEmail'});
	
});