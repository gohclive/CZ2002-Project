package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import Database.CreditDB;
import Database.GuestDB;
import Database.ReadinFile;
import Entity.CreditCard;
import Entity.Guest;

public class GuestController {

	private static String fileName = "Guest.txt";
	public static final String SEPARATOR = "|";

	/**
	 * Creation of new guest
	 * 
	 */

	public static void createGuest() throws IOException {
		String drivingLicense = "";
		String pp = "";
		String Name = "";
		String Gender = "";
		String Nationality = "";
		String Country = "";
		String phoneNumber = "";
		String Address = "";
		String creditCardNumber = "";
		String guestSelection = "";
		int idType = 0;
		Scanner sc = new Scanner(System.in);

		// To be used for data validation
		String digit = "\\d+";
		String alpha = "[a-zA-Z.*\\s+.]+";
		// String cc = "\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}";
		String cc = "\\d{4}\\-\\d{4}\\-\\d{4}\\-\\d{4}";// For credit card number
		String cvvRegex = "\\d{3}";// 
		
		// Prompt user for guest details and set it into a guest object
		do {
			Guest guest = new Guest();

			System.out.println("\n==================================================");
			System.out.println(" Guest Creation ");
			System.out.println("==================================================");

			// Guest Identity
			do {

				System.out.println("Please Choose Identity Type");
				System.out.println("(1) Driving License");
				System.out.println("(2) Passport ");
				Guest check = new Guest();
				idType = sc.nextInt();
				sc.nextLine();

				if (idType != 1 && idType != 2) {

					System.out.println("Please select a valid identity type\n");

				} else {

					switch (idType) {

					case 1:

						do {
							System.out.print("Please enter Driving License: ");
							drivingLicense = sc.nextLine();

							guest.setGuestId(drivingLicense);
							check = retrieveGuest(guest);
							if (check != null) {
								System.out
										.println("Error. Driving license exists! Please enter a new driving license\n");
							}

							else if (drivingLicense.equals("")) {
								System.out.println("Error. Please enter a valid driving license\n");
							}

							else {
								guest.setGuestId(drivingLicense);
							}
						} while (check != null || drivingLicense.equals(""));
						break;

					case 2:

						do {
							System.out.print("Please Enter Passport Number: ");
							pp = sc.nextLine();

							guest.setGuestId(pp);
							check = retrieveGuest(guest);
							if (check != null) {
								System.out
										.println("\nError. Passport Number exists! Please enter a new passport Number");
							}

							else if (pp.equals("")) {
								System.out.println("\nError. Please enter a valid Passport Number");
							}

							else {
								guest.setGuestId(pp);
							}
						} while (check != null || pp.equals(""));
						break;
					}

				}
			} while (idType != 1 && idType != 2);

			// Guest Name
			do {
				System.out.print("\nPlease Enter Name: ");
				Name = sc.nextLine();

				if (Name.equals("") || !Name.matches(alpha)) {

					System.out.println("Please enter a valid Name.");

				} else {

					guest.setName(Name);

				}

			} while (Name.equals("") || !Name.matches(alpha));

			// Guest Gender
			do {
				System.out.println("\nPlease choose Gender");
				System.out.println("(1) Male");
				System.out.println("(2) Female ");
				Gender = sc.nextLine();

				if (!Gender.equals("1") && !Gender.equals("2")) {

					System.out.println("Please select a valid Gender.");

				} else {

					switch (Gender) {

					case "1":
						guest.setGender("Male");
						break;

					case "2":
						guest.setGender("Female");
						break;

					}

				}

			} while (!Gender.equals("1") && !Gender.equals("2"));

			// Guest Nationality
			do {
				System.out.print("\nPlease Enter Nationality: ");
				Nationality = sc.nextLine();

				if (Nationality.equals("") || !Nationality.matches(alpha)) {

					System.out.println("Please enter a valid Nationality.");
				} else {

					guest.setNationality(Nationality);

				}

			} while (Nationality.equals("") || !Nationality.matches(alpha));

			// Guest Country
			do {
				System.out.print("\nPlease Enter Country: ");
				Country = sc.nextLine();

				if (Country.equals("") || !Country.matches(alpha)) {

					System.out.println("Please enter a valid Country.");
				} else {
					guest.setCountry(Country);
				}

			} while (Country.equals("") || !Country.matches(alpha));

			System.out.println("");

			// Guest PhoneNumber
			do {
				System.out.print("Please Enter Contact Number: ");
				phoneNumber = sc.nextLine();

				if (phoneNumber.equals("") || !phoneNumber.matches(digit)) {

					System.out.println("Please enter a valid Contact Number.\n");

				} else {

					guest.setPhoneNumber(phoneNumber);

				}

			} while (phoneNumber.equals("") || !phoneNumber.matches(digit));

			System.out.println("");
			// Guest Address
			do {
				System.out.print("Please Enter Residential Address: ");
				Address = sc.nextLine();

				if (Address.equals("")) {

					System.out.println("Please enter a valid Residential Address.\n");

				} else {

					guest.setAddress(Address);

				}

			} while (Address.equals(""));

			// Guest Credit Card - Number.
			do {
				System.out.print("Please Enter Card Number(Format: XXXX-XXXX-XXXX-XXXX): ");
				creditCardNumber = sc.nextLine();

				if (creditCardNumber.equals("") || !creditCardNumber.matches(cc)) {

					System.out.println("Please enter a valid Credit Card Number.\n");

				} else {

					guest.setCreditCardNumber(creditCardNumber);
				}

			} while (creditCardNumber.equals("") || !creditCardNumber.matches(cc));

			boolean creditcardsaved = CreditController.createCreditCard(creditCardNumber, guest.getGuestId());

			GuestDB guestDB = new GuestDB();
			ArrayList al = guestDB.read(fileName);
			al.add(guest);

			try {
				// Write Guest records to file
				guestDB.save(fileName, al);
				if (creditcardsaved) {
					System.out.println("You have successfully created a new guest! ");
				}

			} catch (IOException e) {
				System.out.println("IOException > " + e.getMessage());
			}
			System.out.println("Do you want to continue? (Y/N)");
			guestSelection = sc.nextLine();
		} while (!guestSelection.equalsIgnoreCase("N"));

	}

	/**
	 * Retrieval of all guests details.
	 * 
	 */
	public static void retrieveAllGuest() throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList<CreditCard> creditArray = CreditController.retrieveAllCredit();
		System.out.println("\n==================================================");
		System.out.println(" Guest Details ");
		System.out.println("==================================================");
		// System.out.println(" ID Name Gender Nationality Country Phone Number Address
		// CreditCard Type CreditCard Number CreditCard Expiry CreditCard CVV ");
		System.out.printf("%-10s %-10s %-8s %-15s %-13s %-10s %-25s %-19s %-18s %-15s %-3s", "GuestID", "Name",
				"Gender", "Nationality", "Country", "Contact", "Address", "CreditCard Number", "CreditCard Type",
				"CreditCard Exp", "CreditCard Cvv ");
		System.out.println();
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String guestId = star.nextToken().trim();
			String name = star.nextToken().trim();
			String gender = star.nextToken().trim();
			String nationality = star.nextToken().trim();
			String country = star.nextToken().trim();
			String phoneNo = star.nextToken().trim();
			String address = star.nextToken().trim();
			String ccNum = star.nextToken().trim();

			// credit card object
			String ccType = creditArray.get(i).getCreditCardType();
			String ccExp = creditArray.get(i).getCreditCardExp();
			String ccCvv = creditArray.get(i).getCreditCardCvv();

			System.out.printf("%-10s %-10s %-8s %-15s %-13s %-10s %-25s %-19s %-18s %-15s %-3s", guestId, name, gender,
					nationality, country, phoneNo, address, ccNum, ccType, ccExp, ccCvv);
			System.out.println("");
			// System.out.println(" " + guestId + "\t" + name + "\t" + gender + "\t\t" +
			// nationality + "\t " + country
			// + "\t\t" + phoneNo + "\t\t" + address + "\t\t" + ccType + "\t\t\t" + ccNum +
			// "\t " + ccExp
			// + "\t\t " + ccCvv);
		}
	}

	/**
	 * Retrieval of specific guest's details by ID
	 * 
	 */
	public static void retrieveGuestbyId() throws IOException {
		String ID;
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		Scanner sc = new Scanner(System.in);
		System.out.println("\n==================================================");
		System.out.println(" Search Guest Record");
		System.out.println("==================================================");

		System.out.print("Enter Guest ID: ");
		ID = sc.nextLine();

		// System.out.println(" ID Name Gender Nationality Country Phone Number Address
		// CreditCard Type CreditCard Number CreditCard Expiry CreditCard CVV ");
		System.out.printf("%-10s %-10s %-8s %-15s %-13s %-10s %-25s %-19s %-18s %-15s %-3s", "GuestID", "Name",
				"Gender", "Nationality", "Country", "Contact", "Address", "CreditCard Number", "CreditCard Type",
				"CreditCard Exp", "CreditCard Cvv ");
		System.out.println("");
		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","
			String guestId = star.nextToken().trim();
			String name = star.nextToken().trim();
			String gender = star.nextToken().trim();
			String nationality = star.nextToken().trim();
			String country = star.nextToken().trim();
			String phoneNo = star.nextToken().trim();
			String address = star.nextToken().trim();

			String ccNum = star.nextToken().trim();
			if (guestId.contains(ID)) {
				// System.out.println(" " + guestId + "\t" + name + "\t" + gender + "\t\t" +
				// nationality + "\t "+ country + "\t\t" + phoneNo + "\t\t" + address + "\t\t" +
				// ccType + "\t\t\t" + ccNum + "\t "+ ccExp + "\t\t " + ccCvv);

				// credit card object
				CreditCard credit = CreditController.retrieveCreditById(ccNum);
				String ccType = credit.getCreditCardType();
				String ccExp = credit.getCreditCardExp();
				String ccCvv = credit.getCreditCardCvv();

				System.out.printf("%-10s %-10s %-8s %-15s %-13s %-10s %-25s %-19s %-18s %-15s %-3s", guestId, name,
						gender, nationality, country, phoneNo, address, ccNum, ccType, ccExp, ccCvv);
			}
		}
	}

	/**
	 * Retrieval of specific guest's details.
	 * 
	 * @param guest Parameter to search for guest details.
	 * @return guest if found else return null.
	 */
	public static Guest retrieveGuest(Guest guest) {
		ArrayList alr = retrieveAllGuestFromDb();

		for (int i = 0; i < alr.size(); i++) {
			Guest searchGuest = (Guest) alr.get(i);

			if (guest.getGuestId().equals(searchGuest.getGuestId())) {
				guest = searchGuest;
				return guest;
			}
		}
		return null;
	}

	/**
	 * Return guest object if found.
	 * 
	 * @param ID Parameter to search for guest details.
	 * @return guest if found else return null.
	 */
	public static Guest retrieveGuestWithString(String ID) {
		ArrayList alr = retrieveAllGuestFromDb();

		for (int i = 0; i < alr.size(); i++) {
			Guest searchGuest = (Guest) alr.get(i);

			if (ID.equals(searchGuest.getGuestId())) {
				return searchGuest;
			}
		}
		return null;
	}

	/**
	 * Update Guest Details by ID
	 * 
	 */
	public static void updateGuestById() throws IOException {
		System.out.println("\n==================================================");
		System.out.println(" Update Guest");
		System.out.println("==================================================");
		String Name = "";
		String Gender = "";
		String Nationality = "";
		String Country = "";
		String phoneNumber = "";
		String Address = "";
		String creditCardNumber = "";
		String creditCardExp = "";
		String creditCardCvv = "";
		int cardType = 0;
		int updateType = 0;
		int updateCard = 0;
		String selection = "";

		Scanner sc = new Scanner(System.in);

		// To be used for data validation
		String digit = "\\d+";
		String alpha = "[a-zA-Z.*\\s+.]+";
		String cc = "\\d{4}\\-\\d{4}\\-\\d{4}\\-\\d{4}"; // For credit card number

		Guest updateGuest = new Guest();
		updateGuest = retrieveGuestDetails();

		do {
			System.out.println("\nPlease choose Guest Detail to update");
			System.out.println("(1) Name");
			System.out.println("(2) Gender");
			System.out.println("(3) Nationality");
			System.out.println("(4) Country");
			System.out.println("(5) Contact Number");
			System.out.println("(6) Address");
			System.out.println("(7) Credit Card");
			updateType = sc.nextInt();
			sc.nextLine();

			switch (updateType) {

			case 1:
				// Guest Name
				do {
					System.out.print("\nPlease Enter New Name: ");
					Name = sc.nextLine();

					if (Name.equals("") || !Name.matches(alpha)) {

						System.out.println("Please enter a valid Name.");

					} else {

						updateGuest.setName(Name);
						break;
					}

				} while (Name.equals("") || !Name.matches(alpha));
				break;

			case 2:
				// Guest Gender
				do {
					System.out.println("\nPlease Enter New Gender: ");

					System.out.println("(1) Male");
					System.out.println("(2) Female ");
					Gender = sc.nextLine();

					if (!Gender.equals("1") && !Gender.equals("2")) {

						System.out.println("Please select a valid Gender.");

					} else {

						switch (Gender) {

						case "1":
							updateGuest.setGender("Male");
							break;
						case "2":
							updateGuest.setGender("Female");
							break;
						}
					}
				} while (!Gender.equals("1") && !Gender.equals("2"));
				break;
			case 3:
				// Guest Nationality
				do {
					System.out.print("\nPlease Enter New Nationality: ");
					Nationality = sc.nextLine();

					if (Nationality.equals("") || !Nationality.matches(alpha)) {

						System.out.println("Please enter a valid Nationality.");
					} else {
						updateGuest.setNationality(Nationality);
						break;
					}
				} while (Nationality.equals("") || !Nationality.matches(alpha));
				break;
			case 4:
				// Guest Country
				do {
					System.out.print("\nPlease Enter New Country: ");
					Country = sc.nextLine();

					if (Country.equals("") || !Country.matches(alpha)) {

						System.out.println("Please enter a valid Country.");
					} else {
						updateGuest.setCountry(Country);
						break;
					}

				} while (Country.equals("") || !Country.matches(alpha));
				break;
			case 5:
				// Guest PhoneNumber
				do {
					System.out.print("Please Enter New Contact Number: ");
					phoneNumber = sc.nextLine();

					if (phoneNumber.equals("") || !phoneNumber.matches(digit)) {

						System.out.println("Please enter a valid Contact Number\n");

					} else {

						updateGuest.setPhoneNumber(phoneNumber);
						break;
					}

				} while (phoneNumber.equals("") || !phoneNumber.matches(digit));
				break;
			case 6:
				// Guest Address
				do {
					System.out.print("Please Enter New Residential Address: ");
					Address = sc.nextLine();

					if (Address.equals("")) {
						System.out.println("Please enter a valid Residential Address.\n");
					} else {
						updateGuest.setAddress(Address);
						break;
					}
				} while (Address.equals(""));
				break;
			case 7:
				// Guest Credit Card - Type
				Boolean creditCardChecker = false;
				CreditCard credit = CreditController.retrieveCreditByGuestId(updateGuest.getGuestId());
				do {
					System.out.println("Please Choose Credit Card Details to update");
					System.out.println("(1) Card Type");
					System.out.println("(2) Card Number");
					System.out.println("(3) Card Exp");
					System.out.println("(4) Card Cvv");
					updateCard = sc.nextInt();
					sc.nextLine();

					if (updateCard != 1 && updateCard != 2 && updateCard != 3 && updateCard != 4) {
						System.out.println("Please select a valid Credit Card detail to be updated.\n");
					} else {

						switch (updateCard) {

						case 1:
							do {
								System.out.println("Please choose new Card Type");
								System.out.println("(1) Visa");
								System.out.println("(2) Master");
								System.out.println("(3) Amex");
								cardType = sc.nextInt();
								sc.nextLine();

								if (cardType != 1 && cardType != 2 && cardType != 3) {

									System.out.println("Please select a valid Credit Card Type\n");

								} else {

									switch (cardType) {

									case 1:
										credit.setCreditCardType("Visa");
										break;

									case 2:
										credit.setCreditCardType("Master");
										break;

									case 3:
										credit.setCreditCardType("Amex");
										break;
									}
								}
							} while (cardType != 1 && cardType != 2 && cardType != 3);
							creditCardChecker = true;
							break;
						case 2:
							// Guest Credit Card - Number.
							do {
								System.out.print("Please enter new Credit Card Number(Format: XXXX-XXXX-XXXX-XXXX): ");
								creditCardNumber = sc.nextLine();

								if (creditCardNumber.equals("") || !creditCardNumber.matches(cc)) {
									System.out.println(
											"Please enter a valid Credit Card Number(Format: XXXX-XXXX-XXXX-XXXX)\n");
								} else {
									credit.setCreditCardNumber(creditCardNumber);
									updateGuest.setCreditCardNumber(creditCardNumber);
									break;
								}
							} while (creditCardNumber.equals("") || !creditCardNumber.matches(cc));
							creditCardChecker = true;
							break;
						case 3:
							// Guest Credit Card - Exp Date
							boolean checker = false;

							do {
								System.out.print("Please Enter new Exp (MM/YY): ");
								creditCardExp = sc.nextLine();

								SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
								sdf.setLenient(false);
								Date todaysdate = new Date();
								Date inputdate = null;

								try {

									inputdate = sdf.parse(creditCardExp);

									if (inputdate.before(todaysdate)) {

										System.out.println("Please enter a valid Credit Card Expiration Date.\n");

									} else {

										credit.setCreditCardExp(creditCardExp);
										checker = true;
										break;

									}

								} catch (ParseException e1) {
									if (inputdate == null) {

										System.out.println("Please enter a valid Credit Card Expiration Date.\n");

									}
								}
							} while (!checker);
							creditCardChecker = true;
							break;
						case 4:
							// Guest Credit Card - CVV
							do {
								System.out.print("Please Enter new CVV: ");
								creditCardCvv = sc.nextLine();

								if (creditCardCvv.equals("") || !creditCardCvv.matches(digit)) {

									System.out.println("Please enter a valid credit card CVV\n");

								} else {

									credit.setCreditCardCvv(creditCardCvv);
									break;
								}

							} while (creditCardCvv.equals("") || !creditCardCvv.matches(digit));
							creditCardChecker = true;
							break;
						}

					}

				} while (creditCardChecker != true);

				// save credit details
				try

				{
					ArrayList alr = CreditController.retrieveAllCredit();
					for (int i = 0; i < alr.size(); i++) {
						CreditCard searchCredit = (CreditCard) alr.get(i);

						if (credit.getGuestId().equals(searchCredit.getGuestId())) {
							alr.set(i, credit);
						}
					}

					// Write Credit records to file
					CreditDB creditDB = new CreditDB();
					creditDB.save("Creditcard.txt", alr);

					System.out.println("Credit Card details has been successfully updated!");
				} catch (

				IOException e)

				{
					System.out.println("IOException > " + e.getMessage());
				}

				break;
			}
			System.out.println("Do you want to continue? (Y/N)");
			selection = sc.nextLine();
		} while (!selection.equalsIgnoreCase("N"));
		try

		{
			ArrayList alr = retrieveAllGuestFromDb();
			for (int i = 0; i < alr.size(); i++) {
				Guest searchGuest = (Guest) alr.get(i);

				if (updateGuest.getGuestId().equals(searchGuest.getGuestId())) {
					alr.set(i, updateGuest);
				}
			}

			// Write Guest records to file
			GuestDB guestDB = new GuestDB();
			guestDB.save(fileName, alr);

			System.out.println("Guest details has been successfully updated!");
		} catch (

		IOException e)

		{
			System.out.println("IOException > " + e.getMessage());
		}

	}

	public static void deleteGuestById() throws IOException {
		Scanner strInput = new Scanner(System.in);
		String ID, record;

		File tempDB = new File("tempDel1.txt");
		File db = new File(fileName);

		BufferedReader br = new BufferedReader(new FileReader(db));
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempDB));

		System.out.println("\n==================================================");
		System.out.println(" Delete Guest Record");
		System.out.println("==================================================");

		System.out.println("Enter Guest ID: ");
		ID = strInput.nextLine();

		while ((record = br.readLine()) != null) {

			String array1[] = record.split(",");
			if (array1[0].contains(ID))
				continue;

			bw.write(record);
			bw.flush();
			bw.newLine();

		}

		br.close();
		bw.close();

		db.delete();

		boolean success = tempDB.renameTo(db);
		if (success == true)
		{
			System.out.println("Guest has been successfully deleted!");
			CreditController.deleteCreditCardByGuestId(ID);
		}
		else
			System.out.println("Guest has not been successfully deleted");
		// System.out.println(success);
	}


	/**
	 * Retrieval of guest's details by driving license or passport number.
	 * 
	 * @return guest details.
	 */

	public static Guest retrieveGuestDetails() {
		String drivingLicense = "";
		String pp = "";
		int idType = 0;
		Scanner sc = new Scanner(System.in);

		Guest guest = new Guest();
		Guest check = new Guest();

		// Guest Identity
		do {

			System.out.println("Please choose Identity Type to update by");
			System.out.println("(1) Driving License");
			System.out.println("(2) Passport");
			idType = sc.nextInt();
			sc.nextLine();

			if (idType == 1) {
				do {
					System.out.print("Please enter Driving License: ");
					drivingLicense = sc.nextLine();

					guest.setGuestId(drivingLicense);
					check = retrieveGuest(guest);
					if (check == null) {
						System.out.println("Error. Please Enter a valid Driving License");
					}
				} while (check == null);

			} else if (idType == 2) {
				do {
					System.out.print("Please enter Passport Number: ");
					pp = sc.nextLine();
					guest.setGuestId(pp);
					check = retrieveGuest(guest);
					if (check == null) {
						System.out.println("\nError. Please enter a valid Passport Number");
					}
				} while (check == null);
			} else {
				System.out.println("Error. Please select a valid Identity type\n");
			}
		} while (idType != 1 && idType != 2);

		return check;

	}

	/**
	 * Retrieval of guest details.
	 * 
	 * @return ArrayList of all guests.
	 */
	public static ArrayList retrieveAllGuestFromDb() {
		ArrayList alr = null;
		try {
			// read file containing Guest records
			GuestDB guestDB = new GuestDB();
			alr = guestDB.read(fileName);

		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		return alr;
	}
}
