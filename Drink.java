public class Drink extends FoodItem {
    private DrinkType type;

    public Drink(DrinkType type) {
        super(type.getName(), type.getPrice());
        this.type = type;
    }

    public DrinkType getType() {
        return type;
    }

    
    public void displayInfo() {
        System.out.println("Drink: " + name + " - " + price);
    }
}
