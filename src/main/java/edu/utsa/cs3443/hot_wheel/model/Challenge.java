package edu.utsa.cs3443.hot_wheel.model;

public class Challenge {
    private double targetMoney;
    private double startingMoney;
    private boolean isCompleted;

    public Challenge (double targetMoney, double startingMoney, boolean isCompleted) {
        this.targetMoney = targetMoney;
        this.startingMoney = startingMoney;
        this.isCompleted = isCompleted;
    }

    public void startChallenge(double currentMoney) {
        //Creates the challenge with the given value as the startingMoney
    }

    public void updateChallenge(double newTargetMoney) {
        //creates a new targetMoney value from given value as the game progresses
    }

    public boolean isChallengeCompleted(double currentMoney) {
        //returns a boolean value for the given value if it is greater than or equal to the targetMoney
        return isCompleted;
    }

    //Potentially change/move this when we make a timer
    public boolean isChallengeExpired() {
        //returns a boolean value if the time has passed
        return false;
    }



    public double getMoneyEarned(double currentMoney) {
        //Returns how much money was made during the challenge
        return currentMoney - startingMoney;
    }

    public double getTargetMoney() {
        return targetMoney;
    }

    public double getStartingMoney() {
        return startingMoney;
    }

    public boolean isCompleted() {
         return isCompleted;
    }
}
