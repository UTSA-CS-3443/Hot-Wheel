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
            "Step 1: Spin the wheel to earn money, boost multipliers, and expand high-value segments!",
            "Step 2: Spend your earnings in the shop on powerful, permanent upgrades to scale faster.",
            "Step 3: Meet the target money condition within the 5-minute run time to win prestige points and beat the challenge!"
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