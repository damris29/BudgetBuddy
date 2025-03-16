package budbud.budgetbuddyui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginPage.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("BudgetBuddy");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNIFIED);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}

