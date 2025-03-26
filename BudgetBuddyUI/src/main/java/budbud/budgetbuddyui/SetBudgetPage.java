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
    @FXML private Button btnHome, btnViewBudget, btnGoals, btnTips, btnSettings, btnNewBudget, btnSubmitBudget;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane createBudget;
    @FXML private HBox hbox;
    @FXML private CheckBox ess_cat, life_cat, edu_cat, sav_cat, gift_cat;
    @FXML private ComboBox<String> monthCB;
    @FXML private TextField txtAmount;

    // Static List to Retain Budget Data Across Scene Changes
    private static final List<Budget> budgetList = new ArrayList<>();
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
        BudgetDataModel.addBudget(newBudget);

        if (isEditMode) {
            for (int i = 0; i < budgetList.size(); i++) {
                if (budgetList.get(i).getMonth().equals(selectedMonth)) {
                    budgetList.set(i, newBudget);
                    break;
                }
            }
        } else {
            budgetList.add(newBudget);
        }

        updateBudgetDisplay();
        resetBudgetForm();
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

        Label amountLabel = new Label("Amount: RM " + budget.getAmount());
        amountLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
        amountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountLabel.setLayoutX(30);
        amountLabel.setLayoutY(320);

        Button editButton = new Button("Edit");
        editButton.setLayoutX(150);
        editButton.setLayoutY(390);
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        editButton.setOnAction(e -> editBudget(budget));

        newPane.getChildren().addAll(monthLabel, categoriesLabel, amountLabel, editButton);
        return newPane;
    }

    private void editBudget(Budget budget) {
        createBudget.setVisible(true);
        scrollPane.setVisible(false);
        btnNewBudget.setDisable(true);

        monthCB.setValue(budget.getMonth());
        monthCB.setDisable(true);
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
        for (CheckBox checkBox : new CheckBox[]{ess_cat, life_cat, edu_cat, sav_cat, gift_cat}) {
            checkBox.setSelected(false);
        }
        isEditMode = false;
    }

    // Method to collect and store budget data
    private void storeBudget() {
        String selectedMonth = (monthCB.getValue() != null) ? monthCB.getValue() : "";
        List<String> selectedCategories = new ArrayList<>();
        double enteredAmount = 0.0;

        // Ensure a month is selected
        if (selectedMonth.isEmpty()) {
            System.out.println("Please select a month.");
            return;
        }

        // Collect selected categories
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        }

        // Ensure at least one category is selected
        if (selectedCategories.isEmpty()) {
            System.out.println("Please select at least one category.");
            return;
        }

        // Validate and parse amount
        if (!txtAmount.getText().isEmpty() && txtAmount.getText().matches("\\d+(\\.\\d{1,2})?")) {
            enteredAmount = Double.parseDouble(txtAmount.getText());
        } else {
            System.out.println("Please enter a valid amount.");
            return;
        }

        // Store the Budget object
        Budget budget = new Budget(selectedMonth, selectedCategories, enteredAmount);
        budgetList.add(budget);

        System.out.println("Budget stored: " + budget.getMonth() + " | " + budget.getCategories() + " | $" + budget.getAmount());
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
    private void toTips(ActionEvent event) { switchScene(event, "Tips.fxml"); }
    @FXML
    private void toSetting(ActionEvent event) { switchScene(event, "Settings.fxml"); }
}
