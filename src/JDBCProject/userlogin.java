package JDBCProject;

//import stocks;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;
/**
 * Servlet implementation class userlogin
 */
public class userlogin extends HttpServlet {
	
	Connection conn1 =null;
	Statement st =null;
	PreparedStatement preparedStatement = null;
public void init() throws ServletException {
      //Open the connection here
	
	String dbURL2 = "jdbc:postgresql://localhost/cs387";
    String user = "aman";
    String pass = "password";

    try {
		Class.forName("org.postgresql.Driver");
	
		conn1 = DriverManager.getConnection(dbURL2, user, pass);
		st = conn1.createStatement();
		System.out.println("init"+conn1);
    	} catch (Exception e) {
		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public void destroy() {
     //Close the connection here
    	try{
    		conn1.close();
    		System.out.println("close");
    	}catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }
    
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userlogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String type = request.getParameter("type");
		String sql = null;
		ResultSet rs = null;
		boolean status = false;
		if(type.equals("login"))
		{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			sql = "select * from users where username = ? and password = ?";
			
			try{
				preparedStatement = conn1.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				
				rs = preparedStatement.executeQuery();
				status=rs.next();
				System.out.println(username);
				System.out.println(password);
				System.out.println(status);
				 
				if(status)
				{ // once you are logged in you can send the data related to all the stocks owned
					// by this user
					Vector<stocks> v = new Vector<stocks>();
					try{
						sql = "select * from ownership where username = ?";
						preparedStatement = conn1.prepareStatement(sql);
						preparedStatement.setString(1, username);
						rs = preparedStatement.executeQuery();
						
				        String retval = "";
				        String retval1 = "";
				        int retval2;
				        float retval3; 
				       String result1= "<table>" + " <thead>" +
				       		"<tr>" + " <h3>Shares Owned</h3>  </tr> </thead> <tbody> ";
				        
						while(rs.next())
						{
		                    retval = rs.getString(1);
		                    retval1 = rs.getString(2);
		                    retval2 = rs.getInt(3);
		                    retval3 = rs.getFloat(4);
		                    
		                   // System.out.println(retval);
		                   // System.out.println(retval1);
		                   // System.out.println(retval2);
		                   // System.out.println(retval3);
		                    stocks s = new   stocks(retval1, retval2, retval3);
		                    v.addElement(s);
		                    
		                    result1 = result1.concat("<tr>"
		                    		//+ "<td>" + retval + "</td>"
		                    		+ "<td>" + retval1 + "</td>"
		                    		+ "<td>" + Integer.toString(retval2) + "</td>"
		                    		+ "<td>" + Float.toString(retval3) + "</td>"
		                    		+ "</tr>");
		                   // System.out.println(s.stocksym);
						}
						result1= result1.concat("</tbody> </table>");
						System.out.println(v.get(0).stocksym + " " + v.get(0).quant);
						request.setAttribute("result", result1 );
						request.setAttribute("username", username);
						request.setAttribute("vec", v);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/userhome.jsp");
						rd.forward(request, response);
						} catch (SQLException e) 
						{
						System.out.println(e.getMessage());
			 
						}	
					
						
				}
				
				else
				{
					response.sendRedirect("/JDBCProject/login.jsp?flag=" + true);
				}
			}
			catch (SQLException e) {
				 
				System.out.println(e.getMessage());
	 
			}
			
		}
		
		else if(type.equals("search"))
		{
			String str=request.getParameter("search");
			Vector<stocks> v = (Vector<stocks>)request.getAttribute("vec");
			//System.out.println("here" + v.get(0).stocksym + " " + v.get(0).quant);
			sql = "select * from stocks where stockSymbol = ?";
			String result2= "<table>" + " <thead>" +
       		"<tr>" + " <h3>Shares Owned</h3>  </tr> </thead> <tbody> ";
			try{
				preparedStatement = conn1.prepareStatement(sql);
				preparedStatement.setString(1, str);
				rs = preparedStatement.executeQuery();
		        String retval = "";
		        String retval1 = "";
		        int retval2;
				while(rs.next())
				{
                    retval = rs.getString(1);
                    retval1 = rs.getString(2);
                    retval2 = rs.getInt(4); 
                    result2 = result2.concat("<tr>"
                    		//+ "<td>" + retval + "</td>"
                    		+ "<td>" + retval1 + "</td>"
                    		+ "<td>" + Integer.toString(retval2) + "</td>"
                    		+ "</tr>");
				}
				result2= result2.concat("</tbody> </table>");
				//System.out.println("results" + result2);
				
				out.println(result2);
				//request.setAttribute("vec", v);
				//request.setAttribute("searchresult", result2 );
				//request.setAttribute("flag", true);
				//RequestDispatcher rd = getServletContext().getRequestDispatcher("/userhome.jsp");
				//rd.forward(request, response);
				
			}catch(SQLException e) {
				 
				System.out.println(e.getMessage());
	 
			}
			
		}
        
		else if(type.equals("register"))
		{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String name = request.getParameter("name");
			double phone = Integer.parseInt(request.getParameter("phone"));
			//int phone = 0047363512;
			String email = request.getParameter("email");
			double bal = Float.parseFloat(request.getParameter("balance"));
			//double bal = 123;
			String address = request.getParameter("address");
			//System.out.println(name);
			//System.out.println(phone);
			sql = "insert into users values (?, ?, ?, ?, ?, ?, ?);";
			try{
				preparedStatement = conn1.prepareStatement(sql);
				preparedStatement.setString(3, name);
				preparedStatement.setDouble(5, phone);
				preparedStatement.setString(4, email);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				preparedStatement.setString(6, address);
				preparedStatement.setDouble(7, bal);
				
				preparedStatement.executeUpdate();
				response.sendRedirect("/JDBCProject/login.jsp?flag="+ false);

			}
			catch(SQLException e) {
				 
				System.out.println(e.getMessage());
	 
			}
		}
		
		else if(type.equals("editinfo"))
		{
			String username = request.getParameter("username");
			sql = "select username, name, email, phoneNumber, address from users where username = ?";
	        String retval = "";
	        String retval1 = "";
	        String retval2 = "";
	        double retval3 = 000000;
	        String retval4 = ""; 
	        String result3= "";
			try{
				preparedStatement = conn1.prepareStatement(sql);
				preparedStatement.setString(1, username);
				rs = preparedStatement.executeQuery();
				while(rs.next())
				{
                    retval = rs.getString(1);
                    retval1 = rs.getString(2);
                    retval2 = rs.getString(3);
                    retval3 = rs.getDouble(4);
                    retval4 = rs.getString(5);
	                 System.out.println(retval);
	                  System.out.println(retval1);
	                  System.out.println(retval2);
	                  System.out.println(retval3);
				}
				
				result3 = "</br><table border=\"1\" width=\"30%\" cellpadding=\"5\">"
                + "<thead> <tr> <th colspan=\"2\"> Enter Information Here</th>" 
                + "</tr> </thead> <tbody>"
                + "<tr> <td>Name</td> <td><input type=\"text\" name=\"name\" value=" + retval1 + "></td> </tr>"
                + "<tr> <td>Phone</td> <td><input type=\"text\" name=\"phone\" value=" + retval3 +"></td> </tr>"
                + "<tr> <td>Email</td> <td><input type=\"text\" name=\"email\" value=" + retval2 + "></td> </tr>"
                + "<tr> <td>Address</td> <td><input type=\"text\" name=\"address\" value=" + retval4 +"></td> </tr> " +
                		"</tbody> </table>";
				
				System.out.println(result3);
				request.setAttribute("result", result3 );
				request.setAttribute("username", username );
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/editInfo.jsp");
				rd.forward(request, response);
				
				
			}catch(SQLException e) {	 
				System.out.println(e.getMessage());
			}
			
		}
        
	}

}
