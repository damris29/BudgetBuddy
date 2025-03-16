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

public class MainController {

    @FXML private TextField user_login;
    @FXML private PasswordField passw_login;
    @FXML private Button btnlogin;
    @FXML private Button btnsignup;
    @FXML private Hyperlink passwforgor;

    @FXML
    public void handleLogin(ActionEvent e) {
        String username = user_login.getText();
        String password = passw_login.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out.");
            return;
        }

        loadScene(e, "HomePage.fxml", "BudgetBuddy - Dashboard");
    }

    @FXML
    public void handleSignUp(ActionEvent e) {
        loadScene(e, "SignUpPage.fxml", "BudgetBuddy - Sign Up");
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}