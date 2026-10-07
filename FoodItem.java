public class FoodItem {
    protected String name;
    protected double price;

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getDisplayInfo() {
        return name + " - $" + price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}