import java.sql.*;

public class TransRecord
{
	public String symbol;
	public String time;
	public float price;
       
    public TransRecord(String sym, String t, float val)
    {
        symbol = sym;
        time = t;
        price = val;
    }
    
    public void print()
    {	System.out.println(symbol + " " + time + " " + price);	}
}
