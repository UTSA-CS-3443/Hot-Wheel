package edu.utsa.cs3443.hot_wheel.model;

import java.util.List;

public class GameState {
    private double currentMoney;
    private double totalMoneyEarned;
    private int prestigePoints;
    private double timeRemaining;
    private int selectedSlot;
    private Wheel wheel;
    private List<Upgrade> upgrade;

    //Check if challenge was won and award prestige point here?
    public void addMoney(double amount) {
        //Adds given value to the totalMoneyEarned
    }

    public boolean spendMoney(double money) {
        //Checks if the given value is <= totalMoneyEarned, then removes the given value from totalMoneyEarned
        return false;
    }

    public void addPrestigePoint() {
        //Increase the current prestigePoints by 1 or however much we choose
    }

    public boolean spendPrestigePoint(int amount) {
        //Checks if the given value is <= prestigePoints, then removes the given value from prestigePoints
        return false;
    }

    public void addUpgrade(Upgrade upgrade) {
        //Adds given upgrade value to the list upgrade
    }



    public double getCurrentMoney() {
        return currentMoney;
    }

    public double getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public double getTimeRemaining() {
        return timeRemaining;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public Wheel getWheel() {
        return wheel;
    }

    public List<Upgrade> getUpgrade() {
        return upgrade;
    }
}
