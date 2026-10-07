import java.util.ArrayList;

public class OrderCatalog {
    private final ArrayList<FoodItem> orderItems = new ArrayList<>();

    public void addOrder(FoodItem item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            orderItems.add(item);
        }
    }

    public void removeOrder(FoodItem item) {
        orderItems.remove(item);
    }

    public void clearOrder() {
        orderItems.clear();
    }

    public double calculateTotal() {
        double total = 0;
        for (FoodItem item : orderItems) {
            total += item.getPrice();
        }
        return total;
    }

    public String getOrderSummary() {
        if (orderItems.isEmpty()) return "No items ordered.";

        StringBuilder sb = new StringBuilder("--- Customer Order ---\n");
        double total = 0;
        for (FoodItem item : orderItems) {
            sb.append(item.getDisplayInfo()).append("\n");
            total += item.getPrice();
        }
        sb.append(String.format("Total: $%.2f", total));
        return sb.toString();
    }

    public ArrayList<FoodItem> getOrderItems() {
        return orderItems;
    }

    // ✅ Console-only method to print the order
    public void displayOrder() {
        System.out.println(getOrderSummary());
    }
}