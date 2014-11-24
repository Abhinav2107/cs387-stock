<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="JDBCProject.CompanyStock" %>
<%@ page import="JDBCProject.TransRecord" %>
<%
	Vector<CompanyStock> stockVector = (Vector<CompanyStock>) session.getAttribute("stocks");
	Vector<TransRecord> transVector = (Vector<TransRecord>) session.getAttribute("transactions");
	String username = (String)session.getAttribute("username");
	String errorMessage = (String) request.getAttribute("error");
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Home</title>
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
}

        </style>
    </head>

<body>

<div id = "header">
<img src="images/logo.png" alt="logo" height="300" width="300">
</div>

<table class = "table table-striped" style = "text-align: center; margin: 0 auto; width: 100%;">
<tr>
<td width = "50%">
<input type = "button" class = "btn btn-large btn-info" value = "Welcome <%=username%>" />
 <!-- <h2> Welcome <%=username%> </h2> -->
</td>
<td rowspan = "3" width = "50%">

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
	else
	{
		 %>
         <tr>
             <td>No transactions yet...</td>
         </tr>
	<%
	}

%>
			</tbody>
		</table>

</td>
</tr>
<tr>
<td>
<%
	if(stockVector!= null)
	{
		for(int i=0; i<stockVector.size(); i++)
		{
			String symbol = stockVector.get(i).symbol;
			float ltp = stockVector.get(i).ltp;
			 %>
			<table border="1" width="100%" cellpadding="5">
               <thead>
                   <tr>
                       <th colspan="2">Current Standing of Stock</th>
                   </tr>
               </thead>
               <tbody>
                   <tr>
                       <td>Stock Symbol:</td>
                       <td>Last Traded Price</td>
                   </tr>
                   <tr>
                       <td><% session.setAttribute("stocksym",symbol); out.println(symbol);%></td>
                       <td><%=ltp%></td>
                   </tr>
			</tbody>
			</table>
			<%	
		}
	}
%>

</td>
</tr>
<tr>
<td>
<div id="chart_div" style="width:600; height:400"></div>
</td>
</tr>
<tr>
<td>
		<form name="login-form" class="login-form" action="PayDividends" method="post">
		 <input type="text" name="amount" class="input" placeholder="amount" />
		 <input type="submit" name="payDividends" value="Pay Dividends" class="btn btn-primary" />
		 </form>
</td>		 
<td>
		<form name="login-form" class="login-form" action="LaunchIPO" method="post">
        <input name="numShares" type="text" class="input" placeholder="# of Shares" />
        <input name="price" type="text" class="input" placeholder="Price per Share" />
        <input type="submit" name="submit" value="Launch IPO" class="btn btn-primary" />
		</form>
</td>
</tr>
</table>	 
		 <% if(errorMessage!=null) out.print(errorMessage);%>

</body>
</html>
