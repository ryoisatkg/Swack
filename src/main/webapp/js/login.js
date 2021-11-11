'use strict';

$(window).on('load', function() {
	$('body').css('display', 'none');
	if ($('#errorMsg').text().length == 0) {
		var oldData = localStorage.getItem('loginData');
		if (oldData) {
			var realData = JSON.parse(oldData);
			if (realData.autoLogin) {
				$('#mailAddress').val(realData.mailAddress);
				$('#password').val(realData.password);
				$('#loginForm').submit();
			} else {
				$('body').css('display', 'block');
			}
		} else {
			$('body').css('display', 'block');
		}
	} else {
		localStorage.removeItem('loginData');
		$('body').css('display', 'block');
	}
});

function login() {
	var save = $('#save').prop('checked');// 「ログイン状態を保持する」にチェックがついていたら
	if (save) {
		var loginData = {
			mailAddress: $('#mailAddress').val(),
			password: $('#password').val(),
			autoLogin: save
		};
		localStorage.setItem('loginData', JSON.stringify(loginData));
	}
}
