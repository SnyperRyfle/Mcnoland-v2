public class ComboMeal extends FoodItem {
    private final Burger burger;
    private final Drink drink;
    private final Dessert dessert;
    private final double discount;

    public ComboMeal(String name, Burger burger, Drink drink, Dessert dessert, double discount) {
        super(name, burger.getPrice() + drink.getPrice() + dessert.getPrice() - discount);
        this.burger = burger;
        this.drink = drink;
        this.dessert = dessert;
        this.discount = discount;
    }

    @Override
    public String getDisplayInfo() {
        return String.format("🍱 %s (Burger: %s, Drink: %s, Dessert: %s) - $%.2f (Save $%.2f!)",
                name, burger.getName(), drink.getName(), dessert.getName(), price, discount);
    }

    // Getters
    public Burger getBurger() { return burger; }
    public Drink getDrink() { return drink; }
    public Dessert getDessert() { return dessert; }
    public double getDiscount() { return discount; }
}