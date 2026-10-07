import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class McnolandCustomer {
    private final String name;
    private boolean isMember;
    private String email;
    private String phone;
    private LocalDateTime joinDate;
    private List<Order> orderHistory = new ArrayList<>();
    private Order currentOrder;

    public McnolandCustomer(String name, boolean isMember, String email, String phone) {
        this.name = name;
        this.isMember = isMember;
        this.email = email;
        this.phone = phone;
        this.joinDate = LocalDateTime.now();
        this.currentOrder = new Order();
    }

    public void initializeCurrentOrder() {
        if (currentOrder == null) {
            currentOrder = new Order();
        }
    }

    public void completeCurrentOrder() {
        if (!currentOrder.getItems().isEmpty()) {
            currentOrder.setCompletionTime(LocalDateTime.now());
            orderHistory.add(currentOrder);
            currentOrder = new Order();
        }
    }

    public double getTotalSpending() {
        return orderHistory.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    // Text serialization methods
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(escape(name)).append(";")
          .append(isMember).append(";")
          .append(escape(email)).append(";")
          .append(escape(phone)).append(";")
          .append(joinDate);

        sb.append("|");
        for (Order order : orderHistory) {
            sb.append(order.serialize()).append(",");
        }
        if (!orderHistory.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static McnolandCustomer deserialize(String data) {
        String[] parts = data.split("\\|", 2);
        String[] basicInfo = parts[0].split(";");

        McnolandCustomer customer = new McnolandCustomer(
            unescape(basicInfo[0]),
            Boolean.parseBoolean(basicInfo[1]),
            unescape(basicInfo[2]),
            unescape(basicInfo[3])
        );
        customer.setJoinDate(LocalDateTime.parse(basicInfo[4]));

        if (parts.length > 1 && !parts[1].isEmpty()) {
            for (String orderData : parts[1].split(",")) {
                customer.getOrderHistory().add(Order.deserialize(orderData));
            }
        }

        customer.initializeCurrentOrder();
        return customer;
    }

    private static String escape(String str) {
        return str.replace(";", "\\;").replace("|", "\\|").replace(",", "\\,");
    }

    private static String unescape(String str) {
        return str.replace("\\;", ";").replace("\\|", "|").replace("\\,", ",");
    }

    // Standard getters and setters...
    public String getName() { return name; }
    public boolean isMember() { return isMember; }
    public void setMember(boolean member) { isMember = member; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
    public List<Order> getOrderHistory() { return orderHistory; }
    public Order getCurrentOrder() { return currentOrder; }
}