package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Database.ReadinFile;
import Database.RoomDB;
import Entity.Room;


public class RoomController {
	
	public static final String SEPARATOR = "|";
	private static String fileName = "Room.txt";
	
	/**
	 * Creation of new Room 
	 * @throws IOException 
	 */
	public static void createRoom() throws IOException {
		String roomId = "";
		String roomType = "";
		String bedType = "";
		String viewType = "";
		String roomStatus = "";
		String roomRate = "";
		String roomFloor;
		String roomNumber;
		String wifiEnabled = "";
		String smokingStatus = "";
		
		String regExp = "[0-9]+([.][0-9]{2})";
		String roomRegExp = "[0][2-7][-][0][1-8]";
		String digit = "\\d+";
		String alpha = "[a-zA-Z.*\\s+.]+";
		String roomIdDigit = "\\d+\\-";
		
		Pattern pattern = Pattern.compile(regExp);
		Pattern roomIdPattern = Pattern.compile(roomRegExp);
		
		Room room = new Room();
		Room checkRoomId = new Room();
		Scanner sc = new Scanner(System.in);
		
		// Setting Room Id
		do {
			System.out.println("Please enter Room ID(E.g 02-04):");
			System.out.println("*Format xx-yy where xx is Floor Number and yy is Room Number.");
			System.out.println("*Floor number from 02 - 07");
			System.out.println("*Room number from 01 - 08");
			
			roomId = sc.nextLine();
			Matcher matcher = roomIdPattern.matcher(roomId); 
			if(roomId.length() != 5 || !matcher.matches()) {
				roomId = "";
				System.out.println("You have entered a invalid Room Id. Please try again. (E.g. 02-04)");
			}else {
				room.setRoomId(roomId);
				String[] parts = roomId.split("-");
				roomFloor = parts[0];
				roomNumber = parts[1];
				room.setRoomFloor(roomFloor);
				room.setRoomNumber(roomNumber);
				checkRoomId = retrieveRoom(room);
				if(checkRoomId != null) {
					roomId = "";
					System.out.println("The Room Id you have entered exist. Please enter another Room Id.");
				}
			}
		} while (roomId.equals(""));
		
		
		// Setting Room Type
		do {
			System.out.println("Please enter Room Type: ");
			System.out.println("(1) Single");
			System.out.println("(2) Double");
			System.out.println("(3) Deluxe");
			System.out.println("(4) VIP Suite");
			roomType = sc.nextLine();
			if (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (roomType) {
					case "1":
						room.setRoomType("Single Room");
						break;
					case "2":
						room.setRoomType("Double Room");
						break;
					case "3":
						room.setRoomType("Deluxe Room");
						break;
					case "4":
						room.setRoomType("VIP Suite");
						break;
				}
				System.out.println("HELLO");
			}
		} while (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4"));
		
		// Setting Bed Type
		do {
			System.out.println("Please enter Bed Type: ");
			System.out.println("(1) Single Bed");
			System.out.println("(2) Double Bed");
			System.out.println("(3) Super Single Bed");
			System.out.println("(4) Queen Bed");
			System.out.println("(5) King Bed");
			bedType = sc.nextLine();

			if (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4")&& !bedType.equals("5")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (bedType) {
					case "1":
						room.setBedType("Single Bed");
						break;
					case "2":
						room.setBedType("Double Bed");
						break;
					case "3":
						room.setBedType("Super Single Bed");
						break;
					case "4":
						room.setBedType("Queen Bed");
						break;
					case "5":
						room.setBedType("King Bed");
						break;
				}
			}
		} while (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4") && !bedType.equals("5"));
		
		
		// Setting View Type
		do {
			System.out.println("Please enter View Type: ");
			System.out.println("(1) Sea View");
			System.out.println("(2) City View");
			System.out.println("(3) No View");
			viewType = sc.nextLine();

			if (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (viewType) {
					case "1":
						room.setViewType("Sea View");
						break;
					case "2":
						room.setViewType("City View");
						break;
					case "3":
						room.setViewType("No View");
						break;
				}
			}
		} while (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3"));
		
		
		// Setting Room Status
		do {
			System.out.println("Please enter Room Status: ");
			System.out.println("(1) Vacant");
			System.out.println("(2) Reserved");
			System.out.println("(3) Occupied");
			System.out.println("(4) Under Maintenance");
			roomStatus = sc.nextLine();

			if (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3")&& !roomStatus.equals("4")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (roomStatus) {
					case "1":
						room.setRoomStatus("VACANT");
						break;
					case "2":
						room.setRoomStatus("RESERVED");
						break;
					case "3":
						room.setRoomStatus("OCCUPIED");
						break;
					case "4":
						room.setRoomStatus("UNDER MAINTENANCE");
						break;
				}
			}
		} while (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3") && !roomStatus.equals("4"));
		
		
		// Setting Room Rate
		do {
			System.out.println("Please enter Room Rate(E.g. 154.40):");
			System.out.println("*Enter amount in 2 decimal places.");
			roomRate = sc.nextLine();
			
			// Regex check if input is in price format.
			Matcher matcher = pattern.matcher(roomRate); 
			if(matcher.matches()) {
				Float checkPrice = Float.parseFloat(roomRate);
				if(checkPrice>0.0) {
					room.setRoomRate(roomRate);
				}else {
					roomRate = "";
					System.out.println("Please enter a positive room rate.");
				}
				
			}else {
				roomRate = "";
				System.out.println("Please enter a valid rate in 2 decimal places.");
			}
		} while (roomRate.equals(""));
		
		
		// Setting Wifi Enabled
		do {
			System.out.println("Please select if Wifi is enabled in the room:");
			System.out.println("(1) Enabled");
			System.out.println("(2) Not enabled");
			wifiEnabled = sc.nextLine();

			if (!wifiEnabled.equals("1") && !wifiEnabled.equals("2")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (wifiEnabled) {
					case "1":
						room.setWifiEnabled("Enabled");
						break;
					case "2":
						room.setWifiEnabled("Not enabled");
						break;
				}
			}
		} while (!wifiEnabled.equals("1") && !wifiEnabled.equals("2"));
		
		
		// Setting Smoking Status
		do {
			System.out.println("Please select if Smoking is allowed in the room:");
			System.out.println("(1) Allowed");
			System.out.println("(2) Not allowed");
			smokingStatus = sc.nextLine();

			if (!smokingStatus.equals("1") && !smokingStatus.equals("2")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (smokingStatus) {
					case "1":
						room.setSmokingStatus("Allowed");
						break;
					case "2":
						room.setSmokingStatus("Not allowed");
						break;
				}
			}
		} while (!smokingStatus.equals("1") && !smokingStatus.equals("2"));
		
		System.out.println(room.getRoomRate());

		RoomDB roomDB = new RoomDB();
		ArrayList al = roomDB.read(fileName);
		al.add(room);
		
		try {
			// Write Room records to file
			roomDB.save(fileName, al);

			System.out.println("You have successfully created a new room! ");

		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		
	}
	
	/**
	 * Update Room Details by using roomId
	 * @throws IOException 
	 * 
	 */
	public static void updateRoom() throws IOException {
		Scanner sc = new Scanner(System.in);
		String roomType;
		String bedType;
		String viewType;
		String roomStatus;
		String roomRate;
		String wifiEnabled;
		String smokingStatus;
		int option = 0;
		// To be used for data validation
		
		String regExp = "[0-9]+([.][0-9]{2})";
		String roomRegExp = "[0][2-7][-][0][1-8]";
		String digit = "\\d+";
		String alpha = "[a-zA-Z.*\\s+.]+";
		
		Pattern pattern = Pattern.compile(regExp);
		Pattern roomIdPattern = Pattern.compile(roomRegExp);
		
		retrieveAllRoom();
		System.out.println("");
		Room updateRoom = new Room();
		updateRoom = retrieveRoomDetails();
		System.out.println("\nPlease choose guest details to update \n(1) Room Type \n(2) Bed Type "
				+ "\n(3) View Type \n(4) Room Status \n(5) Room Rate \n(6) Wifi Enabled \n(7) Room Smoking Status \n(8) All details");
		option = sc.nextInt();
		sc.nextLine();
		
		switch(option) {
			case 1:
				// Room Type
				do {
					System.out.println("Please enter a new Room Type:");
					System.out.println("(1) Single");
					System.out.println("(2) Double");
					System.out.println("(3) Deluxe");
					System.out.println("(4) VIP Suite");
					roomType = sc.nextLine();
	
					if (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (roomType) {
							case "1":
								updateRoom.setRoomType("Single Room");
								break;
							case "2":
								updateRoom.setRoomType("Double Room");
								break;
							case "3":
								updateRoom.setRoomType("Deluxe Room");
								break;
							case "4":
								updateRoom.setRoomType("VIP Suite");
								break;
						}
					}
				} while (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4"));
				break;
			case 2:
				// Bed Type
				do {
					System.out.println("Please enter a Bed Type: ");
					System.out.println("(1) Single Bed");
					System.out.println("(2) Double Bed");
					System.out.println("(3) Super Single Bed");
					System.out.println("(4) Queen Bed");
					System.out.println("(5) King Bed");
					bedType = sc.nextLine();
	
					if (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4")&& !bedType.equals("5")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (bedType) {
							case "1":
								updateRoom.setBedType("Single Bed");
								break;
							case "2":
								updateRoom.setBedType("Double Bed");
								break;
							case "3":
								updateRoom.setBedType("Super Single Bed");
								break;
							case "4":
								updateRoom.setBedType("Queen Bed");
								break;
							case "5":
								updateRoom.setBedType("King Bed");
								break;
						}
					}
				} while (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4") && !bedType.equals("5"));
				break;
			case 3:
				// View Type
				do {
					System.out.println("Please enter View Type: ");
					System.out.println("(1) Sea View");
					System.out.println("(2) City View");
					System.out.println("(3) No View");
					viewType = sc.nextLine();
	
					if (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (viewType) {
							case "1":
								updateRoom.setViewType("Sea View");
								break;
							case "2":
								updateRoom.setViewType("City View");
								break;
							case "3":
								updateRoom.setViewType("No View");
								break;
						}
					}
				} while (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3"));
				break;
			case 4:
				// Room Status
				do {
					System.out.println("Please enter Room Status: ");
					System.out.println("(1) Vacant");
					System.out.println("(2) Reserved");
					System.out.println("(3) Occupied");
					System.out.println("(4) Under Maintenance");
					roomStatus = sc.nextLine();
	
					if (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3")&& !roomStatus.equals("4")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (roomStatus) {
							case "1":
								updateRoom.setRoomStatus("VACANT");
								break;
							case "2":
								updateRoom.setRoomStatus("RESERVED");
								break;
							case "3":
								updateRoom.setRoomStatus("OCCUPIED");
								break;
							case "4":
								updateRoom.setRoomStatus("UNDER MAINTENANCE");
								break;
						}
					}
				} while (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3") && !roomStatus.equals("4"));
				break;
			case 5:
				// Room Rate
				do {
					System.out.println("Please enter Room Rate(E.g. 154.40):");
					System.out.println("*Enter amount in 2 decimal places.");
					roomRate = sc.nextLine();
					
					// Regex check if input is in price format.
					Matcher matcher = pattern.matcher(roomRate); 
					if(matcher.matches()) {
						Float checkPrice = Float.parseFloat(roomRate);
						if(checkPrice>0.0) {
							updateRoom.setRoomRate(roomRate);
						}else {
							System.out.println("Please enter a positive room rate.");
						}
					}else {
						roomRate = "";
						System.out.println("Please enter a valid rate in 2 decimal places.");
					}
				} while (roomRate.equals(""));
				break;
			case 6:
				// Wifi Enabled
				do {
					System.out.println("Please select if Wifi is enabled in the room: ");
					System.out.println("(1) Enabled");
					System.out.println("(2) Not enabled");
					wifiEnabled = sc.nextLine();
	
					if (!wifiEnabled.equals("1") && !wifiEnabled.equals("2")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (wifiEnabled) {
							case "1":
								updateRoom.setWifiEnabled("Enabled");
								break;
							case "2":
								updateRoom.setWifiEnabled("Not enabled");
								break;
						}
					}
				} while (!wifiEnabled.equals("1") && !wifiEnabled.equals("2"));
				break;
			case 7:
				// Smoking Status
				do {
					System.out.println("Please select if Smoking is allowed in the room: ");
					System.out.println("(1) Allowed");
					System.out.println("(2) Not allowed");
					smokingStatus = sc.nextLine();
	
					if (!smokingStatus.equals("1") && !smokingStatus.equals("2")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (smokingStatus) {
							case "1":
								updateRoom.setSmokingStatus("Allowed");
								break;
							case "2":
								updateRoom.setSmokingStatus("Not allowed");
								break;
						}
					}
				} while (!smokingStatus.equals("1") && !smokingStatus.equals("2"));
				break;
			case 8:
				// Room Type
				do {
					System.out.println("Please enter a new Room Type: ");
					System.out.println("(1) Single");
					System.out.println("(2) Double");
					System.out.println("(3) Deluxe");
					System.out.println("(4) VIP Suite");
					roomType = sc.nextLine();
	
					if (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (roomType) {
							case "1":
								updateRoom.setRoomType("Single Room");
								break;
							case "2":
								updateRoom.setRoomType("Double Room");
								break;
							case "3":
								updateRoom.setRoomType("Deluxe Room");
								break;
							case "4":
								updateRoom.setRoomType("VIP Suite");
								break;
						}
					}
				} while (!roomType.equals("1") && !roomType.equals("2") && !roomType.equals("3") && !roomType.equals("4"));
				
				// Bed Type
				do {
					System.out.println("Please enter a Bed Type: ");
					System.out.println("(1) Single Bed");
					System.out.println("(2) Double Bed");
					System.out.println("(3) Super Single Bed");
					System.out.println("(4) Queen Bed");
					System.out.println("(5) King Bed");
					bedType = sc.nextLine();
	
					if (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4")&& !bedType.equals("5")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (bedType) {
							case "1":
								updateRoom.setBedType("Single Bed");
								break;
							case "2":
								updateRoom.setBedType("Double Bed");
								break;
							case "3":
								updateRoom.setBedType("Super Single Bed");
								break;
							case "4":
								updateRoom.setBedType("Queen Bed");
								break;
							case "5":
								updateRoom.setBedType("King Bed");
								break;
						}
					}
				} while (!bedType.equals("1") && !bedType.equals("2") && !bedType.equals("3") && !bedType.equals("4")&& !bedType.equals("5"));
				
				// View Type
				do {
					System.out.println("Please enter View Type: ");
					System.out.println("(1) Sea View");
					System.out.println("(2) City View");
					System.out.println("(3) No View");
					viewType = sc.nextLine();
	
					if (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (viewType) {
							case "1":
								updateRoom.setViewType("Sea View");
								break;
							case "2":
								updateRoom.setViewType("City View");
								break;
							case "3":
								updateRoom.setViewType("No View");
								break;
						}
					}
				} while (!viewType.equals("1") && !viewType.equals("2") && !viewType.equals("3"));
				
				
				// Room Status
				do {
					System.out.println("Please enter Room Status: ");
					System.out.println("(1) Vacant");
					System.out.println("(2) Reserved");
					System.out.println("(3) Occupied");
					System.out.println("(4) Under Maintenance");
					roomStatus = sc.nextLine();
	
					if (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3")&& !roomStatus.equals("4")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (roomStatus) {
							case "1":
								updateRoom.setRoomStatus("VACANT");
								break;
							case "2":
								updateRoom.setRoomStatus("RESERVED");
								break;
							case "3":
								updateRoom.setRoomStatus("OCCUPIED");
								break;
							case "4":
								updateRoom.setRoomStatus("UNDER MAINTENANCE");
								break;
						}
					}
				} while (!roomStatus.equals("1") && !roomStatus.equals("2") && !roomStatus.equals("3")&& !roomStatus.equals("4"));
				
				// Room Rate
				do {
					System.out.println("Please enter Room Rate(E.g. 154.40):");
					System.out.println("*Enter amount in 2 decimal places.");
					roomRate = sc.nextLine();
					
					// Regex check if input is in price format.
					Matcher matcher = pattern.matcher(roomRate); 
					if(matcher.matches()) {
						Float checkPrice = Float.parseFloat(roomRate);
						if(checkPrice>0.0) {
							updateRoom.setRoomRate(roomRate);
						}else {
							roomRate = "";
							System.out.println("Please enter a positive room rate.");
						}
					}else {
						roomRate = "";
						System.out.println("Please enter a valid rate in 2 decimal places.");
					}
				} while (roomRate.equals(""));
				
				// Wifi Enabled
				do {
					System.out.println("Please select if Wifi is enabled in the room: ");
					System.out.println("(1) Enabled");
					System.out.println("(2) Not enabled");
					wifiEnabled = sc.nextLine();
	
					if (!wifiEnabled.equals("1") && !wifiEnabled.equals("2")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (wifiEnabled) {
							case "1":
								updateRoom.setWifiEnabled("Enabled");
								break;
							case "2":
								updateRoom.setWifiEnabled("Not enabled");
								break;
						}
					}
				} while (!wifiEnabled.equals("1") && !wifiEnabled.equals("2"));
				
				// Smoking Status
				do {
					System.out.println("Please select if Smoking is allowed in the room: ");
					System.out.println("(1) Allowed");
					System.out.println("(2) Not allowed");
					smokingStatus = sc.nextLine();
	
					if (!smokingStatus.equals("1") && !smokingStatus.equals("2")) {
						System.out.println("Please select a valid option.");
					} else {
						switch (smokingStatus) {
							case "1":
								updateRoom.setSmokingStatus("Allowed");
								break;
							case "2":
								updateRoom.setSmokingStatus("Not allowed");
								break;
						}
					}
				} while (!smokingStatus.equals("1") && !smokingStatus.equals("2"));
				
				break;
		}
		
		try{
			ArrayList alr = retrieveRoom();
			for (int i = 0; i < alr.size(); i++) {
				Room searchRoom = (Room) alr.get(i);

				if(updateRoom.getRoomId().equals(searchRoom.getRoomId())) {
					alr.set(i, updateRoom);
				}
			}
			// Write Room records to file
			RoomDB roomDB = new RoomDB();
			roomDB.save(fileName, alr);

			System.out.println("Room details has been successfully updated!");
		} catch (

		IOException e)

		{
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	/**
	 * Update Room Status only by using roomId
	 * @throws IOException 
	 * 
	 */
	public static void updateRoomStatusOnly() throws IOException {
		String newStatus;
		String roomId;
		RoomController.retrieveRoomStatus();
		Scanner sc = new Scanner(System.in);
		Room room = new Room();
		Room checkRoomId = new Room();
		Boolean checker = false;
		
		String digit = "\\d+";
		String alpha = "[a-zA-Z.*\\s+.]+";
		
		do {
			System.out.println("Please enter a Room Id for updating(E.g 02-01):");
			roomId = sc.nextLine();
			
			room.setRoomId(roomId);
			checkRoomId = retrieveRoom(room);
			if(checkRoomId == null) {
				System.out.println("Room Id does not exist.");
			}
			
		} while (checkRoomId != null && checker == true);
		
		do {
			System.out.println("Please enter Room Status: ");
			System.out.println("(1) Vacant");
			System.out.println("(2) Reserved");
			System.out.println("(3) Occupied");
			System.out.println("(4) Under Maintenance");
			newStatus = sc.nextLine();

			if (!newStatus.equals("1") && !newStatus.equals("2") && !newStatus.equals("3")&& !newStatus.equals("4")) {
				System.out.println("Please select a valid option.");
			} else {
				switch (newStatus) {
					case "1":
						checkRoomId.setRoomStatus("VACANT");
						break;
					case "2":
						checkRoomId.setRoomStatus("RESERVED");
						break;
					case "3":
						checkRoomId.setRoomStatus("OCCUPIED");
						break;
					case "4":
						checkRoomId.setRoomStatus("UNDER MAINTENANCE");
						break;
				}
			}
		} while (newStatus.equals("") || !newStatus.matches(digit));
		
		try{
			ArrayList alr = retrieveRoom();
			for (int i = 0; i < alr.size(); i++) {
				Room searchRoom = (Room) alr.get(i);
				if(checkRoomId.getRoomId().equals(searchRoom.getRoomId())) {
					alr.set(i, checkRoomId);
				}
			}
			// Write Room records to file
			RoomDB roomDB = new RoomDB();
			roomDB.save(fileName, alr);

			System.out.println("Room Status has been successfully updated!");
		} catch (

		IOException e)

		{
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	/**
	 * Update Room Status only by using roomId
	 * @throws IOException
	 * @param roomId
	 * 				Specifies the roomId
	 * @param status
	 * 				Specifies the status of the room
	 * @return Boolean value
	 */
	public static Boolean updateRoomStatus(String roomId, String status) {
		Room room = new Room();
		Room checkRoomId = new Room();
		Boolean checker = false;
		
		do {
			room.setRoomId(roomId);
			checkRoomId = retrieveRoom(room);
			if(checkRoomId == null) {
				System.out.println("Room ID does not exist.");
			}
			
		} while (checkRoomId != null && checker == true);
		
		
		try{
			ArrayList alr = retrieveRoom();
			for (int i = 0; i < alr.size(); i++) {
				Room searchRoom = (Room) alr.get(i);
				if(checkRoomId.getRoomId().equals(searchRoom.getRoomId())) {
						checkRoomId.setRoomStatus(status);
						alr.set(i, checkRoomId);
				}
			}
			// Write Room records to file
			RoomDB roomDB = new RoomDB();
			roomDB.save(fileName, alr);
			checker = true;
			System.out.println("Room Status has been successfully updated!");
		} catch (

		IOException e)

		{
			System.out.println("IOException > " + e.getMessage());
		}
		return checker;
	}
	
	/**
	 * Retrieval of all room details
	 * @throws IOException
	 *
	 */
	public static void retrieveAllRoom() throws IOException {

		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);

		System.out.println("\n==================================================");
		System.out.println(" Room Details ");
		System.out.println("==================================================");
		System.out.printf("%-8s %-13s %-18s %-11s %-19s %-15s %-12s %-13s %-13s %-10s", "RoomID", "Room Type", "Bed Type", "View Type", "Room Status", "Room Rate(S$)", "Room Floor","Room Number","Wifi Status","Smoking Status");
	    System.out.println();
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String roomId = star.nextToken().trim();
			String roomType = star.nextToken().trim();
			String bedType = star.nextToken().trim();
			String viewType = star.nextToken().trim();
			String roomStatus = star.nextToken().trim();
			String roomRate = star.nextToken().trim();
			String roomFloor = star.nextToken().trim();
			String roomNumber = star.nextToken().trim();
			String wifiEnabled = star.nextToken().trim();
			String smokingStatus = star.nextToken().trim();

			
			System.out.printf("%-8s %-13s %-18s %-11s %-19s %-15s %-12s %-13s %-13s %-10s", roomId, roomType, bedType, viewType, roomStatus, roomRate,roomFloor,roomNumber,wifiEnabled,smokingStatus);
			System.out.println("");
		}
	}
	
	/**
	 * Retrieval of specific room's details.
	 * @throws IOException
	 * @param room
	 *            Parameter to search for room details.
	 *            
	 * @return Room if found else return null.
	 */
	public static Room retrieveRoom(Room room) {
		ArrayList alr = retrieveRoom();
		for (int i = 0; i < alr.size(); i++) {
			Room searchRoom = (Room) alr.get(i);

			if (room.getRoomId().equals(searchRoom.getRoomId())) {
				room = searchRoom;
				return room;
			}
		}
		return null;
	}
	
	/**
	 * Retrieval of room details.
	 * 
	 * @return ArrayList of all rooms.
	 */
	public static ArrayList retrieveRoom() {
		ArrayList alr = null;
		try {
			// read file containing Room records
			RoomDB roomDB = new RoomDB();
			alr = roomDB.read(fileName);

		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		return alr;
	}

	/**
     * Retrieval of room's details by roomId.
     * 
     * @return room details.
     */
    public static Room retrieveRoomDetails() {
        String roomId;
        Scanner sc = new Scanner(System.in);
        ArrayList alr = retrieveRoom();
        Room room = null;
        do {
            System.out.println("Please enter Room Id (E.g 02-01): ");
            roomId = sc.nextLine();

            for (int i = 0; i < alr.size(); i++) {
                Room searchRoom = (Room) alr.get(i);

                if (searchRoom.getRoomId().equalsIgnoreCase(roomId)) {
                    room = searchRoom;
                }
            }

            if (room == null) {
                System.out.println("Room Id does not exist.");
            }

        } while (room == null);

        return room;
    }
	
	/**
	 * Retrieval of room details
	 * @throws IOException
	 */
	public static void retrieveOneRoom() throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		Scanner sc = new Scanner(System.in);
		String checkRoomId;
		System.out.print("Enter the Room Id: ");
		checkRoomId = sc.nextLine();
		
		System.out.println("\n==================================================");
		System.out.println(" Room Details ");
		System.out.println("==================================================");
		System.out.printf("%-8s %-13s %-18s %-11s %-19s %-10s %-11s %-12s %-13s %-10s", "roomId", "roomType", "bedType", "viewType", "roomStatus","roomRate","roomFloor","roomNumber","wifiEnabled","smokingStatus");
	    System.out.println();
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String roomId = star.nextToken().trim();
			String roomType = star.nextToken().trim();
			String bedType = star.nextToken().trim();
			String viewType = star.nextToken().trim();
			String roomStatus = star.nextToken().trim();
			String roomRate = star.nextToken().trim();
			String roomFloor = star.nextToken().trim();
			String roomNumber = star.nextToken().trim();
			String wifiEnabled = star.nextToken().trim();
			String smokingStatus = star.nextToken().trim();

			
			if(roomId.contains(checkRoomId)) {
				System.out.printf("%-8s %-13s %-18s %-11s %-19s %-10s %-11s %-12s %-13s %-10s", roomId, roomType, bedType, viewType, roomStatus, roomRate,roomFloor,roomNumber,wifiEnabled,smokingStatus);
				System.out.println("");
			}
		}
	}
	
	
	/**
	 * Retrieval of room type by using roomId
	 * @throws IOException
	 * @param id
	 *            Parameter to search for room details and retrieve room type
	 * @return type
	 */
	public static String retrieveRoomType(String id) throws IOException {
		String type = null;
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String roomId = star.nextToken().trim();
			String roomType = star.nextToken().trim();

			
			if(roomId.contains(id)) {
				type = roomType;
			}
		}
		return type;
	}
	
	/**
	 * Retrieval of room rate by using roomId
	 * @throws IOException
	 * @param id
	 *            Parameter to search for room details and retrieve room type
	 * @return rate
	 */	
	public static String retrieveRoomRate(String id) throws IOException {
		String rate = null;
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String roomId = star.nextToken().trim();
			String roomRate = star.nextToken().trim();

			
			if(roomId.contains(id)) {
				rate = roomRate;
			}
		}
		return rate;
	}
	
	/**
	 * Retrieval of room details by using roomId
	 * @throws IOException
	 * @param checkRoomId
	 *            Parameter to search for room details.
	 */
	public static void retrieveOneRoom(String checkRoomId) throws IOException {

		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		
		System.out.println("\n==================================================");
		System.out.println(" Room Details ");
		System.out.println("==================================================");
		System.out.printf("%-8s %-13s %-18s %-11s %-19s %-10s %-11s %-12s %-13s %-10s", "roomId", "roomType", "bedType", "viewType", "roomStatus","roomRate","roomFloor","roomNumber","wifiEnabled","smokingStatus");
	    System.out.println();
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());

			
			if(room.getRoomId().contains(checkRoomId)) {
				System.out.printf("%-8s %-13s %-18s %-11s %-19s %-10s %-11s %-12s %-13s %-10s", room.getRoomId(), room.getRoomType(), room.getBedType(), room.getViewType(), room.getRoomStatus(), room.getRoomRate(), room.getRoomFloor(), room.getRoomNumber(), room.getWifiEnabled(), room.getSmokingStatus());
				System.out.println("");
			}
		}
	}
	
	/**
	 * Retrieval of all room's status
	 * @throws IOException
	 * 
	 */
	public static void retrieveRoomStatus() throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		String vacantString = "";
		String occupiedString = "";
		String reservedString = "";
		String maintenanceString = "";
		int countVacant = 0;
		int countOccupied = 0;
		int countReserved = 0;
		int countMaintenance = 0;
		System.out.println("\n==================================================");
		System.out.println(" All Room Status ");
		System.out.println("==================================================");
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String roomId = star.nextToken().trim();
			String roomType = star.nextToken().trim();
			String bedType = star.nextToken().trim();
			String viewType = star.nextToken().trim();
			String roomStatus = star.nextToken().trim();
			String roomRate = star.nextToken().trim();
			String roomFloor = star.nextToken().trim();
			String roomNumber = star.nextToken().trim();
			String wifiEnabled = star.nextToken().trim();
			String smokingStatus = star.nextToken().trim();

			if(roomStatus.contentEquals("VACANT")) {
				if(vacantString.equals("")) {
					vacantString = roomFloor + "-" + roomNumber;
				}else {
					vacantString = vacantString + ", " + roomFloor + "-" + roomNumber;
				}
				countVacant += 1;
			}else if(roomStatus.contentEquals("OCCUPIED")) {
				if(occupiedString.equals("")) {
					occupiedString = roomFloor + "-" + roomNumber;
				}else {
					occupiedString = occupiedString + ", " + roomFloor + "-" + roomNumber;
				}
				countOccupied += 1;
			}else if(roomStatus.contentEquals("RESERVED")) {
				if(reservedString.equals("")) {
					reservedString = roomFloor + "-" + roomNumber;
				}else {
					reservedString = reservedString + ", " + roomFloor + "-" + roomNumber;
				}
				countReserved += 1;
			}else if(roomStatus.contentEquals("UNDER MAINTENANCE")) {
				if(maintenanceString.equals("")) {
					maintenanceString = roomFloor + "-" + roomNumber;
				}else {
					maintenanceString = maintenanceString + ", " + roomFloor + "-" + roomNumber;
				}
				countMaintenance += 1;
			}
		}
		
		System.out.println("\nVacant(" + countVacant + "): ");
		System.out.println("\t\tRooms: " + vacantString);
		System.out.println("\nOccupied(" + countOccupied + "): ");
		System.out.println("\t\tRooms: " + occupiedString);
		System.out.println("\nReserved(" + countReserved + "): ");
		System.out.println("\t\tRooms: " + reservedString);
		System.out.println("\nUnder Maintenance(" + countMaintenance + "): ");
		System.out.println("\t\tRooms: " + maintenanceString);
		System.out.println("");
	}
	
	/**
	 * Retrieval of all room and calculate occupancy rate
	 * @throws IOException
	 * 
	 */
	public static void retrieveOccupancyRate() throws IOException{
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<Room> roomList = new ArrayList<Room>();
		
		String vacantString = "";
		int vacantSingle = 0;
		int vacantDouble = 0;
		int vacantDeluxe = 0;
		int vacantVIP = 0;
		int singleTotal = 0;
		int doubleTotal = 0;
		int deluxeTotal = 0;
		int vipTotal = 0;
		System.out.println("\n==================================================");
		System.out.println(" Occupancy Rate ");
		System.out.println("==================================================");
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","
			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());

			
			if(room.getRoomType().equals("Single Room")) {
				singleTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					roomList.add(room);
					vacantSingle += 1;
				}
			}
			if(room.getRoomType().equals("Double Room")) {
				doubleTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					roomList.add(room);
					vacantDouble += 1;
				}
			}
			if(room.getRoomType().equals("Deluxe Room")) {
				deluxeTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					roomList.add(room);
					vacantDeluxe += 1;
				}
			}
			if(room.getRoomType().equals("VIP Suite")) {
				vipTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					roomList.add(room);
					vacantVIP += 1;
				}
			}
		}

		System.out.println("\nSingle Room: " + vacantSingle + " out of " + singleTotal);
		formatPrintRooms(roomList, "Single Room");
		System.out.println("\nDouble Room: " + vacantDouble + " out of " + doubleTotal);
		formatPrintRooms(roomList, "Double Room");
		System.out.println("\nDeluxe Room: " + vacantDeluxe + " out of " + deluxeTotal);
		formatPrintRooms(roomList, "Deluxe Room");
		System.out.println("\nVIP Suite: " + vacantVIP + " out of " + vipTotal);
		formatPrintRooms(roomList, "VIP Suite");

	}
	
	/**
	 * Formatting of room id into readable format
	 * @param roomList
	 * 				An ArrayList of Room to search
	 * @param roomType
	 * 				String room type used to search for room details
	 */
	public static void formatPrintRooms(ArrayList<Room> roomList, String roomType) {
		System.out.print("\t\tRooms: ");
		ArrayList<String> roomTypeList = new ArrayList<String>();
		for(int i = 0; i < roomList.size(); i++) {
			if(roomList.get(i).getRoomType().equals(roomType)) {
				roomTypeList.add(roomList.get(i).getRoomId());
			}
		}
		String toPrint = String.join(", ", roomTypeList);
		System.out.println(toPrint);
	}

	/**
	 * Retrieve a list of room ids by room type and bed type
	 * @throws IOException
	 * 
	 * @param inputRoomType
	 * 					String input of room type used to search for room
	 * @param inputBedTyped
	 * 					String input of bed type used to search for room
	 * 
	 * @return An array of Room
	 * 					
	 */
	public static ArrayList<Room> retrieveRoomIdByTypes(String inputRoomType, String inputBedType) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<Room> roomIdList = new ArrayList<Room>();

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			
			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());
			
			if(room.getRoomType().equals(inputRoomType) && room.getBedType().equals(inputBedType)) {
				roomIdList.add(room);
			}
		}
		
		return roomIdList;
	}
	
	
	/**
	 * Retrieve a list of room ids by room type
	 * @throws IOException
	 * 
	 * @param inputRoomType
	 * 					String input of room type used to search for room
	 * 
	 * @return An array of Room
	 * 					
	 */
	public static ArrayList<Room> retrieveRoomIdByRoomType(String inputRoomType) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<Room> roomIdList = new ArrayList<Room>();

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			
			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());
			
			if(room.getRoomType().equals(inputRoomType)) {
				roomIdList.add(room);
			}
		}
		
		return roomIdList;
	}

	/**
	 * Retrieve room status by room id
	 * 
	 * @param retrieveRoomId
	 * 					String input of room id to retrieve room details
	 * 
	 * @return A String of room status
	 * 					
	 */
	public static String retrieveRoomStatus(String retrieveRoomId) {

		Room room = new Room();
		Room checkRoomId = new Room();
		
		room.setRoomId(retrieveRoomId);
		checkRoomId = retrieveRoom(room);
		if(checkRoomId == null) {
			System.out.println("Room Id does not exist.");
			return null;
		}
		
		return checkRoomId.getRoomStatus();
	}
	
	/**
	 * Retrieve and calculate the number of available room types
	 * @throws IOException
	 * 
	 * @return An ArrayList of string
	 */
	public static ArrayList<String> retrieveAllAvailableRoomTypes() throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<Room> roomList = new ArrayList<Room>();
		ArrayList<String> vacantList = new ArrayList<String>();
		
		String vacantString = "";
		int vacantSingle = 0;
		int vacantDouble = 0;
		int vacantDeluxe = 0;
		int vacantVIP = 0;
		int singleTotal = 0;
		int doubleTotal = 0;
		int deluxeTotal = 0;
		int vipTotal = 0;

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","
			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());

			
			if(room.getRoomType().equals("Single Room")) {
				singleTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					vacantSingle += 1;
				}
			}
			if(room.getRoomType().equals("Double Room")) {
				doubleTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					vacantDouble += 1;
				}
			}
			if(room.getRoomType().equals("Deluxe Room")) {
				deluxeTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					vacantDeluxe += 1;
				}
			}
			if(room.getRoomType().equals("VIP Suite")) {
				vipTotal += 1;
				if(room.getRoomStatus().contentEquals("VACANT")) {
					vacantVIP += 1;
				}
			}
		}
		vacantList.add(String.valueOf(vacantSingle));
		vacantList.add(String.valueOf(vacantDouble));
		vacantList.add(String.valueOf(vacantDeluxe));
		vacantList.add(String.valueOf(vacantVIP));
		return vacantList;
	}
	
	/**
	 * Retrieve and calculate the number of available bed types
	 * @throws IOException
	 * @param option
	 * 					String input of room type
	 * 
	 * @return An ArrayList of string
	 */
	public static ArrayList<String> retrieveBedTypes(String option) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<Room> roomList = new ArrayList<Room>();
		ArrayList<String> roomOption = new ArrayList<String>(Arrays.asList("Single Room", 
                "Double Room", 
                "Deluxe Room",
                "VIP Suite"));
		ArrayList<String> bedList = new ArrayList<String>();
		
		String vacantString = "";
		int singleBed = 0;
		int doubleBed = 0;
		int superSingleBed = 0;
		int queenBed = 0;
		int kingBed = 0;

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","
			Room room = new Room();
			room.setRoomId(star.nextToken().trim());
			room.setRoomType(star.nextToken().trim());
			room.setBedType(star.nextToken().trim());
			room.setViewType(star.nextToken().trim());
			room.setRoomStatus(star.nextToken().trim());
			room.setRoomRate(star.nextToken().trim());
			room.setRoomFloor(star.nextToken().trim());
			room.setRoomNumber(star.nextToken().trim());
			room.setWifiEnabled(star.nextToken().trim());
			room.setSmokingStatus(star.nextToken().trim());

			
			if(room.getRoomType().equals(roomOption.get(Integer.parseInt(option)-1))) {
				if(room.getBedType().contentEquals("Single Bed")) {
					singleBed += 1;
				}
				if(room.getBedType().contentEquals("Double Bed")) {
					doubleBed += 1;
				}
				if(room.getBedType().contentEquals("Super Single Bed")) {
					superSingleBed += 1;
				}
				if(room.getBedType().contentEquals("Queen Bed")) {
					queenBed += 1;
				}
				if(room.getBedType().contentEquals("King Bed")) {
					kingBed += 1;
				}
			}
		}
		bedList.add(String.valueOf(singleBed));
		bedList.add(String.valueOf(doubleBed));
		bedList.add(String.valueOf(superSingleBed));
		bedList.add(String.valueOf(queenBed));
		bedList.add(String.valueOf(kingBed));
		return bedList;
	}
}
