package edu.utsa.cs3443.hot_wheel.model;

public class Challenge {
    private double targetMoney;
    private double startingMoney;
    private boolean isCompleted;

    public void startChallenge(double currentMoney) {
        //Creates the challenge with the given value as the startingMoney
    }

    public void updateChallenge(double newTargetMoney) {
        //creates an updated/higher targetMoney value as the game progresses
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

}
