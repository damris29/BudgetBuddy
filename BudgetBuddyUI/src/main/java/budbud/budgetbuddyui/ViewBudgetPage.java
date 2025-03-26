package budbud.budgetbuddyui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBudgetPage {
    @FXML
    private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnSettings;
    @FXML private Button btnGoals;
    @FXML private Button btnTips;
    @FXML private ComboBox<String> monthCB;
    @FXML private AnchorPane listPane;
    @FXML private PieChart pieExpenses;
    @FXML private Button btnRemove;

    private List<Budget> budgets = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    @FXML
    public void initialize() {
        addHoverEffect(btnHome);
        addHoverEffect(btnSet);
        addHoverEffect(btnSettings);
        addHoverEffect(btnGoals);
        addHoverEffect(btnTips);

        // Initialize months in combo box
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthCB.setItems(months);

        // Set up listener for month selection
        monthCB.setOnAction(event -> displayBudgetForSelectedMonth());

        // Load sample data if no budgets exist
        if (BudgetDataModel.getBudgetList().isEmpty()) {
            loadSampleData();
        }

        // Set up remove button action
        btnRemove.setOnAction(event -> {
            String selectedMonth = monthCB.getValue();
            if (selectedMonth != null) {
                Budget budget = findBudgetForMonth(selectedMonth);
                if (budget != null) {
                    deleteBudget(budget);
                }
            }
        });
    }

    /**
     * Displays budget information for the selected month
     */
    private void displayBudgetForSelectedMonth() {
        String selectedMonth = monthCB.getValue();
        if (selectedMonth == null) return;

        // Clear previous data
        listPane.getChildren().clear();
        pieExpenses.getData().clear();

        // Find budget for selected month
        Budget selectedBudget = findBudgetForMonth(selectedMonth);

        if (selectedBudget == null) {
            showNoBudgetMessage();
            return;
        }

        // Display budget categories and amount
        displayBudgetDetails(selectedBudget);

        // Display spending chart
        displaySpendingChart(selectedMonth, selectedBudget);
    }

    /**
     * Finds a budget for the specified month
     */
    // Update these methods to use the shared model
    private Budget findBudgetForMonth(String month) {
        for (Budget budget : BudgetDataModel.getBudgetList()) {
            if (budget.getMonth().equals(month)) {
                return budget;
            }
        }
        return null;
    }

    /**
     * Displays a message when no budget is found for the selected month
     */
    private void showNoBudgetMessage() {
        Label noDataLabel = new Label("No budget found for this month");
        noDataLabel.setPadding(new Insets(10));
        listPane.getChildren().add(noDataLabel);
    }

    /**
     * Displays the budget details in the list pane
     */
    private void displayBudgetDetails(Budget budget) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        Label titleLabel = new Label("Budget for " + budget.getMonth());
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        container.getChildren().add(titleLabel);

        Label amountLabel = new Label("Total Budget: $" + String.format("%.2f", budget.getAmount()));
        amountLabel.setTextFill(Color.WHITE);
        amountLabel.setStyle("-fx-font-size: 14px;");
        container.getChildren().add(amountLabel);

        // Calculate total spending
        double totalSpent = calculateTotalSpending(budget.getMonth());
        double remaining = budget.getAmount() - totalSpent;

        Label spentLabel = new Label("Total Spent: $" + String.format("%.2f", totalSpent));
        spentLabel.setTextFill(Color.WHITE);
        container.getChildren().add(spentLabel);

        Label remainingLabel = new Label("Remaining Balance: $" + String.format("%.2f", remaining));
        remainingLabel.setTextFill(Color.WHITE);
        remainingLabel.setStyle("-fx-font-weight: bold;");
        if (remaining < 0) {
            remainingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }
        container.getChildren().add(remainingLabel);

        // Separator
        Label separator = new Label("Categories:");
        separator.setTextFill(Color.WHITE);
        separator.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");
        container.getChildren().add(separator);

        // Display categories
        for (String category : budget.getCategories()) {
            Label categoryLabel = new Label(category);
            container.getChildren().add(categoryLabel);
        }

        listPane.getChildren().add(container);
    }

    /**
     * Displays the spending chart for the selected month
     */
    private void displaySpendingChart(String month, Budget budget) {
        // Get spending data by category
        Map<String, Double> categorySpending = calculateCategorySpending(month);

        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " ($" + String.format("%.2f", entry.getValue()) + ")",
                    entry.getValue()));
        }

        pieExpenses.setData(pieChartData);
        pieExpenses.setTitle("Spending by Category in " + month);
    }

    /**
     * Calculates total spending for a given month
     */
    private double calculateTotalSpending(String month) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getMonth().equals(month)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Calculates spending by category for a given month
     */
    private Map<String, Double> calculateCategorySpending(String month) {
        Map<String, Double> categorySpending = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getMonth().equals(month)) {
                String category = transaction.getCategory();
                double amount = transaction.getAmount();

                categorySpending.put(category,
                        categorySpending.getOrDefault(category, 0.0) + amount);
            }
        }

        return categorySpending;
    }

    /**
     * Deletes an entire budget
     */
    private void deleteBudget(Budget budget) {
        BudgetDataModel.removeBudget(budget);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Budget Deleted");
        alert.setHeaderText(null);
        alert.setContentText("Budget for " + budget.getMonth() + " has been deleted.");
        alert.showAndWait();

        // Clear displays
        listPane.getChildren().clear();
        pieExpenses.getData().clear();
        monthCB.setValue(null);
    }

    /**
     * Loads sample data for demonstration
     */
    private void loadSampleData() {
        // Sample budgets
        List<String> janCategories = new ArrayList<>();
        janCategories.add("Groceries");
        janCategories.add("Utilities");
        janCategories.add("Entertainment");
        budgets.add(new Budget("January", janCategories, 1500.0));

        List<String> febCategories = new ArrayList<>();
        febCategories.add("Groceries");
        febCategories.add("Rent");
        febCategories.add("Transportation");
        budgets.add(new Budget("February", febCategories, 1800.0));

        // Sample transactions
        transactions.add(new Transaction("January", "Groceries", 350.0));
        transactions.add(new Transaction("January", "Utilities", 120.0));
        transactions.add(new Transaction("January", "Entertainment", 200.0));

        transactions.add(new Transaction("February", "Groceries", 325.0));
        transactions.add(new Transaction("February", "Rent", 850.0));
        transactions.add(new Transaction("February", "Transportation", 150.0));
    }


    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #6d6d6d;")); //back to original color
    }

    @FXML
    public void toHome (ActionEvent event){
        loadScene(event, "HomePage.fxml", "Budget Buddy - Set Budget");
    }
    @FXML
    public void toSetBudget(ActionEvent event){
        loadScene(event, "SetBudget.fxml", "Budget Buddy - Budget Viewer");
    }
    @FXML
    public void toSetting(ActionEvent event){
        loadScene(event, "SetProfile.fxml", "Budget Buddy - Settings");
    }
    @FXML
    public void toGoals(ActionEvent event){
        loadScene(event, "GoalsPage.fxml", "Budget Buddy - Saving Goals");
    }
    @FXML
    public void toTips(ActionEvent event){
        loadScene(event, "FinTipsPage.fxml", "Budget Buddy - Financial Tips");
    }

    private void loadScene(ActionEvent e, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (IOException ex) {
            System.out.println("Error loading " + fxmlFile + ": " + ex.getMessage());
        }
    }

    // Inner class for transaction data
    private static class Transaction {
        private final String month;
        private final String category;
        private final double amount;

        public Transaction(String month, String category, double amount) {
            this.month = month;
            this.category = category;
            this.amount = amount;
        }

        public String getMonth() {
            return month;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }
    }
}
