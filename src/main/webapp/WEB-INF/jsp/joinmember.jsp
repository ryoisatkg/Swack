<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - メンバー招待画面</title>

<link rel="shortcut icon" href="images/favicon.ico">

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="css/bootstrap-select.min.css" rel="stylesheet">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/joinmember.css">

</head>

<body>
	<div class="container form-container">
		<div class="row">
			<div class="col-md-12 member-form">
				<h3>他のユーザを${roomInfo.roomName}に招待する</h3>
				<form action="JoinMemberServlet" method="post">
					<input type="hidden" name="roomId" value="${roomInfo.roomId}"></input>
					<div class="form-group">
						<label class="control-label">招待の送信先:</label> <select id="userIds"
							name="needsJoinUsers"
							class="form-control selectpicker" data-live-search="true"
							data-selected-text-format="count > 1" multiple>
							<c:forEach var="user" items="${users}">
								<option value="${user.userId}">${user.userName}</option>
							</c:forEach>
						</select> <span class="users-note">このルームに追加したい人を選んでください。</span>
					</div>
					<div class="member-form-btn">
						<a href="MainServlet?roomId=${roomInfo.roomId}"><input type="button" class="btn btn-default" value="キャンセルする"></a>
						<input type="submit" class="btn btn-default" value="招待する">
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

	<script src="js/joinmember.js"></script>
</body>
</html>
