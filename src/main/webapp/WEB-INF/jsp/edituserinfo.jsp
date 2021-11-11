<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - ユーザ登録情報変更画面</title>

<link rel="shortcut icon" href="images/favicon.ico">

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="css/bootstrap-select.min.css" rel="stylesheet">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/createroom.css">

</head>

<body>
	<div class="container form-container">
		<div class="row">
			<div class="col-md-12 room-form">
				<h3>ユーザ情報を変更する</h3>
				<form action="UserInfoUpdateServlet" method="post">

					<div>${errmsg}</div>
					
					<div class="form-group">
						<label class="control-label">名前</label> <input id="name"
							name="name" class="form-control" type="text"
							placeholder="# 情報 太郎" value="${user.userName}" autofocus>
						<span class="name-note">変更後の名前を入力してください。</span>
					</div>

					<div class="form-group">
						<label class="control-label">メールアドレス</label> <input id="name"
							name="mail" class="form-control" type="text"
							placeholder="# foo@hcs.ac.jp" autofocus> <span
							class="name-note">変更後のメールアドレスを入力してください。</span>
					</div>

					<div class="form-group">
						<label class="control-label">パスワード</label> <input id="name"
							name="password" class="form-control" type="text"
							placeholder="# foo@hcs.ac.jp" autofocus> <span
							class="name-note">変更後のパスワードを入力してください。</span>
					</div>

					<div class="room-form-btn">
						<a href="MainServlet">キャンセル</a> <input type="submit"
							class="btn btn-default" value="変更する">
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- container -->

	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>

	<script src="js/createroom.js"></script>
</body>
</html>