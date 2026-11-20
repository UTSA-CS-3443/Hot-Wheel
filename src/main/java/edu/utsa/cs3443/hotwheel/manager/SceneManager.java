package edu.utsa.cs3443.hotwheel.manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static final double DEFAULT_WIDTH = 1280.0;
    private static final double DEFAULT_HEIGHT = 720.0;

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) {
        if (primaryStage == null) return;

        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            Scene currentScene = primaryStage.getScene();

            if (currentScene == null) {
                currentScene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                primaryStage.setScene(currentScene);
            } else {
                boolean wasFullScreen = primaryStage.isFullScreen();
                boolean wasMaximized = primaryStage.isMaximized();

                currentScene.setRoot(root);

                if (wasFullScreen) {
                    primaryStage.setFullScreen(true);
                } else if (wasMaximized) {
                    primaryStage.setMaximized(true);
                }
            }

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}