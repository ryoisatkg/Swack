$(function() {
	$('.selectpicker').selectpicker({
		size: 10
	});
	$("input[type=text]").keypress(function(ev) {
		if ((ev.which && ev.which === 13) || (ev.keyCode && ev.keyCode === 13)) {
			return false;
		} else {
			return true;
		}
	});
	if ($("#name").val().length == 0) {
		$("#send").css("color", "rgba(44,45,48,.75)");
		$("#send").css("background", "#e8e8e8");
		$("#send").prop("disabled", true);
	}
	$("#name").on("keydown keyup keypress change", function() {
		if ($(this).val().length < 1) {
			$("#send").css("color", "rgba(44,45,48,.75)");
			$("#send").css("background", "#e8e8e8");
			$("#send").prop("disabled", true);
		} else {
			$("#send").css("color", "#ffffff");
			$("#send").css("background", "#008952");
			$("#send").prop("disabled", false);
		}
	});

	$('#chk').on('change', function() {
		var thisValue = $(this).prop('checked');
		var state = thisValue ? true : false;
		if (state) {
			$("h3").text("ルームを作成する");
			$(".toggle_label").text("このルームは、ワークスペースのメンバーであれば誰でも閲覧・参加することができます。");
		} else {
			$("h3").text("プライベートルームを作成する");
			$(".toggle_label").text("このルームは招待によってのみ参加または確認することができます。");

		}
	});
});