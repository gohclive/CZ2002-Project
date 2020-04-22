package Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Payment {
    private static int idCount = 1;
    private int paymentId;
    private double subTotal = 0;
    private double total;
    private String reservationNum;
    private ArrayList<Order> orders;
    private String date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

    public Payment(ArrayList<Order> orders, String reservationNum) {
    	this.paymentId = idCount;
    	this.reservationNum = reservationNum;
        this.orders = orders;
        Calendar c = Calendar.getInstance();
        String d = sdf.format(c.getTime());
        this.date = d;
        idCount++;
    }
    
    public Payment(int paymentId, ArrayList<Order> orders, String reservationNum, String date) {
    	this.paymentId = paymentId;
    	this.reservationNum = reservationNum;
        this.orders = orders;
        this.date = date;
        idCount = paymentId + 1; //ADDED TO CHECK // removed on left
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int ID) {
        idCount = ID;
    }
    
    public int getPaymentId() {
    	return paymentId;
    }
    
    public String getRes() {
    	return reservationNum;
    }

	public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public ArrayList<Order> getOrders(){
    	return orders;
    }

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
    
}
