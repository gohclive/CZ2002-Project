package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import Entity.Item;
import Entity.Order;
import Entity.Payment;

public class PaymentDB implements DB {
	
	public static final String SEPARATOR = "|";

	@Override
	public ArrayList<Payment> read(String fileName) throws IOException {
		ArrayList<String> stringArray = (ArrayList<String>) ReadinFile.read(fileName);
		ArrayList<Payment> paymentList = new ArrayList<Payment>();
		
		
		for (int i = 0; i < stringArray.size(); i++) {
			String st = stringArray.get(i);
			StringTokenizer star = new StringTokenizer(st, SEPARATOR);
			ArrayList<Order> orderList = new ArrayList<Order>();
			
			
			int paymentId = Integer.valueOf(star.nextToken().trim());
	    	String resNum = star.nextToken().trim();
	    	String d = star.nextToken().trim();
	    	while(star.hasMoreTokens()) {
	    		int orderId = Integer.valueOf(star.nextToken().trim());
	    		String roomId = star.nextToken().trim();
	    		String reservationNum = star.nextToken().trim();
	    		String date = star.nextToken().trim();
	    		String status = star.nextToken().trim();
	    		String remarks = star.nextToken().trim();
	    		ArrayList<Item> items = new ArrayList<Item>();
	    		while(star.hasMoreTokens()) {
			    	int itemId = Integer.valueOf(star.nextToken().trim());
			    	if (itemId == -1) break;
			    	String name = star.nextToken().trim();
			    	String desc = star.nextToken().trim();
			    	double price = Double.valueOf(star.nextToken().trim());
			    	int type = Integer.valueOf(star.nextToken().trim());
			    	Item item = new Item(itemId, name, desc, price, type);
			    	items.add(item);
			    }
	    		Order order = new Order(orderId, roomId, reservationNum, items, date, status, remarks);
			    orderList.add(order);
	    	}
		  Payment payment = new Payment(paymentId, orderList, resNum, d);
		  paymentList.add(payment);
		}
		System.out.println(stringArray.size() + " Payment(s) Loaded.");
		return paymentList;
	}
	
	@Override
	public void save(String filename, List paymentList) throws IOException {
		ArrayList<String> paymentsw = new ArrayList<String>();
		for (int i = 0; i < paymentList.size(); i++) {
			Payment payment = (Payment) paymentList.get(i);
			StringBuilder st = new StringBuilder();
			
			st.append(payment.getPaymentId());
			st.append(SEPARATOR);
			st.append(payment.getRes());
			st.append(SEPARATOR);
			st.append(payment.getDate());
			st.append(SEPARATOR);
			
			ArrayList<Order> orderList = payment.getOrders();
			if(orderList != null) {
				for (Order order : orderList) {
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
					st.append("-1");
					st.append(SEPARATOR);
				}
			}

			paymentsw.add(st.toString());
		}
		
		ReadinFile.write(filename, paymentsw);
	}
}
