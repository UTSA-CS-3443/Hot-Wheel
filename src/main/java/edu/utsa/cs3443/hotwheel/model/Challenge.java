package edu.utsa.cs3443.hotwheel.model;

import edu.utsa.cs3443.hotwheel.manager.GameState;

public class Challenge {
    private double targetMoney;
    private double defaultValue;
    private double challengeMultiplier;
    private double challengeLevel;
    private double remainingMoney;

    public Challenge(double defaultValue, double challengeMultiplier, double challengeLevel) {
        updateTargetMoney();
        this.defaultValue = defaultValue;
        this.challengeMultiplier = challengeMultiplier;
        this.challengeLevel = challengeLevel;
    }

    public void updateTargetMoney() {
        targetMoney = Math.round((defaultValue + challengeMultiplier * (challengeLevel - 1)) / 50.0) * 50.0;
    }
    public int checkCompletion(double amount) {
        int wins = 0;
            while(amount >= targetMoney)
            {
                amount -= targetMoney;
                challengeLevel++;
                updateTargetMoney();
                wins++;
            }
        remainingMoney = amount;
        return wins;
    }

    // Getters and Setters
    public double getTargetMoney() { return targetMoney; }
    public double getDefaultValue() { return defaultValue; }
    public double getChallengeMultiplier() { return challengeMultiplier; }
    public double getChallengeLevel() { return challengeLevel; }
    public double getRemainingMoney() { return remainingMoney; }
}
