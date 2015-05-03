<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="shortcut icon" href="../css/image/logo2.png" />
<title>Login</title>
<!-- Bootstrap core CSS -->
<link href="../css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="css/Login.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="container">
		<form class="form-signin" method="post">
			<h2 class="form-signin-heading">${message }</h2>
			<input type="text" name="email" class="form-control"
				placeholder="Email address" required autofocus> <input
				name="password" type="password" class="form-control"
				placeholder="Password" required> <label class="checkbox">
				<input type="checkbox" value="remember-me"> Remember me
			</label>
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		</form>
	</div>
</body>
</html>
