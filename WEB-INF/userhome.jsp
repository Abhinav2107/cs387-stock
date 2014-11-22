
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %> 
<%@ page import="JDBCProject.stocks"%>

<%! boolean flag; %>

<%
	Vector<stocks> vecObj = (Vector<stocks>) request.getAttribute("vec");
   String name = request.getParameter( "username" );
   session.setAttribute( "theName", name );
%>
 <h2> Welcome <%=name%> </h2>
	
	<!--<a href="wishlist.jsp">Wish List</a>
	-->
	<form action="userlogin" method="post">
		<input type="hidden" name="username" VALUE=<%=name%> >
		<input type="hidden" name="type" VALUE="editinfo" >
		<input type="submit" value="Edit Info">
	</form>
	
	<form action="userlogin" method="post">
		<br/>Search <input type="text" name="search"> 
		<input type="submit" value="Submit">
		<br/><input type="hidden" name="type" VALUE="search"><br/>	
		<% session.setAttribute("vec",vecObj); %>
	</form>
	 
	
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

