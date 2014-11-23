
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %> 
<%@ page import="JDBCProject.stocks"%>

<%! boolean flag; %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Home</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <style>

@import url(http://fonts.googleapis.com/css?family=Bree+Serif);

        .button {
        padding: 11px 25px;

        font-family: 'Bree Serif', serif;
        font-weight: 300;
        font-size: 18px;
        color: #fff;
        text-shadow: 0px 1px 0 rgba(0,0,0,0.25);

        background: #56c2e1;
        border: 1px solid #46b3d3;
        border-radius: 5px;
        cursor: pointer;

        box-shadow: inset 0 0 2px rgba(256,256,256,0.75);
        -moz-box-shadow: inset 0 0 2px rgba(256,256,256,0.75);
        -webkit-box-shadow: inset 0 0 2px rgba(256,256,256,0.75);
        }   

.button:hover {
    background: #3f9db8;
    border: 1px solid rgba(256,256,256,0.75);

    box-shadow: inset 0 1px 3px rgba(0,0,0,0.5);
    -moz-box-shadow: inset 0 1px 3px rgba(0,0,0,0.5);
    -webkit-box-shadow: inset 0 1px 3px rgba(0,0,0,0.5);
}

.button:focus {
    position: relative;
    bottom: -1px;

    background: #56c2e1;

    box-shadow: inset 0 1px 6px rgba(256,256,256,0.75);
    -moz-box-shadow: inset 0 1px 6px rgba(256,256,256,0.75);
    -webkit-box-shadow: inset 0 1px 6px rgba(256,256,256,0.75);
}

.input {
    width: 100px;
    padding: 15px 25px;

    font-family: "HelveticaNeue-Light", "Helvetica Neue Light", "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif;
    font-weight: 400;
    font-size: 14px;
    color: #9d9e9e;
    text-shadow: 1px 1px 0 rgba(256,256,256,1.0);

    background: #fff;
    border: 1px solid #fff;
    border-radius: 5px;

    box-shadow: inset 0 1px 3px rgba(0,0,0,0.50);
    -moz-box-shadow: inset 0 1px 3px rgba(0,0,0,0.50);
    -webkit-box-shadow: inset 0 1px 3px rgba(0,0,0,0.50);
}


        </style>
    </head>

<body>
<%
	Vector<stocks> vecObj = (Vector<stocks>) request.getAttribute("vec");
   String name = request.getParameter( "username" );
   String transactions = request.getParameter("transaction");
   session.setAttribute("transaction", transactions);
   session.setAttribute("stockOwned", vecObj);
   session.setAttribute( "username", name );
%>
<div id = "welcome" style = "width: 67%; float: left;"> 
<h2><span class = "text"> Welcome <%=name%> </span></h2>
	
	<!--<a href="wishlist.jsp">Wish List</a>
	-->
	<form action="userlogin" method="post">
		<input type="hidden" name="username" VALUE=<%=name%> >
		<input type="hidden" name="type" VALUE="editinfo" >
		<input type="submit" class = "btn btn-primary" value="Edit Info">
	</form>
	
</div>
    <div id = "search" style = "width:33%; float: left; text-align: center;">
    <br /><br /><br /><br />
        <input id="searchText" type="text" class = "input" name="search">&nbsp;&nbsp;&nbsp; 
        <input id = "searchButton" type="button" class = "btn btn-primary" value="Search">
    </div>
		<br/>
        <br />
    <div id = "wrapper" style = "width:100%;">
    <div id = "owned" style = "width:32%; height: 100%; text-align: center; margin-right: 10px; background-color: #d3d3d3; float:left; position: relative;">			
		<% session.setAttribute("vec",vecObj); %>
	 
    <h3> Owned Stocks </h3>	
                <table class = "table">
                <tr style = "font-size: 18px;">
                <td style = "width: 30%; align: center;"><strong>Symbol</strong></td>
                <td style = "width: 20%; align: center;"><strong>Quantity</strong></td>
                <td style = "width: 20%; align: center;"><strong>LTP</strong></td>
                <td style = "width: 30%; align: center;"></td>
                </tr>
                <br />
	<% //out.print(request.getAttribute("vec")) ;
		if(vecObj!= null)
		{
			for(int i=0; i<vecObj.size(); i++)
			{
                %>

                <tr style = "font-size: 18px;">
                <td style = "width: 30%; align: center;"><% out.print(vecObj.get(i).stocksym); %></td>
                <td style = "width: 20%; align: center;"><% out.print(vecObj.get(i).quant); %></td>
                <td style = "width: 20%; align: center;"><% out.print(vecObj.get(i).salesprice); %></td>
                <td style = "width: 30%; align: center;">
                 <form action="transaction" method="post">
					<input type="hidden" name="username" VALUE=<%=name%>
					<input type="hidden" name="stock" VALUE=<%= vecObj.get(i).stocksym %>
					<input type="hidden" name="type" VALUE="sell" >
					<input type="submit" class = "btn btn-info" value="sell">		
				</form>
                </td>
                </tr>
				<%
				
			}
		}
		//request.getAttribute("searchresult");
	%>
    </table>
</div>

<div id = "transaction" style = "width:32%; height: 100%; float: left; text-align: center; margin-right: 10px; background-color: #d3d3d3;">
	<%= request.getAttribute("transaction") %>
</div>

<div id = "searchRes" style = "width:32%; height: 100%; float: left; text-align: center; margin-right: 10px; background-color: #d3d3d3;">

</div>
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
                            $("#buyButton").addClass("btn");
                            $("#buyButton").addClass("btn-info");
						});
			});
</script>

</body>
</html>
