<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Swack - ユーザ一覧</title>

<link rel="shortcut icon" href="images/favicon.ico">

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/bootstrap-toggle.min.css" rel="stylesheet">
<link href="css/bootstrap-select.min.css" rel="stylesheet">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/joinmember.css">

</head>

<body>
	<c:forEach var="userInfo" items="${users}">
		<c:if test="${isRoot}">
			<div class="well">
			${ userInfo.userName}
			<form action="ForcedOutServlet" class="forcedOutForm" data-userid="${userInfo.userId}" method="post">
				<input type="hidden" name="forcedOutUserId"
					value="${userInfo.userId}"> <input type="button" class="btn btn-default" value="退会"
					onclick="forcedoutAlert('${userInfo.userId}')">
			</form>
			</div>
		</c:if>
	</c:forEach>
	<a href="MainServlet">メイン画面へ戻る</a>
	<!-- container -->

	<script src="js/jquery-3.2.0.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
	<script type="text/javascript" src="js/forcedoutAlert.js"></script>
</body>
</html>