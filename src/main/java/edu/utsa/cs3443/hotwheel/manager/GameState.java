package edu.utsa.cs3443.hotwheel.manager;

import edu.utsa.cs3443.hotwheel.model.Challenge;
import edu.utsa.cs3443.hotwheel.model.Wheel;

public class GameState {
    private String activeSaveFile;
    private Wheel wheel;
    private Challenge challenge;
    private double money;
    private double revenue;
    private int prestigePoints;
    private double timeRemaining;

    public GameState(String activeSaveFile, Wheel wheel, double startingMoney, double timeRemaining) {
        this.activeSaveFile = activeSaveFile;
        this.wheel = wheel;
        this.money = startingMoney;
        this.prestigePoints = 0;
        this.timeRemaining = timeRemaining;
    }
    public GameState(String activeSaveFile, Wheel wheel, double startingMoney, double timeRemaining, Challenge challenge) {
        this.activeSaveFile = activeSaveFile;
        this.wheel = wheel;
        this.money = startingMoney;
        this.prestigePoints = 0;
        this.timeRemaining = timeRemaining;
        this.challenge = challenge;
    }

    public void addMoney(double amount) {
        this.money += amount;
        revenue += amount;
        if(amount >= 0 && challenge != null)
        {
            int wins = challenge.checkCompletion(revenue);
            if(wins > 0)
            {
                addPrestigePoint(wins);
                revenue = challenge.getRemainingMoney();
            }
        }
    }

    public void addPrestigePoint(int amount) {
        prestigePoints += amount;
    }

    // Getters and Setters
    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }
    public int getPrestigePoints() { return prestigePoints; }
    public void setPrestigePoints(int prestigePoints) { this.prestigePoints = prestigePoints; }
    public Wheel getWheel() { return wheel; }
    public double getTimeRemaining() { return timeRemaining; }
    public void setTimeRemaining(double timeRemaining) { this.timeRemaining = timeRemaining; }

    public boolean spendMoney(double amount) {
        if (amount < 0 || this.money < amount) {
            return false;
        }
        this.money -= amount;
        return true;
    }
}
