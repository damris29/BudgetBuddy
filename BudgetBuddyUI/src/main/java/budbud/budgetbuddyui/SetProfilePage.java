package budbud.budgetbuddyui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SetProfilePage {
    //Menu Buttons
    @FXML private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnViewBudget;
    @FXML private Button btnTips;
    @FXML private Button btnGoals;

    //Page MenuProfile
    @FXML private TextField txtUserProfile;
    @FXML private TextField txtEmailProfile;
    @FXML private TextField txtPhoneProfile;
    @FXML private TextField txtBirthProfile;
    @FXML private Button btnChangeInfo;
    @FXML private Button btnLogout;
    @FXML private Button btnSave;
    @FXML private Label lblUsername, lblEmail, lblPhone, lblBirthdate;
    
    @FXML
    public void initialize() {
        addHoverEffect(btnHome);
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnTips);
        addHoverEffect(btnGoals);
        addHoverEffectOnPanel(btnChangeInfo);
        addHoverEffectOnPanel(btnLogout);
        addHoverEffectOnPanel(btnSave);

        displayUserInfo();
    }
    
    //Button Change Info
    @FXML
    private void handleChangeInfo(ActionEvent event){
        UserData user = UserData.UserManager.getLoggedInUser();
        lblUsername.setVisible(false);
        lblEmail.setVisible(false);
        lblPhone.setVisible(false);
        lblBirthdate.setVisible(false);

        btnChangeInfo.setVisible(false);
        btnLogout.setVisible(false);
        btnSave.setVisible(true);

        txtUserProfile.setDisable(false);
        txtEmailProfile.setDisable(false);
        txtPhoneProfile.setDisable(false);
        txtBirthProfile.setDisable(false);
        txtUserProfile.setText(user.getUsername());
        txtEmailProfile.setText(user.getEmail());
        txtPhoneProfile.setText(user.getPhone());
        txtBirthProfile.setText(user.getBirthdate());
    }

    //Save Info after complete edit
    @FXML
    private void handleSaveInfo(ActionEvent event) {
        UserData user = UserData.UserManager.getLoggedInUser();

        // Get new values from text fields
        String newUsername = txtUserProfile.getText();
        String newEmail = txtEmailProfile.getText();
        String newPhone = txtPhoneProfile.getText();
        String newBirthdate = txtBirthProfile.getText();

        // Update user details in ArrayList
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPhone(newPhone);
        user.setBirthdate(newBirthdate);

        // Update display
        displayUserInfo();

        // Hide text fields and show labels again
        lblUsername.setVisible(true);
        lblEmail.setVisible(true);
        lblPhone.setVisible(true);
        lblBirthdate.setVisible(true);

        txtUserProfile.setDisable(true);
        txtEmailProfile.setDisable(true);
        txtPhoneProfile.setDisable(true);
        txtBirthProfile.setDisable(true);
        txtUserProfile.clear();
        txtEmailProfile.clear();
        txtPhoneProfile.clear();
        txtBirthProfile.clear();

        btnSave.setVisible(false);
        btnChangeInfo.setVisible(true);
        btnLogout.setVisible(true);

        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Your profile has been successfully updated.");
        alert.showAndWait();
    }

    //Button Logout
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

    private void addHoverEffectOnPanel(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:  #5b5b5b; -fx-background-radius: 20;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d; -fx-background-radius: 20;")); //back to original color
    }

    //Display user's info from login/signup
    private void displayUserInfo() {
        UserData user = UserData.UserManager.getLoggedInUser();
        if (user != null) {
            lblUsername.setText(user.getUsername());
            lblEmail.setText(user.getEmail());
            lblPhone.setText(user.getPhone());
            lblBirthdate.setText(user.getBirthdate());
        } else {
            lblUsername.setText("N/A");
            lblEmail.setText("N/A");
            lblPhone.setText("N/A");
            lblBirthdate.setText("N/A");
        }
    }

}
