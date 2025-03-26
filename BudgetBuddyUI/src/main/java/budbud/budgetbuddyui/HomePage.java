package budbud.budgetbuddyui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HomePage {

    @FXML private Button btnHome;
    @FXML private Button btnSet;
    @FXML private Button btnViewBudget;
    @FXML private Button btnSettings;
    @FXML private Button btnGoals;
    @FXML private Button btnTips;

    @FXML ImageView logoutImage;

    private final String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"; // Update if needed
    Image imageView = new Image(Objects.requireNonNull(getClass().getResourceAsStream("src.main.images/Logout-BBwUI.png")));

    @FXML
    public void initialize() {
        addHoverEffect(btnSet);
        addHoverEffect(btnViewBudget);
        addHoverEffect(btnSettings);
        addHoverEffect(btnGoals);
        addHoverEffect(btnTips);

        logoutImage.setOnMouseClicked(e -> handleImageLogout());
    }



    @FXML
    private void handleToReport1() {
        openLinkInChrome("https://www.crowe.com/my/news/key-highlights-of-malaysias-budget-2025");
    }

    @FXML
    private void handleToReport2() {
        openLinkInChrome("https://www.malaymail.com/news/malaysia/2024/10/19/budget-2025-whats-in-it-for-you-and-your-family/154101");
    }

    @FXML
    private void handleToReport3() {
        openLinkInChrome("https://thesun.my/malaysia-news/malaysia-budget-2025-live-updates-FG13159639");
    }

    @FXML
    private void handleToReport4() {
        openLinkInChrome("https://www.aseanbriefing.com/news/malaysias-budget-2025-impact-for-businesses/");
    }

    @FXML
    private void openLinkInChrome(String url) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(chromePath, url);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImageLogout() {
        handleLogout(new ActionEvent(logoutImage, null));
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #5b5b5b;")); //changes color when hover mouse
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #6d6d6d;")); //back to original color
    }

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
    public void toGoals(ActionEvent event){
        loadScene(event, "GoalsPage.fxml", "Budget Buddy - Saving Goals");
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
