package UI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Entity.Item;
import Entity.Order;
import Entity.Reservation;
import Controller.MenuController;
import Controller.OrderController;
import Controller.ReservationController;

public class OrderUI {
    public static OrderUI instance = null;
    Scanner sc = new Scanner(System.in);
    
    private OrderUI() {
        sc = new Scanner(System.in);
    }

    public static OrderUI getInstance() {
        if (instance == null) instance = new OrderUI();
        return instance;
    }
    
    public void displayOptions() {
        int choice;
    	do {
            System.out.println("\n==================================================");
			System.out.println(" Room Service Orders: ");
			System.out.println("==================================================");
			System.out.println("(1) Create Order\t(2) Update Order");
			System.out.println("(3) Remove Order\t(4) View Order");
			System.out.println("(5) Back");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                	createOrder();
                    break;
                case 2:
                	Scanner sc = new Scanner(System.in);
                    int id = -1;
                    do {
					    try {
					    	System.out.println("Enter order ID to be updated: ");
					    	id = sc.nextInt();
					    	if(id <= 0) System.out.printf("Invalid input! ");
					    } catch (InputMismatchException e) {
					    	System.out.printf("Invalid input! ");
					    }
					    sc.nextLine();
					} while (id <= 0);
                    Order order = OrderController.getInstance().retrieveOrder(id);
                    if (order != null) updateOrder(order);
                    else System.out.println("Order does not exist!");
                    break;
                case 3:
                	if (OrderController.getInstance().displayOrder() > 0) {
                        OrderUI.getInstance().runRemoveOrder();
                    } else {
                        System.out.println("No order made yet!");
                    }
                    break;
                case 4:
                	viewOrder();
                    break;
                case 5:
                    break;
                default:
                	System.out.println("Please enter a valid option!");
					choice = 0;
            }
        } while (choice < 5);
    }

    public void createOrder() {
    	sc = new Scanner(System.in);
    	String roomId;
        String remarks;
        System.out.println("Enter room number:");
        roomId = sc.nextLine();
        System.out.println("");
        OrderController.getInstance().checkID();
        Order order = new Order(roomId);
        try {
			Reservation r = ReservationController.retrieveReservationByRoomId(roomId);
			if(r == null) {
				System.out.println("Room is not checked-in by anyone.");
				return;
			}
			else order.setReservationNum(r.getReservationNum());
		} catch (IOException e) {
			e.printStackTrace();
		}
        updateOrder(order);
        if(order.getItems().size() == 0) {
        	System.out.println("You did not add any items! Order deleted.");
        	OrderController.getInstance().deleteOrder(order);
        } else {
        	sc.nextLine();
        	System.out.println("Enter remarks: ");
        	remarks = sc.nextLine();
        	order.setRemarks(remarks);
        	order.setStatus("Confirmed");
        	System.out.println("Order created for room " + roomId);
        }
        OrderController.getInstance().savetoDB();
    }

    public void updateOrder(Order order) {
    	sc = new Scanner(System.in);
    	int input;
        int id;
    	order.viewOrder();
        do {
        	System.out.println("Please Choose a option to Continue:");
            System.out.println("(1) Add item");
            if(order.getStatus().equals("Ordering")) {
            	System.out.println("(2) Remove item");
    			System.out.println("(3) Finish");
            }else {
            	System.out.println("(2) Update Status");
            	System.out.println("(3) Change Remarks\n(4) Remove item");
    			System.out.println("(5) Finish");
            }
            input = sc.nextInt();
            switch (input) {
                case 1:
                    MenuController.retrieveInstance().displayMenu();
                    System.out.println("");
                    id = -1;
                    do {
					    try {
					    	System.out.print("Enter Item ID: ");
		                    id = sc.nextInt();
					    	if(id <= 0) System.out.printf("Invalid input! ");
					    } catch (InputMismatchException e) {
					    	System.out.printf("Invalid input! ");
					    }
					    sc.nextLine();
					} while (id <= 0);
                    OrderController.getInstance().createOrderItem(order, id);
                    input = 0;
                    break;
                case 2:
                	if(order.getStatus().equals("Ordering")) {
                		if (!(order.getItems().isEmpty())) {
                    		id = -1;
                            do {
        					    try {
        					    	System.out.print("Enter Item ID: ");
        		                    id = sc.nextInt();
        					    	if(id <= 0) System.out.printf("Invalid input! ");
        					    } catch (InputMismatchException e) {
        					    	System.out.printf("Invalid input! ");
        					    }
        					    sc.nextLine();
        					} while (id <= 0);
                            Item it = MenuController.retrieveInstance().retrieveItem(id);
                            if (it == null)
                                System.out.println("Item does not exist!");
                            else if (!order.removeItem(it))
                                System.out.println("Item not in order!");
                            input = 0;
                    	}
                    	else System.out.println("Order has no item!");
                    } else {
                    	sc.nextLine();
                    	System.out.println("Enter new status: ");
                    	System.out.println("(1) Preparing");
            			System.out.println("(2) Prepared");
            			System.out.println("(3) Delivered");
            			String input1 = sc.nextLine();
            			switch (input1) {
        				case "1":
        					order.setStatus("Preparing");
        					break;
        				case "2":
        					order.setStatus("Prepared");
        					break;
        				case "3":
        					order.setStatus("Delivered");
        					break;
        				}
                    	
                    }
                	break;
                case 3:
                	if(!order.getStatus().equals("Ordering")) {
                    	sc.nextLine();
                    	System.out.print("Enter new remarks: ");
                    	order.setRemarks(sc.nextLine());
                    } else input = 5;
                	break;
                case 4:
                	if (!(order.getItems().isEmpty())) {
                		id = -1;
                        do {
    					    try {
    					    	System.out.print("Enter Item ID: ");
    		                    id = sc.nextInt();
    					    	if(id <= 0) System.out.printf("Invalid input! ");
    					    } catch (InputMismatchException e) {
    					    	System.out.printf("Invalid input! ");
    					    }
    					    sc.nextLine();
    					} while (id <= 0);
                        Item it = MenuController.retrieveInstance().retrieveItem(id);
                        if (it == null)
                            System.out.println("Item does not exist!");
                        else if (!order.removeItem(it))
                            System.out.println("Item not in order!");
                        input = 0;
                	}
                	else System.out.println("Order has no item!");
                    break;
                case 5:
                    break;
                default:
                	System.out.println("Please enter a valid option.");
					input = 0;
            }
            OrderController.getInstance().updateOrder(order);
            order.viewOrder();
        } while (input < 5);
        OrderController.getInstance().savetoDB();
    }
    

    public void runRemoveOrder() {
    	sc = new Scanner(System.in);
    	int orderID = -1;
        do {
		    try {
		    	System.out.print("Enter Order ID:");
		        orderID = sc.nextInt();
		    	if(orderID <= 0) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (orderID <= 0);
        Order order = OrderController.getInstance().retrieveOrder(orderID);
        if (order != null) {
        	OrderController.getInstance().deleteOrder(order);
            System.out.printf("Order %d has been removed.\n", orderID);
        }
        else System.out.println("Order does not exist!");
    }

    public void viewOrder() {
    	sc = new Scanner(System.in);
    	if (OrderController.getInstance().displayOrder() > 0) {
        	int choice = -1;
        	do {
    		    try {
    		    	System.out.println("View order by (0 - Order ID, 1 - Room ID):");
    	        	choice = sc.nextInt();
    		    	if(choice != 0 && choice != 1) System.out.printf("Invalid input! ");
    		    } catch (InputMismatchException e) {
    		    	System.out.printf("Invalid input! ");
    		    }
    		    sc.nextLine();
    		} while (choice != 0 && choice != 1);
        	if(choice == 0) {
        		int orderID = -1;
                do {
        		    try {
        		    	System.out.print("Enter Order ID:");
        		        orderID = sc.nextInt();
        		    	if(orderID <= 0) System.out.printf("Invalid input! ");
        		    } catch (InputMismatchException e) {
        		    	System.out.printf("Invalid input! ");
        		    }
        		    sc.nextLine();
        		} while (orderID <= 0);
        		Order order = OrderController.getInstance().retrieveOrder(orderID);
        		if (order != null) order.viewOrder();
        		else System.out.println("Order does not exist!");
        	} else {
        		System.out.println("Enter Room ID:");
        		String roomID = sc.nextLine();
        		ArrayList<Order> orderList = OrderController.getInstance().retrieveOrderList(roomID);
        		if (orderList != null) {
        			for (Order order : orderList) {
        				order.viewOrder();
        				System.out.println();
        			}
        		}
        		else System.out.println("Order does not exist!");
        	}
       } else System.out.println("No order made yet!");
    }
}
