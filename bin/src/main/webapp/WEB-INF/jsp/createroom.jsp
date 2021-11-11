<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - ルーム作成画面</title>

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
				<h3>ルームを作成する</h3>
				<p class="input_note_special medium_bottom_margin">ルームとはメンバーがコミュニケーションを取る場所です。特定のトピックに基づいてルームを作ると良いでしょう
					(例: #営業)。</p>
				<form action="#">
					<div class="form-group">
						<div>
							<label><input type="checkbox" id="chk" checked
								data-toggle="toggle" data-on="パブリック" data-off="プライベート"
								data-onstyle="success" data-offstyle="warning"></label> <span
								class="toggle_label">このルームは、ワークスペースのメンバーであれば誰でも閲覧・参加することができます。</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">名前</label> <input id="name"
							class="form-control" type="text" placeholder="# 例:営業" autofocus>
						<span class="name-note">ルームの名前を入力してください。</span>
					</div>
					<div class="form-group">
						<label class="control-label">招待の送信先:(任意)</label> <select
							class="form-control selectpicker" data-live-search="true"
							data-selected-text-format="count > 1" multiple>
							<option value="joho01">情報 太郎１</option>
							<option value="joho02">情報 太郎２</option>
							<option value="joho03">情報 太郎３</option>
							<option value="joho04">情報 太郎４</option>
							<option value="joho05">情報 太郎５</option>
							<option value="joho06">情報 太郎６</option>
							<option value="joho07">情報 太郎７</option>
							<option value="joho08">情報 太郎８</option>
							<option value="joho09">情報 太郎９</option>
							<option value="joho10">情報 太郎１０</option>
							<option value="joho11">情報 太郎１１</option>
							<option value="joho12">情報 太郎１２</option>
							<option value="joho13">情報 太郎１３</option>
							<option value="joho14">情報 太郎１４</option>
							<option value="joho15">情報 太郎１５</option>
							<option value="joho16">情報 太郎１６</option>
							<option value="joho17">情報 太郎１７</option>
							<option value="joho18">情報 太郎１８</option>
							<option value="joho19">情報 太郎１９</option>
							<option value="joho20">情報 太郎２０</option>
						</select> <span class="users-note">このルームに追加したい人を選んでください。</span>
					</div>
					<div class="room-form-btn">
						<button class="btn btn-default">キャンセル</button>
						<button id="send" class="btn btn-default">ルームを作成する</button>
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
