package edu.utsa.cs3443.hot_wheel.model;

import java.util.ArrayList;
import java.util.List;

public class Wheel {
    private List<WheelSegment> segments;
    private double spinDuration;
    private double moneyMultiplier;


    //Change return value to WheelSegment?
    public void spin() {
        //returns the wheel segment's value that is won
    }

    public void increaseMoneyPerSpace(double amount) {
        //increases the value of each wheel segment by the given value
    }

    public void reduceSpinDuration(double amount) {
        //Decreases the spin duration of the wheel by the given value
    }

    //Change the parameters to (WheelSegment segment, double amount)
    //since changing specific segment?
    public void increaseSegmentSize(double amount) {
        //Increases a specific segment's size/decreases other segment's sizes
    }

    public void increaseMoneyMultiplier(double amount) {
        //Increases the money multiplier by the given value
    }



    public List<WheelSegment> getSegments() {
        return segments;
    }

    public double getSpinDuration() {
        return spinDuration;
    }

    public double getMoneyMultiplier() {
        return moneyMultiplier;
    }

}
