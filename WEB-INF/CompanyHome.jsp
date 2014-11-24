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
<center>
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
                       <td><% session.setAttribute("stocksym",symbol); out.println(symbol);%></td>
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
		<form name="login-form" class="login-form" action="PayDividends" method="post">
		 <input type="text" name="amount" class="input username" placeholder="amount" />
		 <input type="submit" name="payDividends" value="Pay Dividends" class="register" />
		 </form>
		 

		<form name="login-form" class="login-form" action="LaunchIPO" method="post">
		<div class="content">
        <input name="numShares" type="text" class="input numShares" placeholder="# of Shares" />
        <input name="price" type="text" class="input price" placeholder="Price per Share" />
        </div>

        <div class="footer">
        <input type="submit" name="submit" value="Launch IPO" class="button" />
        </div>
		</form>
		 
		 <% if(errorMessage!=null) out.print(errorMessage);%>
		</center>
