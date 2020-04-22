package Entity;

public class Item {
    private static int idCount = 1;
    private int itemId;
    private String name;
    private String desc;
    private double price;
    private int type;

    public Item(String name, String desc, double price, int type) {
        this.itemId = idCount;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.type = type;
        idCount++;
    }

    public Item(int id, String name, String desc, double price, int type) {
        this.itemId = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.type = type;
    }
    
    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int ID) {
        idCount = ID;
    }

    public int getItemId() {
        return itemId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }

    public String toString() {

        return (String.format("%-5d%-15s%-52s%.2f", itemId, name, desc, price));
    }

}
