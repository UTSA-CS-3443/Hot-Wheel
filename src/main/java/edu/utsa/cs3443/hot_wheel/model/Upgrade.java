package edu.utsa.cs3443.hot_wheel.model;

public class Upgrade {
    //Change these to final?
    private String upgradeName;
    private String description;
    private boolean permanent;

    private int level;
    private double costInMoney;
    private UpgradeType type;

    public Upgrade(String upgradeName, String description, int level,
                   double costInMoney, UpgradeType type, boolean permanent) {
        this.upgradeName = upgradeName;
        this.description = description;
        this.level = level;
        this.costInMoney = costInMoney;
        this.type = type;
        this.permanent = permanent;
    }
    public void increaseLevel() {
        //Increase the current level by 1 or however much we choose
    }

    public void applyUpgrade(Wheel wheel) {
        //Applies an upgrade to a given wheel from the upgrade type enum
    }



    public String getUpgradeName() {
        return upgradeName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public int getLevel() {
        return level;
    }

    public double getCostInMoney() {
        return costInMoney;
    }

    public UpgradeType getType() {
        return type;
    }
}
