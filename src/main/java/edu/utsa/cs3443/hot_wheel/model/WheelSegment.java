package edu.utsa.cs3443.hot_wheel.model;

//import javafx.scene.paint.Color to create certain colors for spaces?

public class WheelSegment {
    private double size;
    private double value;
    private String label;

    public WheelSegment(double size, double value, String label) {
        this.size = size;
        this.value = value;
        this.label = label;
    }



    public double getValue() {
        return value;
    }

    public void setValue(double amount) {
        value = amount;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double amount) {
        size = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String newText) {
        label = newText;
    }

}
