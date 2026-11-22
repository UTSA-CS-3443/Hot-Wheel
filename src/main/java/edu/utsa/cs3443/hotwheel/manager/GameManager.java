package edu.utsa.cs3443.hotwheel.manager;

import edu.utsa.cs3443.hotwheel.model.Wheel;
import edu.utsa.cs3443.hotwheel.model.WheelSegment;

public class GameManager {
    private static final double DEFAULT_RUN_SECONDS = 300.0;

    private GameState gameState;
    private boolean isRunning;

    public GameManager(GameState gameState) {
        this.gameState = gameState;
        this.isRunning = false;
    }

    public void startRun() {
        if (gameState == null) {
            this.isRunning = false;
            return;
        }

        if (gameState.getTimeRemaining() <= 0) {
            gameState.setMoney(0.0);
            gameState.resetRunRevenue();
            gameState.setTimeRemaining(DEFAULT_RUN_SECONDS);
        }

        this.isRunning = gameState.getTimeRemaining() > 0;
    }


    public WheelSegment spinWheel(double finalRotation) {
        if (!isRunning) return null;

        Wheel wheel = gameState.getWheel();

        WheelSegment landedSegment =
                wheel.getLandedSegment(finalRotation);

        if (landedSegment != null) {
            double earned = landedSegment.getValue()
                    * wheel.getMoneyMultiplier();

            gameState.addMoney(earned);
        }

        return landedSegment;
    }


    public void upgradeSegmentSize(WheelSegment segment, double amount) {
        if (!isRunning) return;

        gameState.getWheel().increaseSegmentSize(segment, amount);
    }



    public void updateTimer(double deltaSeconds) {
        if (!isRunning || gameState == null) return;
        double remaining = gameState.getTimeRemaining() - deltaSeconds;
        gameState.setTimeRemaining(Math.max(0.0, remaining));
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