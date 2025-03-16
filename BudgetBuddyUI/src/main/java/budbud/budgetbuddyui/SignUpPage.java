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
import java.util.Objects;

public class SignUpPage {

    @FXML
    private TextField user_signup;

    @FXML
    private PasswordField passw_signup;

    @FXML
    private DatePicker user_birth;

    @FXML
    private TextField phonenum;

    @FXML
    private CheckBox checksignup;

    @FXML
    private Button btnSignUp;

    @FXML
    private void handleSignUp(ActionEvent e){
        String username = user_signup.getText();
        String password = passw_signup.getText();
        String birthdate = (user_birth.getValue() != null) ? user_birth.getValue().toString() : "";
        String usernum = phonenum.getText();
        boolean check = checksignup.isSelected();

        if (username.isEmpty() || password.isEmpty() || birthdate.isEmpty() || usernum.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out.");
            return;
        }

        if (!check) {
            showAlert(Alert.AlertType.WARNING, "Warning", "You must agree to the terms.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Sign-up successful!");
        loadScene(e, "LoginPage.fxml", "BudgetBuddy - Main Page");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(ActionEvent e, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException ex) {
            System.out.println("Error loading " + fxmlFile + ": " + ex.getMessage());
        }
    }
}



