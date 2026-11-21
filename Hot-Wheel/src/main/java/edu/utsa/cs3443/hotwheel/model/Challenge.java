package edu.utsa.cs3443.hotwheel.model;

import edu.utsa.cs3443.hotwheel.manager.GameState;

public class Challenge {
    private double targetMoney;
    private double defaultValue;
    private double challengeMultiplier;
    private double challengeLevel;
    private double remainingMoney;

    public Challenge(double targetMoney, double defaultValue, double challengeMultiplier, double challengeLevel) {
        this.targetMoney = targetMoney;
        this.defaultValue = defaultValue;
        this.challengeMultiplier = challengeMultiplier;
        this.challengeLevel = challengeLevel;
    }

    public void updateTargetMoney() {
        targetMoney = defaultValue * challengeMultiplier * (challengeLevel-1);
    }
    public int checkCompletion(double amount) {
        int wins = 0;
            while(amount >= targetMoney)
            {
                amount -= targetMoney;
                updateTargetMoney();
                challengeLevel++;
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