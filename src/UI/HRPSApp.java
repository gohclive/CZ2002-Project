package UI;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import Controller.GuestController;
import Controller.MenuController;
import Controller.OrderController;
import Controller.PaymentController;
import Controller.ReservationController;
import Controller.RoomController;
import Database.ReservationDB;
import Entity.Reservation;

public class HRPSApp {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		Date d = new Date();
		System.out.println(d);
		Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);
        Date time = calendar.getTime();
        timer.schedule(new checkExpired(), time);
		MenuController.retrieveInstance().loadinDB();
		OrderController.getInstance().loadinDB();
		PaymentController.getInstance().loadinDB();
		int main_menu_choice;
		Scanner sc = new Scanner(System.in);
		do {

			System.out.println("\n _    _ _____  _____   _____ ");
			System.out.println("| |  | |  __ \\|  __ \\ / ____|");
			System.out.println("| |__| | |__) | |__) | (___  ");
			System.out.println("|  __  |  _  /|  ___/ \\___ \\ ");
			System.out.println("| |  | | | \\ \\| |     ____) |");
			System.out.println("|_|  |_|_|  \\_\\_|    |_____/    v1.0");
			System.out.println("Copyright \u00a9 2020\n");

			System.out.println("==================================================");
			System.out.println(" Please select one of the following functions: ");
			System.out.println("==================================================");
			System.out.println("(1) Hotel Management\t\t(2) Administration");
			System.out.println("(3) Check-in Guest\t\t(4) Check-out Guest");
			System.out.println("(5) Room Statistics Report\t(6) Save and exit");

			System.out.println("\nEnter your choice:");
			main_menu_choice = sc.nextInt();
			sc.nextLine();

			switch (main_menu_choice) {
			case 1:
				int hotel_mgt_choice;
				do {
					System.out.println("\n==================================================");
					System.out.println(" Hotel Management: ");
					System.out.println("==================================================");
					System.out.println("(1) Guest Details Management\t(2) Reservations");
					System.out.println("(3) Back");
					hotel_mgt_choice = sc.nextInt();
					switch (hotel_mgt_choice) {
					case 1:
						int guest_mgt_choice;
						do {
							System.out.println("\n==================================================");
							System.out.println(" Guest Details Management: ");
							System.out.println("==================================================");
							System.out.println("(1) Create Guest\t(2) Update Guest");
							System.out.println("(3) Remove Guest\t(4) View Guests");
							System.out.println("(5) Search Guest\t(6) Back");
							guest_mgt_choice = sc.nextInt();
							switch (guest_mgt_choice) {
							case 1:
								// Create new guest function
								GuestController.createGuest();
								break;
							case 2:
								GuestController.updateGuestById();
								break;
							case 3:
								GuestController.deleteGuestById();
								break;
							case 4:
								GuestController.retrieveAllGuest();
								break;
							case 5:
								GuestController.retrieveGuestbyId();
								break;
							case 6:
								guest_mgt_choice = 7;
								break;
							default:
								System.out.println("Please enter a valid option.");
								guest_mgt_choice = 0;
							}
						} while (guest_mgt_choice < 7);

						break;
					case 2:

						int reservation_choice;
						do {
							System.out.println("\n==================================================");
							System.out.println(" Reservation Management: ");
							System.out.println("==================================================");
							System.out.println("(1) Create Reservation\t\t(2) Update Reservation");
							System.out.println("(3) Remove Reservation\t\t(4) Print Reservation");
							System.out.println("(5) View All Reservations \t(6) Back");
							reservation_choice = sc.nextInt();
							switch (reservation_choice) {
							case 1:
								ReservationController.createReservation(); // function to create Reservation
								break;
							case 2:
								ReservationController.updateReservation(); // function to update Reservation
								break;
							case 3:
								System.out.println("Enter Reservation Number: ");
								String cancelReservation_id = sc.next();
								ReservationController.updateReservationToCancelled(cancelReservation_id);
								break;
							case 4:
								ReservationController.retrieveAllReservations();
								System.out.println("");
								System.out.println("Enter Reservation Number: ");
								String printReservation_id = sc.next();
								ReservationController.printReservation(printReservation_id);
								break;
							case 5:
								ReservationController.retrieveAllReservations();
								break;
							case 6:
								reservation_choice = 6;
								break;
							default:
								System.out.println("Please enter a valid option.");
								reservation_choice = 0;
							}
						} while (reservation_choice < 6);

						break;
					case 3:
						hotel_mgt_choice = 4;
						break;
					default:
						System.out.println("Please enter a valid option.");
						hotel_mgt_choice = 0;
					}
				} while (hotel_mgt_choice < 4);

				break;
			case 2:
				int admin_choice;
				do {
					System.out.println("\n==================================================");
					System.out.println(" Administration: ");
					System.out.println("==================================================");
					System.out.println("(1) Room Management\t\t(2) Room Service");
					System.out.println("(3) Back");
					admin_choice = sc.nextInt();
					switch (admin_choice) {
					case 1:
						int room_mgt_choice;
						do {
							System.out.println("\n==================================================");
							System.out.println(" Room Management: ");
							System.out.println("==================================================");
							System.out.println("(1) Create Room\t\t\t(2) Update Room details");
							System.out.println("(3) Update Room Status\t\t(4) View All Room details");
							System.out.println("(5) View a Room detail\t\t(6) View All Room Status");
							System.out.println("(7) Print Occupancy Rate\t(8) Back");
							room_mgt_choice = sc.nextInt();
							switch (room_mgt_choice) {
							case 1:
								// Create Room
								RoomController.retrieveAllRoom();
								RoomController.createRoom();
								break;
							case 2:
								// Retrieve room and update by room id
								RoomController.updateRoom();
								break;
							case 3:
								// Retrieve room and update room status only by room id
								RoomController.updateRoomStatusOnly();
								break;
							case 4:
								// Retrieve and print all room details
								RoomController.retrieveAllRoom();
								break;
							case 5:
								// Retrieve and print room details by room id
								RoomController.retrieveOneRoom();
								break;
							case 6:
								// Print all room status
								RoomController.retrieveRoomStatus();
								break;
							case 7:
								// Print different room type's occupancy rate
								RoomController.retrieveOccupancyRate();
								break;
							case 8:
								room_mgt_choice = 9;
								break;
							default:
								System.out.println("Please enter a valid option.");
								room_mgt_choice = 0;
							}

						} while (room_mgt_choice < 9);

						break;
					case 2:

						int room_service_choice;
						do {
							System.out.println("\n==================================================");
							System.out.println(" Room Service: ");
							System.out.println("==================================================");
							System.out.println("(1) Room Service Order\t\t(2) Menu Management");
							System.out.println("(3) Back");
							room_service_choice = sc.nextInt();
							switch (room_service_choice) {
							case 1:
								OrderUI.getInstance().displayOptions();
								OrderController.getInstance().savetoDB();
								break;
							case 2:
								MenuUI.getInstance().displayOptions();
								MenuController.retrieveInstance().savetoDB();
								break;
							case 3:
								break;
							default:
								System.out.println("Please enter a valid option.");
								room_service_choice = 0;
							}

						} while (room_service_choice < 3);

						break;
					case 3:
						admin_choice = 4;
						break;
					default:
						System.out.println("Please enter a valid option.");
						admin_choice = 0;
					}
				} while (admin_choice < 4);
				break;
			case 3:
				// check in guest function
				System.out.println("Check-in Guest By:");
				System.out.println("1. Reservation (Number)");
				System.out.println("2. Reservation (Guest Id)");
				System.out.println("3. Walk-In");
				String checkin_choice = sc.next();
				String checkin_id;
				switch (checkin_choice) {
				case "1":
					System.out.println("Enter Reservation Number: ");
					checkin_id = sc.next();
					ReservationController.checkInGuest(checkin_id, "R");
					break;

				case "2":
					System.out.println("Enter Guest Id: ");
					checkin_id = sc.next();
					ReservationController.checkInGuest(checkin_id, "G");
					break;
				case "3":
					ReservationController.walkIn();
					break;
				}
				break;
			case 4:
				System.out.println("Enter Room Id for Check-Out(E.g. 02-04):");
				String checkout_id = sc.nextLine();
				PaymentUI.getInstance().printPayment(checkout_id);
				PaymentController.getInstance().savetoDB();
				ReservationController.checkOutGuest(checkout_id);
				break;
			case 5:
				RoomController.retrieveOccupancyRate();
				RoomController.retrieveRoomStatus();
				break;
			case 6:
				System.out.println("Saving all data... Program terminating ..."); // Save "database into file"
				main_menu_choice = 7;
				break;
			default:
				System.out.println("Please enter a valid option.");
				main_menu_choice = 0;
			}
		} while (main_menu_choice < 7);
		MenuController.retrieveInstance().savetoDB();
		OrderController.getInstance().savetoDB();
		PaymentController.getInstance().savetoDB();
		sc.close();
	}
	static class checkExpired extends TimerTask {
        public void run() {
            //copy code here
        	String fileName = "Reservation.txt";
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		Date today = new Date();
    		Date cid;
    		ArrayList<Reservation> reservationList = ReservationController.retrieveReservation();
    		for (int i = 0; i < reservationList.size(); i++) {
    			cid = reservationList.get(i).getCheckInDate();
    			if (cid.before(today) && reservationList.get(i).getStatus().equalsIgnoreCase("CONFIRMED")) {
    				reservationList.get(i).setStatus("EXPIRED");
    				// change room status to vacant
    				String roomId = reservationList.get(i).getRoomId();
    				RoomController.updateRoomStatus(roomId, "VACANT");
    			}
    		}
    		// Write Reservation records to file
    		ReservationDB reservationDB = new ReservationDB();
    		try {
    			reservationDB.save(fileName, reservationList);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			System.out.println("IOException > " + e.getMessage());
    		}
        }
	}
}
