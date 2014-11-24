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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Servlet implementation class transaction
 */
public class transaction extends HttpServlet {
	Connection conn1 =null;
	Statement st =null;
	String errorOccured = "";
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

	public void redeemMoney(String username, float addend) throws SQLException
	{
		String sql2 = "select balance, username from users where username = ?;";

		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				float am = rs.getFloat(1);
				if(am-addend >= 0) {rs.updateFloat(1, (float) (am - addend)); rs.updateRow(); conn1.commit(); errorOccured = "stock(s) bought";}
				else {conn1.rollback(); System.out.println("rolling back"); errorOccured = "Not Enough Money";}

			}
		}
		catch (SQLException e) 
		{
			System.out.println(e);
			conn1.rollback();

		}

	}

	public void updateSellerOwnership(String username, String stocksym, int quant, float ltp, int i)
	{
		String sql2 = "select * from ownership where username = ? and stocksymbol = ?;";
		i = -1;
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, stocksym);
			ResultSet rs = preparedStatement.executeQuery();

			while(rs.next())
			{
				int currentquant = rs.getInt(3);
				int newquant = currentquant + i * quant;
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

	public void updateBuyerOwnership(String username, String stocksym, int quant, float ltp, int i)
	{
		String sql2 = "select * from ownership where username = ? and stocksymbol = ?;";
		i = 1;
		try{
			preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, stocksym);
			ResultSet rs = preparedStatement.executeQuery();
			boolean check = rs.next();
			while(check)
			{
				int currentquant = rs.getInt(3);
				int newquant = currentquant + i * quant;
				rs.updateInt(3, newquant);
				rs.updateFloat(4, ltp);
				rs.updateRow();

				break;
			}

			if(!check)
			{
				//then there is no such tuple and so you have to insert 1

				sql2 = "insert into ownership values (?, ?, ?,?)";
				preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, stocksym);
				preparedStatement.setInt(3, quant);
				preparedStatement.setFloat(4, ltp);			
				preparedStatement.executeUpdate();
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
			System.out.println("some error in catch block in validate quant");
			return false;

		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String sql = null;
		ResultSet rs = null;
		//System.out.println("I was here too");
		if(type.equals("buy"))
		{
			String stocksym=request.getParameter("stocksym");
			HttpSession session = request.getSession(true);
			String username=(String) session.getAttribute("username");
			//System.out.println("I was here");
			//System.out.println("stock " + stocksym + " " + username);
			String resultStock= "";
			sql= "select stocksymbol, quantity, salesprice from ownership where stockSymbol = ?";
			try{
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();

				String retval = "";
				int retval1 ;
				float retval2;

				while(rs.next())
				{
					retval = rs.getString(1);
					retval1 = rs.getInt(2);
					retval2 = rs.getFloat(3);

					resultStock = "<table border=\"1\"  > <thead> <tr> <td> Share </td> <td> Company </td> <td> Last Traded Price </td></tr> " +
					"</thead><tr> <td>" +  retval +  "</td> <td> " + retval1 + " </td> <td>" + retval2 +"</td></tr> </table>";
				}
				//System.out.println("stock details" + resultStock);
				
				sql= "select stocksymbol, askPrice, quantity from sellOrders where stockSymbol = ?";
				String orderSell="";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				
				
				while(rs.next())
				{
					String ret = rs.getString(1);
					float ret1 = rs.getFloat(2);
					int ret2 = rs.getInt(3);

					orderSell = "<h2> Sell Order Table </h2> <table class=\"table\" border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Ask Price </td></tr> " +
					"</thead><tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr> </table>";
				}
				session.setAttribute("sellOrderTable", orderSell);
				
				sql= "select stocksymbol, bidPrice, quantity from buyOrders where stockSymbol = ?";
				String orderBuy="<h2> Buy Order Table </h2> <table class=\"table\" border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Bid Price </td></tr> " +
				"</thead>";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				
				
				while(rs.next())
				{
					String ret = rs.getString(1);
					float ret1 = rs.getFloat(2);
					int ret2 = rs.getInt(3);

					orderBuy += "<tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr>";
				}
				orderBuy+= "</table>";
				session.setAttribute("buyOrderTable", orderBuy);
				
				System.out.println("buytag " + username + " " + stocksym);
				request.setAttribute("resultStock", resultStock);
				request.setAttribute("stocksym", retval);
				request.setAttribute("username", username);
				
				sql= "select tradedPrice, transdatetime from transactions where stockSymbol = ? order by transdatetime asc;";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				
				Vector<TransRecord> transVector = new Vector<TransRecord>();
				while(rs.next())
				{
					float tradedPrice = Float.parseFloat(rs.getString(1));
					String transDateTime = rs.getString(2);
					TransRecord temp = new TransRecord(stocksym, transDateTime, tradedPrice);
					
					transVector.addElement(temp);
					//order = "<h1> Order Table </h1> <table class=\"table\" border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Ask Price </td></tr> " +
					//"</thead><tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr> </table>";
				}
				
				session.setAttribute("transactions", transVector);
				
				
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/buy.jsp");
				rd.forward(request, response);
			}catch (SQLException e) 
			{
				System.out.println(e.getMessage());

			}		
		}
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

			String stocksym=request.getParameter("stocksym");
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			System.out.println(stocksym + " " + username);
			String resultStock= "";
			sql= "select stocksymbol, quantity, salesprice from ownership where stockSymbol = ?";
			try{
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();

				String retval = "";
				float retval1 ;
				float retval2;
				while(rs.next())
				{
					retval = rs.getString(1);
					retval1 = rs.getFloat(2);
					retval2 = rs.getFloat(3);

					resultStock = "<table border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Last Traded Price </td></tr> " +
					"</thead><tr> <td>" +  retval +  "</td> <td> " + retval1 + " </td> <td>" + retval2 +"</td></tr> </table>";
				}
				
				sql= "select stocksymbol, askPrice, quantity from sellOrders where stockSymbol = ?";
				String orderSell= "<h2> Sell Order Table </h2> <table class=\"table\" border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Ask Price </td></tr> " +
				"</thead>";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				while(rs.next())
				{
					String ret = rs.getString(1);
					float ret1 = rs.getFloat(2);
					int ret2 = rs.getInt(3);

					orderSell +="<tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr>";
				}
				orderSell +=  "</table>";
				
				sql= "select stocksymbol, bidPrice, quantity from buyOrders where stockSymbol = ?";
				String orderBuy="<h2> Buy Order Table </h2><table BORDER=\"1\" > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Bid Price </td></tr> </thead>";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				while(rs.next())
				{
					String ret = rs.getString(1);
					float ret1 = rs.getFloat(2);
					int ret2 = rs.getInt(3);

					orderBuy += "<tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr>";
				}
				orderBuy += "</table>";
				
				sql= "select tradedPrice, transdatetime from transactions where stockSymbol = ? order by transdatetime asc;";
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				
				Vector<TransRecord> transVector = new Vector<TransRecord>();
				while(rs.next())
				{
					float tradedPrice = Float.parseFloat(rs.getString(1));
					String transDateTime = rs.getString(2);
					TransRecord temp = new TransRecord(stocksym, transDateTime, tradedPrice);
					
					transVector.addElement(temp);
					//order = "<h1> Order Table </h1> <table class=\"table\" border=\"1\"  > <thead> <tr> <td> Share </td> <td> Quantity </td> <td> Ask Price </td></tr> " +
					//"</thead><tr> <td>" +  ret +  "</td> <td> " + ret2 + " </td> <td>" + ret1 +"</td></tr> </table>";
				}
				
				session.setAttribute("transactions", transVector);
				
				
				session.setAttribute("sellOrderTable", orderSell);
				session.setAttribute("buyOrderTable", orderBuy);
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

		else if(type.equals("sellmarketorder"))
		{
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			String stocksym= (String) session.getAttribute("stocksym");
			String stock= (String) session.getAttribute("stock");
			int quant = Integer.parseInt(request.getParameter("marketquant"));

			boolean isGreater = validateQuant(username, stocksym, quant);

			if(!isGreater)
			{
				request.setAttribute("error", "<center> <h3> Status Message </h3> <br> Insufficient amount of stocks to be sold </center> ");
				request.setAttribute("resultStock", stock);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
				rd.forward(request, response);
			}

			else{
				//float userBal = admin.userBalance("admin", "adminpass", username);
				sql = "select id, bidprice, quantity, buydatetime, username  from buyOrders where stocksymbol=? order by bidPrice desc, buyDateTime asc limit 10";
				try{
					conn1.setAutoCommit(false);
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

						if(quant_sold + stockquant <= quant)
						{
							rs.deleteRow();
							ltp = stockprice;
							quant_sold +=  stockquant;
							amount_received +=  stockquant*stockprice;

							makeTransaction(stocksym, ltp, stockquant, username, buyer);
							//Update the ownership of this resource in the buyer 
							updateBuyerOwnership(buyer, stocksym, stockquant, ltp, 1);
							//also as this stock is now bought by second user you have to update 
							//its balance and also update balance of the current user that made the sale  
						}

						else if(quant_sold + stockquant > quant )
						{
							int diff = quant - quant_sold;
							quant_sold = quant;
							ltp = stockprice;
							amount_received += diff*stockprice;
							rs.updateInt(3, stockquant - diff);
							rs.updateRow();
							makeTransaction(stocksym, ltp, diff, username, buyer);
							updateBuyerOwnership(buyer, stocksym, diff, ltp, 1);
							//admin.updateltp("admin", "adminpass", stocksym, stockprice);
							//admin.withdrawMoney("admin", "adminpass", buyer, diff*stockprice);
							break;
						}                   
					}

					//admin.addMoney("admin", "adminpass", username, amount_received); //add all the share money that got sent
					//update ltp 

					if(amount_received > 0) addMoney(username, amount_received);
					if(ltp > 0)updateLtp(stocksym, ltp);
					if(quant_sold > 0) updateSellerOwnership(username, stocksym, quant_sold, ltp, -1);

					conn1.commit();
					conn1.setAutoCommit(true);
					request.setAttribute("error", "<center> <h3> Status Message </h3> <br>"+quant_sold +" stocks sold </center>");
					request.setAttribute("resultStock", traderesult);
					request.setAttribute("stocksym", retval);
					request.setAttribute("username", username);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
					rd.forward(request, response);
				}catch (SQLException e) 
				{
					System.out.println(e.getMessage());
					try {
						conn1.rollback(); conn1.setAutoCommit(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		//following code is for limit orders
		else if(type.equals("selllimitorder"))
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
				request.setAttribute("error", "<h3> Status Message </h3> <br> Don't have sufficient amount of stocks for this trade");
				request.setAttribute("resultStock", stock);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
				rd.forward(request, response);
			}

			else
			{
				sql = "select id, bidprice, quantity, buydatetime, username  from buyOrders where stocksymbol=? order by bidPrice desc, buyDateTime asc limit 10";
				try{
					conn1.setAutoCommit(false);
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
							updateBuyerOwnership(buyer, stocksym, stockquant, ltp, 1);
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
							rs.updateRow();
							makeTransaction(stocksym, ltp, diff, username, buyer);
							updateBuyerOwnership(buyer, stocksym, diff, ltp, 1);
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
								preparedStatement.setInt(4, quant-quant_sold);	
								preparedStatement.setTimestamp(5,getCurrentTimeStamp());
								preparedStatement.executeUpdate();
							}
							catch (SQLException e) 
							{
								System.out.println(e);

							}
							break;
						}

					}


					if(amount_received > 0) addMoney(username, amount_received);
					if(ltp > 0)updateLtp(stocksym, ltp);
					updateSellerOwnership(username, stocksym, quant, ltp, -1);

					conn1.commit();
					conn1.setAutoCommit(true);
					request.setAttribute("error", "<center> <h3> Status Message </h3> <br>  Order Placed </center>");
					request.setAttribute("resultStock", stock);
					request.setAttribute("stocksym", retval);
					request.setAttribute("username", username);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
					rd.forward(request, response);


				}catch (SQLException e) 
				{
					System.out.println(e.getMessage());
					try {
						conn1.rollback(); conn1.setAutoCommit(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			//check whether the user has required quant or not 
		}

		// now deal with the buy orders 
		if(type.equals("buymarketorder"))
		{
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			String stocksym= (String) session.getAttribute("stocksym");
			String stock= (String) session.getAttribute("stock");
			System.out.println("transaction test " + username + " " + stocksym + " " );
			int quant = Integer.parseInt(request.getParameter("marketquant"));

			boolean isGreater = validateQuant(username, stocksym, quant);

			if(!isGreater)
			{
				request.setAttribute("error", "<center> <h3> Status Message </h3> <br>" + "Don't have enought stocks for this order" + "</center>");
				request.setAttribute("resultStock", stock);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/buy.jsp");
				rd.forward(request, response);
			}

			else{
				//float userBal = admin.userBalance("admin", "adminpass", username);
				sql = "select id, askprice, quantity, selldatetime, username  from sellOrders where stocksymbol=? order by askPrice asc, sellDateTime asc limit 10";
				try{
					conn1.setAutoCommit(false);
					preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
					preparedStatement.setString(1, stocksym);
					rs = preparedStatement.executeQuery();
					int quant_bought = 0;
					float amount_due= 0;
					float ltp = 0;

					String retval = "";
					String retval1 = "";
					String seller="";
					//stmt = conn.createStatement();
					while(rs.next())
					{
						retval = rs.getString(2);
						retval1 = rs.getString(3);
						int stockquant=Integer.parseInt(retval1);
						float stockprice=Float.parseFloat(retval);
						seller= rs.getString(5);

						if(quant_bought + stockquant <= quant)
						{
							rs.deleteRow();
							ltp = stockprice;
							quant_bought +=  stockquant;
							amount_due +=  stockquant*stockprice;

							if(stockquant > 0) makeTransaction(stocksym, ltp, stockquant, seller, username);
							else System.out.println("diff equal to 0") ;
							//Update the ownership of this resource in the buyer 
							//updateSellerOwnership(seller, stocksym, stockquant, ltp, -1);
							//also as this stock is now bought by second user you have to update 
							//its balance and also update balance of the current user that made the sale  
						}

						else if(quant_bought + stockquant > quant )
						{
							int diff = quant - quant_bought;
							quant_bought = quant;
							ltp = stockprice;
							amount_due += diff*stockprice;
							rs.updateInt(3, stockquant - diff);
							rs.updateRow();
							if(diff > 0) makeTransaction(stocksym, ltp, diff, seller, username);
							else System.out.println("diff equal to 0") ;
							//updateSellerOwnership(seller, stocksym, diff, ltp, -1);
							break;
						}                   
					} 

					updateLtp(stocksym, ltp);

					//ADD MONEY TO ACCOUNT
					updateBuyerOwnership(username, stocksym, quant_bought, ltp, 1);
					redeemMoney(username, amount_due);
					conn1.setAutoCommit(true);
					request.setAttribute("error", "<center> <h3> Status Message </h3> <br>" + errorOccured + "</center>");
					request.setAttribute("resultStock", stock);
					request.setAttribute("stocksym", retval);
					request.setAttribute("username", username);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/buy.jsp");
					rd.forward(request, response);
				}catch (SQLException e) 
				{
					System.out.println(e.getMessage());
					try {
						conn1.rollback();conn1.setAutoCommit(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}


		}

		else if(type.equals("buylimitorder"))
		{
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("username");
			String stocksym= (String) session.getAttribute("stocksym");
			String stock= (String) session.getAttribute("stock");

			int quant = Integer.parseInt(request.getParameter("limitquant"));
			float bidPrice = Float.parseFloat(request.getParameter("bidPrice"));

			boolean isGreater = validateQuant(username, stocksym, quant);

			/*			if(!isGreater)
			{
				request.setAttribute("error", "Insufficient amount of stocks to buy");
				request.setAttribute("resultStock", stock);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/buy.jsp");
				rd.forward(request, response);
			}*/

			sql = "select id, username, askprice, quantity, selldatetime  from sellOrders where stocksymbol=? order by askPrice desc, sellDateTime asc limit 10";
			try{
				conn1.setAutoCommit(false);
				preparedStatement = conn1.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				int quant_bought= 0;
				float amount_due = 0;
				float ltp = 0;

				String retval = "";
				String retval1 = "";
				String seller= "";

				while(rs.next())
				{
					retval = rs.getString(3);
					retval1 = rs.getString(4);
					int stockquant=Integer.parseInt(retval1);
					float stockprice=Float.parseFloat(retval);
					seller = rs.getString(2);

					if(quant_bought + stockquant <= quant && stockprice <= bidPrice)
					{
						rs.deleteRow();
						ltp = stockprice;  // I am assuming that the transaction will happen at the rate of the buyer and 
						//the seller will be benefitted out of it

						quant_bought +=  stockquant;
						amount_due +=  stockquant*stockprice;

						makeTransaction(stocksym, ltp, stockquant, seller, username);
						//updateSellerOwnership(seller, stocksym, quant, ltp, -1);
						//also as this stock is now bought by second user you have to update 
						//its balance and also update balance of the current user that made the sale  
					}

					else if(quant_bought + stockquant > quant && stockprice <= bidPrice)
					{
						int diff = quant - quant_bought;
						quant_bought= quant;
						ltp = stockprice;
						amount_due += diff*stockprice;
						rs.updateInt(3, stockquant - diff);
						rs.updateRow();
						makeTransaction(stocksym, ltp, diff, seller, username);
						//updateSellerOwnership(seller, stocksym, diff, ltp, -1);
						break;
					}

					else if(stockprice > bidPrice) //no available buy order to trade
					{
						//add this to sell order table 
						String sql2 = "insert into buyOrders values (?, ?, ?, ?, ?);";
						try{
							preparedStatement = conn1.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
							preparedStatement.setString(1, stocksym);
							preparedStatement.setString(2, username);
							preparedStatement.setFloat(3, bidPrice);
							preparedStatement.setInt(4, quant - quant_bought);	
							preparedStatement.setTimestamp(5,getCurrentTimeStamp());
							preparedStatement.executeUpdate();
						}
						catch (SQLException e) 
						{
							System.out.println(e);

						}
						break;
					}
				}

				
				if(ltp > 0)updateLtp(stocksym, ltp);
				if(ltp>0)updateBuyerOwnership(username, stocksym, quant_bought, ltp, 1);
				redeemMoney(username, quant*bidPrice);
				
				conn1.setAutoCommit(true);
				request.setAttribute("error", "<center> <h3> Status Message </h3> <br>" + errorOccured + "</center>");
				request.setAttribute("resultStock", stock);
				request.setAttribute("stocksym", retval);
				request.setAttribute("username", username);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/buy.jsp");
				rd.forward(request, response);


			}catch (SQLException e) 
			{
				System.out.println(e.getMessage());
				try {
					conn1.rollback();conn1.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			//check whether the user has required quant or not 
		}


	}

}
