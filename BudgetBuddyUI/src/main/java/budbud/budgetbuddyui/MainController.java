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
        UserData authenticatedUser = authenticateUser(username, password);
        if (authenticatedUser != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + authenticatedUser.getUsername() + "!");
            loadScene(e, "HomePage.fxml", "BudgetBuddy - Dashboard");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
        }
    }

    private UserData authenticateUser(String username, String password) {
        for (UserData user : UserData.UserManager.getUsers()) {
            if ((username.equals(user.getUsername()) || username.equals(user.getEmail())) && password.equals(user.getPassword())) {
                return user;  // Successfully authenticated
            }
        }
        return null;  // Authentication failed
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
