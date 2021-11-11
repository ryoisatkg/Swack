<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - DM作成画面</title>

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
				<h3>DMを開始する</h3>

				<c:forEach var="user" items="${users}">
					<c:if test="${user.userId != 'U0000'}">
						<div class="well">
							${user.userName}
							<c:if test="${roomMap[user.userId] == null}">
								<form action="MakeNewDMRoomServlet" method="post" target="_parent">
									<input type="hidden" name="partnerUserId" value="${user.userId}">
									<button class="btn btn-default">新しい部屋を作成</button>
								</form>
							</c:if>
							<c:if test="${roomMap[user.userId] != null}">
								<button class="btn btn-default" onclick="jump('MainServlet?roomId=${roomMap[user.userId].roomId}')">DMへGO</button>
							</c:if>
						</div>
					</c:if>
				</c:forEach>
				<a href="MainServlet">
					<button class="btn btn-default">キャンセル</button>
				</a>
			</div>
		</div>
	</div>
	<!-- container -->

	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
	<script type="text/javascript" src="js/jump.js"></script>
</body>
</html>