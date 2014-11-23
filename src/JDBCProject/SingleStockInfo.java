package JDBCProject;


//this one gets the stockInfo of a specific stock according to either
//stockSymbol or stockName as specified by the user and sends all of the
//information to a displayStock page

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.security.Principal;

public class SingleStockInfo extends HttpServlet
{
	Connection conn1 = null;
	PreparedStatement stockSymbolStatement = null;
	
	public void init() throws ServletException
	{
		String dbURL2 = "jdbc:postgresql://10.105.1.12/cs387";
     String user = "db14v051001";
     String pass = "14v051001";

     try
     {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			stockSymbolStatement = conn1.prepareStatement("select * from stocks where ?=?");
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
		String parameter = request.getParameter("parameter");
		String value = request.getParameter("value");
			
		try
     {
			stockSymbolStatement.setString(1, parameter);
			stockSymbolStatement.setString(2, value);
			ResultSet rs = stockSymbolStatement.executeQuery();
			
			if(rs.next())
			{
				String stockSymbol = rs.getString(1);
				String stockName = rs.getString(2);
				double stockLTP = Double.parseDouble(rs.getString(3));
				response.sendRedirect(""); // this is where we would forward the information to the display page
			} 
			else
			{	response.sendRedirect("");	} // forward back to displayStock page, stockSymbol did not exist
		}
		catch (SQLException e)
		{
			System.out.println(e);
			response.sendRedirect(""); // forward back to displayStock page, some error occurred
		}
	}
}
