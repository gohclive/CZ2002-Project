package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Entity.CreditCard;

public class CreditDB implements DB {

	public static final String SEPARATOR = "|";
	
	@Override
	public ArrayList read(String fileName) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList alr = new ArrayList();// to store credit Card data

		for (int i = 0; i < stringArray.size(); i++) {

			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			String creditCardNumber = star.nextToken().trim();
			String creditCardType = star.nextToken().trim();
			String creditCardExp = star.nextToken().trim();
			String creditCardCvv = star.nextToken().trim();
			String guestId = star.nextToken().trim();

			// create credit card object from file data
			CreditCard cc = new CreditCard(creditCardNumber, creditCardType, creditCardExp, creditCardCvv, guestId);
			// add to credit card list
			alr.add(cc);
		}
		return alr;
	}

	@Override
	public void save(String fileName, List al) throws IOException {
		List alw = new ArrayList();// to store credit card data

		for (int i = 0; i < al.size(); i++) {
			CreditCard cc = (CreditCard) al.get(i);
			StringBuilder st = new StringBuilder();
			st.append(cc.getCreditCardNumber());
			st.append(SEPARATOR);
			st.append(cc.getCreditCardType());
			st.append(SEPARATOR);
			st.append(cc.getCreditCardExp());
			st.append(SEPARATOR);
			st.append(cc.getCreditCardCvv());
			st.append(SEPARATOR);
			st.append(cc.getGuestId());
			st.append(SEPARATOR);
			alw.add(st.toString());
		}

		ReadinFile.write(fileName, alw);
		
	}

}
