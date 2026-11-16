package edu.utsa.cs3443.hotwheel.manager;

import edu.utsa.cs3443.hotwheel.model.Wheel;

public class GameState {
    private String activeSaveFile;
    private Wheel wheel;
    private double money;
    private int prestigePoints;
    private double timeRemaining;

    public GameState(String activeSaveFile, Wheel wheel, double startingMoney, double timeRemaining) {
        this.activeSaveFile = activeSaveFile;
        this.wheel = wheel;
        this.money = startingMoney;
        this.prestigePoints = 0;
        this.timeRemaining = timeRemaining;
    }

    public void addMoney(double amount) {
        this.money += amount;
    }

    public void addPrestigePoint() {
        this.prestigePoints++;
    }

    // Getters and Setters
    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }
    public int getPrestigePoints() { return prestigePoints; }
    public void setPrestigePoints(int prestigePoints) { this.prestigePoints = prestigePoints; }
    public Wheel getWheel() { return wheel; }
    public double getTimeRemaining() { return timeRemaining; }
    public void setTimeRemaining(double timeRemaining) { this.timeRemaining = timeRemaining; }
}