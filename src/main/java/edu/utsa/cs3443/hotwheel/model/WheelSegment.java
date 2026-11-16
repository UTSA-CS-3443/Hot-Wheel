package edu.utsa.cs3443.hotwheel.model;

public class WheelSegment {
    private double size;
    private double value;
    private String label;

    public WheelSegment(double size, double value, String label) {
        this.size = size;
        this.value = value;
        this.label = label;
    }

    public double getSize() { return size; }
    public void setSize(double size) { this.size = size; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}