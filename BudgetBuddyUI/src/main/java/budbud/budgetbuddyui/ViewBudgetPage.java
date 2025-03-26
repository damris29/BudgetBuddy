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
import java.util.Map;

public class ViewBudgetPage {
    @FXML private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnSettings;
    @FXML private Button btnGoals;
    @FXML private Button btnTips;
    @FXML private ComboBox<String> monthCB;
    @FXML private AnchorPane listPane;
    @FXML private PieChart pieExpenses;
    @FXML private Button btnRemove;

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

        // Set up remove button action
        btnRemove.setOnAction(event -> {
            String selectedMonth = monthCB.getValue();
            if (selectedMonth != null) {
                Budget budget = BudgetDataModel.getBudgetByMonth(selectedMonth);
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
        Budget selectedBudget = BudgetDataModel.getBudgetByMonth(selectedMonth);

        if (selectedBudget == null) {
            showNoBudgetMessage();
            return;
        }

        // Display budget categories and amount
        displayBudgetDetails(selectedBudget);

        // Display spending chart
        displaySpendingChart(selectedMonth);
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
        double totalSpent = BudgetDataModel.getTotalExpensesForMonth(budget.getMonth());
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

        // Display categories with their spending
        for (String category : budget.getCategories()) {
            double categorySpent = BudgetDataModel.getExpenseForMonthAndCategory(budget.getMonth(), category);
            Label categoryLabel = new Label(category + ": $" + String.format("%.2f", categorySpent));
            categoryLabel.setTextFill(Color.WHITE);
            container.getChildren().add(categoryLabel);
        }

        listPane.getChildren().add(container);
    }

    /**
     * Displays the spending chart for the selected month
     */
    private void displaySpendingChart(String month) {
        // Get spending data by category
        Map<String, Double> categorySpending = BudgetDataModel.getExpensesForMonth(month);

        // Check if there are any expenses
        if (categorySpending.isEmpty()) {
            pieExpenses.setTitle("No expenses recorded for " + month);
            return;
        }

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

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #6d6d6d;")); //back to original color
    }

    @FXML
    public void toHome(ActionEvent event){
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
    public void toTips(ActionEvent event) {
        // Instead of loading the scene, show an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText("Financial Tips");
        alert.setContentText("This feature is coming soon! Stay tuned for financial tips and advice in our next update.");

        // Show the dialog and wait for user response
        alert.showAndWait();

        // For debugging purposes, you can log this interaction
        System.out.println("User attempted to access upcoming Financial Tips feature");
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
}
