package Controller;
import java.io.*;
import java.util.ArrayList;

import Entity.CreditCard;
import Entity.Guest;
import Entity.Item;
import Entity.Order;
import Entity.Payment;
import Entity.Reservation;
import Database.PaymentDB;

public class PaymentController{
    private static final double serviceCharge = 0.10;
    private static final double GST = 0.07;
	private static final String fileName = "Payment.txt";
	public static PaymentController instance = null;
    ArrayList<Payment> paymentList = new ArrayList<Payment>();

    public PaymentController() {
    	paymentList = new ArrayList<Payment>();
    }

    /**
	 * Creating new instance of payment controller
	 * 
	 * @return paymentcontroller instance
	 */
    public static PaymentController getInstance() {
        if (instance == null) instance = new PaymentController();
        return instance;
    }

    /**
	 * Creating new payment
	 * 
	 * @param payment
	 * 				Specifies the payment to be made
	 */
    public void createPayment(Payment payment) {
        paymentList.add(payment);
    }
    
    /**
	 * Retrieving payment id by reservation number
	 * 
	 * @param reservationNum
	 * 				Specifies the reservation number to retrieve payment details
	 * @return Payment id if found. Else will return 0
	 */
    public int retrievePaymentId(String reservationNum){
    	for (Payment payment : paymentList){
    		if(payment.getRes().equals(reservationNum)) {
    			return payment.getPaymentId();
    		}
    	}
    	return 0;
    }
    
    /**
	 * Retrieving room price by nights stayed
	 * 
	 * @param roomId
	 * 				Specifies the room id
	 * @return Subtotal if found. Else will return -1
	 */
    public double retrieveRoomST(String roomId) {
    	Reservation r;
		try {
			r = ReservationController.retrieveReservationByRoomId(roomId);
			double rate = Double.valueOf(RoomController.retrieveRoomRate(roomId));
			long diff = r.getCheckOutDate().getTime() - r.getCheckInDate().getTime();
			double nights = (diff / (1000*60*60*24));
			if(diff == 0) nights = 1.0;
	    	double st = rate * nights;
	    	return st;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return -1;
    }
    
    /**
	 * Retrieving payment subtotal
	 * 
	 * @param payment
	 * 				Specifies the payment
	 * @param roomId
	 * 				Specifies the room id
	 * @return Subtotal
	 */
    public double retrieveSubTotal(Payment payment, String roomId) {
    	double subTotal = retrieveRoomST(roomId);
    	ArrayList<Order> orders = payment.getOrders();
    	if(orders != null) {
            for(Order order : orders) {
            	for (Item item : order.getItems()) {
            		subTotal += item.getPrice();
            	}
            }
    	}
        payment.setSubTotal(subTotal);
        return subTotal;
    }
    
    /**
	 * Retrieving payment total price
	 * 
	 * @param payment
	 * 				Specifies the payment
	 * @param roomId
	 * 				Specifies the room id
	 * @return total
	 */
    public double retrieveTotal(Payment payment, String roomId) {
    	double subTotal = retrieveSubTotal(payment, roomId);
        double total = subTotal * (1 + GST) * (1 + serviceCharge);
    	payment.setTotal(total);
        return total;
    }
    
    /**
	 * Retrieving payment details for printing
	 * 
	 * @param payment
	 * 				Specifies the payment
	 * @param roomId
	 * 				Specifies the room id
	 * @param method
	 * 				Specifies the method of payment
	 * @param cash
	 * 				Specifies the cash amount
	 */
    public void retrievePayment(Payment payment, String roomId, int method, double cash) {
    	double sTotal = retrieveSubTotal(payment, roomId);
    	double total = retrieveTotal(payment, roomId);
    	double svc = sTotal * serviceCharge;
    	double gst = sTotal * (GST * (1 + serviceCharge));
    	Reservation r;
		try {
			r = ReservationController.retrieveReservationByRoomId(roomId);
			String gid = r.getGuestId();
			Guest guest = GuestController.retrieveGuestWithString(gid);
			CreditCard credit = CreditController.retrieveCreditByGuestId(gid);
			System.out.printf("                                     Payment                     #%s             \n", payment.getRes());
			System.out.println("---------------------------------------------------------------------------------");
			System.out.printf("GUEST: %s                                                                        \n", guest.getName());
	    	System.out.println("---------------------------------------------------------------------------------");
	    	System.out.printf("ROOM CHARGE                                                             %.2f\n", retrieveRoomST(roomId));
	    	System.out.println("---------------------------------------------------------------------------------");
	    	ArrayList<Order> orders = payment.getOrders();
	    	if(orders != null) {
		    	for(int i = 0; i < orders.size(); i++){
		    		System.out.printf("Room Service #%d                                                                 \n", i+1);
		        	System.out.println("---------------------------------------------------------------------------------");
		        	orders.get(i).viewOrder();
		    	}
	    	}
	        System.out.printf("SUBTOTAL                                                                %.2f\n", sTotal);
	        System.out.printf("10%% SVC CHG                                                             %.2f\n", svc);
	        System.out.printf("7%% GST                                                                  %.2f\n", gst);
	        System.out.println("---------------------------------------------------------------------------------");
	        System.out.printf("TOTAL                                                                   %.2f\n", total);
	        if(method == 0) {
	        	System.out.printf("CASH                                                                    %.2f\n", cash);
	        	System.out.printf("CHANGE                                                                  %.2f\n", cash - total);
	        	System.out.println("---------------------------------------------------------------------------------");
	        }
	        else {
	        	System.out.println("---------------------------------------------------------------------------------");
	            System.out.printf("CARD TYPE                                                               %s\n", credit.getCreditCardType());
	            System.out.printf("CARD NUMBER                                                %s\n", credit.getCreditCardNumber());
	            System.out.printf("CARD EXPIRY                                                             %s\n", credit.getCreditCardExp());
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Checking of payment id
	 * 
	 */
    public void checkID() {
    	int id = 1;
		if(paymentList!=null) {
			for(Payment payment : paymentList){
				if(payment.getPaymentId() > id) id = payment.getPaymentId();
			}
		}
		Payment.setIdCount(id+1);
    }

    /**
	 * Retrieval of all payments
	 * 
	 */
    public void loadinDB() {
    	PaymentDB paymentdb = new PaymentDB();
        try {
			this.paymentList = paymentdb.read(fileName);
			paymentdb.save(fileName, paymentList);
			checkID(); // ADDED TO CHECK
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Saving of all payments
	 * 
	 */
    public void savetoDB() {
    	PaymentDB paymentdb = new PaymentDB();
        try {
			paymentdb.save(fileName, paymentList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

}
