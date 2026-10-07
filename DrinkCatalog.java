import java.util.List;
import java.util.stream.Collectors;

public class DrinkCatalog extends FoodCatalog {
    public DrinkCatalog() {
        for (DrinkType type : DrinkType.values()) {
            addItem(new Drink(type));
        }
    }

    // Add this new method
    public List<Drink> getAllDrinks() {
        return foodItems.stream()
                .filter(item -> item instanceof Drink)
                .map(item -> (Drink) item)
                .collect(Collectors.toList());
    }
}