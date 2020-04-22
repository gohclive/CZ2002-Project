package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Scanner;
import Entity.Guest;

public class GuestDB implements DB{
	public static final String SEPARATOR = "|";

	@Override
	public ArrayList read(String fileName) throws IOException {

		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList alr = new ArrayList();// to store Guest data

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
			//int phoneNo = Integer.parseInt(star.nextToken().trim());
			String address = star.nextToken().trim();
			String ccNum = star.nextToken().trim();
			//int ccCvv = Integer.parseInt(star.nextToken().trim());
			

			// create Guest object from file data
			Guest g = new Guest(guestId, name, gender, nationality,country,phoneNo, address,ccNum);
			// add to Guest list
			alr.add(g);
		}
		return alr;

	}

	@Override
	public void save(String filename, List al) throws IOException {
		// ArrayList<String> alw = new ArrayList<String>();
		List alw = new ArrayList();// to store Guest data

		for (int i = 0; i < al.size(); i++) {
			Guest g = (Guest) al.get(i);
			StringBuilder st = new StringBuilder();
			st.append(g.getGuestId().trim());
			st.append(SEPARATOR);
			st.append(g.getName().trim());
			st.append(SEPARATOR);
			st.append(g.getGender());
			st.append(SEPARATOR);
			st.append(g.getNationality());
			st.append(SEPARATOR);
			st.append(g.getCountry());
			st.append(SEPARATOR);
			st.append(g.getPhoneNumber());
			st.append(SEPARATOR);
			st.append(g.getAddress());
			st.append(SEPARATOR);
			st.append(g.getCreditCardNumber());
			st.append(SEPARATOR);
			alw.add(st.toString());
		}

		ReadinFile.write(filename, alw);
	}


}