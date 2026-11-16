package edu.utsa.cs3443.hotwheel.model;

import edu.utsa.cs3443.hotwheel.manager.GameState;

public class Challenge {
    private double targetMoney;
    private double startingMoney;
    private boolean success;

    public Challenge(double targetMoney, double startingMoney) {
        this.targetMoney = targetMoney;
        this.startingMoney = startingMoney;
        this.success = false;
    }

    public void startChallenge(GameState gameState) {
        gameState.setMoney(startingMoney);
        this.success = false;
    }

    public void updateTargetMoney(double newTarget) {
        this.targetMoney = newTarget;
    }

    public boolean checkCompletion(GameState gameState) {
        if (gameState.getMoney() >= targetMoney) {
            this.success = true;
            gameState.addPrestigePoint();
            return true;
        }
        return false;
    }

    // Getters and Setters
    public double getTargetMoney() { return targetMoney; }
    public double getStartingMoney() { return startingMoney; }
    public boolean isSuccess() { return success; }
}