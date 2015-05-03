<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="../css/image/logo2.png" />
<title>Manager</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css">

<!-- Custom styles for this template -->
<link href="css/dashboard.css" type="text/css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="http://www.hzuhelper.com">hzuhelper
					@惠大校园助手</a>
			</div>
			<div class="navbar-collapse collapse" style="padding-right: 20px">
				<ul class="nav navbar-nav navbar-right">
					<li><a>Dear Jarvis. welcome</a></li>
					<li><a href="login?cmd=exit">安全退出</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li><a href="#">Overview</a></li>
				</ul>
				<ul class="nav nav-sidebar">
					<li><a class="active" href="takeoutres">外卖店铺管理</a></li>
					<li><a href="takeoutmenu">菜单管理</a></li>
					<li><a href="user">查看用户</a></li>
					<li><a href="">树洞管理</a></li>
					<li><a href="">树洞评论管理</a></li>
				</ul>
			</div>