package JDBCProject;

public class CompanyStock
{
	public String symbol;
	public String company;
	public float ltp;
       
    public CompanyStock(String sym, String com, float val)
    {
        symbol = sym;
        company = com;
        ltp = val;
    }
    
    public void print()
    {	System.out.println(symbol + " " + company + " " + ltp);	}
}
