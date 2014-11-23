<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%  String flag = (String) request.getParameter("flag");
	String errorMessage = (String) request.getAttribute("error");%> 
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Virtual Stock Exchange - Login</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="css/style.css" rel="stylesheet" media="screen">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<div id="wrapper">

    <form name="login-form" class="login-form" action="userlogin" method="post">

        <div class="header">
        <h2>User Login</h2>
		<% if (flag != null){ out.print("Username and Password do not match");} %>
        </div>

        <div class="content">
        <input name="username" type="text" class="input username" placeholder="Username" />
        <input name="password" type="password" class="input password" placeholder="Password" />
        <input type="hidden" name="type" value="login" />
        </div>

        <div class="footer">
        <input type="submit" name="submit" value="Login" class="button" />
        <input type="button" name="register" value="Register" class="register" onclick="location.href = 'register.jsp'"/>
        </div>

    </form>

</div>
<div class="gradient"></div>
</body>
</html>


