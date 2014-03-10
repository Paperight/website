$(function(){

	$('form#resetPassword #confirmNewPassword').rules("add", {equalTo: '#newPassword'});
	
});