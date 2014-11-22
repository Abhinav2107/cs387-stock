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
<center>
<%  
	//if(!stock.equals(""))  <%
	String stocksym = (String) request.getAttribute("stocksym");
	String stock = (String) request.getAttribute("resultStock");
	String user= (String) request.getAttribute("username");
	String errorMessage = (String) request.getAttribute("error");
	session.setAttribute("stocksym", stocksym);
	session.setAttribute("username", user);
	session.setAttribute("stock", stock);
	if(stock != null) out.print(stock);%>
	
	</center>
<br>

	<form action="transaction" method="post">
		<button type="submit" name="type" VALUE="marketorder">Market Order</button>
		<input id="textfield" name="marketquant" type="text" placeholder="quantity"> <br>
		<!--<a href = "marketorder.jsp"> Market Order</a>--> <br>
		<!--<a href = "limitorder.jsp"> Limit Order</a>
		-->
	</form>
	<form action="transaction" method="post">
		<button type="submit" name="type" VALUE="limitorder" >limit Order</button>
		<input id="textfield" name="askPrice" type="text" placeholder="ask price">
		<input id="textfield" name="limitquant" type="text" placeholder="quantity">
		
	</form>
	
	</center>
	<% if(errorMessage!=null) out.print(errorMessage);%>
</body>
</html>