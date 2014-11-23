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
		String dbURL2 = "jdbc:postgresql://10.42.0.1/cs387";
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
				String sql = "select companyID from companies where username = ? and password = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				
				String companyID = null;
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next())
				{	companyID = rs.getString(1);	}
				 
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
						String stockSymbol = null;
						
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
						
						request.setAttribute("companyID", companyID);
						request.setAttribute("username", username);
						request.setAttribute("stocks", stocksVector);
						
						sql = "select tradedPrice, transDateTime from transactions where stockSymbol = ?";
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
						request.setAttribute("transactions", transVector);
						
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/CompanyHome.jsp");
						rd.forward(request, response);
					}
					catch (SQLException e) 
					{	System.out.println(e.getMessage());	}		
				}
				
				else
				{	response.sendRedirect("/CompanyLogin.jsp?flag=true");	}
			}
			catch (SQLException e)
			{	System.out.println(e.getMessage());	}
		}
		else if(type.equals("register"))
		{
			String companyID = request.getParameter("id");
			String companyName = request.getParameter("name");
			int phone = Integer.parseInt(request.getParameter("phone"));
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
				preparedStatement.setInt(5, phone);
				preparedStatement.setString(6, address);
				preparedStatement.setString(7, email);
				
				preparedStatement.executeUpdate();
				response.sendRedirect("/CompanyLogin.jsp?flag=false");
			}
			catch(SQLException e)
			{	System.out.println(e.getMessage());	}
		}
	}
}
