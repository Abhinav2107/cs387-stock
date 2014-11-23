<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%
	Vector<CompanyStock> stockVector = (Vector<CompanyStock>) request.getAttribute("stocks");
	Vector<TransRecord> transVector = (Vector<TransRecord>) request.getAttribute("transactions");
	String username = request.getParameter("username");
	session.setAttribute("name", username);
%>
 <h2> Welcome <%=username%> </h2>
<%
	if(stockVector!= null)
	{
		for(int i=0; i<stockVector.size(); i++)
		{
			String symbol = stockVector.get(i).symbol;
			float ltp = stockVector.get(i).ltp;
			 %>
			<table border="1" width="30%" cellpadding="5">
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
                       <td><%=symbol%></td>
                       <td><%=ltp%></td>
                   </tr>
			</tbody>
			</table>
			<%	
		}
	}
%>

<p>
<p>


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
