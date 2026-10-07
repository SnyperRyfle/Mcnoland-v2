public class OrderItem {
    private final FoodItem item;
    private final int quantity;

    public OrderItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }

    public String serialize() {
        return String.format("%s:%f:%d",
            item.getName().replace(":", "\\:"),
            item.getPrice(),
            quantity);
    }

    public static OrderItem deserialize(String data) {
        String[] parts = data.replace("\\:", "\0").split(":");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\0", ":");
        }
        
        return new OrderItem(
            new FoodItem(parts[0], Double.parseDouble(parts[1])),
            Integer.parseInt(parts[2])
        );
    }

    // Standard getters...
    public FoodItem getItem() { return item; }
    public int getQuantity() { return quantity; }
}