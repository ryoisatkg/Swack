<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - お気に入り</title>
<link rel="shortcut icon" href="images/favicon.ico">

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="css/bootstrap-select.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/chatlogs.css">
</head>
<body>
	<div id="logArea" class=" pre-scrollable chat-body"
		style="width: 100%;">
		<c:forEach var="chatlog" items="${chatloglist}">
			<div class="panel panel-default" style="margin-bottom: 17px;">
				<div class="panel-body">
					<div class="row">
						<div class="pull-left" style="padding: 10px;">
							<img
								style="object-fit: cover; object-position: 50% 50%; height: 50px;"
								src="images/${chatlog.userId}.png"
								onerror="this.src='images/profile.png';">
						</div>
						<div class="pull-left">
							<p class="log-name">
								${chatlog.userName}
								<c:if test="${chatlog.withdrawal}">
														(退会済)
													</c:if>
								<span class="log-time">${chatlog.createdAt}</span>
							</p>

							<p>${chatlog.message}</p>

							<c:if
								test="${isRoot or (chatlog.userId != null and chatlog.userId == user.userId)}">
								<a class="btn btn-default btn-xs"
									href="ChatUpdateServlet?chatLogId=${chatlog.chatLogId}">編集</a>
								<a class="btn btn-default btn-xs"
									href="ChatDeleteServlet?chatLogId=${chatlog.chatLogId}">削除</a>
							</c:if>
							<c:if test="${chatlog.favorited and (!favolist)}">
							<a class="btn btn-default btn-xs"
									href="RemoveFavoriteServlet?chatLogId=${chatlog.chatLogId}">
									⭐ <!-- 絵文字の星 -->
								</a>
												
							</c:if>
							<c:if test="${chatlog.favorited and (favolist)}">
							<a class="btn btn-default btn-xs"
									href="RemoveFavoriteServlet?chatLogId=${chatlog.chatLogId}&favorite=true">
									⭐ <!-- 絵文字の星 -->
								</a>
												
							</c:if>
							<c:if test="${!chatlog.favorited}">
								<a class="btn btn-default btn-xs"
									href="AddFavoriteServlet?chatLogId=${chatlog.chatLogId}">☆
									<!-- いわゆるJISの中空の星 -->
								</a>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
	<script type="text/javascript" src="js/load_on_bottom.js"></script>
</body>
</html>