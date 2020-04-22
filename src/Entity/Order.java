package Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Order {
	private static int idCount = 1;
    private int orderId;
    private String roomId;
    private String reservationNum;
    private ArrayList<Item> items = new ArrayList<Item>();
    private String date;
    private String status = "Ordering";
    private String remarks = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
    
    public Order(String roomId, ArrayList<Item> items, String status, String remarks){
        this.orderId = idCount;
        this.items = items;
        Calendar c = Calendar.getInstance();
        String d = sdf.format(c.getTime());
        this.date = d;
        this.status = status;
        this.remarks = remarks;
        this.roomId = roomId;
        idCount++;
    }
    
    public Order(int orderId, String roomId, String reservationNum, ArrayList<Item> items, String date, String status, String remarks){
        this.orderId = orderId;
        this.roomId = roomId;
        this.reservationNum = reservationNum;
        this.items = items;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
        idCount = orderId+1;//ADDED TO CHECK //removed on left
    }
    
    public Order(String roomId){
        this.orderId = idCount;
        Calendar c = Calendar.getInstance();
        String d = sdf.format(c.getTime());
        this.date = d;
        this.roomId = roomId;
        idCount++;
    }
    
    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int ID) {
        idCount = ID;
    }
    
    public ArrayList<Item> getItems() {
        return items;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getReservationNum() {
		return reservationNum;
	}

	public void setReservationNum(String reservationNum) {
		this.reservationNum = reservationNum;
	}

	public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public void addItem(Item item) {
        this.items.add(item);
    }

    public boolean removeItem(Item item) {
        for (Item it : items) {
            if (it.getItemId() == item.getItemId()) {
                this.items.remove(it);
                return true;
            }
        }
        return false;
    }
    
    public void viewOrder() {
        System.out.println("ID   Room   Date                          Remarks                       Status   ");
        System.out.println(toString());
        System.out.println("=================================================================================");
        System.out.println("ID   Name                          Description                          Price(S$)");
        System.out.println("=================================================================================");
        for (Item item : items) {
        	System.out.println(item.toString());
        }
        System.out.println("=================================================================================");
    }
    
    public String toString() {

        return (String.format("%-5d%-7s%-30s%-30s%-10s", orderId, roomId, date, remarks, status));
    }

}
