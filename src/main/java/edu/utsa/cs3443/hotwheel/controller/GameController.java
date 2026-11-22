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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private static final double INITIAL_RUN_SECONDS = 300.0;

    private GameManager gameManager;
    private SaveFileManager saveFileManager;
    private Timeline gameTimer;
    private int currentSlot;
    private double currentWheelAngle = 0;
    private List<WheelSegment> currentSegments;
    private Pane wheelPane;
    private Label centerTimerLabel;
    private Arc timerRing;

    // Tracks active spin and run states so UI controls lock cleanly without manual setDisable calls
    private final BooleanProperty wheelSpinning = new SimpleBooleanProperty(false);
    private final BooleanProperty runActive = new SimpleBooleanProperty(true);

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

    // Matches <fx:id="shopPane" source="ShopPane.fxml"/> in the game FXML
    @FXML
    private ShopController shopPaneController;

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

        GameState state = new GameState("Slot" + currentSlot, wheel, 0.0, INITIAL_RUN_SECONDS);
        saveFileManager.loadGame(currentSlot, state);

        gameManager = new GameManager(state);
        gameManager.startRun();
        runActive.set(gameManager.isRunning());

        if (spinButton != null) {
            spinButton.disableProperty().bind(wheelSpinning.or(runActive.not()));
        }

        if (shopPaneController != null) {
            shopPaneController.initShop(gameManager.getGameState(), wheelSpinning, () -> {

                drawVisualWheel(gameManager.getGameState().getWheel().getSegments());

                updateUI();
                saveCurrentProgress();

                if (actionFeedLabel != null) {
                    actionFeedLabel.setText("Purchased an upgrade!");
                }
            });
        }

        drawVisualWheel(currentSegments);
        updateUI();
        startRunTimer();
    }

    private void drawVisualWheel(List<WheelSegment> segments) {
        if (wheelContainer == null || segments == null || segments.isEmpty()) {
            return;
        }

        // Preserve the wheel's rotation when redrawing
        double previousRotation = wheelPane == null
                ? currentWheelAngle
                : wheelPane.getRotate();

        wheelContainer.getChildren().clear();

        wheelPane = new Pane();
        wheelPane.setPrefSize(220, 220);
        wheelPane.setMinSize(220, 220);
        wheelPane.setMaxSize(220, 220);
        wheelPane.setRotate(previousRotation);

        // Calculate the total size of every segment
        double totalSize = 0;

        for (WheelSegment segment : segments) {
            totalSize += segment.getSize();
        }

        if (totalSize <= 0) {
            return;
        }

        double startAngle = 90;

        // Explicit color mapping matching each metal
        Color[] metalColors = {
                Color.CHOCOLATE,  // Bronze
                Color.LIGHTGRAY,  // Silver
                Color.GOLD,       // Gold
                Color.DEEPSKYBLUE // Platinum
        };

        for (int i = 0; i < segments.size(); i++) {
            WheelSegment seg = segments.get(i);

            // Each arc occupies a proportion of the full circle
            double arcLength = 360.0 * seg.getSize() / totalSize;

            Arc arc = new Arc(
                    110, 110, 100, 100,
                    startAngle, -arcLength
            );

            arc.setType(ArcType.ROUND);
            arc.setFill(metalColors[i % metalColors.length]);
            arc.setStroke(Color.BLACK);
            arc.setStrokeWidth(2);

            wheelPane.getChildren().add(arc);

            startAngle -= arcLength;
        }

        // Outer draining ring around the edge of the 220x220 wheel (center 110, 110; wheel radius is 100)
        timerRing = new Arc(110, 110, 106, 106, 90, 360);
        timerRing.setType(ArcType.OPEN);
        timerRing.setFill(Color.TRANSPARENT);
        timerRing.setStroke(Color.GOLD);
        timerRing.setStrokeWidth(4);
        timerRing.setMouseTransparent(true);

        // Lowered pointer arrow sitting right above the top rim
        Polygon pointerArrow = new Polygon(
                110.0, 14.0,
                100.0, -4.0,
                120.0, -4.0
        );
        pointerArrow.setFill(Color.CRIMSON);
        pointerArrow.setStroke(Color.GOLD);
        pointerArrow.setStrokeWidth(1.5);
        pointerArrow.setMouseTransparent(true);

        Pane overlayPane = new Pane(timerRing, pointerArrow);
        overlayPane.setPrefSize(220, 220);
        overlayPane.setMinSize(220, 220);
        overlayPane.setMaxSize(220, 220);
        overlayPane.setMouseTransparent(true);

        // Dark pill/box behind the center timer readout so it stays legible over any segment color
        centerTimerLabel = new Label("300s");
        centerTimerLabel.setStyle(
                "-fx-background-color: rgba(11, 23, 18, 0.90);" +
                        "-fx-text-fill: #FFF8DC;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 5 9 5 9;" +
                        "-fx-background-radius: 999;" +
                        "-fx-border-color: #d4af37;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 999;"
        );
        centerTimerLabel.setMouseTransparent(true);

        wheelContainer.getChildren().addAll(wheelPane, overlayPane, centerTimerLabel);
    }

    private void startRunTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
            gameManager.updateTimer(1.0);
            updateUI();
            if (!gameManager.isRunning()) {
                runActive.set(false);
                gameTimer.stop();
                if (actionFeedLabel != null) {
                    actionFeedLabel.setText("Run Ended! Time is up.");
                }
                saveCurrentProgress();
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }


    @FXML
    protected void onSpinClick() {
        // Prevent spinning if the run is over or already spinning
        if (!gameManager.isRunning() || wheelPane == null || wheelSpinning.get()) {
            return;
        }

        // Lock spin button and shop during animation
        wheelSpinning.set(true);

        Wheel wheel = gameManager.getGameState().getWheel();

        // Start from the wheel's current rotation
        double startingAngle = wheelPane.getRotate();

        // Random final rotation, independent of segment count
        double randomAngle = Math.random() * 360;
        double fullSpins = 360 * 4;

        double targetAngle = startingAngle + fullSpins + randomAngle;

        // Use the upgraded spin duration
        RotateTransition rotator = new RotateTransition(
                Duration.seconds(wheel.getSpinDuration()),
                wheelPane
        );

        rotator.setFromAngle(startingAngle);
        rotator.setToAngle(targetAngle);

        rotator.setOnFinished(e -> {
            // Keep rotation within 0-360 degrees
            currentWheelAngle = targetAngle % 360;
            wheelPane.setRotate(currentWheelAngle);

            // Determine the winning segment from final rotation
            WheelSegment landed = gameManager.spinWheel(targetAngle);

            if (landed != null) {
                // GameManager already added the earned money
                double earned = landed.getValue()
                        * wheel.getMoneyMultiplier();

                if (actionFeedLabel != null) {
                    actionFeedLabel.setText(
                            String.format(
                                    "Landed on %s ($%.2f)! Earned $%.2f",
                                    landed.getLabel(),
                                    landed.getValue(),
                                    earned
                            )
                    );
                }
            }

            // Unlock controls and refresh the interface
            wheelSpinning.set(false);
            runActive.set(gameManager.isRunning());

            updateUI();
            saveCurrentProgress();
        });

        rotator.play();
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
        double timeLeft = Math.max(0.0, state.getTimeRemaining());

        if (moneyLabel != null) moneyLabel.setText(String.format("Money: $%.2f", state.getMoney()));
        if (timerLabel != null) timerLabel.setText(String.format("Time Left: %.0fs", timeLeft));
        if (prestigeLabel != null) prestigeLabel.setText("Prestige Pts.: " + state.getPrestigePoints());

        if (centerTimerLabel != null) {
            centerTimerLabel.setText(String.format("%.0fs", timeLeft));
        }
        if (timerRing != null) {
            double ratio = Math.min(1.0, timeLeft / INITIAL_RUN_SECONDS);
            timerRing.setLength(-360.0 * ratio);
        }

        if (shopPaneController != null) shopPaneController.refreshShop();
    }
}