public enum DrinkType {
    COKE("Coke: ", 1.99),
    SPRITE("Sprite: ", 1.99),
    ICED_LEMON_TEA("Iced Lemon Tea ", 2.49),
    COFFEE("Coffee: ", 2.99),
    ICED_MILO("Iced Milo: ", 3.49);

    private final String name;
    private final double price;

    DrinkType(String name, double price) {
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