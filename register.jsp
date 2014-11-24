<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
<div id="wrapper" style="width: 600px;">

    <form name="login-form" class="login-form" action="userlogin" method="post" style="width: 600px;">

        <div class="header">
        <h2>User Registration</h2>
        </div>

        <div class="content">
        <input name="name" type="text" class="input username" placeholder="Name" />
        <input name="phone" type="text" class="input password" placeholder="Phone" />
        <input name="email" type="text" class="input password" placeholder="Email" />
        <input name="username" type="text" class="input password" placeholder="Username" />
        <input name="password" type="password" class="input password" placeholder="Password" />
        <input name="address" type="text" class="input password" placeholder="Address" />
        <!-- <div class="user-icon"></div> -->
        <input name="balance" type="text" class="input password" placeholder="Balance" />
        <input type="hidden" name="type" value="register" />
        <!-- <div class="pass-icon"></div> -->
        </div>

        <div class="footer">
        <input type="submit" name="submit" value="Register" class="button" />
        <input type="button" name="register" value="Login" class="register" onclick="location.href = '/JDBCProject/login.jsp'"/>
        </div>

    </form>

</div>
<div class="gradient"></div>
    </body>
</html>

