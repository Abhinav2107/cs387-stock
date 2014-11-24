package JDBCProject;


import java.sql.*;
import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class PayDividends extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	
	public void init() throws ServletException {
		//Open the connection here

		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "aman";
		String pass = "password";

		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection(dbURL2, user, pass);
			//st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			System.out.println("init"+conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		float amount = Float.parseFloat(request.getParameter("amount"));
		HttpSession session = request.getSession(true);
		String stocksym = (String) session.getAttribute("stocksym");
		Map<String, Float> map = new HashMap<String, Float>();
		
		System.out.println(stocksym + " " + amount);
		try
		{
			conn.setAutoCommit(false);
			//System.out.println(stocksym + " in this " + amount);
			String sql = "select username, quantity from ownership where stockSymbol = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, stocksym);
			//System.out.println(stocksym + " " + amount);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				String username = rs.getString(1);
				int stockquant = rs.getInt(2);
				//System.out.println(username + " " + stockquant);
				if(! map.containsKey(username)) map.put(username, stockquant*amount);
				else map.put(username, map.get(username) + stockquant*amount);
			}	
			
			for(String username : map.keySet()) {
			    Float value = map.get(username);
			    System.out.println(username + " " + value);
			    sql = "select username, balance from users where username = ?;";
				preparedStatement = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, username);
				rs = preparedStatement.executeQuery();
				while(rs.next()){
				float currentBalance = rs.getFloat(2);
				currentBalance += value;
				rs.updateFloat(2, currentBalance);
				rs.updateRow();
				}			    
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			//HttpSession session = request.getSession(true);
			request.setAttribute("error", "Dividends Paid");
			//session.setAttribute("companyID", companyID);
			session.setAttribute("username", (String)session.getAttribute("username"));
			session.setAttribute("stocks", (Vector<CompanyStock>) session.getAttribute("stocks"));
			session.setAttribute("transactions", (Vector<TransRecord>) session.getAttribute("transactions"));
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/CompanyHome.jsp");
			rd.forward(request, response);
		}
		catch (SQLException e)
		{	System.out.println(e.getMessage());	try {
			conn.rollback(); conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}}
	}
}
