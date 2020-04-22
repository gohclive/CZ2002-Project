package UI;
import java.util.InputMismatchException;
import java.util.Scanner;
import Entity.Item;
import Controller.MenuController;

public class MenuUI {

    private static MenuUI instance = null;
    Scanner sc = new Scanner(System.in);

    public MenuUI() {
        sc = new Scanner(System.in);
    }

    public static MenuUI getInstance() {
        if (instance == null) instance = new MenuUI();
        return instance;
    }

    public void displayOptions() {
    	int choice;
    	do {
            MenuController.retrieveInstance().displayMenu();
            System.out.println("\n==================================================");
			System.out.println(" Menu item Management: ");
			System.out.println("==================================================");
			System.out.println("(1) Create Menu item\t(2) Update Menu item");
			System.out.println("(3) Remove Menu item\t(4) Back");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                	createIn();
                    break;
                case 2:
                	updateIn();
                    break;
                case 3:
                	removeIn();
                    break;
                case 4:
                    break;
                default:
                	System.out.println("Please enter a valid option!");
					choice = 0;
            }
        } while (choice < 4);
    }

    public void createIn() {
        String itemName = "";
        String itemDesc = "";
        double price = 0.0;
        int itemType = -1;
        sc.nextLine();
        System.out.println("Enter item name:");
        itemName = sc.nextLine();
        System.out.println("Enter item description:");
        itemDesc = sc.nextLine();
        do {
		    try {
		    	System.out.println("Enter item price:");
		        price = sc.nextDouble();
		    	if(price <= 0.0) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (price <= 0.0);
        do {
		    try {
		    	System.out.println("Enter type (0 - Appetizer, 1 - Main Course, 2 - Beverages):");
		        itemType = sc.nextInt();
		    	if(itemType != 0 && itemType != 1 && itemType != 2) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (itemType != 0 && itemType != 1 && itemType != 2);
        MenuController.retrieveInstance().checkID();
        Item item = new Item(itemName, itemDesc, price, itemType);
        MenuController.retrieveInstance().createItem(item);
        System.out.println("Item " + item.getItemId() + ": " + item.getName() + " is created.");
    }
    
    private void updateIn() {
        int itemId = -1;
        do {
		    try {
		    	System.out.println("Enter item ID to be updated:");
		        itemId = sc.nextInt();
		    	if(itemId <= 0) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (itemId <= 0);
        Item item = MenuController.retrieveInstance().retrieveItem(itemId);
        if (item != null) {
            MenuController.retrieveInstance().printItem(item);
            doUpdate(itemId);
        } else {
            System.out.println("Item does not exist!");
        }
    }
    
    public void doUpdate(int itemID) {
    	String itemName = "";
        String itemDesc = "";
        double price = 0.0;
        int itemType = -1;
        System.out.println("Enter item name:");
        itemName = sc.nextLine();
        System.out.println("Enter item description:");
        itemDesc = sc.nextLine();
        do {
		    try {
		    	System.out.println("Enter item price:");
		        price = sc.nextDouble();
		    	if(price <= 0.0) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (price <= 0.0);
        do {
		    try {
		    	System.out.println("Enter type (0 - Appetizer, 1 - Main Course, 2 - Beverages):");
		        itemType = sc.nextInt();
		    	if(itemType != 0 && itemType != 1 && itemType != 2) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (itemType != 0 && itemType != 1 && itemType != 2);
        MenuController.retrieveInstance().updateItem(itemID, itemName, itemDesc, price, itemType);
    }
    
    public void removeIn() {
        int itemId = -1;
        do {
		    try {
		    	System.out.println("Enter item ID to be removed: ");
		        itemId = sc.nextInt();
		    	if(itemId <= 0) System.out.printf("Invalid input! ");
		    } catch (InputMismatchException e) {
		    	System.out.printf("Invalid input! ");
		    }
		    sc.nextLine();
		} while (itemId <= 0);
        Item item = MenuController.retrieveInstance().retrieveItem(itemId);
        if (item != null) {
            MenuController.retrieveInstance().deleteItem(item);
            System.out.println("Item has been removed.");
        } else {
            System.out.println("Item does not exist!");
        }
    }

}
