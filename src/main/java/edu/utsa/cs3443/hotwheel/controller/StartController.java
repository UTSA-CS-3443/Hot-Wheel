package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import javafx.fxml.FXML;

public class StartController {

    @FXML
    protected void onStartGameClick() {
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/saveScreen.fxml");
    }

    @FXML
    protected void onTutorialClick() {
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/tutorialScreen.fxml");
    }
}