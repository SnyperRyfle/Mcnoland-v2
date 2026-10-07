import java.util.List;
import java.util.stream.Collectors;

public class DessertCatalog extends FoodCatalog {
    public DessertCatalog() {
        for (DessertType type : DessertType.values()) {
            addItem(new Dessert(type));
        }
    }

    // Add this new method
    public List<Dessert> getAllDesserts() {
        return foodItems.stream()
                .filter(item -> item instanceof Dessert)
                .map(item -> (Dessert) item)
                .collect(Collectors.toList());
    }
}