package edu.utsa.cs3443.hotwheel.manager;

import edu.utsa.cs3443.hotwheel.model.Wheel;
import edu.utsa.cs3443.hotwheel.model.WheelSegment;

public class GameManager {
    private GameState gameState;
    private boolean isRunning;

    public GameManager(GameState gameState) {
        this.gameState = gameState;
        this.isRunning = false;
    }

    public void startRun() {
        this.isRunning = true;
    }

    public void spinWheel() {
        if (!isRunning) return;
        Wheel wheel = gameState.getWheel();
        WheelSegment landedSegment = wheel.spin();
        if (landedSegment != null) {
            double earned = landedSegment.getValue() * wheel.getMoneyMultiplier();
            gameState.addMoney(earned);
        }
    }

    public void updateTimer(double deltaSeconds) {
        if (!isRunning) return;
        double remaining = gameState.getTimeRemaining() - deltaSeconds;
        gameState.setTimeRemaining(Math.max(0, remaining));
        if (gameState.getTimeRemaining() <= 0) {
            this.isRunning = false; // Run ends when time runs out
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean isRunning() {
        return isRunning;
    }
}