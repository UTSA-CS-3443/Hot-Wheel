package edu.utsa.cs3443.hotwheel.view;

import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameStartApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.initialize(primaryStage);
        // Load the initial start screen FXML
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/startScreen.fxml");
        primaryStage.setTitle("Hot Wheel - Team Critical Hit");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}