package JDBCProject;


//this one creates a new stock and adds it to our database. if an
//error occurs, it says error occurred and asks for new information

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.security.Principal;

public class CreateNewStock extends HttpServlet
{
	Connection conn1 = null;
	PreparedStatement newStock = null;
	
	public void init() throws ServletException
	{
		String dbURL2 = "jdbc:postgresql://10.105.1.12/cs387";
     String user = "db14v051001";
     String pass = "14v051001";

     try
     {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			newStock = conn1.prepareStatement("insert into stocks values(?,?,?,0);");
			System.out.println("init"+conn1);
     }
     catch (Exception e)
     {	System.out.println(e);	}
	}

	public void destroy()
	{
		try
		{
			conn1.close();
			System.out.println("close");
		}
		catch(Exception e)
		{	System.out.println(e);	}
 }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException
	{
		String stockSymbol = request.getParameter("stockSymbol");
		String stockName = request.getParameter("stockName");
		double stockPrice = Double.parseDouble(request.getParameter("stockPrice"));
			
		try
     {
			newStock.setString(1, stockSymbol);
			newStock.setString(2, stockName);
			newStock.setDouble(3, stockPrice);
			
			newStock.executeUpdate();
			
			response.sendRedirect(""); // redirect to required page
		}
		catch (SQLException e)
		{
			System.out.println(e);
			response.sendRedirect(""); // redirect to same page, say error occurred so try again
		}
	}
}
