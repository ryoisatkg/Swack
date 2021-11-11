'use strict';
//ローディング非表示処理
function endLoading() {
	$('.js-loading div').fadeOut(1500, function() {
		$('.js-loading').fadeOut(500);
	})
}

$(function() {
	const fn = function() {
		endLoading();
		console.log(location.search);
		if(location.search.includes('loadingCheck')) {
			history.replaceState(null, "", "MainServlet");
		}
	};
	fn();
	setTimeout(fn, 5000);
});