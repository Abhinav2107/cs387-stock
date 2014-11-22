
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %> 
<%@ page import="JDBCProject.stocks"%>

<%! boolean flag; %>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

<%
	Vector<stocks> vecObj = (Vector<stocks>) request.getAttribute("vec");
   String name = request.getParameter( "username" );
   String transactions = request.getParameter("transaction");
   session.setAttribute("transaction", transactions);
   session.setAttribute("stockOwned", vecObj);
   session.setAttribute( "username", name );
%>
 <h2> Welcome <%=name%> </h2>
	
	<!--<a href="wishlist.jsp">Wish List</a>
	-->
	<form action="userlogin" method="post">
		<input type="hidden" name="username" VALUE=<%=name%> >
		<input type="hidden" name="type" VALUE="editinfo" >
		<input type="submit" value="Edit Info">
	</form>
	
	
		<br/>Search <input id="searchText" type="text" name="search"> 
		<input id = "searchButton" type="button" value="Search">
			
		<% session.setAttribute("vec",vecObj); %>
	 
	
	<% //out.print(request.getAttribute("vec")) ;
		if(vecObj!= null)
		{
			for(int i=0; i<vecObj.size(); i++)
			{
				 %>
				<form action="transaction" method="post">
					<%out.print(vecObj.get(i).stocksym + " " + vecObj.get(i).quant + " "+ vecObj.get(i).salesprice); %>
					<input type="hidden" name="username" VALUE=<%=name%> >
					<input type="hidden" name="stock" VALUE=<%= vecObj.get(i).stocksym %> >
					<input type="hidden" name="type" VALUE="sell" >
					<input type="submit" value="sell">		
				</form>
				<%
				
			}
		}
		//request.getAttribute("searchresult");
	%>

<div id = "searchRes">

</div>
<script>
	$("#searchButton").click(function()
			{
					$.post("userlogin",
						{
							type: "search",
							search: $("#searchText").val()
						}, 

						function(data, status)
						{
							$("#searchRes").html(data);
						});
			});
</script>

<div id = "transaction">
	<%= request.getAttribute("transaction") %>
</div>