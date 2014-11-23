package JDBCProject;

import java.sql.*;
import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class CompanyLogin extends HttpServlet
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
		String type = request.getParameter("type");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(type.equals("login"))
		{
			try
			{
				String sql = "select companyID, companyName, stocksymbol from company where username = ? and password = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				
				HttpSession session = request.getSession(true);
				String companyID = null;
				String companyName = null;
				String stockSymbol = null;
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next())
				{
					companyID = rs.getString(1);
					companyName = rs.getString(2);
					stockSymbol = rs.getString(3);
					session.setAttribute("companyID", companyID);
					session.setAttribute("username", username);
					session.setAttribute("symbol", stockSymbol);
					session.setAttribute("name", companyName);
				}
				 
				if(companyID!=null)
				{
					Vector<CompanyStock> stocksVector = new Vector<CompanyStock>();
					Vector<TransRecord> transVector = new Vector<TransRecord>();
					try
					{
						sql = "select stockSymbol, ltp from stocks where companyID = ?";
						preparedStatement = conn.prepareStatement(sql);
						preparedStatement.setString(1, companyID);
						rs = preparedStatement.executeQuery();
						stockSymbol = null;
						
						String result1= "<table> <thead> <tr> <h3>Company Shares</h3> </tr> </thead> <tbody> ";
						while(rs.next())
						{
		                    stockSymbol = rs.getString(1);
		                    float ltp = Float.parseFloat(rs.getString(2));
		                    
		                    CompanyStock s = new CompanyStock(stockSymbol, companyID, ltp);
		                    stocksVector.addElement(s);
		                    
		                    result1 = result1.concat("<tr>"
		                    		+ "<td>" + stockSymbol + "</td>"
		                    		+ "<td>" + companyID + "</td>"
		                    		+ "<td>" + ltp + "</td>" + "</tr>");
						}
						result1= result1.concat("</tbody> </table>");
						
						
						sql = "select tradedPrice, transDateTime from transactions where stockSymbol = ? order by transdatetime asc;";
						preparedStatement = conn.prepareStatement(sql);
						preparedStatement.setString(1, stockSymbol);
						rs = preparedStatement.executeQuery();
						
						while(rs.next())
						{
							float tradedPrice = Float.parseFloat(rs.getString(1));
							String time = rs.getString(2);
							
							TransRecord temp = new TransRecord(stockSymbol, time, tradedPrice);
							transVector.addElement(temp);
						}
						
						session.setAttribute("stocks", stocksVector);
						session.setAttribute("transactions", transVector);
						
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/CompanyHome.jsp");
						rd.forward(request, response);
					}
					catch (SQLException e) 
					{	System.out.println(e.getMessage());	}		
				}
				
				else
				{	response.sendRedirect("/JDBCProject/CompanyLogin.jsp?flag=true");	}
			}
			catch (SQLException e)
			{	System.out.println(e.getMessage());	}
		}
		else if(type.equals("register"))
		{
			String companyID = request.getParameter("id");
			String stockSymbol = request.getParameter("symbol");
			String companyName = request.getParameter("name");
			long  phone = Long.parseLong(request.getParameter("phone"));
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			
			try
			{
				String sql = "insert into company values (?, ?, ?, ?, ?, ?, ?, ?);";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, companyID);
				preparedStatement.setString(2, companyName);
				preparedStatement.setString(3, username);
				preparedStatement.setString(4, password);
				preparedStatement.setLong(5, phone);
				preparedStatement.setString(6, address);
				preparedStatement.setString(7, email);
				preparedStatement.setString(8, stockSymbol);
				preparedStatement.executeUpdate();
				
				response.sendRedirect("/JDBCProject/CompanyLogin.jsp");
			}
			catch(SQLException e)
			{	System.out.println(e.getMessage());	}
		}
	}
}
