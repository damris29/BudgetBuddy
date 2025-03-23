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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class SignUpPage {

    @FXML private TextField txtUserS;
    @FXML private TextField txtEmailS;
    @FXML private TextField txtPhoneS;
    @FXML private TextField txtBirthS;
    @FXML private PasswordField txtPassS;
    @FXML private PasswordField txtCPassS;
    @FXML private Button btnSignUp;

    public void initialize() {
        addHoverEffect(btnSignUp);
    }

    @FXML
    private void handleSignUp(ActionEvent e) {
        String username = txtUserS.getText();
        String email = txtEmailS.getText();
        String phonenum = txtPhoneS.getText();
        String birthdate = txtBirthS.getText();
        String password = txtPassS.getText();
        String Cpassword = txtCPassS.getText();

        if (username.isEmpty() || email.isEmpty() || phonenum.isEmpty() || birthdate.isEmpty() || password.isEmpty() || Cpassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out.");
            return;
        }

        if (!isValidBirthdate(birthdate)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid birthdate. Use DD/MM/YYYY format.");
            return;
        }

        if (!password.equals(Cpassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        // Check if email or username is already taken
        if (UserData.UserManager.isUserExists(username, email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username or email is already registered.");
            return;
        }

        // Add the new user to the list
        UserData newUser = new UserData(username, email, phonenum, birthdate, password);
        UserData.UserManager.addUser(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Sign-up successful!");
        loadScene(e);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidBirthdate(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(birthdate, formatter);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private void loadScene(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginPage.fxml")));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BudgetBuddy - Main Page");
            stage.show();
        } catch (IOException ex) {
            System.out.println("Error loading " + "LoginPage.fxml" + ": " + ex.getMessage());
        }
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:  #FFFFFF; -fx-text-fill: #242424; -fx-border-color: #000000;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:   #242424; -fx-text-fill: #FFFFFF;"));
    }
}
