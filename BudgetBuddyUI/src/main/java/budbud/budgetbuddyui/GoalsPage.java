package budbud.budgetbuddyui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GoalsPage {
    @FXML private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnViewBudget;
    @FXML private Button btnTips;
    @FXML private Button btnSettings;

    @FXML
    public void initialize() {
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnHome);
        addHoverEffect(btnTips);
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
