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

    @FXML private TextField txtUserL;
    @FXML private PasswordField txtPassL;
    @FXML private Button btnNext;
    @FXML private Hyperlink toSignUp;

    public void initialize() {
        addHoverEffect(btnNext);
    }

    @FXML
    public void handleLogin(ActionEvent e) {
        String username = txtUserL.getText();
        String email = txtUserL.getText();
        String password = txtPassL.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out.");
            return;
        }
        // Get user data from singleton instance
        UserData userData = UserData.getInstance();
        String storedUsername = userData.getUsername();
        String storedEmail = userData.getEmail();
        String storedPassword = userData.getPassword();

        // Check if the input matches either the username or email and the password
        if ((username.equals(storedUsername) || username.equals(storedEmail)) && password.equals(storedPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome to BudgetBuddy!");
            loadScene(e, "HomePage.fxml", "BudgetBuddy - Dashboard");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
        }
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

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:  #FFFFFF; -fx-text-fill: #242424; -fx-border-color: #000000;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:   #242424; -fx-text-fill: #FFFFFF;")); //back to original color
    }
}
