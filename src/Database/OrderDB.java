package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import Entity.Item;
import Entity.Order;

public class OrderDB implements DB {
	
	public static final String SEPARATOR = "|";

	@Override
	public ArrayList<Order> read(String fileName) throws IOException {
		ArrayList<String> stringArray = (ArrayList<String>) ReadinFile.read(fileName);
		ArrayList<Order> orderList = new ArrayList<Order>();
		
		
		for (int i = 0; i < stringArray.size(); i++) {
			String st = stringArray.get(i);
			StringTokenizer star = new StringTokenizer(st, SEPARATOR);
			ArrayList<Item> items = new ArrayList<Item>();
			
			int orderId = Integer.valueOf(star.nextToken().trim());
		    String roomId = star.nextToken().trim();
		    String reservationNum = star.nextToken().trim();
		    String date = star.nextToken().trim();
		    String status = star.nextToken().trim();
		    String remarks = star.nextToken().trim();
			
		    while(star.hasMoreTokens()) {
		    	int itemID = Integer.valueOf(star.nextToken().trim());
		    	String name = star.nextToken().trim();
		    	String desc = star.nextToken().trim();
		    	double price = Double.valueOf(star.nextToken().trim());
		    	int type = Integer.valueOf(star.nextToken().trim());
		    	Item item = new Item(itemID, name, desc, price, type);
		    	items.add(item);
		    }
		    
		    Order order = new Order(orderId, roomId, reservationNum, items, date, status, remarks);
		    orderList.add(order);
		}
		System.out.println(stringArray.size() + " Order(s) Loaded.");
		return orderList;
	}
	
	@Override
	public void save(String filename, List orderList) throws IOException {
		ArrayList<String> ordersw = new ArrayList<String>();
		for (int i = 0; i < orderList.size(); i++) {
			Order order = (Order) orderList.get(i);
			StringBuilder st = new StringBuilder();
			st.append(order.getOrderId());
			st.append(SEPARATOR);
			st.append(order.getRoomId());
			st.append(SEPARATOR);
			st.append(order.getReservationNum());
			st.append(SEPARATOR);
			st.append(order.getDate());
			st.append(SEPARATOR);
			st.append(order.getStatus());
			st.append(SEPARATOR);
			st.append(order.getRemarks());
			st.append(SEPARATOR);
			
			
			ArrayList<Item> items = order.getItems();
			for(Item item : items) {
				st.append(item.getItemId());
				st.append(SEPARATOR);
				st.append(item.getName());
				st.append(SEPARATOR);
				st.append(item.getDesc());
				st.append(SEPARATOR);
				st.append(item.getPrice());
				st.append(SEPARATOR);
				st.append(item.getType());
				st.append(SEPARATOR);
			}
			ordersw.add(st.toString());
		}
		
		ReadinFile.write(filename, ordersw);
	}
}
