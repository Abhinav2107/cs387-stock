package JDBCProject;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class LaunchIPO extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	
	public void init() throws ServletException
	{
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "aman";
		String pass = "password";

    	try
    	{
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbURL2, user, pass);
    	}
    	catch (Exception e)
    	{	e.printStackTrace();	}
	}

    public void destroy()
    {
    	try
    	{	conn.close();	}
    	catch(Exception e)
    	{	System.out.println(e);	}
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int numShares = Integer.parseInt(request.getParameter("numShares"));
		float price = Float.parseFloat(request.getParameter("price"));
		
		HttpSession session = request.getSession(true);
		String symbol = (String) session.getAttribute("symbol");
		String companyName = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("companyID");
		
		try
		{
			conn.setAutoCommit(false);
			String sql = "insert into stocks values(?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, symbol);
			preparedStatement.setString(2, companyName);
			preparedStatement.setFloat(3, price);
			preparedStatement.setString(4, id);
			preparedStatement.executeUpdate();
			
			sql = "insert into sellOrders values(?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, symbol);
			preparedStatement.setString(2, "admin");
			preparedStatement.setFloat(3, price);
			preparedStatement.setInt(4, numShares);
			preparedStatement.setTimestamp(5, getCurrentTimeStamp());
			preparedStatement.executeUpdate();
			
			request.setAttribute("error", "IPO Launched");			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/CompanyHome.jsp");
			rd.forward(request, response);
			
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (SQLException e)
		{	System.out.println(e.getMessage());	
			try {
				conn.rollback();conn.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
	}
	
	public static Timestamp getCurrentTimeStamp()
	{
		Date today = new Date();
		return new Timestamp(today.getTime());
	}
}
