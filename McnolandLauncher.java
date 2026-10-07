import java.util.Scanner;

public class McnolandLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BurgerCatalog burgerCatalog = new BurgerCatalog();
        DrinkCatalog drinkCatalog = new DrinkCatalog();
        OrderCatalog orderCatalog = new OrderCatalog();
        DessertCatalog dessertCatalog = new DessertCatalog();

        System.out.println("=== Welcome to Mcnoland ===\n");

        while (true) {
            System.out.println("Select category to order:");
            System.out.println("1. Burger\n2. Drink\n3. Dessert\n4. Pay\n5. Exit/Cancel");

            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n-- Burger Menu --");
                    burgerCatalog.displayMenu();
                    System.out.print("Select burger number: ");
                    int burgerIndex = scanner.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int burgerQty = scanner.nextInt();
                    scanner.nextLine();
                    FoodItem burger = burgerCatalog.getItem(burgerIndex);
                    if (burger != null) {
                        orderCatalog.addOrder(burger, burgerQty);
                    } else {
                        System.out.println("Invalid burger selection.");
                    }
                    break;
                case 2:
                    System.out.println("\n-- Drink Menu --");
                    drinkCatalog.displayMenu();
                    System.out.print("Select drink number: ");
                    int drinkIndex = scanner.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int drinkQty = scanner.nextInt();
                    scanner.nextLine();
                    FoodItem drink = drinkCatalog.getItem(drinkIndex);
                    if (drink != null) {
                        orderCatalog.addOrder(drink, drinkQty);
                    } else {
                        System.out.println("Invalid drink selection.");
                    }
                    break;


                case 3:
                    System.out.println("\n-- Dessert Menu --");
                    dessertCatalog.displayMenu();
                    System.out.print("Select dessert number: ");
                    int dessertIndex = scanner.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int dessertQty = scanner.nextInt();
                    scanner.nextLine();
                    FoodItem dessert = dessertCatalog.getItem(dessertIndex);
                    if (dessert != null) {
                        orderCatalog.addOrder(dessert, dessertQty);
                    } else {
                        System.out.println("Invalid dessert selection.");
                    }
                    break;
                case 4: // Pay
                    orderCatalog.displayOrder();// This will auto-clear and show total
                    System.out.println("Thank you for your payment!");
                break;
                case 5:
                    System.out.println("If you didn't paid, it is considered as canceled order");
                    System.out.println("Thank you for visiting Mcnoland!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
