package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.GameManager;
import edu.utsa.cs3443.hotwheel.manager.GameState;
import edu.utsa.cs3443.hotwheel.manager.SaveFileManager;
import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import edu.utsa.cs3443.hotwheel.model.Wheel;
import edu.utsa.cs3443.hotwheel.model.WheelSegment;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameManager gameManager;
    private SaveFileManager saveFileManager;
    private Timeline gameTimer;
    private int currentSlot;
    private double currentWheelAngle = 0;
    private List<WheelSegment> currentSegments;
    private Pane wheelPane;

    @FXML
    private Label moneyLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label prestigeLabel;
    @FXML
    private Label actionFeedLabel;
    @FXML
    private StackPane wheelContainer;
    @FXML
    private Button spinButton;

    @FXML
    public void initialize() {
        saveFileManager = new SaveFileManager();
        currentSlot = SaveController.getActiveSlot();

        // Initialize rare metal wheel segments in exact order
        currentSegments = new ArrayList<>();
        currentSegments.add(new WheelSegment(40.0, 10.0, "Bronze"));
        currentSegments.add(new WheelSegment(30.0, 50.0, "Silver"));
        currentSegments.add(new WheelSegment(20.0, 150.0, "Gold"));
        currentSegments.add(new WheelSegment(10.0, 500.0, "Platinum"));

        Wheel wheel = new Wheel(currentSegments, 1.5, 1.0);

        GameState state = new GameState("Slot" + currentSlot, wheel, 100.0, 300.0);
        saveFileManager.loadGame(currentSlot, state);

        gameManager = new GameManager(state);
        gameManager.startRun();

        drawVisualWheel(currentSegments);
        updateUI();
        startRunTimer();
    }

    private void drawVisualWheel(List<WheelSegment> segments) {
        if (wheelContainer == null || segments == null || segments.isEmpty()) return;
        wheelContainer.getChildren().clear();

        wheelPane = new Pane();
        wheelPane.setPrefSize(220, 220);
        wheelPane.setMinSize(220, 220);
        wheelPane.setMaxSize(220, 220);

        double arcLength = 360.0 / segments.size();
        // Offset start angle so segment 0 is centered at the top pointer (90 degrees)
        double startAngle = 90 - (arcLength / 2.0);

        // Explicit color mapping matching each metal
        Color[] metalColors = {
                Color.CHOCOLATE,  // Bronze
                Color.LIGHTGRAY,  // Silver
                Color.GOLD,       // Gold
                Color.DEEPSKYBLUE // Platinum
        };

        for (int i = 0; i < segments.size(); i++) {
            WheelSegment seg = segments.get(i);

            Arc arc = new Arc(110, 110, 100, 100, startAngle, arcLength);
            arc.setType(ArcType.ROUND);
            arc.setFill(metalColors[i % metalColors.length]);
            arc.setStroke(Color.BLACK);
            arc.setStrokeWidth(2);

            wheelPane.getChildren().add(arc);
            startAngle += arcLength;
        }

        wheelContainer.getChildren().add(wheelPane);
    }

    private void startRunTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
            gameManager.updateTimer(1.0);
            updateUI();
            if (!gameManager.isRunning()) {
                gameTimer.stop();
                actionFeedLabel.setText("Run Ended! Time is up.");
                if (spinButton != null) spinButton.setDisable(true);
                saveCurrentProgress();
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    @FXML
    protected void onSpinClick() {
        if (!gameManager.isRunning() || wheelPane == null) return;
        if (spinButton != null) spinButton.setDisable(true);

        WheelSegment landed = gameManager.getGameState().getWheel().spin();
        if (landed != null) {
            int landedIndex = currentSegments.indexOf(landed);
            if (landedIndex == -1) landedIndex = 0;

            double arcLength = 360.0 / currentSegments.size();

            // Perfect forward rotation math to align the winning slice directly with the top pointer
            double targetSegmentAngle = landedIndex * arcLength;
            double forwardOffset = (360 - targetSegmentAngle - (currentWheelAngle % 360) + 360) % 360;
            double fullSpins = 1440; // 4 full rotations for spin inertia
            double targetAngle = currentWheelAngle + fullSpins + forwardOffset;

            RotateTransition rotator = new RotateTransition(Duration.seconds(1.5), wheelPane);
            rotator.setFromAngle(currentWheelAngle);
            rotator.setToAngle(targetAngle);
            rotator.setOnFinished(e -> {
                currentWheelAngle = targetAngle % 360;
                double earned = landed.getValue() * gameManager.getGameState().getWheel().getMoneyMultiplier();
                gameManager.getGameState().addMoney(earned);
                actionFeedLabel.setText("Landed on " + landed.getLabel() + " ($" + landed.getValue() + ")! Earned $" + earned);
                updateUI();
                saveCurrentProgress();

                if (spinButton != null && gameManager.isRunning()) {
                    spinButton.setDisable(false);
                }
            });
            rotator.play();
        }
    }

    @FXML
    protected void onBackToSaveClick() {
        if (gameTimer != null) gameTimer.stop();
        saveCurrentProgress();
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/saveScreen.fxml");
    }

    private void saveCurrentProgress() {
        if (gameManager != null && saveFileManager != null) {
            saveFileManager.saveGame(currentSlot, gameManager.getGameState());
        }
    }

    private void updateUI() {
        GameState state = gameManager.getGameState();
        if (moneyLabel != null) moneyLabel.setText(String.format("Money: $%.2f", state.getMoney()));
        if (timerLabel != null) timerLabel.setText(String.format("Time Left: %.0fs", state.getTimeRemaining()));
        if (prestigeLabel != null) prestigeLabel.setText("Prestige: " + state.getPrestigePoints());
    }
}