package JDBCProject;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.corba.se.pept.transport.Connection;

/**
 * Servlet implementation class Admin
 */
public class Admin extends HttpServlet {
	
	private String adminId;
	private String password;
	
	Connection conn1 = null;
	Statement st =null;
	public void init() throws ServletException {
	      //Open the connection here
		
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
	    String user = "aman";
	    String pass = "password";

	    try {
			Class.forName("org.postgresql.Driver");
		
			conn1 = (Connection) DriverManager.getConnection(dbURL2, user, pass);
			st = ((java.sql.Connection) conn1).createStatement();
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
    public Admin(String adminId, String pass) {
        super();
        this.adminId = adminId;
        this.password = pass;
        // TODO Auto-generated constructor stub
    }
    
    public float userBalance(String admin, String pass, String username)
    {
    	if(admin == this.adminId && pass == this.password)
    	{
    		String sql = "select * from users where username = '" + username + "';";
    		try{
    			ResultSet rs = st.executeQuery(sql);
    			while(rs.next())
    			{
    				return rs.getFloat(7);
    				
    			}
    		}
    		catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}
    	}
    	
    	else
    		System.out.println("Admin Error : Password and ID doesn't match");
		return -1;
    }
    
    public void addMoney(String admin, String pass, String username, float amount) throws SQLException
    {
    	if(admin == this.adminId && pass == this.password)
    	{
    		String sql = "select * from users where username = '" + username + "';";
    		try{
    			ResultSet rs = st.executeQuery(sql);
    			while(rs.next())
    			{
    				float am = rs.getFloat(7);
    				rs.updateFloat(7, (float) (am + amount));
    			}
    		}
    		catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}
    	}
    	
    	else
    		System.out.println("Admin Error : Password and ID doesn't match");
    	
    }

    public void withdrawMoney(String admin, String pass, String username, float amount) throws SQLException
    {
    	if(admin == this.adminId && pass == this.password)
    	{
    		String sql = "select * from users where username = '" + username + "';";
    		try{
    			ResultSet rs = st.executeQuery(sql);
    			while(rs.next())
    			{
    				float am = rs.getFloat(7);
    				rs.updateFloat(7, am - amount);
    			}
    		}
    		catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}
    	}
    	
    	else
    		System.out.println("Admin Error : Password and ID doesn't match");
    	
    }
    
    public void updateltp(String admin, String pass, String stocksym, float amount)
    {
    	System.out.println("I am fucking here");
    	if(admin == this.adminId && pass == this.password)
    	{
    		String sql = "select * from stocksymbol = '" + stocksym + "';";
    		try{
    			ResultSet rs = st.executeQuery(sql);
    			while(rs.next())
    			{
    				//float am = rs.getFloat(4);
    				rs.updateFloat(4, amount);
    			}
    		}
    		catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}
    	}
    	
    	else
    		System.out.println("Admin Error : Password and ID doesn't match");	
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
		// TODO Auto-generated method stub
	}

}
