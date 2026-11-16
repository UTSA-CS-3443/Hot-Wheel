package edu.utsa.cs3443.hotwheel.model;

import java.util.List;

public class Wheel {
    private List<WheelSegment> segments;
    private double spinDuration;
    private double moneyMultiplier;

    public Wheel(List<WheelSegment> segments, double spinDuration, double moneyMultiplier) {
        this.segments = segments;
        this.spinDuration = spinDuration;
        this.moneyMultiplier = moneyMultiplier;
    }

    public WheelSegment spin() {
        if (segments.isEmpty()) return null;
        int index = (int) (Math.random() * segments.size());
        return segments.get(index);
    }

    public void increasePayouts(double amount) {
        this.moneyMultiplier += amount;
    }

    public void reduceSpinDuration(double amount) {
        this.spinDuration = Math.max(0.5, this.spinDuration - amount);
    }

    public void increaseGreatSegmentSize(double amount) {
        if (!segments.isEmpty()) {
            segments.get(0).setSize(segments.get(0).getSize() + amount);
        }
    }

    public void increaseMoneyMultiplier(double multiplier) {
        this.moneyMultiplier *= multiplier;
    }

    // Getters and Setters
    public List<WheelSegment> getSegments() { return segments; }
    public double getSpinDuration() { return spinDuration; }
    public double getMoneyMultiplier() { return moneyMultiplier; }
}