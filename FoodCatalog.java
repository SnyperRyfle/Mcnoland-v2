import java.util.ArrayList;

public abstract class FoodCatalog extends McnolandMain {
    protected ArrayList<FoodItem> foodItems = new ArrayList<>();

    public void addItem(FoodItem item) {
        foodItems.add(item);
    }

    public void displayMenu() {
        System.out.println("\n--- Food Catalog ---");
        for (int i = 0; i < foodItems.size(); i++) {
            System.out.print((i + 1) + ". ");
            System.out.println(foodItems.get(i).getDisplayInfo());
        }
    }

    public FoodItem getItem(int index) {
        if (index >= 0 && index < foodItems.size()) {
            return foodItems.get(index);
        }
        return null;
    }

    public int getSize() {
        return foodItems.size();
    }

    public ArrayList<FoodItem> getAllItems() {
        return foodItems;
    }
}