<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sell Stock</title>
<script>
</script>
</head>
<body>
<center>
<h2> Stock Movement </h2> 
<%=request.getAttribute("resultStock")%>

<% String stock = request.getParameter("stocksym");
	String user= request.getParameter("username");
	session.setAttribute("stocksym", stock);
	session.setAttribute("username", user);%>
<br>

	<form action="transaction" method="post">
		<button type="submit" name="type" VALUE="marketorder">Market Order</button>
		<input id="textfield" name="marketquant" type="text" placeholder="quantity"> <br>
		<!--<a href = "marketorder.jsp"> Market Order</a>--> <br>
		<!--<a href = "limitorder.jsp"> Limit Order</a>
		-->
		<button type="submit" name="type" VALUE="limitorder" >limit Order</button>
		<input id="textfield" name="askPrice" type="text" placeholder="ask price">
		<input id="textfield" name="limitquant" type="text" placeholder="quantity">
		
	</form>
	</center>
</body>
</html>