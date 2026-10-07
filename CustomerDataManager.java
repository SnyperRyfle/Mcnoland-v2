import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CustomerDataManager {
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String DELIMITER = "|";

    // Save customer to text file
    public static void saveCustomer(McnolandCustomer customer, String password) throws IOException {
        // Create file if it doesn't exist
        if (!Files.exists(Paths.get(CUSTOMER_FILE))) {
            Files.createFile(Paths.get(CUSTOMER_FILE));
        }

        // Read all existing customers
        List<String> customers = Files.readAllLines(Paths.get(CUSTOMER_FILE));

        // Remove existing record if present
        customers.removeIf(line -> line.startsWith(customer.getEmail() + DELIMITER));

        // Add new customer record
        customers.add(customer.getEmail() + DELIMITER +
                     customer.getName() + DELIMITER +
                     customer.isMember() + DELIMITER +
                     customer.getPhone() + DELIMITER +
                     password);

        // Write back to file
        Files.write(Paths.get(CUSTOMER_FILE), customers);
    }

    // Authenticate customer from text file
    public static McnolandCustomer authenticateCustomer(String email, String password) throws IOException {
        if (!Files.exists(Paths.get(CUSTOMER_FILE))) {
            return null;
        }

        for (String line : Files.readAllLines(Paths.get(CUSTOMER_FILE))) {
            String[] parts = line.split("\\" + DELIMITER);
            if (parts.length >= 5 && parts[0].equals(email) && parts[4].equals(password)) {
                return new McnolandCustomer(
                    parts[1],  // name
                    Boolean.parseBoolean(parts[2]),  // isMember
                    parts[0],  // email
                    parts[3]   // phone
                );
            }
        }
        return null;
    }

    // Check if email already exists
    public static boolean emailExists(String email) throws IOException {
        if (!Files.exists(Paths.get(CUSTOMER_FILE))) {
            return false;
        }

        return Files.lines(Paths.get(CUSTOMER_FILE))
                   .anyMatch(line -> line.startsWith(email + DELIMITER));
    }
}