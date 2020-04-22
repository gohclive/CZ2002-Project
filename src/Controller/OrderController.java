package Controller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import Entity.Item;
import Entity.Order;
import Database.OrderDB;

public class OrderController{
    
	private static final String filename = "Order.txt";
    private static OrderController instance = null;
    ArrayList<Order> orderList = new ArrayList<Order>();
    
    public OrderController() {
    	orderList = new ArrayList<Order>();
    }

    /**
	 * Creating new instance of order controller
	 * 
	 * @return ordercontroller instance
	 */
    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }
    
    /**
	 * Creating new order item
	 * 
	 */
    public void createOrderItem(Order order, int itemId) {
        Item item = MenuController.retrieveInstance().retrieveItem(itemId);
        if (item != null) order.addItem(item);
        else System.out.println("This item does not exist");
    }

    /**
	 * Updating order
	 * 
	 */
    public void updateOrder(Order order) {
        orderList.remove(order);
        checkID(); 
        orderList.add(order);
    }

    /**
	 * Delete order
	 * 
	 * @param order
	 * 				Specifies the order to be deleted
	 */
    public void deleteOrder(Order order) {
    	int id = order.getOrderId(); 
    	if(id == (Order.getIdCount()-1))	
    		Order.setIdCount(id); 
        orderList.remove(order);
        checkID(); 
    }
    
    /**
	 * Retrieval of order
	 * 
	 * @param orderID
	 * 				Specifies the order id to retrieve order
	 * 
	 * @return Order if found. Else will return null
	 */
    public Order retrieveOrder(int orderID) {
        for (Order order : orderList) {
            if (order.getOrderId() == orderID)
                return order;
        }
        return null;
    }
    
    /**
	 * Retrieval of order list by room id
	 * 
	 * @param roomID
	 * 				Specifies the room id to retrieve order
	 * 
	 * @return An ArrayList of order if found. Else will return null
	 */
    public ArrayList<Order> retrieveOrderList(String roomID) {
    	ArrayList<Order> ol = new ArrayList<Order>();
    	for (Order order : orderList) {
            if (order.getRoomId().equals(roomID))
                ol.add(order);
        }
    	if(ol.size() > 0) return ol;
    	else return null;
    }

    /**
	 * Format printing of orders by order id
	 * 
	 * @param orderID
	 * 				Specifies the order id to retrieve order
	 */
    public void displayOrder(int orderID) {
        Order order;
        order = retrieveOrder(orderID);
        order.viewOrder();
    }
    
    /**
	 * Retrieval of order size
	 * 
	 */
    public final int displayOrder() {
        Set<Integer> s = new HashSet<>();
        for (Order order : orderList) {
            int i = order.getOrderId();
            if (!s.contains(i)) {
                s.add(i);
            }
        }
        return orderList.size();
    }
    
    /**
	 * Updating of items in orders
	 * 
	 * @param item
	 * 				Specifies the item to update
	 */
    public void updateItemInOrders(Item item) {
        for (Order order : orderList) {
            ArrayList<Item> items = order.getItems();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItemId() == item.getItemId()) {
                    items.set(i, item);
                    System.out.println(String.format("Order %d was updated.", order.getOrderId()));
                }
            }
        }
        this.savetoDB();
    }
    
    /**
	 * Checking of order id
	 * 
	 */
    public void checkID() {
    	int id = 1;
		if(orderList!=null) {
			for(Order order : orderList){
				if(order.getOrderId() > id) id = order.getOrderId();
			}
		}
		Order.setIdCount(id+1);
    }

    /**
	 * Retrieval of all orders
	 * 
	 */
    public void loadinDB() {
    	OrderDB orderdb = new OrderDB();
        try {
			this.orderList = orderdb.read(filename);
			checkID(); //ADDED TO CHECK
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Saving of all orders
	 * 
	 */
    public void savetoDB() {
    	OrderDB orderdb = new OrderDB();
        try {
        	orderdb.save(filename, orderList);
        	checkID(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

}

