package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.SaveFileManager;
import edu.utsa.cs3443.hotwheel.manager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SaveController {

    private static int activeSlot = 1;
    private SaveFileManager saveFileManager;
    private int selectedSlot = activeSlot; // Remember the active slot on load

    @FXML
    private Label slotStatusLabel;

    @FXML
    public void initialize() {
        saveFileManager = new SaveFileManager();
        selectedSlot = activeSlot; // Ensure sync when returning to screen
        updateSlotDisplay();
    }

    public static int getActiveSlot() {
        return activeSlot;
    }

    @FXML
    protected void onSelectSlot1() { selectedSlot = 1; updateSlotDisplay(); }

    @FXML
    protected void onSelectSlot2() { selectedSlot = 2; updateSlotDisplay(); }

    @FXML
    protected void onSelectSlot3() { selectedSlot = 3; updateSlotDisplay(); }

    @FXML
    protected void onPlayClick() {
        activeSlot = selectedSlot;
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/gameScreen.fxml");
    }

    @FXML
    protected void onDeleteClick() {
        saveFileManager.deleteSave(selectedSlot);
        updateSlotDisplay();
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("/edu/utsa/cs3443/hotwheel/view/startScreen.fxml");
    }

    private void updateSlotDisplay() {
        boolean exists = saveFileManager.saveExists(selectedSlot);
        if (slotStatusLabel != null) {
            slotStatusLabel.setText("Selected Slot: " + selectedSlot + " (" + (exists ? "Saved Data Found" : "Empty Slot") + ")");
        }
    }
}