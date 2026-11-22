package edu.utsa.cs3443.hotwheel.model;

import java.util.List;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Wheel {
    private List<WheelSegment> segments;
    private double spinDuration;
    private double moneyMultiplier;

    public Wheel(List<WheelSegment> segments, double spinDuration, double moneyMultiplier) {
        this.segments = segments;
        this.spinDuration = spinDuration;
        this.moneyMultiplier = moneyMultiplier;
    }

    public void increasePayouts(double amount) {
        this.moneyMultiplier += amount;
    }

    public void reduceSpinDuration(double amount) {
        this.spinDuration = Math.max(0.5, this.spinDuration - amount);
    }


    public void increaseSegmentSize(WheelSegment segment, double amount) {
        if (segment == null || !segments.contains(segment) || amount <= 0) {
            return;
        }

        segment.setSize(segment.getSize() + amount);
    }

    //Assumes all segment sizes are non-negative and their total is greater than 0
    public WheelSegment getLandedSegment(double finalRotation) {
        if (segments.isEmpty()) return null;
        /* Based on a fixed pointer at 0 deg, calculates angle based on the
           landing spot of the spinning wheel angle(variable pointer starting at 0 deg)
         */
        double pointerAngle = (360 - (finalRotation % 360)) % 360;
        //Determine how big the circle is
        double totalSize = 0;
        for (WheelSegment segment : segments) {
            totalSize += segment.getSize();
        }

        double cumulativeAngle = 0;
        //Checks what segment gets landed on
        for (WheelSegment segment : segments) {
            //cumulativeAngle is the high end of a segment's range
            /*EX: If the first segment was a 1/4 of the circle, then
            the first iteration of cumulativeAngle would be 90 deg
             */
            cumulativeAngle += 360 * (segment.getSize() / totalSize);
            //if pointer falls between the segment's range return segment
            if (pointerAngle < cumulativeAngle) {
                return segment;
            }
        }

        return segments.get(segments.size() - 1);
    }

    public void increaseMoneyMultiplier(double multiplier) {
        this.moneyMultiplier *= multiplier;
    }

    // Getters and Setters
    public List<WheelSegment> getSegments() { return segments; }
    public double getSpinDuration() { return spinDuration; }
    public double getMoneyMultiplier() { return moneyMultiplier; }
}