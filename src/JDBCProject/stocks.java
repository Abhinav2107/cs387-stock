package JDBCProject;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class stocks
 */
public class stocks extends HttpServlet {
	
	//stockSymbol char(8) primary key,
	
	public String stocksym;
	public int quant;
	public float salesprice;

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public stocks(String sym, int t, float sp) {
        super();
        stocksym = sym;
        quant = t;
        salesprice = sp;
    }
    
    public void print()
    {
    	System.out.println(stocksym + " " + quant + " " + salesprice);
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
