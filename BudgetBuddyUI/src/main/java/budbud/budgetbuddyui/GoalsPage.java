package budbud.budgetbuddyui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class GoalsPage {
    @FXML private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnViewBudget;
    @FXML private Button btnTips;
    @FXML private Button btnSettings;
    @FXML private Button btnCreateGoal;
    @FXML private ScrollPane GoalsPane;
    @FXML private AnchorPane createPane;
    @FXML private AnchorPane addAmountPane;
    @FXML private TextField txtTitle, txtAmount;
    @FXML private Button btnSubmit;
    @FXML private Label lblTitle, lblTarget;
    @FXML private ProgressBar progressAmount;
    @FXML private VBox vbox;
    @FXML private Button btnAddSubmit;
    @FXML private TextField txtAmountAdd;

    @FXML
    public void initialize() {
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnHome);
        addHoverEffect(btnTips);
    }

    @FXML
    public void handleCreateGoal(){
        int entamt = 0;
        GoalsPane.setVisible(false);
        createPane.setVisible(true);
        btnCreateGoal.setDisable(true);

        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            lblTitle.setText("Title: " + newValue);
        });
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAmount.setText(oldValue);
            }
            lblTarget.setText("Target: RM" + entamt + " out of RM" + newValue);
        });
    }

    //When user is finish creating goal, it creates a pane on the scroll pane when clicked the submit button
    @FXML
    private void handleSubmit(){
        createPane.setVisible(false);
        GoalsPane.setVisible(true);
        btnCreateGoal.setDisable(false);

        //Create pane
        AnchorPane newPane = new AnchorPane();
        newPane.setPrefSize(630, 100); // Set pane size
        newPane.setStyle("-fx-background-color:  #6d6d6d; -fx-background-radius: 20;");

        //Title Label
        Label title = new Label(lblTitle.getText());
        title.setFont(Font.font("Lucida Console", 15));
        title.setTextFill(Color.WHITE);
        title.setLayoutX(14);
        title.setLayoutY(14);

        //Title Label
        Label target = new Label(lblTarget.getText());
        target.setFont(Font.font("Lucida Console", 15));
        target.setTextFill(Color.WHITE);
        target.setLayoutX(14);
        target.setLayoutY(42);

        //Progress Bar
        ProgressBar goalProg = new ProgressBar();
        goalProg.setProgress(0);
        goalProg.setPrefSize(535, 18);
        goalProg.setLayoutX(14);
        goalProg.setLayoutY(70);

        //Add amount button
        Button addAmt = new Button("Add");
        addAmt.setLayoutX(580);
        addAmt.setLayoutY(10);
        addAmt.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        //Add amount button
        Button editGoal = new Button("Edit");
        editGoal.setLayoutX(580);
        editGoal.setLayoutY(65);
        editGoal.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Event for the Add Amount button
        addAmt.setOnAction(event -> handleAddAmount(goalProg, target));
        // Add Edit button event
        editGoal.setOnAction(event -> handleEditGoal(title, target, goalProg));

        newPane.getChildren().addAll(title, target, goalProg, addAmt, editGoal);
        vbox.getChildren().add(newPane);// Add to VBox
        vbox.setPrefHeight(vbox.getChildren().size() * (100 + 10));// Update height dynamically

    }

    // Store accumulated amounts for each goal
    private double currentSavedAmount = 0;

    @FXML
    private void handleAddAmount(ProgressBar goalProg, Label target) {
        GoalsPane.setDisable(true);
        addAmountPane.setVisible(true);

        btnAddSubmit.setOnAction(event -> {
            try {
                double targetAmount = Double.parseDouble(txtAmount.getText()); // Goal amount
                double addAmount = Double.parseDouble(txtAmountAdd.getText()); // Amount to add

                currentSavedAmount += addAmount;

                // Ensure progress doesn't exceed 100%
                double progress = Math.min(currentSavedAmount / targetAmount, 1.0);
                goalProg.setProgress(progress);

                target.setText("Target: RM" + currentSavedAmount + " out of RM" + targetAmount);
                txtAmountAdd.clear();
                GoalsPane.setDisable(false);
                addAmountPane.setVisible(false);

            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        });
    }

    //User can edit the title and amount of a goal after creating it
    // Store references to the goal being edited
    private Label currentTitleLabel;
    private Label currentTargetLabel;
    private ProgressBar currentProgressBar;

    @FXML
    private void handleEditGoal(Label title, Label target, ProgressBar goalProg) {
        GoalsPane.setDisable(true);
        addAmountPane.setVisible(false);
        createPane.setVisible(true); // Reuse createPane for editing

        // Store current goal details
        currentTitleLabel = title;
        currentTargetLabel = target;
        currentProgressBar = goalProg;

        // Populate existing values
        txtTitle.setText(title.getText().replace("Title: ", ""));
        txtAmount.setText(target.getText().split("out of RM")[1]); // Extract target amount

        // Change Submit button action for editing
        btnSubmit.setOnAction(event -> {
            try {
                String newTitle = txtTitle.getText();
                double newTargetAmount = Double.parseDouble(txtAmount.getText());

                // Update UI labels
                currentTitleLabel.setText("Title: " + newTitle);
                currentTargetLabel.setText("Target: RM" + currentSavedAmount + " out of RM" + newTargetAmount);

                // Recalculate progress based on new target
                double newProgress = Math.min(currentSavedAmount / newTargetAmount, 1.0);
                currentProgressBar.setProgress(newProgress);

                // Hide edit pane after updating
                createPane.setVisible(false);
                GoalsPane.setDisable(false);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        });
    }


    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d;")); //back to original color
    }

    //Change scenes
    @FXML
    public void toHome(ActionEvent event){
        loadScene(event, "HomePage.fxml", "Budget Buddy - Home Page");
    }
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
}
