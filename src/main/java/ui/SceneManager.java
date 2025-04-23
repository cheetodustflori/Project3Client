package ui;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(Parent root) {
        primaryStage.getScene().setRoot(root);
    }
}
