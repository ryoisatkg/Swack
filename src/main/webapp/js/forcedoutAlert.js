'use strict'

function forcedoutAlert(userid) {
	var check = window.confirm('退会させますか？');
	let form = $('.forcedOutForm[data-userid="' + userid + '"]');
	console.log(form);
	if (check) {
		form.submit();
		return true;
	}
	return false;
}
