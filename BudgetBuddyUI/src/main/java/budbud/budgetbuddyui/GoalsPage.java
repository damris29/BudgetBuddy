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
import java.util.HashMap;
import java.util.Map;

public class GoalsPage {
    @FXML
    private Button btnHome;
    @FXML
    private Button btnSet;
    @FXML
    private Button btnViewBudget;
    @FXML
    private Button btnTips;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnCreateGoal;
    @FXML
    private ScrollPane GoalsPane;
    @FXML
    private AnchorPane createPane;
    @FXML
    private AnchorPane addAmountPane;
    @FXML
    private TextField txtTitle, txtAmount;
    @FXML
    private Button btnSubmit;
    @FXML
    private Label lblTitle, lblTarget;
    @FXML
    private ProgressBar progressAmount;
    @FXML
    private VBox vbox;
    @FXML
    private Button btnAddSubmit;
    @FXML
    private TextField txtAmountAdd;

    // Reference to the data manager
    private GoalsDataManager dataManager = GoalsDataManager.getInstance();

    // Map to keep track of AnchorPane for each Goal
    private Map<Goal, AnchorPane> goalPaneMap = new HashMap<>();

    // Current editing goal and index
    private Goal currentEditingGoal;
    private int currentEditingIndex = -1;

    @FXML
    public void initialize() {
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnHome);
        addHoverEffect(btnTips);

        // Load existing goals from data manager
        loadGoals();

        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            lblTitle.setText("Title: " + newValue);
        });

        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtAmount.setText(oldValue);
            } else if (!newValue.isEmpty()) {
                try {
                    double amount = Double.parseDouble(newValue);
                    lblTarget.setText("Target: RM0 out of RM" + amount);
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            }
        });
    }

    private void loadGoals() {
        vbox.getChildren().clear();

        for (Goal goal : dataManager.getGoals()) {
            AnchorPane goalPane = createGoalPane(goal);
            vbox.getChildren().add(goalPane);
            goalPaneMap.put(goal, goalPane);
        }

        // Update scroll pane height
        vbox.setPrefHeight(vbox.getChildren().size() * (100 + 10));
    }

    private AnchorPane createGoalPane(Goal goal) {
        //Create pane
        AnchorPane newPane = new AnchorPane();
        newPane.setPrefSize(630, 100); // Set pane size
        newPane.setStyle("-fx-background-color:  #6d6d6d; -fx-background-radius: 20;");

        //Title Label
        Label title = new Label("Title: " + goal.getTitle());
        title.setFont(Font.font("Lucida Console", 15));
        title.setTextFill(Color.WHITE);
        title.setLayoutX(14);
        title.setLayoutY(14);

        //Target Label
        Label target = new Label("Target: RM" + goal.getCurrentAmount() + " out of RM" + goal.getTargetAmount());
        target.setFont(Font.font("Lucida Console", 15));
        target.setTextFill(Color.WHITE);
        target.setLayoutX(14);
        target.setLayoutY(42);

        //Progress Bar
        ProgressBar goalProg = new ProgressBar();
        goalProg.setProgress(goal.getProgress());
        goalProg.setPrefSize(535, 18);
        goalProg.setLayoutX(14);
        goalProg.setLayoutY(70);

        //Add amount button
        Button addAmt = new Button("Add");
        addAmt.setLayoutX(530);
        addAmt.setLayoutY(10);
        addAmt.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        //Edit button
        Button editGoal = new Button("Edit");
        editGoal.setLayoutX(580);
        editGoal.setLayoutY(10);
        editGoal.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Event for the Add Amount button
        int goalIndex = dataManager.getGoals().indexOf(goal);
        addAmt.setOnAction(event -> handleAddAmount(goalIndex));

        // Add Edit button event
        editGoal.setOnAction(event -> handleEditGoal(goalIndex));

        // Delete button
        Button deleteGoal = new Button("Delete");
        deleteGoal.setLayoutX(560);
        deleteGoal.setLayoutY(65);
        deleteGoal.setStyle("-fx-background-color: #d40000; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteGoal.setOnAction(event -> handleDeleteGoal(goal));

        newPane.getChildren().addAll(title, target, goalProg, addAmt, editGoal, deleteGoal);
        return newPane;
    }

    @FXML
    public void handleCreateGoal() {
        GoalsPane.setVisible(false);
        createPane.setVisible(true);
        btnCreateGoal.setDisable(true);

        // Reset fields
        txtTitle.clear();
        txtAmount.clear();
        lblTitle.setText("Title: ");
        lblTarget.setText("Target: RM0 out of RM0");

        // Disable submit button initially
        btnSubmit.setDisable(true);

        // Enable submit only when valid data is entered
        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> validateSubmitButton());
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> validateSubmitButton());

        // Set submit button to create new goal
        btnSubmit.setOnAction(event -> handleSubmit());
    }

    @FXML
    private void handleSubmit() {
        try {
            String title = txtTitle.getText().trim();
            double targetAmount = Double.parseDouble(txtAmount.getText());

            if (title.isEmpty()) {
                // Show error message for empty title
                return;
            }

            // Create new goal
            Goal newGoal = new Goal(title, targetAmount);
            dataManager.addGoal(newGoal);

            // Create and add new pane
            AnchorPane goalPane = createGoalPane(newGoal);
            vbox.getChildren().add(goalPane);
            goalPaneMap.put(newGoal, goalPane);

            // Update vbox height
            vbox.setPrefHeight(vbox.getChildren().size() * (100 + 10));

            // Hide create pane
            createPane.setVisible(false);
            GoalsPane.setVisible(true);
            btnCreateGoal.setDisable(false);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAmount(int goalIndex) {
        GoalsPane.setDisable(true);
        addAmountPane.setVisible(true);
        txtAmountAdd.clear();

        // Store the goal being edited
        currentEditingIndex = goalIndex;
        currentEditingGoal = dataManager.getGoals().get(goalIndex);

        btnAddSubmit.setOnAction(event -> {
            try {
                double addAmount = Double.parseDouble(txtAmountAdd.getText());

                // Update goal amount
                currentEditingGoal.addAmount(addAmount);

                // Update UI
                AnchorPane goalPane = goalPaneMap.get(currentEditingGoal);
                updateGoalPane(goalPane, currentEditingGoal);

                // Hide add amount pane
                GoalsPane.setDisable(false);
                addAmountPane.setVisible(false);

            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        });
    }

    private void updateGoalPane(AnchorPane pane, Goal goal) {
        // Update labels and progress bar
        Label title = (Label) pane.getChildren().get(0);
        Label target = (Label) pane.getChildren().get(1);
        ProgressBar progressBar = (ProgressBar) pane.getChildren().get(2);

        title.setText("Title: " + goal.getTitle());
        target.setText("Target: RM" + goal.getCurrentAmount() + " out of RM" + goal.getTargetAmount());

        double newProgress = goal.getProgress();
        animateProgress(progressBar, newProgress);
    }

    @FXML
    private void handleEditGoal(int goalIndex) {
        GoalsPane.setDisable(true);
        addAmountPane.setVisible(false);
        createPane.setVisible(true);

        // Store the goal being edited
        currentEditingIndex = goalIndex;
        currentEditingGoal = dataManager.getGoals().get(goalIndex);

        // Populate fields with current values
        txtTitle.setText(currentEditingGoal.getTitle());
        txtAmount.setText(String.valueOf(currentEditingGoal.getTargetAmount()));
        lblTitle.setText("Title: " + currentEditingGoal.getTitle());
        lblTarget.setText("Target: RM" + currentEditingGoal.getCurrentAmount() + " out of RM" + currentEditingGoal.getTargetAmount());

        // Disable submit button initially
        btnSubmit.setDisable(true);
        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> validateSubmitButton());
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> validateSubmitButton());

        // Change submit button action for updating
        btnSubmit.setOnAction(event -> {
            try {
                String newTitle = txtTitle.getText().trim();
                double newTargetAmount = Double.parseDouble(txtAmount.getText());

                if (newTitle.isEmpty()) return;

                // Update goal
                currentEditingGoal.setTitle(newTitle);
                currentEditingGoal.setTargetAmount(newTargetAmount);

                // Update UI
                AnchorPane goalPane = goalPaneMap.get(currentEditingGoal);
                updateGoalPane(goalPane, currentEditingGoal);

                showConfirmation("Goal Updated", "Your goal has been successfully updated!");
                // Hide edit pane
                createPane.setVisible(false);
                GoalsPane.setDisable(false);
                btnCreateGoal.setDisable(false);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        });
    }

    private void handleDeleteGoal(Goal goal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Goal");
        alert.setHeaderText("Are you sure you want to delete this goal?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dataManager.deleteGoal(goal);
                vbox.getChildren().remove(goalPaneMap.get(goal));
                goalPaneMap.remove(goal);
                vbox.setPrefHeight(vbox.getChildren().size() * (100 + 10));
            }
        });
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d;")); //back to original color
    }

    private void animateProgress(ProgressBar progressBar, double newProgress) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(500),
                        new javafx.animation.KeyValue(progressBar.progressProperty(), newProgress)));
        timeline.play();
    }

    private void validateSubmitButton() {
        btnSubmit.setDisable(txtTitle.getText().trim().isEmpty() || txtAmount.getText().trim().isEmpty());
    }

    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void toHome(ActionEvent event) {
        loadScene(event, "HomePage.fxml", "Budget Buddy - Home Page");
    }

    @FXML
    public void toBudgetSet(ActionEvent event) {
        loadScene(event, "SetBudget.fxml", "Budget Buddy - Set Budget");
    }

    @FXML
    public void toBudgetViewer(ActionEvent event) {
        loadScene(event, "ViewBudget.fxml", "Budget Buddy - Budget Viewer");
    }

    @FXML
    public void toSetting(ActionEvent event) {
        loadScene(event, "SetProfile.fxml", "Budget Buddy - Goals");
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
