// BurgerType.java
public enum BurgerType {
    CHEESEBURGER("Cheeseburger", 5.99),
    VEGAN_BURGER("Vegan Burger", 6.49),
    CHICKEN_BURGER("Chicken Burger", 6.99),
    BEEF_BURGER("Beef Burger", 7.49),
    BENJO_BURGER("Benjo Burger", 8.99);

    private final String name;
    private final double price;

    BurgerType(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
