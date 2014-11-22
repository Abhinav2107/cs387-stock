package JDBCProject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Servlet implementation class transaction
 */
public class transaction extends HttpServlet {
	Connection conn1 =null;
	Statement st =null;
	PreparedStatement preparedStatement = null;
	
	
	//Admin admin = new Admin("admin", "adminpass");
	public void init() throws ServletException {
	      //Open the connection here
		
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
	    String user = "aman";
	    String pass = "password";
	
	    try {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			conn1.setAutoCommit(false);
			st = conn1.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
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
    public transaction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private static java.sql.Timestamp getCurrentTimeStamp() {
    	 
    	java.util.Date today = new java.util.Date();
    	return new java.sql.Timestamp(today.getTime());
     
    }
    
    public void makeTransaction(String stocksym, float ltp, int quant, String username, String buyer)
    {
    	String sql2 = "insert into transactions values (?, ?, ?, ?, ?, ?);";
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, stocksym);
			preparedStatement.setFloat(2, ltp);
			preparedStatement.setInt(3, quant);
			preparedStatement.setString(4, username);
			preparedStatement.setString(5, buyer);
			
			preparedStatement.setTimestamp(6,getCurrentTimeStamp());
			
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e) 
		{
			System.out.println(e);
 
			}
    }
    
    public void updateLtp(String stocksym, float ltp)
    {
    	String sql2 = "select stocksymbol, ltp from stocks where stocksymbol = ?;";
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, stocksym);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next())
			{
				System.out.println("one val");
				//float am = rs.getFloat(4);
				rs.updateFloat(2, ltp);
				rs.updateRow();
			}
		}
		catch (SQLException e) 
		{
			System.out.println(e);
 
			}
    }
    
    public void addMoney(String username, float addend)
    {
		String sql2 = "select balance, username from users where username = ?;";
		
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				float am = rs.getFloat(1);
				rs.updateFloat(1, (float) (am + addend));
				rs.updateRow();
			}
		}
		catch (SQLException e) 
		{
			System.out.println(e);
 
			}
		
    }
    
    public void updateOwnership(String username, String stocksym, int quant, float ltp)
    {
    	String sql2 = "select * from ownership where username = ? and stocksymbol = ?;";
		
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, stocksym);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next())
			{
				int currentquant = rs.getInt(3);
				int newquant = currentquant - quant;
				if(newquant > 0)
				{
					rs.updateInt(3, newquant);
					rs.updateFloat(4, ltp);
    				rs.updateRow();
				}
				
				else 
				{
					rs.deleteRow();
				}
			}
		}
		catch (SQLException e) 
		{
			System.out.println(e);
 
			}
    }
    
    public boolean validateQuant(String username, String stocksym, int quant)
    {
    	String sql="select quantity from ownership where username = ? and stocksymbol = ?";
    	boolean isGreater = false;
		try{
			preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, stocksym);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				String retval = rs.getString(1);
				int stockquant=Integer.parseInt(retval);
				if(quant <= stockquant) isGreater = true;
			}
			return isGreater;
		}catch (SQLException e) 
		{
			System.out.println(e);
 
			}
		return isGreater;
    }
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String sql = null;
		ResultSet rs = null;
		//System.out.println("I was here too");
		if(type.equals("sell"))
		{
			String stocksym=request.getParameter("stock");
			String username=request.getParameter("username");
			//System.out.println("I was here");
			//System.out.println("stock " + stocksym + " " + username);
			String resultStock= "";
			sql= "select stockSymbol, companyName, ltp from stocks where stockSymbol = ?";
			try{
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				
				String retval = "";
		        String retval1 = "";
		        double retval2;
		        
				while(rs.next())
				{
                    retval = rs.getString(1);
                    retval1 = rs.getString(2);
                    retval2 = rs.getDouble(3);
                    
                    resultStock = "<table border=\"1\"  > <thead> <tr> <td> Share </td> <td> Company </td> <td> Last Traded Price </td></tr> " +
                    		"</thead><tr> <td>" +  retval +  "</td> <td> " + retval1 + " </td> <td>" + retval2 +"</td></tr> </table>";
				}
				//System.out.println("stock details" + resultStock);
				
				System.out.println("sell tag" + username + retval);
				request.setAttribute("resultStock", resultStock);
				request.setAttribute("stocksym", retval);
				request.setAttribute("username", username);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
				rd.forward(request, response);
			}catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}	
			
		}
		
		else if(type.equals("marketorder"))
		{
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			String stocksym= (String) session.getAttribute("stocksym");
			String stock= (String) session.getAttribute("stock");
			//System.out.println("transaction test " + username + " " + stocksym + " " + stock);
			int quant = Integer.parseInt(request.getParameter("marketquant"));
			
			boolean isGreater = validateQuant(username, stocksym, quant);
				
				if(!isGreater)
				{
					request.setAttribute("error", "Insufficient amount");
					request.setAttribute("resultStock", stock);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
					rd.forward(request, response);
				}
			
				else{
				//float userBal = admin.userBalance("admin", "adminpass", username);
					sql = "select id, bidprice, quantity, buydatetime, username  from buyOrders where stocksymbol=? order by bidPrice desc, buyDateTime asc limit 10";
					try{
						preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
						preparedStatement.setString(1, stocksym);
						rs = preparedStatement.executeQuery();
						int quant_sold = 0;
						float amount_received = 0;
						float ltp = 0;
						
						String retval = "";
				        String retval1 = "";
				        String buyer="";
				       // int retval2;
				        String traderesult = "";
				        //stmt = conn.createStatement();
						while(rs.next())
						{
		                    retval = rs.getString(2);
		                    retval1 = rs.getString(3);
		                    int stockquant=Integer.parseInt(retval1);
		                    float stockprice=Float.parseFloat(retval);
		                    buyer = rs.getString(5);
		                   // float buyerBal = admin.userBalance("admin", "adminpass", buyer);
		                   // float trans_money = stockquant*stockprice;
		                    
		                    //retval2 = rs.getDouble(3);
		                    //if the buyer does not have sufficient balanace to buy all the stocks he placed order for 
		                    //then the transaction wont happen 
		                    
		                    if(quant_sold + stockquant <= quant)
		                    {
		                    	rs.deleteRow();
		                    	ltp = stockprice;
		                    	//admin.withdrawMoney("admin", "adminpass", buyer, trans_money);
		                        quant_sold +=  stockquant;
		                        amount_received +=  stockquant*stockprice;
		                        
		                        makeTransaction(stocksym, ltp, quant, username, buyer);
		                        //also as this stock is now bought by second user you have to update 
		                        //its balance and also update balance of the current user that made the sale  
		                    }
		                    
		                    /*else if(buyerBal < trans_money)
		                    {
		                    	System.out.println("insufficent balance in account of " + buyer + " :: removing the transaction from buy order table");
		                    	rs.deleteRow();
		                    	
		                    }*/
		                   
		                    else if(quant_sold + stockquant > quant )
		                    {
		                    	int diff = quant - quant_sold;
		                    	quant_sold = quant;
		                    	ltp = stockprice;
		                    	amount_received += diff*stockprice;
		                    	rs.updateInt(3, stockquant - diff);
		                    	makeTransaction(stocksym, ltp, diff, username, buyer);
		                    	//admin.updateltp("admin", "adminpass", stocksym, stockprice);
		                    	//admin.withdrawMoney("admin", "adminpass", buyer, diff*stockprice);
		                    	break;
		                    }                   
						}
						
						//admin.addMoney("admin", "adminpass", username, amount_received); //add all the share money that got sent
						//update ltp 
						
						updateLtp(stocksym, ltp);

						//ADD MONEY TO ACCOUNT
						
						addMoney(username, amount_received);
			    		//Update ownership 
			    		
						updateOwnership(username, stocksym, quant, ltp);
						
						conn1.commit();
						
						request.setAttribute("error", quant_sold +" stocks sold");
						request.setAttribute("resultStock", traderesult);
						request.setAttribute("stocksym", retval);
						request.setAttribute("username", username);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
						rd.forward(request, response);
					}catch (SQLException e) 
					{
						System.out.println(e.getMessage());
						try {
							conn1.rollback();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
		}
		//following code is for limit orders
		else if(type.equals("limitorder"))
		{
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			String stocksym= (String) session.getAttribute("stocksym");
			String stock= (String) session.getAttribute("stock");
			
			int quant = Integer.parseInt(request.getParameter("limitquant"));
			float askPrice = Float.parseFloat(request.getParameter("askPrice"));
			
			boolean isGreater = validateQuant(username, stocksym, quant);
			
			if(!isGreater)
			{
				request.setAttribute("error", "Insufficient amount");
				request.setAttribute("resultStock", stock);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
				rd.forward(request, response);
			}
			
			else
			{
				sql = "select id, bidprice, quantity, buydatetime, username  from buyOrders where stocksymbol=? order by bidPrice desc, buyDateTime asc limit 10";
				try{
					preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
					preparedStatement.setString(1, stocksym);
					rs = preparedStatement.executeQuery();
					int quant_sold = 0;
					float amount_received = 0;
					float ltp = 0;

					String retval = "";
			        String retval1 = "";
			        String buyer= "";
			        
					while(rs.next())
					{
	                    retval = rs.getString(2);
	                    retval1 = rs.getString(3);
	                    int stockquant=Integer.parseInt(retval1);
	                    float stockprice=Float.parseFloat(retval);
	                    buyer = rs.getString(5);
	                    
	                    if(quant_sold + stockquant <= quant && stockprice >= askPrice)
	                    {
	                    	rs.deleteRow();
	                    	ltp = stockprice;  // I am assuming that the transaction will happen at the rate of the buyer and 
	                    	//the seller will be benefitted out of it
	                    	
	                        quant_sold +=  stockquant;
	                        amount_received +=  stockquant*stockprice;
	                        
	                        makeTransaction(stocksym, ltp, quant, username, buyer);
	                        //also as this stock is now bought by second user you have to update 
	                        //its balance and also update balance of the current user that made the sale  
	                    }
	                   
	                    else if(quant_sold + stockquant > quant && stockprice >= askPrice)
	                    {
	                    	int diff = quant - quant_sold;
	                    	quant_sold = quant;
	                    	ltp = stockprice;
	                    	amount_received += diff*stockprice;
	                    	rs.updateInt(3, stockquant - diff);
	                    	makeTransaction(stocksym, ltp, diff, username, buyer);
	                    	break;
	                    }
	                    
	                    else if(stockprice < askPrice) //no available buy order to trade
	                    {
	                    	//add this to sell order table 
	                    	String sql2 = "insert into sellOrders values (?, ?, ?, ?, ?);";
	                		try{
	                			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
	                			preparedStatement.setString(1, stocksym);
	                			preparedStatement.setString(2, username);
	                			preparedStatement.setFloat(3, askPrice);
	                			preparedStatement.setInt(4, quant);	
	                			preparedStatement.setTimestamp(5,getCurrentTimeStamp());
	                			preparedStatement.executeUpdate();
	                		}
	                		catch (SQLException e) 
	                		{
	                			System.out.println(e);
	                 
	                			}
	                    	break;
	                    }
	                    
	                    addMoney(username, amount_received);
	                    updateLtp(stocksym, ltp);
	                    updateOwnership(username, stocksym, quant, ltp);
	                    
						conn1.commit();
						
						request.setAttribute("error", "<center> Order placed </center>");
						request.setAttribute("resultStock", stock);
						request.setAttribute("stocksym", retval);
						request.setAttribute("username", username);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
						rd.forward(request, response);
					}

					
				}catch (SQLException e) 
				{
					System.out.println(e.getMessage());
					try {
						conn1.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			//check whether the user has required quant or not 
		}
	}

}
