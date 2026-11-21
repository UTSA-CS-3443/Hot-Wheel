package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TutorialController {

    @FXML
    private Label tutorialTextLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    private int currentIndex = 0;
    private final String[] tutorialSteps = {
            "Spin the wheel to earn money, simple as that! The more valuable your segment, the more money you get! How much can YOU get in 5 minutes?",
            "30-second challenges will appear, giving you a chance to earn prestige points. Spend prestige points or money to unlock powerful, permanent upgrades that will help you climb.",
            "Once your 5 minutes are up, the wheel will no longer spin. You can delete the current save or try again in a different save slot for a chance to earn more!"
    };

    @FXML
    public void initialize() {
        updateTutorialText();
    }

    @FXML
    protected void onNextClick() {
        if (currentIndex < tutorialSteps.length - 1) {
            currentIndex++;
            updateTutorialText();
        }
    }

    @FXML
    protected void onBackClick() {
        if (currentIndex > 0) {
            currentIndex--;
            updateTutorialText();
        }
    }

    @FXML
    protected void onHomeClick() {
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/startScreen.fxml");
    }

    private void updateTutorialText() {
        if (tutorialTextLabel != null) {
            tutorialTextLabel.setText(tutorialSteps[currentIndex]);
        }
        if (backButton != null) {
            backButton.setDisable(currentIndex == 0);
        }
        if (nextButton != null) {
            nextButton.setDisable(currentIndex == tutorialSteps.length - 1);
        }
    }
}