import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerInfo extends McnolandMain implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final List<McnolandCustomer> customerList;

    public CustomerInfo() {
        customerList = new ArrayList<>();
    }

    public void addCustomer(McnolandCustomer customer) {
        if (customer != null && !customerExists(customer.getName())) {
            customerList.add(customer);
        }
    }

    public boolean customerExists(String name) {
        return customerList.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }

    public McnolandCustomer getCustomer(int index) {
        if (index >= 0 && index < customerList.size()) {
            return customerList.get(index);
        }
        return null;
    }

    public McnolandCustomer getCustomerByName(String name) {
        return customerList.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<McnolandCustomer> getAllCustomers() {
        return new ArrayList<>(customerList);
    }

    public void removeCustomer(McnolandCustomer customer) {
        customerList.remove(customer);
    }

    public void removeCustomer(String name) {
        customerList.removeIf(c -> c.getName().equalsIgnoreCase(name));
    }

    public void updateCustomer(McnolandCustomer updatedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getName().equalsIgnoreCase(updatedCustomer.getName())) {
                customerList.set(i, updatedCustomer);
                break;
            }
        }
    }

    public void displayAllCustomers() {
        if (customerList.isEmpty()) {
            System.out.println("No customers to display.");
            return;
        }
        System.out.println("\n--- Customer List ---");
        for (int i = 0; i < customerList.size(); i++) {
            System.out.println("Customer #" + (i + 1) + ":");
            displayCustomerInfo(customerList.get(i));
        }
    }

    public void displayCustomerInfo(McnolandCustomer customer) {
        System.out.println("Name: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("Member: " + (customer.isMember() ? "Yes" : "No"));
        System.out.println("Member since: " + customer.getJoinDate());
        System.out.println("Total spending: $" + String.format("%.2f", customer.getTotalSpending()));
        
        if (!customer.getOrderHistory().isEmpty()) {
            System.out.println("\nOrder History:");
            customer.getOrderHistory().forEach(order -> {
                System.out.println("  " + order.getCreationTime().toLocalDate() + 
                                 " - $" + String.format("%.2f", order.getTotalPrice()));
            });
        }
        System.out.println("---------------------");
    }

    public List<McnolandCustomer> searchCustomers(String query) {
        List<McnolandCustomer> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (McnolandCustomer customer : customerList) {
            if (customer.getName().toLowerCase().contains(lowerQuery) ||
                customer.getEmail().toLowerCase().contains(lowerQuery) ||
                customer.getPhone().contains(query)) {
                results.add(customer);
            }
        }
        return results;
    }

    public void promoteToMember(String customerName) {
        McnolandCustomer customer = getCustomerByName(customerName);
        if (customer != null && !customer.isMember()) {
            customer.setMember(true);
            System.out.println(customerName + " has been promoted to member status.");
        }
    }

    public void applyMembershipDiscounts() {
        customerList.stream()
                .filter(McnolandCustomer::isMember)
                .forEach(customer -> {
                    // Could apply special member-only discounts here
                    System.out.println("Applied member benefits to " + customer.getName());
                });
    }
}