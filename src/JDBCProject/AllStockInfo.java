package JDBCProject;


//this one implements a rudimentary output as well, creating a table
//of all the stocks in the database dynamically

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.security.Principal;

public class AllStockInfo extends HttpServlet
{
	Connection conn1 = null;
	PreparedStatement allStocksStatement = null;
	
	public void init() throws ServletException
	{
		String dbURL2 = "jdbc:postgresql://10.105.1.12/cs387";
     String user = "db14v051001";
     String pass = "14v051001";

     try
     {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			allStocksStatement = conn1.prepareStatement("select * from stocks order by ?");
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
		String orderedBy = request.getParameter("orderedBy");
			
		try
     {
			allStocksStatement.setString(1, orderedBy);
			ResultSet rs = allStocksStatement.executeQuery();
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Stocks</title>");
			out.println("</head>");
			
			out.println("<body>");
			
			out.println("<table>");
			out.println("	<tr>");
			out.println("		<th>Stock Symbol</th>");
			out.println("		<th>Stock Name</th>");
			out.println("		<th>Last Traded Price</th>");
			out.println("	</tr>");
			
			while(rs.next())
			{
				String stockSymbol = rs.getString(1);
				String stockName = rs.getString(2);
				double stockLTP = Double.parseDouble(rs.getString(3));
				
				out.println("	<tr>");
				out.println("		<th>" + stockSymbol + "</th>");
				out.println("		<th>" + stockName + "</th>");
				out.println("		<th>" + stockLTP + "</th>");
				out.println("	</tr>");
				
			}
			
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		}
		catch (SQLException e)
		{
			System.out.println(e);
			response.sendRedirect("");
		}
	}
}
