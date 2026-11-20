package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.GameState;
import edu.utsa.cs3443.hotwheel.manager.SaveFileManager;
import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import edu.utsa.cs3443.hotwheel.model.Wheel;
import edu.utsa.cs3443.hotwheel.model.WheelSegment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SaveController {

    private static final double DEFAULT_RUN_SECONDS = 300.0;

    private static int activeSlot = 1;
    private SaveFileManager saveFileManager;
    private int selectedSlot = activeSlot; // Remember the active slot on load

    @FXML
    private Label slotStatusLabel;
    @FXML
    private VBox statsBox;
    @FXML
    private Label statsSlotLabel;
    @FXML
    private Label statsMoneyLabel;
    @FXML
    private Label statsPrestigeLabel;
    @FXML
    private Label statsTimeLabel;
    @FXML
    private Label statsWheelLabel;

    @FXML
    public void initialize() {
        saveFileManager = new SaveFileManager();
        selectedSlot = activeSlot; // Ensure sync when returning to screen
        hideStatsBox();
        updateSlotDisplay();
    }

    public static int getActiveSlot() {
        return activeSlot;
    }

    @FXML
    protected void onSelectSlot1() { selectSlot(1); }

    @FXML
    protected void onSelectSlot2() { selectSlot(2); }

    @FXML
    protected void onSelectSlot3() { selectSlot(3); }

    private void selectSlot(int slot) {
        selectedSlot = slot;
        updateSlotDisplay();
        if (statsBox != null && statsBox.isVisible()) {
            populateStatsForSlot(selectedSlot);
        }
    }

    @FXML
    protected void onPlaySlotClick() {
        activeSlot = selectedSlot;
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/gameScreen.fxml");
    }

    @FXML
    protected void onStatsSlotClick() {
        if (statsBox == null) return;
        boolean nextVisible = !statsBox.isVisible();
        if (nextVisible) {
            populateStatsForSlot(selectedSlot);
            statsBox.setVisible(true);
            statsBox.setManaged(true);
        } else {
            hideStatsBox();
        }
    }

    @FXML
    protected void onDeleteClick() {
        saveFileManager.deleteSave(selectedSlot);
        updateSlotDisplay();
        if (statsBox != null && statsBox.isVisible()) {
            populateStatsForSlot(selectedSlot);
        }
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/startScreen.fxml");
    }

    private void hideStatsBox() {
        if (statsBox != null) {
            statsBox.setVisible(false);
            statsBox.setManaged(false);
        }
    }

    private void populateStatsForSlot(int slot) {
        if (statsSlotLabel != null) {
            statsSlotLabel.setText("Slot " + slot + " Stats");
        }

        if (!saveFileManager.saveExists(slot)) {
            if (statsMoneyLabel != null) statsMoneyLabel.setText("Money: $0.00");
            if (statsPrestigeLabel != null) statsPrestigeLabel.setText("Prestige Pts.: 0");
            if (statsTimeLabel != null) statsTimeLabel.setText("Time Remaining: " + String.format("%.0fs", DEFAULT_RUN_SECONDS));
            return;
        }

        GameState previewState = new GameState("Slot" + slot, null, 0.0, DEFAULT_RUN_SECONDS);
        saveFileManager.loadGame(slot, previewState);

        if (statsMoneyLabel != null) {
            statsMoneyLabel.setText(String.format("Money: $%.2f", previewState.getMoney()));
        }
        if (statsPrestigeLabel != null) {
            statsPrestigeLabel.setText("Prestige Pts.: " + previewState.getPrestigePoints());
        }
        if (statsTimeLabel != null) {
            statsTimeLabel.setText(String.format("Time Remaining: %.0fs", Math.max(0.0, previewState.getTimeRemaining())));
        }
    }

    private void updateSlotDisplay() {
        boolean exists = saveFileManager.saveExists(selectedSlot);
        if (slotStatusLabel != null) {
            slotStatusLabel.setText("Selected Slot: " + selectedSlot + " (" + (exists ? "Saved Data Found" : "Empty Slot") + ")");
        }
    }
}