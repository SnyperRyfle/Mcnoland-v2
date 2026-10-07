import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import javafx.scene.Node;

public class McnolandGUI extends Application {
    // McDonald's color palette
    private static final String MCD_RED = "#DA291C";
    private static final String MCD_YELLOW = "#FFC72C";
    private static final String MCD_DARK_RED = "#A6192E";
    private static final String MCD_WHITE = "#FFFFFF";

    private List<OrderItem> currentOrder = new ArrayList<>();
    private double orderTotal = 0.0;
    private ListView<String> orderItemsListView = new ListView<>();
    private Label totalValueLabel = new Label("$0.00");
    private Label userInfoLabel = new Label("Welcome, Guest");
    private McnolandCustomer currentCustomer;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MCD_RED + ";");

        // 1. Top Header with functional Sign In button
        HBox header = createHeader(primaryStage);
        root.setTop(header);

        // 2. Center Content - Menu Board
        TabPane menuBoard = createMenuBoard();
        root.setCenter(menuBoard);

        // 3. Bottom Order Panel
        VBox orderPanel = createOrderPanel();
        root.setBottom(orderPanel);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Mcnoland® Digital Ordering");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + MCD_RED + ";");

        // Logo placeholder
        Label logo = new Label("M🍔NOLAND");
        logo.setStyle("-fx-text-fill: " + MCD_YELLOW + "; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label headerText = new Label("DIGITAL ORDERING");
        headerText.setStyle("-fx-text-fill: " + MCD_YELLOW + "; -fx-font-size: 24px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Functional Sign In button
        Button authButton = new Button("SIGN IN");
        authButton.setStyle("-fx-background-color: " + MCD_YELLOW + "; -fx-text-fill: black; -fx-font-weight: bold;");
        authButton.setOnAction(e -> {
            if (currentCustomer == null) {
                showLoginDialog(primaryStage);
            } else {
                currentCustomer = null;
                userInfoLabel.setText("Welcome, Guest");
                authButton.setText("SIGN IN");
            }
        });

        header.getChildren().addAll(logo, headerText, spacer, userInfoLabel, authButton);
        return header;
    }

    private void showLoginDialog(Stage owner) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Sign In");
        dialog.setHeaderText("Enter your credentials");
        dialog.getDialogPane().setStyle("-fx-background-color: " + MCD_WHITE + ";");

        // Set up buttons
        ButtonType loginButtonType = new ButtonType("Sign In", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create login form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Add registration link
        Hyperlink registerLink = new Hyperlink("New customer? Register here");
        registerLink.setStyle("-fx-text-fill: " + MCD_DARK_RED + ";");
        grid.add(registerLink, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Enable/disable login button
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Validation
        emailField.textProperty().addListener((obs, old, newVal) -> 
            loginButton.setDisable(newVal.trim().isEmpty() || passwordField.getText().trim().isEmpty()));
        
        passwordField.textProperty().addListener((obs, old, newVal) -> 
            loginButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty()));

        // Handle registration link
        registerLink.setOnAction(e -> {
            showRegistrationDialog(owner);
            dialog.close();
        });

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(emailField.getText(), passwordField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(credentials -> {
            try {
                McnolandCustomer customer = CustomerDataManager.authenticateCustomer(
                    credentials.getKey(), credentials.getValue());
                
                if (customer != null) {
                    currentCustomer = customer;
                    userInfoLabel.setText("Welcome, " + customer.getName());
                    if (customer.isMember()) {
                        userInfoLabel.setStyle("-fx-text-fill: " + MCD_YELLOW + ";");
                    }
                    ((Button)owner.getScene().lookup("#authButton")).setText("SIGN OUT");
                } else {
                    showAlert("Login Failed", "Invalid email or password");
                }
            } catch (IOException ex) {
                showAlert("Error", "Failed to access customer data: " + ex.getMessage());
            }
        });
    }

    private void showRegistrationDialog(Stage owner) {
        Dialog<Pair<McnolandCustomer, String>> dialog = new Dialog<>();
        dialog.setTitle("Register");
        dialog.setHeaderText("Create your account");
        dialog.getDialogPane().setStyle("-fx-background-color: " + MCD_WHITE + ";");

        // Set up buttons
        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        // Create registration form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        CheckBox memberCheckBox = new CheckBox("Become a member (10% discount)");
        memberCheckBox.setStyle("-fx-text-fill: " + MCD_DARK_RED + ";");

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Confirm Password:"), 0, 4);
        grid.add(confirmPasswordField, 1, 4);
        grid.add(memberCheckBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Enable/disable register button
        Node registerButton = dialog.getDialogPane().lookupButton(registerButtonType);
        registerButton.setDisable(true);

        // Validation
        Runnable validateFields = () -> {
            boolean valid = !nameField.getText().trim().isEmpty() &&
                          !emailField.getText().trim().isEmpty() &&
                          !phoneField.getText().trim().isEmpty() &&
                          !passwordField.getText().trim().isEmpty() &&
                          passwordField.getText().equals(confirmPasswordField.getText());
            
            registerButton.setDisable(!valid);
        };

        nameField.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        emailField.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        phoneField.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        passwordField.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        confirmPasswordField.textProperty().addListener((obs, old, newVal) -> validateFields.run());

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                McnolandCustomer customer = new McnolandCustomer(
                    nameField.getText(),
                    memberCheckBox.isSelected(),
                    emailField.getText(),
                    phoneField.getText()
                );
                return new Pair<>(customer, passwordField.getText());
            }
            return null;
        });

        Optional<Pair<McnolandCustomer, String>> result = dialog.showAndWait();

        result.ifPresent(data -> {
            try {
                if (CustomerDataManager.emailExists(data.getKey().getEmail())) {
                    showAlert("Registration Failed", "Email already registered");
                    return;
                }

                CustomerDataManager.saveCustomer(data.getKey(), data.getValue());
                currentCustomer = data.getKey();
                userInfoLabel.setText("Welcome, " + currentCustomer.getName());
                
                if (currentCustomer.isMember()) {
                    userInfoLabel.setStyle("-fx-text-fill: " + MCD_YELLOW + ";");
                    showAlert("Welcome Member", "You now get 10% discount on all orders!");
                }
                
                ((Button)owner.getScene().lookup("#authButton")).setText("SIGN OUT");
            } catch (IOException ex) {
                showAlert("Error", "Failed to register: " + ex.getMessage());
            }
        });
    }

    private TabPane createMenuBoard() {
        TabPane menuBoard = new TabPane();
        menuBoard.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        menuBoard.setStyle("-fx-background-color: " + MCD_WHITE + ";");

        menuBoard.getTabs().addAll(
            createTabWithContent("🍔 BURGERS", createBurgerMenu()),
            createTabWithContent("🍱 MEALS", createMealDeals()),
            createTabWithContent("🍟 SNACKS", createSnacksMenu()),
            createTabWithContent("🥤 DRINKS", createDrinksMenu()),
            createTabWithContent("🍦 DESSERTS", createDessertsMenu())
        );
        return menuBoard;
    }

    private Tab createTabWithContent(String title, VBox content) {
        Tab tab = new Tab(title);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tab.setContent(scroll);
        return tab;
    }

    private VBox createBurgerMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        addMenuItem(menu, "Big Mac®", "Two 100% beef patties, special sauce...", 4.99, "🍔");
        addMenuItem(menu, "Quarter Pounder®", "Quarter pound beef patty with cheese...", 5.49, "🍔");
        addMenuItem(menu, "McDouble®", "Two beef patties, cheese, pickles...", 2.99, "🍔");
        return menu;
    }

    private VBox createMealDeals() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        addMealItem(menu, "Big Mac® Meal", "Big Mac, Medium Fries, Medium Drink", 7.99);
        addMealItem(menu, "Quarter Pounder® Meal", "Quarter Pounder, Medium Fries, Medium Drink", 8.49);
        addMealItem(menu, "10pc McNuggets® Meal", "10pc McNuggets, Medium Fries, Medium Drink", 8.99);
        return menu;
    }

    private VBox createSnacksMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        addMenuItem(menu, "French Fries", "World Famous Fries®", 2.49, "🍟");
        addMenuItem(menu, "Chicken McNuggets®", "4 piece", 3.49, "🍗");
        addMenuItem(menu, "Apple Slices", "Bag", 1.29, "🍎");
        return menu;
    }

    private VBox createDrinksMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        addMenuItem(menu, "Coca-Cola®", "Medium", 1.99, "🥤");
        addMenuItem(menu, "Sprite®", "Medium", 1.99, "🥤");
        addMenuItem(menu, "Iced Coffee", "Medium", 2.49, "☕");
        return menu;
    }

    private VBox createDessertsMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        addMenuItem(menu, "McFlurry®", "Oreo® or M&M's®", 3.29, "🍦");
        addMenuItem(menu, "Apple Pie", "Baked apple pie", 1.29, "🥧");
        addMenuItem(menu, "Sundae", "Hot fudge or caramel", 2.49, "🍨");
        return menu;
    }

    private void addMenuItem(VBox container, String name, String description, double price, String emoji) {
        HBox itemBox = new HBox(15);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-background-color: #F8F8F8; -fx-background-radius: 5;");

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 24px;");

        VBox textBox = new VBox(5);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");

        textBox.getChildren().addAll(nameLabel, descLabel);

        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label priceLabel = new Label(String.format("$%.2f", price));
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: " + MCD_DARK_RED + ";");

        Spinner<Integer> quantitySpinner = new Spinner<>(0, 10, 0);
        quantitySpinner.setPrefWidth(60);

        Button addButton = new Button("ADD");
        addButton.setStyle("-fx-background-color: " + MCD_YELLOW + "; -fx-text-fill: black; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            int quantity = quantitySpinner.getValue();
            if (quantity > 0) {
                addToOrder(name, price, quantity);
                quantitySpinner.getValueFactory().setValue(0);
            }
        });

        rightBox.getChildren().addAll(priceLabel, quantitySpinner, addButton);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        itemBox.getChildren().addAll(emojiLabel, textBox, spacer, rightBox);
        container.getChildren().add(itemBox);
    }

    private void addMealItem(VBox container, String name, String description, double price) {
        HBox itemBox = new HBox(15);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-background-color: #F8F8F8; -fx-background-radius: 5;");

        Label mealIcon = new Label("🍱");
        mealIcon.setStyle("-fx-font-size: 24px;");

        VBox textBox = new VBox(5);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: " + MCD_DARK_RED + ";");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");

        textBox.getChildren().addAll(nameLabel, descLabel);

        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label priceLabel = new Label(String.format("$%.2f", price));
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: " + MCD_DARK_RED + ";");

        Button addButton = new Button("ADD MEAL");
        addButton.setStyle("-fx-background-color: " + MCD_RED + "; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> addToOrder(name, price, 1));

        rightBox.getChildren().addAll(priceLabel, addButton);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        itemBox.getChildren().addAll(mealIcon, textBox, spacer, rightBox);
        container.getChildren().add(itemBox);
    }

    private VBox createOrderPanel() {
        VBox orderPanel = new VBox(10);
        orderPanel.setPadding(new Insets(15));
        orderPanel.setStyle("-fx-background-color: " + MCD_YELLOW + ";");

        Label orderTitle = new Label("YOUR ORDER");
        orderTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        orderItemsListView.setPrefHeight(150);
        orderItemsListView.setStyle("-fx-control-inner-background: " + MCD_WHITE + ";");

        HBox totalBox = new HBox();
        Label totalLabel = new Label("TOTAL:");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        totalValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MCD_DARK_RED + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        totalBox.getChildren().addAll(totalLabel, spacer, totalValueLabel);

        Button checkoutButton = new Button("CHECKOUT NOW");
        checkoutButton.setStyle("-fx-background-color: " + MCD_RED + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        checkoutButton.setMaxWidth(Double.MAX_VALUE);
        checkoutButton.setOnAction(e -> handleCheckout());

        orderPanel.getChildren().addAll(orderTitle, orderItemsListView, totalBox, checkoutButton);
        return orderPanel;
    }

    private void addToOrder(String itemName, double price, int quantity) {
        currentOrder.add(new OrderItem(new FoodItem(itemName, price), quantity));
        double itemTotal = price * quantity;
        orderTotal += itemTotal;
        
        // Apply 10% discount for members
        if (currentCustomer != null && currentCustomer.isMember()) {
            itemTotal *= 0.9;
        }
        
        orderItemsListView.getItems().add(String.format("%d x %s - $%.2f", quantity, itemName, itemTotal));
        updateTotal();
    }

    private void updateTotal() {
        double total = currentOrder.stream()
            .mapToDouble(item -> item.item.price * item.quantity * 
                (currentCustomer != null && currentCustomer.isMember() ? 0.9 : 1.0))
            .sum();
        
        orderTotal = total;
        totalValueLabel.setText(String.format("$%.2f", orderTotal));
    }

    private void handleCheckout() {
        if (currentOrder.isEmpty()) {
            showAlert("Empty Order", "Please add items before checkout");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText("Review Your Order");
        alert.getDialogPane().setStyle("-fx-background-color: " + MCD_WHITE + ";");

        VBox content = new VBox(10);
        
        Label summaryLabel = new Label("Order Summary:");
        summaryLabel.setStyle("-fx-font-weight: bold;");
        
        ListView<String> summaryList = new ListView<>();
        summaryList.setItems(orderItemsListView.getItems());
        summaryList.setPrefHeight(150);
        
        Label totalLabel = new Label("Total: " + totalValueLabel.getText());
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        if (currentCustomer != null && currentCustomer.isMember()) {
            Label discountLabel = new Label("(Includes 10% member discount)");
            discountLabel.setStyle("-fx-text-fill: " + MCD_DARK_RED + ";");
            content.getChildren().add(discountLabel);
        }
        
        content.getChildren().addAll(summaryLabel, summaryList, totalLabel);
        alert.getDialogPane().setContent(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Save order to customer history would go here
            currentOrder.clear();
            orderItemsListView.getItems().clear();
            orderTotal = 0.0;
            totalValueLabel.setText("$0.00");
            
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Order Complete");
            success.setHeaderText("Thank you for your order!");
            success.setContentText("Your order number is #" + (new Random().nextInt(9000) + 1000));
            success.show();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Helper classes
    private static class FoodItem {
        String name;
        double price;

        public FoodItem(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    private static class OrderItem {
        FoodItem item;
        int quantity;

        public OrderItem(FoodItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }
}