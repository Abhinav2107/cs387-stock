<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%! boolean flag = false; %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Company Login Page</title>

</head>
<body>
	<center> 
		<h2>Company Login</h2>
		<% if (flag){ %> <p> Username and Password don't match.</p> <%} %>
      			
		<form action="CompanyLogin" method="post"> 
		<br/>Username:<input type="text" name="username"> 
		<br/>Password:<input type="password" name="password"> 
		<br/><input type="submit" value="Submit">
		<br/><input type="hidden" name="type" VALUE="login">
		
		<a href="CompanyRegister.jsp">Register Here!!</a>
		</form> 
	</center>
</body>
</html>


