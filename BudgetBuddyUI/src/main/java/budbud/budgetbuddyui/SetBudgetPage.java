package budbud.budgetbuddyui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetBudgetPage {
    @FXML private Button btnHome;
    @FXML private Button btnViewBudget;
    @FXML private Button btnGoals;
    @FXML private Button btnTips;
    @FXML private Button btnSettings;
    @FXML private Button btnNewBudget;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane createBudget;
    @FXML private HBox hbox;
    @FXML private CheckBox ess_cat, life_cat, edu_cat, sav_cat, gift_cat;
    @FXML private ProgressBar createProgressBar;
    @FXML private ComboBox<String> monthCB;
    @FXML private TextField txtAmount;

    private static final List<Budget> budgetList = new ArrayList<>(); // Store budgets

    @FXML
    public void initialize() {
        monthCB.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December");

        addHoverEffect(btnHome);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnGoals);
        addHoverEffect(btnTips);
    }

    @FXML
    private void handleCreateBudget(){
        createBudget.setVisible(true);
        scrollPane.setVisible(false);
        btnNewBudget.setDisable(true);

        createBudget.setVisible(true);  // Show the budget creation pane
        scrollPane.setVisible(false);   // Hide the scroll pane

        monthCB.setOnAction(e -> {
            if (monthCB.getValue() != null) {
                updateProgressBar();
            }
        });

        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            checkBox.setOnAction(e -> updateProgressBar());
        }

        // Ensure txtAmount only accepts numeric input and updates progress bar
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAmount.setText(oldValue);
            }
            updateProgressBar();
        });
    }

    @FXML
    private void handleSubmitBudget(){
        createBudget.setVisible(false);
        scrollPane.setVisible(true);
        btnNewBudget.setDisable(false);

        // Get the selected month
        String selectedMonth = monthCB.getValue();

        // Collect selected categories
        List<String> selectedCategories = new ArrayList<>();
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        }

        // Get the budget amount
        double budgetAmount = Double.parseDouble(txtAmount.getText());

        // Create a new Budget object
        Budget newBudget = new Budget(selectedMonth, selectedCategories, budgetAmount);

        // Check if we're editing an existing budget
        if (isEditMode) {
            // Find and replace the existing budget for this month
            for (int i = 0; i < budgetList.size(); i++) {
                if (budgetList.get(i).getMonth().equals(selectedMonth)) {
                    budgetList.set(i, newBudget);
                    break;
                }
            }
        } else {
            // Add the new budget to the list
            budgetList.add(newBudget);
        }

        // For debugging - print the current budgetList contents
        System.out.println("Current Budget List:");
        for (Budget budget : budgetList) {
            System.out.println("Month: " + budget.getMonth() +
                    ", Categories: " + budget.getCategories() +
                    ", Amount: RM " + budget.getAmount());
        }

        //Create Pane
        AnchorPane newPane = new AnchorPane();
        newPane.setPrefSize(200, 250); // Set pane size
        newPane.setStyle("-fx-background-color:  #6d6d6d; -fx-background-radius: 20;");

        // Month Label
        Label monthLabel = new Label(selectedMonth);
        monthLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 20));
        monthLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        monthLabel.setLayoutX(70);
        monthLabel.setLayoutY(15);

        // Categories Selected Label
        Label categoriesLabel = new Label("Categories Selected:");
        categoriesLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        categoriesLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        categoriesLabel.setLayoutX(12);
        categoriesLabel.setLayoutY(55);

        // Dynamic Categories List
        double categoryY = 90;

        for (String category : selectedCategories) {
            Label categoryLabel = new Label(category);
            categoryLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
            categoryLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            categoryLabel.setLayoutX(15);
            categoryLabel.setLayoutY(categoryY);
            categoryY += 40; // Move down for next label

            newPane.getChildren().add(categoryLabel);
        }

        // Amount Label
        Label amountTitleLabel = new Label("Amount Set:");
        amountTitleLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        amountTitleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountTitleLabel.setLayoutX(51);
        amountTitleLabel.setLayoutY(320);

        Label amountLabel = new Label("RM " + txtAmount.getText());
        amountLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        amountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountLabel.setLayoutX(70);
        amountLabel.setLayoutY(340);

        // Create Edit Button
        Button editButton = new Button("Edit");
        editButton.setLayoutX(150);  // Position at bottom left
        editButton.setLayoutY(390);
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Store the current values to use when editing
        String finalSelectedMonth = selectedMonth;
        boolean[] selectedCategoryFlags = new boolean[5];
        for (int i = 0; i < categories.length; i++) {
            selectedCategoryFlags[i] = categories[i].isSelected();
        }
        String amountValue = txtAmount.getText();

        // Add edit functionality
        editButton.setOnAction(e -> {
            // Show the create budget form
            createBudget.setVisible(true);
            scrollPane.setVisible(false);
            btnNewBudget.setDisable(true);

            // Populate form with existing data
            monthCB.setValue(finalSelectedMonth);
            // Disable month selection in edit mode
            monthCB.setDisable(true);

            for (int i = 0; i < categories.length; i++) {
                categories[i].setSelected(selectedCategoryFlags[i]);
            }
            txtAmount.setText(amountValue);
            updateProgressBar();

            // Remove the current pane when editing
            hbox.getChildren().remove(newPane);
            // Update the HBox width
            hbox.setPrefWidth(hbox.getChildren().size() * (200 + 10));
            // Set edit mode
            isEditMode = true;
            if (btnSubmitBudget != null) {
                btnSubmitBudget.setText("Update Budget");
                btnNewBudget.setDisable(false);
            }
        });

        // Add all elements to the Pane
        newPane.getChildren().addAll(monthLabel, categoriesLabel, amountTitleLabel, amountLabel, editButton);
        hbox.getChildren().add(newPane);// Add to HBox
        hbox.setPrefWidth(hbox.getChildren().size() * (200 + 10));// Update width dynamically

        // Reset the form for next use
        resetBudgetForm();
    }

    //Change scenes
    @FXML
    public void toHome(ActionEvent event){
        loadScene(event, "HomePage.fxml", "Budget Buddy - Home Page");
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
        loadScene(event, "GoalsPage.fxml", "Budget Buddy - Goals");
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
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:  #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d;")); //back to original color
    }

    // Helper method to update the progress bar
    private void updateProgressBar() {
        double progress = 0.0;

        if (monthCB.getValue() != null) {
            progress += 0.3;
        }

        int selectedCount = 0;
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                selectedCount++;
            }
        }
        progress += selectedCount * 0.14;
        if (!txtAmount.getText().isEmpty() && txtAmount.getText().matches("\\d+")) {
            progress += 0.3;
        }
        createProgressBar.setProgress(Math.min(progress, 1.0));
    }

    // Fields for the class
    @FXML private Button btnSubmitBudget;
    private boolean isEditMode = false;

     // Resets all form elements in the budget creation pane to their default state
    private void resetBudgetForm() {
        // Reset month dropdown
        monthCB.setValue(null);
        // Re-enable month selection for new budgets
        monthCB.setDisable(false);

        // Uncheck all category checkboxes
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            checkBox.setSelected(false);
        }

        // Clear amount text field
        txtAmount.clear();

        // Reset progress bar
        createProgressBar.setProgress(0.0);

        // Reset edit mode flag and submit button text
        isEditMode = false;
        if (btnSubmitBudget != null) {
            btnSubmitBudget.setText("Submit Budget");
        }
    }
}


