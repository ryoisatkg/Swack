'use strict'

function removeInputMessage() {
	$("#sendMessageInput").val('');
}

function submitMessage() {
	console.log("test");
	$("#sendChatForm").submit();
	removeInputMessage();
	return false;
}



function logout() {
	let check = window.confirm('ログアウトしますか？');

	if (check) {
		localStorage.removeItem('loginData');
		window.location.href = "LogoutServlet";
	}
}
