package Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

import Database.CreditDB;
import Database.ReadinFile;
import Entity.CreditCard;

public class CreditController {

	private static String fileName = "Creditcard.txt";
	public static final String SEPARATOR = "|";

	/**
	 * Create credit card details
	 * @throws IOException
	 * @param creditcardNum
	 * 				Specifies the credit card number to create a creditcard
	 * @param guestId
	 * 				Specifies the guest id
	 * @return Boolean value
	 */
	public static boolean createCreditCard(String creditcardNum, String guestId) throws IOException {
		String creditCardExp = "";
		String creditCardCvv = "";
		int cardType = 0;
		int idType = 0;
		CreditCard credit = new CreditCard();
		credit.setCreditCardNumber(creditcardNum);
		credit.setGuestId(guestId);
		boolean save = false;
		String cvvRegex = "\\d{3}";// 

		Scanner sc = new Scanner(System.in);

		// To be used for data validation
		String digit = "\\d+";

		System.out.println("\nCredit Card Details ");
		System.out.println("===================== \n ");

		// Guest Credit Card - Type
		do {
			System.out.println("Please Choose Card Type");
			System.out.println("(1) Visa");
			System.out.println("(2) Master");
			System.out.println("(3) Amex ");
			cardType = sc.nextInt();
			sc.nextLine();

			if (cardType != 1 && cardType != 2 && cardType != 3) {

				System.out.println("Please select a valid Credit Card Type.\n");

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

		// Guest Credit Card - Exp Date
		boolean checker = false;

		do {
			System.out.print("Please Enter Exp (MM/YY): ");
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

		// Guest Credit Card - CVV
		do {
			System.out.print("Please Enter CVV: ");
			creditCardCvv = sc.nextLine();

			if (creditCardCvv.equals("") || !creditCardCvv.matches(cvvRegex)) {

				System.out.println("Please enter a valid Credit Card CVV.\n");

			} else {

				credit.setCreditCardCvv(creditCardCvv);

			}

		} while (creditCardCvv.equals("") || !creditCardCvv.matches(cvvRegex));

		CreditDB creditDB = new CreditDB();
		ArrayList<CreditCard> al = creditDB.read(fileName);
		al.add(credit);

		try {
			// Write Guest records to file
			creditDB.save(fileName, al);
			save = true;
		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		return save;
	}

	/**
	 * Update credit card details
	 * @throws IOException
	 * @param creditcardNum
	 * 				Specifies the credit card number to create a creditcard
	 * @param creditCardType
	 * 				Specifies the credit card type to create a creditcard
	 * @param creditCardExp
	 * 				Specifies the credit card expiry date to create a creditcard
	 * @param creditCardCvv
	 * 				Specifies the credit card cvv number to create a creditcard
	 * @param guestId
	 * 				Specifies the guest id
	 * @return Boolean value
	 */
	public static boolean updateCreditCard(String creditCardNumber, String creditCardType, String creditCardExp,
			String creditCardCvv, String guestId) {
		CreditCard cc = new CreditCard(creditCardNumber, creditCardType, creditCardExp, creditCardCvv, guestId);
		return true;
	}

	/**
	 * Retrieval of all creditcards
	 * 
	 * @return An ArrayList of creditcard details
	 */
	public static ArrayList<CreditCard> retrieveAllCredit() {
		ArrayList<CreditCard> alr = null;
		try {
			// read file containing Guest records
			CreditDB creditDB = new CreditDB();
			alr = creditDB.read(fileName);

		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
		return alr;
	}

	/**
	 * Retrieval of all creditcard details by id
	 * @param id
	 * 				Specifies the id to search for credit card details
	 * @return creditcard
	 */
	public static CreditCard retrieveCreditById(String id) {
		ArrayList <CreditCard> ccList =  retrieveAllCredit();
		CreditCard cc = null;
		for (int i = 0; i < ccList.size(); i++) {
			if(ccList.get(i).getCreditCardNumber().equalsIgnoreCase(id))
			{
				cc = ccList.get(i);
			}
		}
		return cc;
	}
	
	/**
	 * Retrieval of all creditcard details by guest id
	 * @param id
	 * 				Specifies the guest id to search for credit card details
	 * @return creditcard
	 */
	public static CreditCard retrieveCreditByGuestId(String id) {
		ArrayList <CreditCard> ccList =  retrieveAllCredit();
		CreditCard cc = null;
		for (int i = 0; i < ccList.size(); i++) {
			if(ccList.get(i).getGuestId().equalsIgnoreCase(id))
			{
				cc = ccList.get(i);
			}
		}
		return cc;
	}
	
	/**
	 * Retrieval of all creditcard details by guest id for delete
	 * @param id
	 * 				Specifies the guest id to search for credit card details
	 * 
	 */
	public static void deleteCreditCardByGuestId(String id) throws IOException {
		ArrayList<CreditCard> ccList = CreditController.retrieveAllCredit();
		for (int i = 0; i < ccList.size(); i++) {
			if (ccList.get(i).getGuestId().equalsIgnoreCase(id)) {
				ccList.remove(i);
			}
		}

		// Write Guest records to file
		CreditDB creditDB = new CreditDB();
		creditDB.save("Creditcard.txt", ccList);

		System.out.println("Credit Card details has been successfully deleted!");
	}

}
