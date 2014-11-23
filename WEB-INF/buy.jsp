<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="JDBCProject.TransRecord" %>
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
	Vector<TransRecord> transVector = (Vector<TransRecord>) session.getAttribute("transactions");
	
	String user= (String) request.getAttribute("username");
	String errorMessage = (String) request.getAttribute("error");
	session.setAttribute("stocksym", stocksym);
	session.setAttribute("username", user);
	session.setAttribute("stock", stock);
	if(stock != null) out.print(stock);%>
	
	</center>
<br>

	<form action="transaction" method="post">
		<button type="submit" name="type" VALUE="buymarketorder">Market Order</button>
		<input id="textfield" name="marketquant" type="text" placeholder="quantity"> <br>
	</form>
	
	<form action="transaction" method="post">
		<button type="submit" name="type" VALUE="buylimitorder" >limit Order</button>
		<input id="textfield" name="bidPrice" type="text" placeholder="bid price">
		<input id="textfield" name="limitquant" type="text" placeholder="quantity">
		
	</form>
	
	<table border="1" width="30%" cellpadding="5">
               <thead>
                   <tr>
                       <th colspan="2">Transaction History</th>
                   </tr>
               </thead>
               <tbody>
                   <tr>
                       <td>Time</td>
                       <td>Price</td>
                   </tr>
                   
<%
	if(transVector!= null)
	{
		for(int i=0; i<transVector.size(); i++)
		{
			String time = transVector.get(i).time;
			float price = transVector.get(i).price;
			 %>
                   <tr>
                       <td><%=time%></td>
                       <td><%=price%></td>
                   </tr>
			<%	
		}
	}
%>

			</tbody>
		</table>
	
	<% if(errorMessage!=null) out.print(errorMessage);%>
	<% out.print(session.getAttribute("orderTable")); %>
	</center>
</body>
</html>