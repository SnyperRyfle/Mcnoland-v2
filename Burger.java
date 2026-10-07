public class Burger extends FoodItem {
    private BurgerType type;

    public Burger(BurgerType type) {
        super(type.getName(), type.getPrice());
        this.type = type;
    }

    public BurgerType getType() {
        return type;
    }

    
    public void displayInfo() {
        System.out.println("Burger: " + name + " - $" + price);
    }
}
