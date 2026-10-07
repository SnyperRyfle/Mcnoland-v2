public class Dessert extends FoodItem {
    private DessertType type;

public Dessert(DessertType type) {
    super(type.getName(), type.getPrice());
    this.type = type;
}

public DessertType getType() {
    return type;
}

@Override
public String getDisplayInfo() {
    return "🍰 " + name + " - $" + price;
}
}
