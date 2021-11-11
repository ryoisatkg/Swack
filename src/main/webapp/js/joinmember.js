$(function() {
	$('.selectpicker').selectpicker({
		size: 10
	});
	if ($("#users").val().length == 0) {
		$("#send").css("color", "rgba(44,45,48,.75)");
		$("#send").css("background", "#e8e8e8");
		$("#send").prop("disabled", true);
	}
	$("#users").on("keydown keyup keypress change", function() {
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

});