package Database;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import Entity.Reservation;


public class ReservationDB implements DB {
	
	public static final String SEPARATOR = "|";

	@Override
	public ArrayList read(String fileName) throws IOException {
		ArrayList stringArray = (ArrayList) ReadinFile.read(fileName);
		ArrayList alr = new ArrayList();// to store Reservation data
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for (int i = 0; i < stringArray.size(); i++) {

			String st = (String) stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
																		// using delimiter ","

			
			String reservationNum = star.nextToken().trim();
			String guestId = star.nextToken().trim();
			String roomId = star.nextToken().trim();
			String status = star.nextToken().trim();
			String checkInDate = star.nextToken().trim();
			String checkOutDate = star.nextToken().trim();
			String numOfAdults = star.nextToken().trim();
			String numOfChildren = star.nextToken().trim();

			Date cid = null;
			try {
				cid = sdf.parse(checkInDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Date cod = null;
			try {
				cod = sdf.parse(checkOutDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int noa = Integer.valueOf(numOfAdults);
			int noc = Integer.valueOf(numOfChildren);
			
			// create  object from file data
			Reservation r = new Reservation(reservationNum, guestId, roomId, status, cid, cod, noa, noc);
			alr.add(r);
		}
		return alr;
		
	}
	
	@Override
	public void save(String filename, List al) throws IOException {
		List alw = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (int i = 0; i < al.size(); i++) {
			Reservation r = (Reservation) al.get(i);
			StringBuilder st = new StringBuilder();
			st.append(r.getReservationNum().trim());
			st.append(SEPARATOR);
			st.append(r.getGuestId().trim());
			st.append(SEPARATOR);
			st.append(r.getRoomId().trim());
			st.append(SEPARATOR);
			st.append(r.getStatus().trim());
			st.append(SEPARATOR);
			st.append(sdf.format(r.getCheckInDate()).trim());
			st.append(SEPARATOR);
			st.append(sdf.format(r.getCheckOutDate()).trim());
			st.append(SEPARATOR);
			st.append(String.valueOf(r.getNumOfAdults()).trim());
			st.append(SEPARATOR);
			st.append(String.valueOf(r.getNumOfChildren()).trim());
			st.append(SEPARATOR);
			alw.add(st.toString());
		}
		
		ReadinFile.write(filename, alw);
	}
}
