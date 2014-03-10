$(function(){

	$('form#registration #confirmPassword').rules("add", {equalTo: '#user\\.password'});
	$('form#registration #confirmEmail').rules("add", {equalTo: '#user\\.email'});
	$('form#registration #captchaYourAnswer').rules("add", {equalTo: '#captchaTheAnswer'});
	
});