package edu.utsa.cs3443.hotwheel.controller;

import edu.utsa.cs3443.hotwheel.manager.GameState;
import edu.utsa.cs3443.hotwheel.model.Upgrade;
import edu.utsa.cs3443.hotwheel.model.UpgradeType;
import edu.utsa.cs3443.hotwheel.model.WheelSegment;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import edu.utsa.cs3443.hotwheel.model.Wheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShopController {

    @FXML private VBox shopRoot;
    @FXML private Label statusLabel;
    @FXML private VBox upgradeListBox;

    private final List<Upgrade> upgrades = new ArrayList<>();
    private GameState gameState;
    private Runnable onAfterPurchase;
    private String selectedSegmentLabel = "Platinum";

    public void initShop(GameState gameState, BooleanProperty wheelSpinning, Runnable onAfterPurchase) {
        this.gameState = gameState;
        this.onAfterPurchase = onAfterPurchase;

        syncSegmentChoices();

        if (shopRoot != null && wheelSpinning != null) {
            shopRoot.disableProperty().bind(wheelSpinning);
            wheelSpinning.addListener((obs, wasSpinning, isSpinning) -> {
                if (statusLabel != null) {
                    statusLabel.setText(isSpinning ? "Spin in progress..." : "Upgrades available between spins");
                }
            });
        }

        loadDefaultUpgrades();
        refreshShop();
    }

    public void applySavedLevels(Map<UpgradeType, Integer> savedLevels) {
        if (savedLevels == null || savedLevels.isEmpty()) {
            return;
        }

        for (Upgrade upgrade : upgrades) {
            int targetLevel = savedLevels.getOrDefault(upgrade.getType(), 1);
            while (upgrade.getLevel() < targetLevel) {
                if (gameState != null && gameState.getWheel() != null) {
                    Wheel wheel = gameState.getWheel();
                    WheelSegment targetSegment = null;

                    if (upgrade.getType() == UpgradeType.SEGMENT_SIZE) {
                        for (WheelSegment seg : wheel.getSegments()) {
                            if (seg.getLabel().equals(selectedSegmentLabel)) {
                                targetSegment = seg;
                                break;
                            }
                        }
                    }

                    upgrade.applyUpgrade(wheel, targetSegment);
                }
                upgrade.increaseLevel();
            }
        }
        refreshShop();
    }

    public List<Upgrade> getUpgrades() {
        return Collections.unmodifiableList(upgrades);
    }

    private void syncSegmentChoices() {
        if (gameState != null && gameState.getWheel() != null && gameState.getWheel().getSegments() != null) {
            List<WheelSegment> segments = gameState.getWheel().getSegments();
            if (!segments.isEmpty()) {
                boolean currentStillExists = segments.stream()
                        .anyMatch(s -> s.getLabel().equals(selectedSegmentLabel));
                if (!currentStillExists) {
                    selectedSegmentLabel = segments.get(segments.size() - 1).getLabel();
                }
            }
        }
    }

    private void loadDefaultUpgrades() {
        upgrades.clear();
        upgrades.add(new Upgrade(
                "Additive Money Bonus",
                "Adds flat bonus money to spin payouts.",
                60.0,
                0,
                false,
                UpgradeType.PAYOUT_BOOST
        ));
        upgrades.add(new Upgrade(
                "Multiplicative Money Bonus",
                "Multiplies money earned from spins.",
                75.0,
                0,
                false,
                UpgradeType.MONEY_MULTIPLIER
        ));
        upgrades.add(new Upgrade(
                "Wheel Segment Size",
                "Increases the size of your chosen wheel segment.",
                100.0,
                0,
                false,
                UpgradeType.SEGMENT_SIZE
        ));
        upgrades.add(new Upgrade(
                "Wheel Cooldown Decrease",
                "Reduces wheel spin / cooldown duration.",
                50.0,
                0,
                false,
                UpgradeType.SPIN_DURATION
        ));
    }

    public void addUpgrade(Upgrade upgrade) {
        if (upgrade != null) {
            upgrades.add(upgrade);
            refreshShop();
        }
    }

    public String getSelectedSegmentLabel() {
        return selectedSegmentLabel;
    }

    public void refreshShop() {
        if (upgradeListBox == null) return;
        syncSegmentChoices();
        upgradeListBox.getChildren().clear();

        double currentMoney = (gameState != null) ? gameState.getMoney() : 0.0;
        int currentPrestige = (gameState != null) ? gameState.getPrestigePoints() : 0;

        for (Upgrade upgrade : upgrades) {
            upgradeListBox.getChildren().add(buildUpgradeCard(upgrade, currentMoney, currentPrestige));
        }
    }

    private VBox buildUpgradeCard(Upgrade upgrade, double currentMoney, int currentPrestige) {
        VBox card = new VBox(8);
        card.getStyleClass().add("upgrade-card");

        HBox headerRow = new HBox(8);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(upgrade.getName() + " (Lv. " + upgrade.getLevel() + ")");
        titleLabel.getStyleClass().add("upgrade-title");
        titleLabel.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(titleLabel, spacer);

        Label descLabel = new Label(upgrade.getDescription());
        descLabel.setWrapText(true);
        descLabel.getStyleClass().add("upgrade-desc");

        card.getChildren().addAll(headerRow, descLabel);

        if (upgrade.getType() == UpgradeType.SEGMENT_SIZE) {
            HBox selectorRow = new HBox(8);
            selectorRow.setAlignment(Pos.CENTER_LEFT);

            Label pickerLabel = new Label("Target Segment:");
            pickerLabel.getStyleClass().add("upgrade-desc");

            ComboBox<String> segmentPicker = new ComboBox<>();
            segmentPicker.setItems(FXCollections.observableArrayList(getAvailableSegmentLabels()));
            segmentPicker.setValue(selectedSegmentLabel);
            segmentPicker.setOnAction(e -> {
                if (segmentPicker.getValue() != null) {
                    selectedSegmentLabel = segmentPicker.getValue();
                }
            });

            selectorRow.getChildren().addAll(pickerLabel, segmentPicker);
            card.getChildren().add(selectorRow);
        }

        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        double moneyCost = upgrade.getCostMoney();
        int prestigeCost = upgrade.getCostPrestige();

        if (moneyCost > 0) {
            Button moneyBuyButton = new Button(String.format("$%.2f", moneyCost));
            moneyBuyButton.getStyleClass().add("casino-buy-btn");
            moneyBuyButton.setMinHeight(36);
            moneyBuyButton.setPrefHeight(36);
            moneyBuyButton.setMinWidth(Button.USE_PREF_SIZE);
            moneyBuyButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(moneyBuyButton, Priority.ALWAYS);
            boolean canAffordMoney = gameState != null && currentMoney >= moneyCost;
            moneyBuyButton.setDisable(!canAffordMoney);
            moneyBuyButton.setOnAction(e -> handleBuyWithMoney(upgrade));
            buttonRow.getChildren().add(moneyBuyButton);
        }

        if (prestigeCost > 0) {
            Button prestigeBuyButton = new Button(prestigeCost + " Prestige");
            prestigeBuyButton.getStyleClass().add("casino-buy-btn");
            prestigeBuyButton.setMinHeight(36);
            prestigeBuyButton.setPrefHeight(36);
            prestigeBuyButton.setMinWidth(Button.USE_PREF_SIZE);
            prestigeBuyButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(prestigeBuyButton, Priority.ALWAYS);
            boolean canAffordPrestige = gameState != null && currentPrestige >= prestigeCost;
            prestigeBuyButton.setDisable(!canAffordPrestige);
            prestigeBuyButton.setOnAction(e -> handleBuyWithPrestige(upgrade));
            buttonRow.getChildren().add(prestigeBuyButton);
        }

        if (!buttonRow.getChildren().isEmpty()) {
            card.getChildren().add(buttonRow);
        }

        return card;
    }

    private List<String> getAvailableSegmentLabels() {
        List<String> labels = new ArrayList<>();
        if (gameState != null && gameState.getWheel() != null && gameState.getWheel().getSegments() != null) {
            for (WheelSegment seg : gameState.getWheel().getSegments()) {
                if (seg != null && seg.getLabel() != null) {
                    labels.add(seg.getLabel());
                }
            }
        }
        if (labels.isEmpty()) {
            labels.add("Bronze");
            labels.add("Silver");
            labels.add("Gold");
            labels.add("Platinum");
        }
        return labels;
    }

    private void handleBuyWithMoney(Upgrade upgrade) {
        if (gameState == null) return;
        double cost = upgrade.getCostMoney();
        if (cost <= 0 || gameState.getMoney() < cost) return;

        gameState.addMoney(-cost);
        applyPurchasedUpgrade(upgrade);
    }

    private void handleBuyWithPrestige(Upgrade upgrade) {
        if (gameState == null) return;
        int prestigeCost = upgrade.getCostPrestige();
        if (prestigeCost <= 0 || gameState.getPrestigePoints() < prestigeCost) return;

        gameState.setPrestigePoints(gameState.getPrestigePoints() - prestigeCost);
        applyPurchasedUpgrade(upgrade);
    }

    private void applyPurchasedUpgrade(Upgrade upgrade) {
        if (gameState == null || gameState.getWheel() == null) {
            return;
        }

        Wheel wheel = gameState.getWheel();
        WheelSegment selectedSegment = null;

        if (upgrade.getType() == UpgradeType.SEGMENT_SIZE) {
            for (WheelSegment segment : wheel.getSegments()) {
                if (segment.getLabel().equals(selectedSegmentLabel)) {
                    selectedSegment = segment;
                    break;
                }
            }

            if (selectedSegment == null) {
                if (statusLabel != null) {
                    statusLabel.setText("Invalid wheel segment selected.");
                }
                return;
            }
        }

        upgrade.applyUpgrade(wheel, selectedSegment);
        upgrade.increaseLevel();

        refreshShop();

        if (onAfterPurchase != null) {
            onAfterPurchase.run();
        }
    }
}