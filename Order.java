import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private LocalDateTime creationTime;
    private LocalDateTime completionTime;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.creationTime = LocalDateTime.now();
    }

    public void addItem(FoodItem item, int quantity) {
        items.add(new OrderItem(item, quantity));
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(creationTime).append(";")
          .append(completionTime != null ? completionTime : "").append(";");

        for (OrderItem item : items) {
            sb.append(item.serialize()).append(",");
        }
        if (!items.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static Order deserialize(String data) {
        String[] parts = data.split(";", 3);
        Order order = new Order();
        
        if (!parts[1].isEmpty()) {
            order.setCompletionTime(LocalDateTime.parse(parts[1]));
        }

        if (parts.length > 2 && !parts[2].isEmpty()) {
            for (String itemData : parts[2].split(",")) {
                order.getItems().add(OrderItem.deserialize(itemData));
            }
        }

        return order;
    }

    // Standard getters and setters...
    public LocalDateTime getCreationTime() { return creationTime; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime time) { this.completionTime = time; }
    public List<OrderItem> getItems() { return items; }
}