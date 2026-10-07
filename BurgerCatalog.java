import java.util.List;
import java.util.stream.Collectors;

public class BurgerCatalog extends FoodCatalog {
    public BurgerCatalog() {
        for (BurgerType type : BurgerType.values()) {
            addItem(new Burger(type));
        }
    }

    // Add this new method
    public List<Burger> getAllBurgers() {
        return foodItems.stream()
                .filter(item -> item instanceof Burger)
                .map(item -> (Burger) item)
                .collect(Collectors.toList());
    }
}