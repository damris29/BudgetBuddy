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
import java.util.Map;

public class SetBudgetPage {
    @FXML private Button btnHome, btnViewBudget, btnGoals, btnTips, btnSettings, btnNewBudget, btnSubmitBudget;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane createBudget;
    @FXML private HBox hbox;
    @FXML private CheckBox ess_cat, life_cat, edu_cat, sav_cat, gift_cat;
    @FXML private ComboBox<String> monthCB;
    @FXML private TextField txtAmount;

    private boolean isEditMode = false;
    private Budget editingBudget = null;

    @FXML
    public void initialize() {
        monthCB.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December");

        addHoverEffect(btnHome);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnGoals);
        addHoverEffect(btnTips);

        updateBudgetDisplay(); // Load existing budgets
    }

    @FXML
    private void handleCreateBudget() {
        createBudget.setVisible(true);
        scrollPane.setVisible(false);
        btnNewBudget.setDisable(true);

        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAmount.setText(oldValue);
            }
        });
    }

    @FXML
    private void handleSubmitBudget() {
        createBudget.setVisible(false);
        scrollPane.setVisible(true);
        btnNewBudget.setDisable(false);

        // Validate input
        if (!validateBudgetInput()) {
            return;
        }

        String selectedMonth = monthCB.getValue();
        List<String> selectedCategories = new ArrayList<>();
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        }

        double budgetAmount = Double.parseDouble(txtAmount.getText());
        Budget newBudget = new Budget(selectedMonth, selectedCategories, budgetAmount);

        // Add to BudgetDataModel
        BudgetDataModel.addBudget(newBudget);

        updateBudgetDisplay();
        resetBudgetForm();
    }

    private boolean validateBudgetInput() {
        String selectedMonth = monthCB.getValue();
        if (selectedMonth == null || selectedMonth.isEmpty()) {
            showAlert("Please select a month.");
            return false;
        }

        boolean hasCategory = false;
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                hasCategory = true;
                break;
            }
        }

        if (!hasCategory) {
            showAlert("Please select at least one category.");
            return false;
        }

        if (txtAmount.getText().isEmpty() || !txtAmount.getText().matches("\\d+(\\.\\d{1,2})?")) {
            showAlert("Please enter a valid amount.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Validation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateBudgetDisplay() {
        hbox.getChildren().clear();
        for (Budget budget : BudgetDataModel.getBudgetList()) {
            hbox.getChildren().add(createBudgetPane(budget));
        }
        hbox.setPrefWidth(hbox.getChildren().size() * (200 + 10));
    }

    private AnchorPane createBudgetPane(Budget budget) {
        AnchorPane newPane = new AnchorPane();
        newPane.setPrefSize(200, 250);
        newPane.setStyle("-fx-background-color: #6d6d6d; -fx-background-radius: 20;");

        Label monthLabel = new Label(budget.getMonth());
        monthLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 20));
        monthLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        monthLabel.setLayoutX(70);
        monthLabel.setLayoutY(15);

        Label categoriesLabel = new Label("Categories:");
        categoriesLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        categoriesLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        categoriesLabel.setLayoutX(15);
        categoriesLabel.setLayoutY(55);

        // Display categories
        double categoryY = 90;
        for (String category : budget.getCategories()) {
            Label categoryLabel = new Label(category);
            categoryLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
            categoryLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            categoryLabel.setLayoutX(15);
            categoryLabel.setLayoutY(categoryY);
            categoryY += 40;
            newPane.getChildren().add(categoryLabel);
        }

        // Display budget amount
        Label amountLabel = new Label("Budget: RM " + budget.getAmount());
        amountLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        amountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountLabel.setLayoutX(15);
        amountLabel.setLayoutY(categoryY);
        categoryY += 30;

        // Buttons
        Button editButton = new Button("Edit");
        editButton.setLayoutX(30);
        editButton.setLayoutY(350);
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        editButton.setOnAction(e -> editBudget(budget));

        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(110);
        deleteButton.setLayoutY(350);
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> {
            BudgetDataModel.removeBudget(budget);
            updateBudgetDisplay();
        });

        newPane.getChildren().addAll(monthLabel, categoriesLabel, amountLabel, editButton, deleteButton);
        return newPane;
    }

    private void editBudget(Budget budget) {
        createBudget.setVisible(true);
        scrollPane.setVisible(false);
        btnNewBudget.setDisable(true);

        monthCB.setValue(budget.getMonth());
        monthCB.setDisable(true);  // Month should not be changeable during edit
        txtAmount.setText(String.valueOf(budget.getAmount()));

        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            checkBox.setSelected(budget.getCategories().contains(checkBox.getText()));
        }

        isEditMode = true;
        editingBudget = budget;
    }

    private void resetBudgetForm() {
        monthCB.setValue(null);
        monthCB.setDisable(false);
        txtAmount.clear();
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            checkBox.setSelected(false);
        }
        isEditMode = false;
        editingBudget = null;
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555555;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent;"));
    }

    @FXML
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toHome(ActionEvent event) { switchScene(event, "HomePage.fxml"); }
    @FXML
    private void toBudgetViewer(ActionEvent event) { switchScene(event, "ViewBudget.fxml"); }
    @FXML
    private void toGoals(ActionEvent event) { switchScene(event, "Goals.fxml"); }
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
    @FXML
    private void toSetting(ActionEvent event) { switchScene(event, "Settings.fxml"); }
}
