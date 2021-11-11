<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - ルーム一覧</title>

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
				<h3>公開ルームに参加する</h3>

				<c:forEach var="roomInfo" items="${roomInfos}">
					<c:if test="${roomInfo.roomId != 'R0000'}">
					<div class="well">

						<c:if test="${roomInfo.roomName != ''}">
						${roomInfo.roomName} (参加人数:${roomInfo.memberCount}人)
						</c:if>
						<c:if test="${roomInfo.roomName == ''}">
						(空文字の名前です) (参加人数:${roomInfo.memberCount}人)
						</c:if>

						<c:if test="${not roomInfo.joined}">
							<form action="JoinRoomServlet" method="post" target="_parent">
								<input type="hidden" name="roomid" value="${roomInfo.roomId}" />

								<div class="form-group" >
									<input type="submit" class="btn btn-success" value="参加する" />
								</div>
							</form>
						</c:if>
						<c:if test="${roomInfo.joined}">
							(参加済)
							<form action="LeaveRoomServlet" method="post" target="_parent">
								<input type="hidden" name="roomid" value="${roomInfo.roomId}" />

								<div class="form-group">
									<input type="submit" class="btn btn-danger" value="退室する" />
								</div>
							</form>
						</c:if>
					</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</div>
	<!-- container -->

	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
</body>
</html>