package budbud.budgetbuddyui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HomePage {

    @FXML private Button btnSet;
    @FXML private Button btnViewBudget;
    @FXML private Button btnSettings;
    @FXML private Button btnGoals;
    @FXML private Button btnTips;
    @FXML private Button btnLodgeExp;
    @FXML private PieChart pieChart;
    @FXML private AnchorPane lodgePane;
    @FXML private ComboBox<String> monthCB;
    @FXML private ChoiceBox<String> catCB;
    @FXML private TextField txtAmount;
    @FXML private Button btnSubmit;
    @FXML private Label lblUsername;

    @FXML private ImageView logoutImage;

    private final String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"; // Update if needed

    @FXML
    public void initialize() {
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnGoals);
        addHoverEffect(btnTips);

        displayUserInfo();

        logoutImage.setOnMouseClicked(e -> handleImageLogout());

        // Set up the Lodge Expense pane (initially hidden)
        lodgePane.setVisible(false);

        // Set up validators for the amount field
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtAmount.setText(oldValue);
            }
        });

        // Setup months dropdown
        List<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        monthCB.setItems(FXCollections.observableArrayList(months));

        // Set up listeners
        monthCB.setOnAction(event -> {
            updateCategoriesForMonth();
            // Update chart when month is selected
            updateExpenseChart(monthCB.getValue());
        });

        btnSubmit.setOnAction(event -> handleSubmitExpense());
        btnLodgeExp.setOnAction(event -> toggleLodgeExpensePane());
    }

    /**
     * Toggles the visibility of the lodge expense pane
     */
    private void toggleLodgeExpensePane() {
        boolean isVisible = lodgePane.isVisible();
        lodgePane.setVisible(!isVisible);
        btnLodgeExp.setText(isVisible ? "Lodge Expense" : "Close");

        // If opening the pane and a month is already selected, update the chart
        if (!isVisible && monthCB.getValue() != null) {
            updateExpenseChart(monthCB.getValue());
        }
    }

    /**
     * Updates the categories dropdown based on the selected month
     */
    private void updateCategoriesForMonth() {
        String selectedMonth = monthCB.getValue();
        if (selectedMonth == null) return;

        Budget budget = BudgetDataModel.getBudgetByMonth(selectedMonth);
        if (budget == null) {
            showAlert(Alert.AlertType.WARNING, "No Budget",
                    "No budget exists for " + selectedMonth,
                    "Please create a budget for this month first.");
            catCB.getItems().clear();
            return;
        }

        // Populate categories
        catCB.setItems(FXCollections.observableArrayList(budget.getCategories()));

        // Select first category if available
        if (!catCB.getItems().isEmpty()) {
            catCB.setValue(catCB.getItems().get(0));
        }
    }

    /**
     * Handles the expense submission
     */
    private void handleSubmitExpense() {
        String selectedMonth = monthCB.getValue();
        String selectedCategory = catCB.getValue();
        String amountText = txtAmount.getText();

        // Validate inputs
        if (selectedMonth == null || selectedMonth.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Month", "Please select a month.");
            return;
        }

        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Category", "Please select a category.");
            return;
        }

        if (amountText == null || amountText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Amount", "Please enter an amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Amount", "Please enter a positive amount.");
                return;
            }

            // Store the expense
            BudgetDataModel.addExpense(selectedMonth, selectedCategory, amount);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Expense Logged",
                    String.format("Successfully logged RM%.2f for %s in %s",
                            amount, selectedCategory, selectedMonth));

            // Clear the form
            txtAmount.clear();

            // Update the chart
            updateExpenseChart(selectedMonth);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Amount", "Please enter a valid number.");
        }
    }

    /**
     * Updates the pie chart with expense data for the selected month
     */
    private void updateExpenseChart(String month) {
        if (month == null) return;

        // Get expenses for month
        var expenses = BudgetDataModel.getExpensesForMonth(month);

        // Update chart only if there are expenses
        if (!expenses.isEmpty()) {
            // Create data for pie chart
            var pieChartData = FXCollections.observableArrayList();
            expenses.forEach((category, amount) ->
                    pieChartData.add(new PieChart.Data(category + " (RM" + String.format("%.2f", amount) + ")", amount))
            );

            // Get budget for the month if exists
            Budget budget = BudgetDataModel.getBudgetByMonth(month);
            if (budget != null) {
                double totalExpenses = BudgetDataModel.getTotalExpensesForMonth(month);
                double remaining = budget.getAmount() - totalExpenses;
            }
        }   
    }

    /**
     * Shows an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleToReport1() {
        openLinkInChrome("https://www.crowe.com/my/news/key-highlights-of-malaysias-budget-2025");
    }

    @FXML
    private void handleToReport2() {
        openLinkInChrome("https://www.malaymail.com/news/malaysia/2024/10/19/budget-2025-whats-in-it-for-you-and-your-family/154101");
    }

    @FXML
    private void handleToReport3() {
        openLinkInChrome("https://thesun.my/malaysia-news/malaysia-budget-2025-live-updates-FG13159639");
    }

    @FXML
    private void handleToReport4() {
        openLinkInChrome("https://www.aseanbriefing.com/news/malaysias-budget-2025-impact-for-businesses/");
    }

    @FXML
    private void openLinkInChrome(String url) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(chromePath, url);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImageLogout() {
        handleLogout(new ActionEvent(logoutImage, null));
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d;")); //back to original color
    }

    @FXML
    private void handleLogout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout!");
        alert.setHeaderText("You are about to logout");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadScene(event, "LoginPage.fxml", "Budget Buddy - Main Page");
        }
    }

    //Change scenes
    @FXML
    public void toBudgetSet(ActionEvent event){
        loadScene(event, "SetBudget.fxml", "Budget Buddy - Set Budget");
    }
    @FXML
    public void toBudgetViewer(ActionEvent event){
        loadScene(event, "ViewBudget.fxml", "Budget Buddy - Budget Viewer");
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

    //Display user's info from login/signup
    private void displayUserInfo() {
        UserData user = UserData.UserManager.getLoggedInUser();
        if (user != null) {
            lblUsername.setText("Welcome, " + user.getUsername() + "!");
        } else {
            lblUsername.setText("N/A");
        }
    }
}
