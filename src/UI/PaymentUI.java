package UI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Entity.Order;
import Entity.Payment;
import Entity.Reservation;
import Controller.OrderController;
import Controller.PaymentController;
import Controller.ReservationController;
import Controller.RoomController;

public class PaymentUI {
    public static PaymentUI instance = null;
    int choice;
    Scanner sc = new Scanner(System.in);
    
    private PaymentUI() {
        sc = new Scanner(System.in);
    }

    public static PaymentUI getInstance() {
        if (instance == null) instance = new PaymentUI();
        return instance;
    }
    
    public static boolean isNumeric(String str) { 
    	  try {  
    	    Double.parseDouble(str);  
    	    return true;
    	  } catch(NumberFormatException e){  
    	    return false;  
    	  }  
    }
    
    public void printPayment(String roomId) {
        int method = 2;
        
		try {
			Reservation r = ReservationController.retrieveReservationByRoomId(roomId);
			if(r == null) System.out.println("Room is not checked-in by anyone.");
			else {
				do {
				    try {
				    	System.out.println("Enter payment method (0 - Cash, 1 - Credit Card):");
				    	method = sc.nextInt();
				    } catch (InputMismatchException e) {
				    	System.out.printf("Invalid input! ");
				    }
				    sc.nextLine(); // clears the buffer
				} while (method != 0 && method != 1);
				double cash = -1;
				ArrayList<Order> orders = OrderController.getInstance().retrieveOrderList(roomId);
				ArrayList<Order> nOrders = new ArrayList<Order>();
				if(orders != null) { 
					for(Order o : orders) {
						if(!o.getStatus().equals("Paid")) nOrders.add(o);
					}
				} else nOrders = null;
				Payment payment = new Payment(nOrders, r.getReservationNum());
				if(method == 0) {
					do {
					    try {
					    	System.out.println("Please enter cash amount:");
					    	cash = sc.nextDouble();
					    	if(cash <= 0.0) System.out.printf("Invalid input! ");
					    } catch (InputMismatchException e) {
					    	System.out.printf("Invalid input! ");
					    }
					    sc.nextLine(); // clears the buffer
					} while (cash <= 0.0);
					double diff = PaymentController.getInstance().retrieveTotal(payment, roomId) - cash;
					double add = -1;
					while(diff > 0) {
						do {
						    try {
						    	System.out.printf("You're short %.2f! Enter cash amount (additional):\n", diff);
								add = sc.nextDouble();
						    } catch (InputMismatchException e) {
						    	System.out.printf("Invalid input! ");
						    }
						    sc.nextLine();
						} while (add < 0.0);
						cash += add;
						diff = PaymentController.getInstance().retrieveTotal(payment, roomId) - cash;
					}
				}
				PaymentController.getInstance().retrievePayment(payment, roomId, method, cash);
				PaymentController.getInstance().createPayment(payment);
				if(orders != null) {
					for(Order order : orders) {
						order.setStatus("Paid");
					}
					OrderController.getInstance().savetoDB();
				}
				PaymentController.getInstance().savetoDB();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
}
