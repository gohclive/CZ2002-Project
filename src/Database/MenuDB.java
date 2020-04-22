package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import Entity.Item;


public class MenuDB implements DB {
	
	public static final String SEPARATOR = "|";

	@Override
	public ArrayList<Item> read(String fileName) throws IOException {
		ArrayList<String> stringArray = (ArrayList<String>) ReadinFile.read(fileName);
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < stringArray.size(); i++) {
			String st = stringArray.get(i);
			StringTokenizer star = new StringTokenizer(st, SEPARATOR);
			
			int itemID = Integer.valueOf(star.nextToken().trim());
			String name = star.nextToken().trim();
			String desc = star.nextToken().trim();
			double price = Double.valueOf(star.nextToken().trim());
			int type = Integer.valueOf(star.nextToken().trim());
			Item item = new Item(itemID, name, desc, price, type);
			items.add(item);
		}
		System.out.println(stringArray.size() + " Menu Item(s) Loaded.");
		return items;
	}
	
	@Override
	public void save(String filename, List items) throws IOException {
		ArrayList<String> itemsw = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			Item item = (Item) items.get(i);
			StringBuilder st = new StringBuilder();
			st.append(item.getItemId());
			st.append(SEPARATOR);
			st.append(item.getName());
			st.append(SEPARATOR);
			st.append(item.getDesc());
			st.append(SEPARATOR);
			st.append(item.getPrice());
			st.append(SEPARATOR);
			st.append(item.getType());
			st.append(SEPARATOR);
			itemsw.add(st.toString());
		}
		
		ReadinFile.write(filename, itemsw);
	}
}
