package JDBCProject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jmx.snmp.Timestamp;

/**
 * Servlet implementation class transaction
 */
public class transaction extends HttpServlet {
	Connection conn1 =null;
	Statement st =null;
	PreparedStatement preparedStatement = null;
	
	Admin admin = new Admin("admin", "adminpass");
public void init() throws ServletException {
      //Open the connection here
	
	String dbURL2 = "jdbc:postgresql://10.105.1.12/cs387";
    String user = "db120050030";
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
    public transaction() {
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
		String type = request.getParameter("type");
		String sql = null;
		ResultSet rs = null;
		
		if(type.equals("sell"))
		{
			String stocksym=request.getParameter("stock");
			String username=request.getParameter("username");
			System.out.println("stock " + stocksym + " " + username);
			String resultStock= "";
			sql= "select stockSymbol, companyName, ltp from stocks where stockSymbol = ?";
			try{
				preparedStatement = conn1.prepareStatement(sql);
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
			String username = request.getParameter("username");
			String stocksym= request.getParameter("stocksym");
			int quant = Integer.parseInt(request.getParameter("marketquant"));
			//float userBal = admin.userBalance("admin", "adminpass", username);
			sql = "select bidprice, quantity, buydatetime, username  from buyOrders where stocksymbol=? order by askPrice desc, sellDateTime asc limit 10";
			try{
				preparedStatement = conn1.prepareStatement(sql);
				preparedStatement.setString(1, stocksym);
				rs = preparedStatement.executeQuery();
				int quant_sold = 0;
				float amount_reveived = 0;
				float ltp = 0;
				String retval = "";
		        String retval1 = "";
		        String buyer="";
		       // int retval2;
		        String traderesult = "";
		        //stmt = conn.createStatement();
				while(rs.next())
				{
                    retval = rs.getString(1);
                    retval1 = rs.getString(2);
                    int stockquant=Integer.parseInt(retval1);
                    float stockprice=Float.parseFloat(retval1);
                    buyer = rs.getString(3);
                    float buyerBal = admin.userBalance("admin", "adminpass", buyer);
                    float trans_money = stockquant*stockprice;
                    
                    //retval2 = rs.getDouble(3);
                    //if the buyer does not have sufficient balanace to buy all the stocks he placed order for 
                    //then the transaction wont happen 
                    if(quant_sold + stockquant<= quant && buyerBal >= trans_money)
                    {
                    	rs.deleteRow();
                    	ltp = stockprice;
                    	admin.withdrawMoney("admin", "adminpass", buyer, trans_money);
                        quant_sold +=  stockquant;
                        amount_reveived +=  Integer.parseInt(retval1)*Float.parseFloat(retval);
                        //also as this stock is now bought by second user you have to update 
                        //its balance and also update balance of the current user that made the sale  
                    }
                    
                    else if(buyerBal < trans_money)
                    {
                    	System.out.println("insufficent balance in account of " + buyer + " :: removing the transaction from buy order table");
                    	rs.deleteRow();
                    	
                    }
                   
                    else if(quant_sold + stockquant > quant )
                    {
                    	int diff = quant - quant_sold;
                    	quant_sold += diff;
                    	ltp = stockprice;
                    	amount_reveived += diff*Float.parseFloat(retval);
                    	rs.updateInt(2, stockquant - diff);
                    	admin.withdrawMoney("admin", "adminpass", buyer, diff*stockprice);
                    	break;
                    }                   
				}
				
				admin.addMoney("admin", "adminpass", username, amount_reveived); //add all the share money that got sent 
				
				if(quant_sold < quant)
				{
					int diff = quant - quant_sold;
					Date date= (Date) new java.util.Date();
					
					// this means there are not enough stock buyers for the current stock and hence it must be added to the sell Order table
					sql = "insert into sellOrders value ('" + stocksym +
					"','" + username + "','" + ltp + "','" + diff + "','" + (new Timestamp(date.getTime())) + "');";
				}
				//System.out.println("stock details" + resultStock);
				request.setAttribute("resultStock", traderesult);
				request.setAttribute("stocksym", retval);
				request.setAttribute("username", username);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/sell.jsp");
				rd.forward(request, response);
			}catch (SQLException e) 
			{
				System.out.println(e.getMessage());
	 
				}	
		}
	}

}
