package Entity;
import java.util.ArrayList;

public class Menu {
    private ArrayList<Item> items = new ArrayList<Item>();

    public Menu(ArrayList<Item> item) {
        items = item;
    }
    
    public ArrayList<Item> getMenu() {
        return items;
    }

}
