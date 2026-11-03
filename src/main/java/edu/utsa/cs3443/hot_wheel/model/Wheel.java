package edu.utsa.cs3443.hot_wheel.model;

import java.util.ArrayList;
import java.util.List;

public class Wheel extends WheelSegment {
    private List<WheelSegment> segments = new ArrayList<WheelSegment>();
    private double spinDuration;
    private double moneyMultiplier;


    void spin() {
        //returns the wheel segment's value that is won
    }


    void increaseMoneyPerSpace(double amount) {
        //increases the value of each wheel segment by the given value
    }


    void reduceSpinDuration(double amount) {
        //Decreases the spin duration of the wheel by the given value
    }


    //Change the parameters to (WheelSegment segment, double amount)
    //since changing specific segment?
    void increaseSegmentSize(double amount) {
        //Increases a specific segment's size/decreases other segment's sizes
    }

    void increaseMoneyMultiplier(double amount) {
        //Increases the money multiplier by the given value
    }

}
