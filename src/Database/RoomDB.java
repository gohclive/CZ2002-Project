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
import Entity.Room;

public class RoomDB implements DB{
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

			

			// create Guest object from file data
			Room r = new Room(roomId, roomType, bedType, viewType, roomStatus, roomRate, roomFloor, roomNumber, wifiEnabled, smokingStatus);
			// add to Guest list
			alr.add(r);
		}
		return alr;

	}

	@Override
	public void save(String filename, List al) throws IOException {
		// ArrayList<String> alw = new ArrayList<String>();
		List alw = new ArrayList();// to store Guest data

		for (int i = 0; i < al.size(); i++) {
			Room r = (Room) al.get(i);
			StringBuilder st = new StringBuilder();
			st.append(r.getRoomId().trim());
			st.append(SEPARATOR);
			st.append(r.getRoomType().trim());
			st.append(SEPARATOR);
			st.append(r.getBedType().trim());
			st.append(SEPARATOR);
			st.append(r.getViewType().trim());
			st.append(SEPARATOR);
			st.append(r.getRoomStatus().trim().toUpperCase());
			st.append(SEPARATOR);
			st.append(r.getRoomRate().trim());
			st.append(SEPARATOR);
			st.append(r.getRoomFloor().trim());
			st.append(SEPARATOR);
			st.append(r.getRoomNumber().trim());
			st.append(SEPARATOR);
			st.append(r.getWifiEnabled().trim());
			st.append(SEPARATOR);
			st.append(r.getSmokingStatus().trim());
			st.append(SEPARATOR);
			alw.add(st.toString());
		}

		ReadinFile.write(filename, alw);
	}
}
