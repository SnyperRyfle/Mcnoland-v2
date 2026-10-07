public enum DessertType {
    APPLE_PIE("Apple Pie", 2.99),
    SUNDAE("Sundae", 3.49),
    MCCAFE_COOKIE("McCafe Cookie", 1.99),
    SOFT_SERVE("Soft Serve Cone", 1.49),
    CHOCOLATE_CHIP_COOKIE("Chocolate Chip Cookie", 1.79);

    private final String name;
    private final double price;

    DessertType(String name, double price) {
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