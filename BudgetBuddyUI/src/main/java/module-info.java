module budbud.budgetbuddyui {
    requires javafx.controls;
    requires javafx.fxml;


    opens budbud.budgetbuddyui to javafx.fxml;
    exports budbud.budgetbuddyui;
}