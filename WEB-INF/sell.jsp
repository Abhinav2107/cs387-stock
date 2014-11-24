<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="JDBCProject.TransRecord" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sell Stock</title>
<script>
</script>
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script>
google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Time', 'Price'],
        
<%
	if(transVector!= null)
	{
		for(int i=0; i<transVector.size(); i++)
		{
			String time = transVector.get(i).time;
			float price = transVector.get(i).price;
		    String[] pieces = time.split("-| |:");
            out.print("[(new Date("+pieces[0]+", "+pieces[1]+", "+pieces[2]+", "+pieces[3]+", "+pieces[4]+", "+Float.parseFloat(pieces[5])+")).getTime(), "+price+"]");
            if(i < transVector.size() - 1)
                out.println(",");
        }
	}
%>


        ]);

        var options = {
          title: 'Stock Performance'
        };

        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

        chart.draw(data, options);
      }
    </script>
    <style>
            #header {
    background-color:#49afcd;
    color:white;
    text-align:left;
    padding:5px;
    </style>
</head>
<body style = "text-align: center;">
<div id = "header">
 <img src="images/logo.png" alt="logo" height="400" width="400">
</div>
<center>
<h2> Stock Movement </h2> 
<center>
<table class = "table table-striped" style = "text-align: center; margin: 0 auto; width: 100%;">
<tr>
<td>
<div id="chart_div" style="width:600; height:400"></div>
</td>
<td>

<%	if(stock != null) out.print(stock);%>
	
	</center>
<br>
</td>
</td>
<tr>
<td>
	<form action="transaction" method="post">
		<button type="submit" name="type" style = "padding-bottom: 10px;" class = "btn btn-primary" VALUE="sellmarketorder">Market Order</button>
		<input id="textfield" name="marketquant" style = "padding-top: 5px;" class = "input" type="text" placeholder="quantity"> <br>
	</form>
</td>
<td>	
	<form action="transaction" method="post">
		<button type="submit" name="type" style = "padding-bottom: 10px;" class = "btn btn-primary" VALUE="selllimitorder" >Limit Order</button>
		<input id="textfield" name="askPrice" style = "padding-top: 5px;" class = "input" type="text" placeholder="ask price">
		<input id="textfield" name="limitquant" style = "padding-top: 5px;" class = "input" type="text" placeholder="quantity">
		
	</form>
</td>
</tr>
<tr>
<td>	
	<table border="1" width="100%" cellpadding="5">
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
</td>
<td>	
	<% if(errorMessage!=null) out.print(errorMessage);%>
		<table>
	<tr>
	<td><% out.print(session.getAttribute("buyOrderTable")); %></td>
	<td>    </td>
	<td><% out.print(session.getAttribute("sellOrderTable")); %></td>
	</tr>
	</table>
</td>
</tr>
</table>
	</center>
</body>
</html>
