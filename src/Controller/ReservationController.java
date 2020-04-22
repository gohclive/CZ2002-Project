package Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Database.ReadinFile;
import Database.ReservationDB;
import Database.RoomDB;
import Entity.Guest;
import Entity.Reservation;
import Entity.Room;

public class ReservationController {

	public static final String SEPARATOR = "|";
	private static String fileName = "Reservation.txt";

	/**
	 * Create new reservation
	 * @throws IOException
	 * 
	 */
	public static void createReservation() throws IOException {
		String reservationNum;
		String guestId = null;
		String roomId = "";
		String status;
		String ciDate = "";
		String coDate = "";
		Date checkInDate = null;
		Date checkOutDate = null;
		ArrayList<Room> availRooms = null;
		int numOfAdults = -1;
		int numOfChildren = -1;
		boolean checker = false;
		boolean checker1 = false;

		String roomRegExp = "[0][2-7][-][0][1-8]";
		Pattern roomIdPattern = Pattern.compile(roomRegExp);

		// To be used for data validation
		String digit = "\\d+";
		String dateValidation = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";

		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date todaysdate = new Date();

		do {
			System.out.println("Enter Check-In Date (dd/mm/yyyy)");
			try {
				ciDate = sc.nextLine();
				checkInDate = sdf.parse(ciDate);

				if (checkInDate.before(todaysdate) || !ciDate.matches(dateValidation))
					System.out.println("Invalid Format! Please use the correct format. E.g (20/01/2020)");
				else {
					checker = true;
				}
			} catch (ParseException e) {
				System.out.println("invalid format! Please use the correct format. E.g (20/01/2020)");
			}
		} while (!checker || !ciDate.matches(dateValidation));

		do {
			System.out.println("Enter Check-Out Date (dd/mm/yyyy)");
			try {
				coDate = sc.nextLine();
				checkOutDate = sdf.parse(coDate);
				if(!checkOutDate.before(checkInDate)) {
					if (checkOutDate.before(todaysdate) || !ciDate.matches(dateValidation))
						System.out.println("Invalid Format! Please use the correct format. E.g (20/01/2020)");
					else {
						checker1 = true;
					}
				}else {
					System.out.println("Choose a check out date after check in date.");
				}
			
			} catch (ParseException e) {
				System.out.println("invalid format! Please use the correct format. E.g (20/01/2020)");
			}
		} while (!checker1 || !coDate.matches(dateValidation));

		ArrayList<Room> rooms = null;
		do {
			// room type
			// vacantList = RoomController.allAvailableRoomTypes();
			ArrayList<Integer> vacantList = new ArrayList<Integer>();
			try {
				rooms = RoomController.retrieveRoom();
				vacantList = ReservationController.retrieveAvailRoomNum(rooms, "room", ciDate, coDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String choice, roomType = null;
			do {
				System.out.println("Please enter Room Type:");
				System.out.println("(1) Single - " + vacantList.get(0) + " rooms left.");
				System.out.println("(2) Double - " + vacantList.get(1) + " rooms left.");
				System.out.println("(3) Deluxe - " + vacantList.get(2) + " rooms left.");
				System.out.println("(4) VIP Suite - " + vacantList.get(3) + " rooms left.");
				choice = sc.nextLine();

				if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
					System.out.println("Please select a valid option.");
				} else {
					switch (choice) {
					case "1":
						roomType = "Single Room";
						break;
					case "2":
						roomType = "Double Room";
						break;
					case "3":
						roomType = "Deluxe Room";
						break;
					case "4":
						roomType = "VIP Suite";
						break;
					}
				}
			} while (choice.equals("") || !choice.matches(digit));
			// bed type
			// ArrayList<String> bedList = new ArrayList<String>();
			try {
				rooms = RoomController.retrieveRoomIdByRoomType(roomType);
				vacantList = ReservationController.retrieveAvailRoomNum(rooms, "bed", ciDate, coDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Please enter Bed Type");
			String bedType = null;
			do {
				System.out.println("(1) Single Bed - " + vacantList.get(0) + " rooms available");
				System.out.println("(2) Double Bed - " + vacantList.get(1) + " rooms available");
				System.out.println("(3) Super Single Bed - " + vacantList.get(2) + " rooms available");
				System.out.println("(4) Queen Bed - " + vacantList.get(3) + " rooms available");
				System.out.println("(5) King Bed - " + vacantList.get(4) + " rooms available");
				choice = sc.nextLine();

				if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")
						&& !choice.equals("5")) {
					System.out.println("Please select a valid option.");
				} else {
					switch (choice) {
					case "1":
						bedType = "Single Bed";
						break;
					case "2":
						bedType = "Double Bed";
						break;
					case "3":
						bedType = "Super Single Bed";
						break;
					case "4":
						bedType = "Queen Bed";
						break;
					case "5":
						bedType = "King Bed";
						break;
					}
				}
			} while (choice.equals("") || !choice.matches(digit));

			rooms = RoomController.retrieveRoomIdByTypes(roomType, bedType);
			try {
				availRooms = ReservationController.retrieveAvailRoom(rooms, ciDate, coDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (rooms.size() == 0) {
				System.out.println("No Rooms Available, Please pick again!");
			} else {
				System.out.println("\n==================================================");
				System.out.println(" Room Details ");
				System.out.println("==================================================");
				System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", "roomId", "viewType", "roomFloor", "roomNumber",
						"wifiEnabled", "smokingStatus");
				System.out.println("");

				ArrayList<String> availRoomIdsOnly = new ArrayList<String>();
				for (int i = 0; i < availRooms.size(); i++) {
					System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", availRooms.get(i).getRoomId(),
							availRooms.get(i).getViewType(), availRooms.get(i).getRoomFloor(),
							availRooms.get(i).getRoomNumber(), availRooms.get(i).getWifiEnabled(),
							availRooms.get(i).getSmokingStatus());
					System.out.println("");
					availRoomIdsOnly.add(availRooms.get(i).getRoomId());
				}
				
				do {
					System.out.println("Enter Room Id: ");
					roomId = sc.next();
					Matcher matcher = roomIdPattern.matcher(roomId);
					if (roomId.length() != 5 || !matcher.matches() || !availRoomIdsOnly.contains(roomId)) {
						roomId = "";
						System.out.println("You have entered a invalid Room Id. Please try again. (E.g. 02-04)");
					}
				} while (roomId.equals(""));
			}

		} while (rooms == null || rooms.size() == 0);

		// check guest id
		String rc;
		do {
			System.out.println("Are you a returning guest? (y/n)");
			rc = sc.next();
			if (rc.equalsIgnoreCase("y")) {
				System.out.println("Enter Guest Id: ");
				guestId = sc.next();
				// find guest Id
				Guest g = GuestController.retrieveGuestWithString(guestId);
				if (g == null) {
					do {
						System.out.println("Invalid Guest Id");
						System.out.println("1. try again");
						System.out.println("2. Create new guest entry");
						int option = sc.nextInt();
						sc.nextLine();
						switch (option) {
						case 1:
							System.out.println("Enter Guest Id: ");
							guestId = sc.next();
							g = GuestController.retrieveGuestWithString(guestId);
							break;

						case 2:
							GuestController.createGuest();
							g = GuestController.retrieveGuestWithString(guestId);
							break;
						}
					} while (g == null);
				}
			} else if (rc.equalsIgnoreCase("n")) {
				GuestController.createGuest();
			}
		} while (!rc.equalsIgnoreCase("y") && !rc.equalsIgnoreCase("n"));

		// enter number of adults and children
		do {
			System.out.println("Enter total Number of Adults: ");
			while (!sc.hasNextInt()) {
		        System.out.println("Please enter numbers only.");
		        sc.next(); // this is important!
		    }
			numOfAdults = sc.nextInt();
			if(numOfAdults < 0) {
				System.out.println("Please enter a valid number.");
			}
			
		}while(numOfAdults <= -1);

		do {
			System.out.println("Enter total Number of Children: ");
			while (!sc.hasNextInt()) {
		        System.out.println("Please enter numbers only.");
		        sc.next(); // this is important!
		    }
			numOfChildren = sc.nextInt();
			if(numOfChildren < 0) {
				System.out.println("Please enter a valid number.");
			}
		}while(numOfChildren <= -1);
		
		// print out details
		System.out.println("");
		System.out.println("Check-in Date: " + sdf.format(checkInDate));
		System.out.println("Check-out Date: " + sdf.format(checkOutDate));
		System.out.println("--------------------------------------------");
		RoomController.retrieveOneRoom(roomId);
		System.out.println("Guest Id: " + guestId);
		System.out.println("Number of adults: " + numOfAdults);
		System.out.println("Number of children: " + numOfChildren);
		System.out.println("Confirm Reservation? (y/n)");
		String confirmation = sc.next();
		if (confirmation.equalsIgnoreCase("y")) {
			String type = RoomController.retrieveRoomType(roomId);
			if (type.equals("Deluxe ")) {
				type = "X";
			}
			String reservationDate = sdf.format(checkInDate).replaceAll("\\D", "");
			String reservationRoomId = roomId.replaceAll("\\D", "");

			reservationNum = type.charAt(0) + "-" + reservationDate + "-" + reservationRoomId;
			status = "CONFIRMED";
			Reservation r = new Reservation(reservationNum, guestId, roomId, status, checkInDate, checkOutDate,
					numOfAdults, numOfChildren);
			ReservationDB reservationDB = new ReservationDB();
			ArrayList al = reservationDB.read(fileName);
			al.add(r);

			try {
				// Write Room records to file
				reservationDB.save(fileName, al);
				System.out.println("Room " + r.getRoomId() + " has been successfully reserved.");
				System.out.println("You have successfully created a new Reservation! ");

			} catch (IOException e) {
				System.out.println("IOException > " + e.getMessage());
			}

		} else {
			System.out.println("Reservation not confirmed!");
		}
	}

	/**
	 * Update Reservation details
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	public static void updateReservation() throws IOException, ParseException {
		Scanner sc = new Scanner(System.in);
		String roomId = null;
		Date checkInDate = null;
		Date checkOutDate = null;
		int numOfAdults;
		int numOfChildren;
		int option = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		boolean checker = false;
		boolean checker1 = false;
		Date todaysdate = new Date();

		// To be used for data validation
		String digit = "\\d+";
		String dateValidation = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";
		String roomRegExp = "[0][1-7][-][0][1-8]";
		Pattern roomIdPattern = Pattern.compile(roomRegExp);

		Reservation updateReservation = new Reservation();
		updateReservation = retrieveReservationDetails();

		System.out.println("\nPlease choose Reservation details to update \n(1) Room Type  "
				+ " \n(2) Check-in Date \n(3) Check-out Date \n(4) Number of People");
		option = sc.nextInt();
		sc.nextLine();

		switch (option) {
		case 1:
			ArrayList<Room> rooms = null;
			do {
				// room type
				String choice, roomType = null;
				do {
					System.out.println("Please enter Room Type:");
					System.out.println("(1) Single");
					System.out.println("(2) Double");
					System.out.println("(3) Deluxe");
					System.out.println("(4) VIP Suite");
					choice = sc.nextLine();

					if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (choice) {
						case "1":
							roomType = "Single Room";
							break;
						case "2":
							roomType = "Double Room";
							break;
						case "3":
							roomType = "Deluxe Room";
							break;
						case "4":
							roomType = "VIP Suite";
							break;
						}
					}
				} while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4"));
				// bed type
				System.out.println("Please enter Bed Type");
				String bedType = null;
				do {
					System.out.println("(1) Single Bed");
					System.out.println("(2) Double Bed");
					System.out.println("(3) Super Single Bed");
					System.out.println("(4) Queen Bed");
					System.out.println("(5) King Bed");
					choice = sc.nextLine();

					if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")
							&& !choice.equals("5")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (choice) {
						case "1":
							bedType = "Single Bed";
							break;
						case "2":
							bedType = "Double Bed";
							break;
						case "3":
							bedType = "Super Single Bed";
							break;
						case "4":
							bedType = "Queen Bed";
							break;
						case "5":
							bedType = "King Bed";
							break;
						}
					}
				} while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")
						&& !choice.equals("5"));

				rooms = RoomController.retrieveRoomIdByTypes(roomType, bedType);
				if (rooms.size() == 0) {
					System.out.println("No Rooms Available, Please pick again!");
				} else {
					System.out.println("\n==================================================");
					System.out.println(" Room Details ");
					System.out.println("==================================================");
					System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", "roomId", "viewType", "roomFloor",
							"roomNumber", "wifiEnabled", "smokingStatus");
					System.out.println("");

					ArrayList<String> availRoomIdsOnly = new ArrayList<String>();
					for (int i = 0; i < rooms.size(); i++) {
						System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", rooms.get(i).getRoomId(),
								rooms.get(i).getViewType(), rooms.get(i).getRoomFloor(), rooms.get(i).getRoomNumber(),
								rooms.get(i).getWifiEnabled(), rooms.get(i).getSmokingStatus());
						System.out.println("");
						availRoomIdsOnly.add(rooms.get(i).getRoomId());
					}
					
					do {
						System.out.println("Enter Room Id: ");
						roomId = sc.next();
						Matcher matcher = roomIdPattern.matcher(roomId);
						if (roomId.length() != 5 || !matcher.matches() || !availRoomIdsOnly.contains(roomId)) {
							roomId = "";
							System.out.println("You have entered a invalid Room Id. Please try again. (E.g. 02-04)");
						}
					} while (roomId.equals(""));
				}

			} while (rooms == null || rooms.size() == 0);

			// change room
			RoomController.updateRoomStatus(updateReservation.getRoomId(), "VACANT");
			updateReservation.setRoomId(roomId);

			try {
				ArrayList alr = retrieveReservation();
				for (int i = 0; i < alr.size(); i++) {
					Reservation searchReservation = (Reservation) alr.get(i);

					if (updateReservation.getReservationNum().equals(searchReservation.getReservationNum())) {
						alr.set(i, updateReservation);
					}
				}
				// Write Reservation records to file
				ReservationDB reservationDB = new ReservationDB();
				reservationDB.save(fileName, alr);

				System.out.println("Reservation details has been successfully updated!");
			} catch (

			IOException e)

			{
				System.out.println("IOException > " + e.getMessage());
			}

			break;

		case 2:
			// check in date
			String ciDate = "";
			do {
				System.out.println("Enter Check-In Date (dd/mm/yyyy)");
				try {

					ciDate = sc.nextLine();
					checkInDate = sdf.parse(ciDate);

					if (checkInDate.before(todaysdate) || !ciDate.matches(dateValidation))
						System.out.println("Invalid Format! Please use the correct format. E.g (20/01/2020)");
					else {
						checker = true;
					}
				} catch (ParseException e) {
					System.out.println("invalid format! Please use the correct format. E.g (20/01/2020)");
				}
			} while (!checker || !ciDate.matches(dateValidation));

			updateReservation.setCheckInDate(checkInDate);
			try {
				ArrayList alr = retrieveReservation();
				for (int i = 0; i < alr.size(); i++) {
					Reservation searchReservation = (Reservation) alr.get(i);

					if (updateReservation.getReservationNum().equals(searchReservation.getReservationNum())) {
						alr.set(i, updateReservation);
					}
				}
				// Write Reservation records to file
				ReservationDB reservationDB = new ReservationDB();
				reservationDB.save(fileName, alr);

				System.out.println("Room details has been successfully updated!");
			} catch (

			IOException e)

			{
				System.out.println("IOException > " + e.getMessage());
			}

			break;
		case 3:
			// check out date
			String coDate = "";
			do {
				System.out.println("Enter Check-out Date (dd/mm/yyyy)");
				try {

					coDate = sc.nextLine();
					checkOutDate = sdf.parse(coDate);

					if (checkOutDate.before(todaysdate) || !coDate.matches(dateValidation))
						System.out.println("Invalid Format! Please use the correct format. E.g (20/01/2020)");
					else {
						checker1 = true;
					}
				} catch (ParseException e) {
					System.out.println("invalid format! Please use the correct format. E.g (20/01/2020)");
				}
			} while (!checker1 || !coDate.matches(dateValidation));

			updateReservation.setCheckInDate(checkOutDate);
			try {
				ArrayList alr = retrieveReservation();
				for (int i = 0; i < alr.size(); i++) {
					Reservation searchReservation = (Reservation) alr.get(i);

					if (updateReservation.getReservationNum().equals(searchReservation.getReservationNum())) {
						alr.set(i, updateReservation);
					}
				}
				// Check if file exist
				// fileChecker.checkFileExist(fileName);
				// Write Reservation records to file
				ReservationDB reservationDB = new ReservationDB();
				reservationDB.save(fileName, alr);

				System.out.println("Reservation details has been successfully updated!");
			} catch (

			IOException e)

			{
				System.out.println("IOException > " + e.getMessage());
			}

			break;
		case 4:
			// number of people
			do {
				System.out.println("Enter total Number of Adults: ");
				while (!sc.hasNextInt()) {
			        System.out.println("Please enter numbers only.");
			        sc.next(); // this is important!
			    }
				numOfAdults = sc.nextInt();
				if(numOfAdults < 0) {
					System.out.println("Please enter a valid number.");
				}
				
			}while(numOfAdults <= -1);

			do {
				System.out.println("Enter total Number of Children: ");
				while (!sc.hasNextInt()) {
			        System.out.println("Please enter numbers only.");
			        sc.next(); // this is important!
			    }
				numOfChildren = sc.nextInt();
				if(numOfChildren < 0) {
					System.out.println("Please enter a valid number.");
				}
			}while(numOfChildren <= -1);
			
			updateReservation.setNumOfAdults(numOfAdults);
			updateReservation.setNumOfChildren(numOfChildren);
			try {
				ArrayList alr = retrieveReservation();
				for (int i = 0; i < alr.size(); i++) {
					Reservation searchReservation = (Reservation) alr.get(i);

					if (updateReservation.getReservationNum().equals(searchReservation.getReservationNum())) {
						alr.set(i, updateReservation);
					}
				}
				// Write Reservation records to file
				ReservationDB reservationDB = new ReservationDB();
				reservationDB.save(fileName, alr);
				System.out.println("Reservation details has been successfully updated!");
			} catch (

			IOException e)

			{
				System.out.println("IOException > " + e.getMessage());
			}
			break;

		}

	}

	/**
	 * Retrieval of Reservation's details by reservationNum.
	 * 
	 * @return Reservation details.
	 */
	private static Reservation retrieveReservationDetails() {
		String reservationId;
		Scanner sc = new Scanner(System.in);
		ArrayList alr = retrieveReservation();
		Reservation r = null;

		do {
			System.out.println("Please enter Reservation ID: ");
			reservationId = sc.nextLine();

			for (int i = 0; i < alr.size(); i++) {
				Reservation searchReservation = (Reservation) alr.get(i);
				if (searchReservation.getReservationNum().equalsIgnoreCase(reservationId)) {
					r = searchReservation;
					return r;
				}
			}
			if (r == null) {
				System.out.println("Reservation ID does not exist.");
			}
		} while (r == null);
		return r;
	}

	/**
	 * Retrieval of reservation details.
	 * 
	 * @return Arraylist of all reservation.
	 */
	public static ArrayList retrieveReservation() {
		ArrayList alr = null;
		try {
			// read file containing Guest records
			ReservationDB reservationDB = new ReservationDB();
			alr = reservationDB.read(fileName);

		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		return alr;
	}

	/**
	 * Retrieval of reservation details by room id
	 * @throws IOException
	 * @param id
	 * 				Specifies the room id
	 * @return reservation
	 */
	public static Reservation retrieveReservationByRoomId(String id) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			Reservation r = new Reservation();
			r.setReservationNum(star.nextToken().trim());
			r.setGuestId(star.nextToken().trim());
			r.setRoomId(star.nextToken().trim());
			r.setStatus(star.nextToken().trim());

			String checkInDate = star.nextToken().trim();
			try {
				r.setCheckInDate(sdf.parse(checkInDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String checkoutDate = star.nextToken().trim();
			try {
				r.setCheckOutDate(sdf.parse(checkoutDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			r.setNumOfAdults(Integer.valueOf(star.nextToken().trim()));
			r.setNumOfChildren(Integer.valueOf(star.nextToken().trim()));

			if (r.getStatus().equals("CHECKED-IN") && id.equals(r.getRoomId())) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Retrieval of reservation details by room id, type and status
	 * @throws IOException
	 * @param id
	 * 				Specifies the reservation number or guest id used
	 * @param type
	 * 				Specifies the check in type of reservation number or guest id
	 * @param status
	 * 				Specifies the room status
	 * @return reservation
	 */
	public static Reservation retrieveReservationById(String id, String type, String status) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			Reservation r = new Reservation();
			r.setReservationNum(star.nextToken().trim());
			r.setGuestId(star.nextToken().trim());
			r.setRoomId(star.nextToken().trim());
			r.setStatus(star.nextToken().trim());

			String checkInDate = star.nextToken().trim();
			try {
				r.setCheckInDate(sdf.parse(checkInDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String checkoutDate = star.nextToken().trim();
			try {
				r.setCheckOutDate(sdf.parse(checkoutDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			r.setNumOfAdults(Integer.valueOf(star.nextToken().trim()));
			r.setNumOfChildren(Integer.valueOf(star.nextToken().trim()));

			switch (type) {
			case "R":
				if (r.getReservationNum().equals(id)) {
					return r;
				}
				break;

			case "G":
				if (status.equalsIgnoreCase("ALL")) {
					break;
				} else if (r.getGuestId().equals(id) && r.getStatus().equals(status)) {
					return r;
				}
				break;
			}

		}
		return null;
	}

	/**
	 * Retrieval of all Reservation details
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	public static void retrieveAllReservations() throws IOException, ParseException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		System.out.println("\n==================================================");
		System.out.println(" Reservations ");
		System.out.println("==================================================");
		System.out.printf("%-16s %-10s %-5s %-13s %-13s %-15s %-10s %-10s", "Reservation No.", "Guest ID", "Room ID",
				"Status", "Check-In Date", "Check-Out Date", "No. of Adults", "No. of children");
		System.out.println("");
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","
			String reservationNumber = star.nextToken().trim();
			String guestID = star.nextToken().trim();
			String roomID = star.nextToken().trim();
			String status = star.nextToken().trim();
			Date checkInDate = sdf.parse(star.nextToken().trim());
			Date checkOutDate = sdf.parse(star.nextToken().trim());
			int numberOfAdults = Integer.parseInt(star.nextToken().trim());
			int numberOfChildren = Integer.parseInt(star.nextToken().trim());
			String checkInDate1 = sdf.format(checkInDate);
			String checkOutDate1 = sdf.format(checkOutDate);
			if (!status.equalsIgnoreCase("expired")) {
				System.out.printf("%-16s %-10s %-7s %-14s %-13s %-19s %-14s %-10s", reservationNumber, guestID, roomID,
						status, checkInDate1, checkOutDate1, numberOfAdults, numberOfChildren);
				System.out.println("");
			}
		}
	}

	/**
	 * Retrieval of reservation details by room id, type and status
	 * @throws IOException
	 * @param id
	 * 				Specifies the reservation number or guest id used
	 * @param type
	 * 				Specifies the check in type of reservation number or guest id
	 */
	public static void checkInGuest(String id, String type) {
		Reservation r = new Reservation();
		if (type.equalsIgnoreCase("R")) {
			// Reservation
			try {
				r = retrieveReservationById(id, type, "CONFIRMED");
			} catch (IOException e) {
				System.out.println("IOException > " + e.getMessage());
			}

		} else if (type.equalsIgnoreCase("G")) {
			// guest
			try {
				r = retrieveReservationById(id, type, "CONFIRMED");
			} catch (IOException e) {
				System.out.println("IOException > " + e.getMessage());
			}
		}

		if (r != null) {

			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			today = new Date();
			String todayS = sdf.format(today);
			try {
				today = sdf.parse(todayS);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (r.getCheckInDate().equals(today)) {
				r.setStatus("CHECKED-IN");
				try {
					ArrayList alr = retrieveReservation();
					for (int i = 0; i < alr.size(); i++) {
						Reservation searchReservation = (Reservation) alr.get(i);

						if (r.getReservationNum().equals(searchReservation.getReservationNum())) {
							alr.set(i, r);
						}
					}
					// Write Reservation records to file
					ReservationDB reservationDB = new ReservationDB();
					reservationDB.save(fileName, alr);

					boolean success = RoomController.updateRoomStatus(r.getRoomId(), "OCCUPIED");
					if (success) {
						System.out.println("Check-In Successful!");
					}
				} catch (

				IOException e)

				{
					System.out.println("IOException > " + e.getMessage());
				}
			} else if (r.getCheckInDate().before(today)) {
				System.out.println("Reservation expired");
				r.setStatus("EXPIRED");
				RoomController.updateRoomStatus(r.getRoomId(), "VACANT");
			} else if (r.getCheckInDate().after(today)) {
				System.out.println("Please check in on " + sdf.format(r.getCheckInDate()));
			}

		} else {
			System.out.println("unable to find reservation!");
		}

	}

	/**
	 * Creation of reservation by walk in
	 * @throws IOException
	 * 
	 */
	public static void walkIn() throws IOException {
		String reservationNum;
		String guestId = null;
		String roomId = null;
		String status;
		Date checkInDate = new Date();
		Date checkOutDate = null;
		int numOfAdults;
		int numOfChildren;
		ArrayList<Room> availRooms = null;

		// To be used for data validation
		String digit = "\\d+";

		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("Enter Check-out Date (dd/mm/yyyy)");
		boolean done = false;
		while (!done) {
			try {
				String date = sc.next();
				sc.nextLine();
				checkOutDate = sdf.parse(date);
				done = true;
			} catch (ParseException e) {
				System.out.println("invalid format! Please use the correct format. E.g (20/01/2020)");
			}
		}

		// select room type
		ArrayList<String> vacantList = new ArrayList<String>();
		vacantList = RoomController.retrieveAllAvailableRoomTypes();
		String choice, roomType = null;
		do {
			System.out.println("Please enter Room Type:");
			System.out.println("(1) Single - " + vacantList.get(0) + " rooms left.");
			System.out.println("(2) Double - " + vacantList.get(1) + " rooms left.");
			System.out.println("(3) Deluxe - " + vacantList.get(2) + " rooms left.");
			System.out.println("(4) VIP Suite - " + vacantList.get(3) + " rooms left.");
			choice = sc.nextLine();

			if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (choice) {
				case "1":
					roomType = "Single Room";
					break;
				case "2":
					roomType = "Double Room";
					break;
				case "3":
					roomType = "Deluxe Room";
					break;
				case "4":
					roomType = "VIP Suite";
					break;
				}
			}
		} while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4"));

		// select bed Type
		ArrayList<String> bedList = new ArrayList<String>();
		bedList = RoomController.retrieveBedTypes(choice);
		String bedType = null;
		do {
			System.out.println("Please enter Bed Type:");
			System.out.println("(1) Single Bed - " + bedList.get(0) + " rooms available");
			System.out.println("(2) Double Bed - " + bedList.get(1) + " rooms available");
			System.out.println("(3) Super Single Bed - " + bedList.get(2) + " rooms available");
			System.out.println("(4) Queen Bed - " + bedList.get(3) + " rooms available");
			System.out.println("(5) King Bed - " + bedList.get(4) + " rooms available");
			choice = sc.nextLine();

			if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")
					&& !choice.equals("5")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (choice) {
				case "1":
					bedType = "Single Bed";
					break;
				case "2":
					bedType = "Double Bed";
					break;
				case "3":
					bedType = "Super Single Bed";
					break;
				case "4":
					bedType = "Queen Bed";
					break;
				case "5":
					bedType = "King Bed";
					break;
				}
			}
		} while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5"));

		ArrayList<Room> rooms = RoomController.retrieveRoomIdByTypes(roomType, bedType);
		try {
			availRooms = ReservationController.retrieveAvailRoom(rooms, sdf.format(checkInDate), sdf.format(checkOutDate));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("\n==================================================");
		System.out.println(" Room Details ");
		System.out.println("==================================================");
		System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", "roomId", "viewType", "roomFloor", "roomNumber",
				"wifiEnabled", "smokingStatus");
		System.out.println("");

		for (int i = 0; i < availRooms.size(); i++) {
			System.out.printf("%-8s %-11s %-11s %-12s %-13s %-10s", rooms.get(i).getRoomId(),
					rooms.get(i).getViewType(), rooms.get(i).getRoomFloor(), rooms.get(i).getRoomNumber(),
					rooms.get(i).getWifiEnabled(), rooms.get(i).getSmokingStatus());
			System.out.println("");
		}

		System.out.println("Enter room Id: ");
		roomId = sc.next();

		// check guest id
		String rc;
		do {
			System.out.println("Are you a returning guest? (y/n)");
			rc = sc.next();
			if (rc.equalsIgnoreCase("y")) {
				System.out.println("Enter Guest Id: ");
				guestId = sc.next();
				// find guest Id
				Guest g = GuestController.retrieveGuestWithString(guestId);
				if (g == null) {
					do {
						System.out.println("Invalid Guest Id");
						System.out.println("1. try again");
						System.out.println("2. Create new guest entry");
						int option = sc.nextInt();
						sc.nextLine();
						switch (option) {
						case 1:
							System.out.println("Enter Guest Id: ");
							guestId = sc.next();
							g = GuestController.retrieveGuestWithString(guestId);
							break;

						case 2:
							GuestController.createGuest();
							g = GuestController.retrieveGuestWithString(guestId);
							break;
						}
					} while (g == null);
				}
			} else if (rc.equalsIgnoreCase("n")) {
				GuestController.createGuest();
			}
		} while (!rc.equalsIgnoreCase("y") && !rc.equalsIgnoreCase("n"));

		// enter number of adults and children
		do {
			System.out.println("Enter total Number of Adults: ");
			while (!sc.hasNextInt()) {
		        System.out.println("Please enter numbers only.");
		        sc.next(); // this is important!
		    }
			numOfAdults = sc.nextInt();
			if(numOfAdults < 0) {
				System.out.println("Please enter a valid number.");
			}
			
		}while(numOfAdults <= -1);

		do {
			System.out.println("Enter total Number of Children: ");
			while (!sc.hasNextInt()) {
		        System.out.println("Please enter numbers only.");
		        sc.next(); // this is important!
		    }
			numOfChildren = sc.nextInt();
			if(numOfChildren < 0) {
				System.out.println("Please enter a valid number.");
			}
		}while(numOfChildren <= -1);

		// print out details
		System.out.println("");
		System.out.println("Check-in Date: " + sdf.format(checkInDate));
		System.out.println("Check-out Date: " + sdf.format(checkOutDate));
		System.out.println("--------------------------------------------");
		RoomController.retrieveOneRoom(roomId);
		System.out.println("Guest Id: " + guestId);
		System.out.println("Number of adults: " + numOfAdults);
		System.out.println("Number of children: " + numOfChildren);
		System.out.println("Confirm Detail? (y/n)");
		String confirmation = sc.next();
		if (confirmation.equalsIgnoreCase("y")) {
			// Check if file exist
			// fileChecker.checkFileExist(fileName);
			String type = RoomController.retrieveRoomType(roomId);
			if (type.equals("Deluxe ")) {
				type = "X";
			}
			String reservationDate = sdf.format(checkInDate).replaceAll("\\D", "");
			String reservationRoomId = roomId.replaceAll("\\D", "");

			reservationNum = type.charAt(0) + "-" + reservationDate + "-" + reservationRoomId;
			status = "CHECKED-IN";
			Reservation r = new Reservation(reservationNum, guestId, roomId, status, checkInDate, checkOutDate,
					numOfAdults, numOfChildren);
			ReservationDB reservationDB = new ReservationDB();
			ArrayList al = reservationDB.read(fileName);
			al.add(r);

			try {
				// Write Room records to file
				reservationDB.save(fileName, al);
				boolean success = RoomController.updateRoomStatus(r.getRoomId(), "OCCUPIED");
				if (success) {
					System.out.println("Check-In Successful!");
				}
				System.out.println("Walk-In Sucessful!");

			} catch (IOException e) {
				System.out.println("IOException > " + e.getMessage());
			}

		} else {
			System.out.println("Walk-In Registration Unsucessful");
		}
	}

	/**
	 * Checking out of reservation by room id
	 * @throws IOException
	 * @param checkOutRoomId
	 * 				Specifies the room id to check out
	 */
	public static void checkOutGuest(String checkOutRoomId) throws IOException {

		Reservation r = new Reservation();
		r = retrieveReservationByRoomId(checkOutRoomId);

		if (!RoomController.retrieveRoomStatus(checkOutRoomId).equalsIgnoreCase("VACANT")) {
			if (r != null) {
				r.setStatus("CHECKED-OUT");
				try {
					ArrayList alr = retrieveReservation();
					for (int i = 0; i < alr.size(); i++) {
						Reservation searchReservation = (Reservation) alr.get(i);

						if (r.getReservationNum().equals(searchReservation.getReservationNum())) {
							alr.set(i, r);
						}
					}
					// Write Reservation records to file
					ReservationDB reservationDB = new ReservationDB();
					reservationDB.save(fileName, alr);

					boolean success = RoomController.updateRoomStatus(r.getRoomId(), "VACANT");
					if (success) {
						System.out.println("Check-out Successful!");
					}
				} catch (

				IOException e)

				{
					System.out.println("IOException > " + e.getMessage());
				}
			} else {
				RoomController.updateRoomStatus(checkOutRoomId, "VACANT");
				System.out.println("Room Status have been updated!");
			}
		}
	}

	/**
	 * Updating reservation to cancel by reservation id
	 * @param id
	 * 				Specifies the reservation id
	 */
	public static void updateReservationToCancelled(String id) {
		// Guest
		Reservation r = new Reservation();
		// Reservation
		try {
			r = retrieveReservationById(id, "R", "ALL");
		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}

		if (r != null) {
			r.setStatus("CANCELLED");
			try {
				ArrayList alr = retrieveReservation();
				for (int i = 0; i < alr.size(); i++) {
					Reservation searchReservation = (Reservation) alr.get(i);

					if (r.getReservationNum().equals(searchReservation.getReservationNum())) {
						if (searchReservation.getStatus().equalsIgnoreCase("CHECKED-OUT")) {
							break;

						} else {
							alr.set(i, r);
						}

					}
				}
				// Write Reservation records to file
				ReservationDB reservationDB = new ReservationDB();
				reservationDB.save(fileName, alr);

				boolean success = RoomController.updateRoomStatus(r.getRoomId(), "VACANT");
				if (success) {
					System.out.println("Reservation Cancelled");
				}
			} catch (

			IOException e)

			{
				System.out.println("IOException > " + e.getMessage());
			}
		} else {
			System.out.println("Unable to find reservation ");
		}
	}

	/**
	 * Format printing reservations by reservation id
	 * @throws IOException
	 * @throws ParseException
	 * @param id
	 * 				Specifies the reservation id
	 */
	public static void printReservation(String id) throws IOException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Reservation r = new Reservation();

		try {

			r = retrieveReservationById(id, "R", "ALL");
		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}

		if (r != null) {
			Guest g = GuestController.retrieveGuestWithString(r.getGuestId());
			System.out.println("\n==================================================");
			System.out.println(" Reservation Details ");
			System.out.println("==================================================");
			System.out.println("Reservation No: " + r.getReservationNum());
			System.out.println("Guest Id: " + r.getGuestId());
			System.out.println("Guest Name: " + g.getName());
			System.out.println("Reservation Status: " + r.getStatus());
			System.out.println("Check-in Date:" + sdf.format(r.getCheckInDate()));
			System.out.println("Check-out Date:" + sdf.format(r.getCheckOutDate()));
			System.out.println("==================================================");
			RoomController.retrieveOneRoom(r.getRoomId());
		} else {
			System.out.println("Unable to find reservation!");
		}

	}

	/**
	 * Retrieval of available rooms by check in and check out dates
	 * @throws ParseException
	 * @param r
	 * 				Specifies an arraylist of room
	 * @param ciDate
	 * 				Specifies the check in date
	 * @param coDate
	 * 				Specifies the check out date
	 * @return An ArrayList of Room
	 */
	public static ArrayList<Room> retrieveAvailRoom(ArrayList<Room> r, String ciDate, String coDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<Reservation> rList = retrieveReservation();
		Date new_cid = sdf.parse(ciDate);
		Date new_cod = sdf.parse(coDate);
		Date cid, cod;
		String roomId;
		for (int i = 0; i < rList.size(); i++) {
			if (rList.get(i).getStatus().equalsIgnoreCase("CONFIRMED")
					|| rList.get(i).getStatus().equalsIgnoreCase("CHECKED-IN")) {
				// check by date
				cid = rList.get(i).getCheckInDate();
				cod = rList.get(i).getCheckOutDate();

				if ((cid.compareTo(new_cid) * new_cid.compareTo(cod) >= 0)
						|| (cid.compareTo(new_cod) * new_cod.compareTo(cod) >= 0)) {
					roomId = rList.get(i).getRoomId();
					for (int j = 0; j < r.size(); j++) {
						// if roomId can be found in room ArrayList r, add it to room ArrayList
						if (r.get(j).getRoomId().equalsIgnoreCase(roomId)) {
							r.remove(r.get(j));
						}
					}
				}

			}

		}
		return r;
	}

	/**
	 * Retrieval of available rooms by room type, check in and check out dates
	 * @throws ParseException
	 * @param r
	 * 				Specifies an arraylist of room
	 * @param type
	 * 				Specifies the room type
	 * @param ciDate
	 * 				Specifies the check in date
	 * @param coDate
	 * 				Specifies the check out date
	 * @return An ArrayList of integer for available room numbers
	 */
	public static ArrayList<Integer> retrieveAvailRoomNum(ArrayList<Room> r, String type, String ciDate, String coDate)
			throws ParseException {
		ArrayList<Integer> num = new ArrayList<Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date new_cid = sdf.parse(ciDate);
		Date new_cod = sdf.parse(coDate);
		Date cid, cod;
		String roomId;
		ArrayList<Reservation> rList = retrieveReservation();

		for (int i = 0; i < rList.size(); i++) {
			if (rList.get(i).getStatus().equalsIgnoreCase("CONFIRMED")
					|| rList.get(i).getStatus().equalsIgnoreCase("CHECKED-IN")) {
				// check by date
				cid = rList.get(i).getCheckInDate();
				cod = rList.get(i).getCheckOutDate();

				if ((cid.compareTo(new_cid) * new_cid.compareTo(cod) >= 0)
						|| (cid.compareTo(new_cod) * new_cod.compareTo(cod) >= 0)) {
					roomId = rList.get(i).getRoomId();
					for (int j = 0; j < r.size(); j++) {
						// if roomId can be found in room ArrayList r, add it to room ArrayList
						if (r.get(j).getRoomId().equalsIgnoreCase(roomId)) {
							r.remove(r.get(j));
						}
					}
				}
			}

		}

		if (type.equals("room")) {
			int singleTotal = 0;
			int doubleTotal = 0;
			int deluxeTotal = 0;
			int vipTotal = 0;

			for (int i = 0; i < r.size(); i++) {
				if (r.get(i).getRoomType().equals("Single Room")) {
					singleTotal += 1;
				}
				if (r.get(i).getRoomType().equals("Double Room")) {
					doubleTotal += 1;
				}
				if (r.get(i).getRoomType().equals("Deluxe Room")) {
					deluxeTotal += 1;
				}
				if (r.get(i).getRoomType().equals("VIP Suite")) {
					vipTotal += 1;
				}
			}

			num.add(singleTotal);
			num.add(doubleTotal);
			num.add(deluxeTotal);
			num.add(vipTotal);
		} else if (type.equals("bed")) {
			int singleBed = 0;
			int doubleBed = 0;
			int superSingleBed = 0;
			int queenBed = 0;
			int kingBed = 0;

			for (int i = 0; i < r.size(); i++) {
				if (r.get(i).getBedType().contentEquals("Single Bed")) {
					singleBed += 1;
				}
				if (r.get(i).getBedType().contentEquals("Double Bed")) {
					doubleBed += 1;
				}
				if (r.get(i).getBedType().contentEquals("Super Single Bed")) {
					superSingleBed += 1;
				}
				if (r.get(i).getBedType().contentEquals("Queen Bed")) {
					queenBed += 1;
				}
				if (r.get(i).getBedType().contentEquals("King Bed")) {
					kingBed += 1;
				}
			}

			num.add(singleBed);
			num.add(doubleBed);
			num.add(superSingleBed);
			num.add(queenBed);
			num.add(kingBed);
		}

		return num;
	}

}
