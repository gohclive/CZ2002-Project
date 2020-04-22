package Controller;
import java.io.IOException;
import java.util.ArrayList;
import Entity.Item;
import Database.MenuDB;

public class MenuController {

    private static final String filename = "Menu.txt";
    private static MenuController instance = null;
    ArrayList<Item> items = new ArrayList<Item>();

    public MenuController() {

        items = new ArrayList<Item>();
    }

    /**
	 * Creating new instance of menu controller
	 * 
	 * @return menucontroller instance
	 */
    public static MenuController retrieveInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }
    
    /**
	 * Retrieval of food menu item
	 * 
	 * @param itemId
	 * 				Specifies the item id to retrieve item
	 * 
	 * @return Item if found. Else will return null
	 */
    public Item retrieveItem(int itemID) {
        for (Item item : items) {
            if (item.getItemId() == itemID) return item;
        }
        return null;
    }
    
    /**
	 * Creation of food menu item
	 * 
	 * @param item
	 * 				Specifies the item to be created
	 */
    public void createItem(Item item) {
        items.add(item);
        checkID();
    }

    /**
	 * Deletion of food menu item
	 * 
	 * @param item
	 * 				Specifies the item
	 */
    public void deleteItem(Item item) {
        items.remove(item);
        checkID();
    }

    /**
	 * Formatting to print food menu item
	 * 
	 * @param item
	 * 				Specifies the item
	 */
    public void printItem(Item item) {
        System.out.println(item.toString());
    }
    
    /**
	 * Deletion of food menu item
	 * 
	 * @param itemID
	 * 				Specifies the item id
	 * @param itemName
	 * 				Specifies the item name
	 * @param itemDesc
	 * 				Specifies the item description
	 * @param price
	 * 				Specifies the item price
	 * @param itemType
	 * 				Specifies the item type
	 */
    public void updateItem(int itemID, String itemName, String itemDesc, double price, int itemType) {
        Item item = null;
        item = retrieveItem(itemID);
        if (!itemName.isEmpty()) item.setName(itemName);
        else System.out.println("Item name cannot be empty! Original name was kept.");
        if (!itemDesc.isEmpty()) item.setDesc(itemDesc);
        else System.out.println("Item description cannot be empty! Original description was kept.");
        if (price > 0.0) item.setPrice(price);
        else System.out.println("Item price cannot be negative nor zero! Original price was kept.");
        if (itemType >= 0 && itemType <= 2) item.setType(itemType);
        else System.out.println("Item type must be either 0, 1 or 2! Original type was kept.");
        System.out.println("Item " + item.getItemId() + ": " + item.getName() + " is updated.");
        OrderController.getInstance().updateItemInOrders(item);
    }
    
    /**
	 * Formating to print menu
	 * 
	 */
    public void displayMenu() {
        System.out.println("ID   Name                          Description                          Price(S$)");
		System.out.println("====================================Appetizer====================================");	
        for (Item item : items) {
            if (item.getType() == 0)
                System.out.println(item.toString());
        }
        System.out.println("===================================Main Course===================================");
        for (Item item : items) {
            if (item.getType() == 1)
                System.out.println(item.toString());
        }
        System.out.println("====================================Beverages====================================");
        for (Item item : items) {
            if (item.getType() == 2)
                System.out.println(item.toString());
        }
    }
    
    /**
	 * Checking of food item id
	 * 
	 */
    public void checkID() {
    	int id = 1;
		if(items!=null) {
			for(Item item : items){
				if(item.getItemId() > id) id = item.getItemId();
			}
		}
		Item.setIdCount(id+1);
    }

    /**
	 * Retrieval of all items
	 * 
	 */
    public void loadinDB() {
    	MenuDB menudb = new MenuDB();
        try {
			this.items = menudb.read(filename);
			checkID();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Saving of all items
	 * 
	 */
    public void savetoDB() {
    	MenuDB menudb = new MenuDB();
        try {
			menudb.save(filename, items);
			checkID(); //ADDED TO CHECK
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

}
