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
        <input type="hidden" name="type" value="login" />
        <!-- <div class="pass-icon"></div> -->
        </div>

        <div class="footer">
        <input type="submit" name="submit" value="Register" class="button" />
        <input type="button" name="register" value="Login" class="register" onclick="location.href = 'login.jsp'"/>
        </div>

    </form>

</div>
<div class="gradient"></div>
<!--
        <form method="post" action="userlogin">
            <center>
            <table border="1" width="30%" cellpadding="5">
                <thead>
                    <tr>
                        <th colspan="2">Enter Information Here</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Name</td>
                        <td><input type="text" name="name" value="" /></td>
                    </tr>
                    <tr>
                        <td>Phone</td>
                        <td><input type="text" name="phone" value="" /></td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td><input type="text" name="email" /></td>
                    </tr>
                    <tr>
                        <td>User Name</td>
                        <td><input type="text" name="username"/></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="password"  /></td>
                    </tr>
                    <tr>
                        <td>Address</td>
                        <td><input type="text" name="address"  /></td>
                    </tr>
                    <tr>
                        <td>Balance</td>
                        <td><input type="text" name="balance" /></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="Submit" /></td>
                        <td><input type="reset" value="Reset" /></td>
                    </tr>
                    <tr>
                        <td colspan="2">Already registered!! <a href="login.jsp">Login Here</a></td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="type" VALUE="register">
            </center>
        </form>-->
    </body>
</html>

