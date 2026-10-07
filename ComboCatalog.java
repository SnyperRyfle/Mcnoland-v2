public class ComboCatalog extends FoodCatalog {
    public ComboCatalog(BurgerCatalog burgerCatalog, DrinkCatalog drinkCatalog, DessertCatalog dessertCatalog) {
        // Safely get items with instanceof checks
        FoodItem burger = burgerCatalog.getAllItems().get(0);
        FoodItem drink = drinkCatalog.getAllItems().get(0);
        FoodItem dessert = dessertCatalog.getAllItems().get(2);

        // Verify types before creating combo
        if (burger instanceof Burger && drink instanceof Drink && dessert instanceof Dessert) {
            addItem(new ComboMeal("Value Meal",
                    (Burger)burger,
                    (Drink)drink,
                    (Dessert)dessert,
                    1.50));
        }

        // Repeat for other combos
        burger = burgerCatalog.getAllItems().get(2);
        drink = drinkCatalog.getAllItems().get(1);
        dessert = dessertCatalog.getAllItems().get(0);

        if (burger instanceof Burger && drink instanceof Drink && dessert instanceof Dessert) {
            addItem(new ComboMeal("Deluxe Meal",
                    (Burger)burger,
                    (Drink)drink,
                    (Dessert)dessert,
                    2.00));
        }
    }
}