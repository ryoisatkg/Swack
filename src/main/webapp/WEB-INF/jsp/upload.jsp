<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Swack - 画像アップロード</title>
</head>
<body>
	<form method="post" enctype="multipart/form-data">
		<input type="file" name="uploadImage" id="uploadImage">
		<img id="img1" style="width: 100px; height: 100px;" />
		<input type="submit" value="送信">
	</form>
	<script src="js/jquery-3.2.0.min.js"></script>
	<script src="js/upload.js"></script>
</body>
</html>

