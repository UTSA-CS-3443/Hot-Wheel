package edu.utsa.cs3443.hotwheel.model;

public class Upgrade {
    private String name;
    private String description;
    private int level;
    private double costMoney;
    private int costPrestige;
    private boolean permanent;
    private UpgradeType type;

    public Upgrade(String name, String description, double costMoney, int costPrestige, boolean permanent, UpgradeType type) {
        this.name = name;
        this.description = description;
        this.level = 1;
        this.costMoney = costMoney;
        this.costPrestige = costPrestige;
        this.permanent = permanent;
        this.type = type;
    }

    public void applyUpgrade(Wheel wheel) {
        switch (type) {
            case SPIN_DURATION:
                wheel.reduceSpinDuration(0.2 * level);
                break;
            case MONEY_MULTIPLIER:
                wheel.increaseMoneyMultiplier(1.1);
                break;
            case SEGMENT_SIZE:
                wheel.increaseGreatSegmentSize(5.0);
                break;
            case PAYOUT_BOOST:
                wheel.increasePayouts(10.0 * level);
                break;
        }
    }

    public void increaseLevel() {
        this.level++;
        this.costMoney *= 1.5; // Scale cost with level
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public double getCostMoney() { return costMoney; }
    public int getCostPrestige() { return costPrestige; }
    public boolean isPermanent() { return permanent; }
    public UpgradeType getType() { return type; }
}