
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Swack - メイン画面</title>
<link rel="shortcut icon" href="images/favicon.ico">

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="css/bootstrap-select.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/main-original.css">

<link rel="stylesheet" href="css/loading.css">
</head>
<body>
	<div class="container-fluid">
		<header class="header global-header">
			<div>${user.userName}</div>
			<div class="dropdown pull-right">
				<!-- 切替ボタンの設定 -->
				<button class="btn dropdown-toggle" type="button" id="dropdownMenu"
					data-toggle="dropdown">
					・・・ <span class="caret"></span>
				</button>
				<!-- ドロップダウンメニューの設定 -->
				<ul class="dropdown-menu dropdown-menu-right" role="menu"
					aria-labelledby="dropdownMenu">
					<li><a tabindex="-1" href="UserInfoUpdateServlet">ユーザ情報変更</a></li>
					<li><a tabindex="-1" href="UploadServlet">ユーザアイコン設定</a></li>
					<li><c:if test="${isRoot}">
							<a tabindex="-1" href="ForcedOutServlet">ユーザ一覧</a>
						</c:if></li>
					<li class="divider"></li>
					<!-- 横仕切り線 -->
					<li><a tabindex="-1" onclick="logout();">ログアウト</a></li>
				</ul>
			</div>
			<!-- /.dropdown -->
			<c:if test="${!currentRoomInfo.directed}">
				<div class="join-member-button pull-right">
					<a href="JoinMemberServlet?roomId=${currentRoomInfo.roomId}"><button>＋</button></a>
				</div>
			</c:if>


		</header>
		<section class="main">
			<div class="row row-no-gutters below-header-height">
				<div class="col-xs-3 below-header-height rooms-menu"
					style="margin-top: 0; margin-bottom: 0;">

					<div style="padding-top: 0; padding-bottom: 0;">

						<h2>Swack</h2>
						<a href="MainServlet?favorite=true"> お気に入り</a>
						<details open>
							<summary>
								ルーム <a href="CreateRoomServlet"><button>＋</button></a>
							</summary>
							<c:forEach var="room" items="${viewableRooms}">
								<a class="list-name" href="MainServlet?roomId=${room.roomId}">#
									${room.roomName}</a>
								<br>
							</c:forEach>
							<a class="list-name" data-toggle="modal" data-target="#joinRoom">+
								ルームに参加する </a>
							<br>

						</details>
						<details open>
							<summary> ダイレクト </summary>
							<c:forEach var="DM" items="${directMessages}">
								<a class="list-name" href="MainServlet?roomId=${DM.roomId}">#
									${DM.roomName}</a>
								<br>
							</c:forEach>

							<a class="list-name" data-toggle="modal"
								data-target="#makeDMroom" href="">+ DMをはじめる</a>
							<br>
						</details>
					</div>

				</div>
				<!--left -->
				<div class="col-xs-9 no-gutters below-header-height"
					style="padding: 0; border-style: solid; border-color: lightgray; border-width: 1px;">
					<div class="chat-header">
						<h2>
							<c:if test="${!favorite}">
								${currentRoomInfo.roomName}
								<c:if test="${!currentRoomInfo.directed}">
										(${currentRoomInfo.memberCount}名)
									</c:if>
							</c:if>
							<c:if test="${favorite}">
								お気に入り一覧
							</c:if>
						</h2>

					</div>
					<c:if test="${!favorite}">
						<div class="container-fluid chat-body" style="padding: 0;">
							<iframe
								src="ViewRoomChatlogServlet?roomId=${currentRoomInfo.roomId}"
								name="chatlogIframe"
								style="border: none; width: 100%; height: 100%;"></iframe>
						</div>
					</c:if>
					<c:if test="${favorite}">
						<div class="container-fluid favorite-body" style="padding: 0;">
							<iframe src="ViewFavoriteServlet" name="chatlogIframe"
								style="border: none; width: 100%; height: 100%;"></iframe>
						</div>
					</c:if>

					<c:if test="${isJoined && !favorite}">
						<div class="chat-footer">
							<form id="sendChatForm" action="MainServlet" method="post">
								<input type="hidden" name="roomId"
									value="${currentRoomInfo.roomId}">
								<div class="form-wrap">
									<input type="text" id="sendMessageInput" name="message">
									<input type="submit" value="送信">
								</div>
							</form>
						</div>
					</c:if>

				</div>
				<!--contents -->

			</div>

		</section>
		<!--main -->
	</div>
	<c:if test="${loadingCheck != null}">
		<div class="loading js-loading">
			<div id="progressbar">
				<span id="loading"></span>
				<div id="load">loading</div>
			</div>

		</div>
	</c:if>
	<div class="modal fade" id="makeDMroom" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					新しいDM部屋を追加
					<button type="button" class="btn btn-default pull-right"
						data-dismiss="modal">閉じる</button>
				</div>
				<div class="modal-body">
					<div class="embed-responsive embed-responsive-16by9">
						<iframe class="embed-responsive-item" src="MakeNewDMRoomServlet"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="joinRoom" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					部屋へ参加、脱退
					<button type="button" class="btn btn-default pull-right"
						data-dismiss="modal">閉じる</button>
				</div>
				<div class="modal-body">
					<div class="embed-responsive embed-responsive-16by9">
						<iframe class="embed-responsive-item" src="ViewRoomServlet"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>


	<!-- container -->
	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
	<script src="js/main.js"></script>
	<script src="js/loading.js"></script>

</body>
</html>