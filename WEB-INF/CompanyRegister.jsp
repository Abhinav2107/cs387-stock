<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company Registration</title>
    </head>
    <body>
        <form method="post" action="CompanyLogin">
            <center>
            <table border="1" width="30%" cellpadding="5">
                <thead>
                    <tr>
                        <th colspan="2">Enter Information Here</th>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                        <td>Company ID</td>
                        <td><input type="text" name="id" value="" /></td>
                    </tr>
                    <tr>
                        <td>Company Name</td>
                        <td><input type="text" name="name" value="" /></td>
                    </tr>
                    <tr>
                        <td>Phone</td>
                        <td><input type="text" name="phone" value="" /></td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td><input type="email" name="email" /></td>
                    </tr>
                    <tr>
                        <td>Username</td>
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
        </form>
    </body>
</html>
