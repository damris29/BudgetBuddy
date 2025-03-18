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

        //Create Pane
        AnchorPane newPane = new AnchorPane();
        newPane.setPrefSize(200, 250); // Set pane size
        newPane.setStyle("-fx-background-color:  #6d6d6d; -fx-background-radius: 20;");

        // Month Label
        Label monthLabel = new Label(monthCB.getValue()); // Get selected month
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
        CheckBox[] categories = {ess_cat, life_cat, edu_cat, sav_cat, gift_cat};
        double categoryY = 90;

        for (CheckBox checkBox : categories) {
            if (checkBox.isSelected()) {
                Label categoryLabel = new Label(checkBox.getText());
                categoryLabel.setFont(Font.font("Cascadia Code", FontWeight.BOLD, 15));
                categoryLabel.setTextFill(javafx.scene.paint.Color.WHITE);
                categoryLabel.setLayoutX(15);
                categoryLabel.setLayoutY(categoryY);
                categoryY += 40; // Move down for next label

                newPane.getChildren().add(categoryLabel);
            }
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

        // Add all elements to the Pane
        newPane.getChildren().addAll(monthLabel, categoriesLabel, amountTitleLabel, amountLabel);

        hbox.getChildren().add(newPane);// Add to HBox
        hbox.setPrefWidth(hbox.getChildren().size() * (200 + 10));// Update width dynamically
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
}


