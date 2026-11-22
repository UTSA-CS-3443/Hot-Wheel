package edu.utsa.cs3443.hotwheel.manager;

import edu.utsa.cs3443.hotwheel.model.Upgrade;
import edu.utsa.cs3443.hotwheel.model.UpgradeType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SaveFileManager {
    private static final String SAVE_DIR = "saves/";
    private final Map<UpgradeType, Integer> loadedUpgradeLevels = new EnumMap<>(UpgradeType.class);

    public SaveFileManager() {
        File directory = new File(SAVE_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public Map<UpgradeType, Integer> getLoadedUpgradeLevels() {
        return new EnumMap<>(loadedUpgradeLevels);
    }

    public void saveGame(int slotNumber, GameState gameState) {
        saveGame(slotNumber, gameState, null);
    }

    public void saveGame(int slotNumber, GameState gameState, List<Upgrade> upgrades) {
        try (FileWriter writer = new FileWriter(SAVE_DIR + "slot" + slotNumber + ".txt")) {
            writer.write("Money:" + gameState.getMoney() + "\n");
            writer.write("Prestige:" + gameState.getPrestigePoints() + "\n");
            writer.write("TimeRemaining:" + gameState.getTimeRemaining() + "\n");
            if (upgrades != null) {
                for (Upgrade upgrade : upgrades) {
                    if (upgrade != null && upgrade.getType() != null) {
                        writer.write("Upgrade:" + upgrade.getType().name() + ":" + upgrade.getLevel() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(int slotNumber, GameState gameState) {
        loadedUpgradeLevels.clear();
        File file = new File(SAVE_DIR + "slot" + slotNumber + ".txt");
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("Money:")) {
                    gameState.setMoney(Double.parseDouble(line.substring("Money:".length())));
                } else if (line.startsWith("Prestige:")) {
                    gameState.setPrestigePoints(Integer.parseInt(line.substring("Prestige:".length())));
                } else if (line.startsWith("TimeRemaining:")) {
                    double timeRemaining = Double.parseDouble(line.substring("TimeRemaining:".length()));
                    gameState.setTimeRemaining(Math.max(0.0, timeRemaining));
                } else if (line.startsWith("Upgrade:")) {
                    String[] parts = line.split(":");
                    if (parts.length == 3) {
                        try {
                            UpgradeType type = UpgradeType.valueOf(parts[1]);
                            int level = Math.max(1, Integer.parseInt(parts[2]));
                            loadedUpgradeLevels.put(type, level);
                        } catch (IllegalArgumentException ignored) {
                            // Skip unrecognized upgrade entries cleanly
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSave(int slotNumber) {
        File file = new File(SAVE_DIR + "slot" + slotNumber + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean saveExists(int slotNumber) {
        File file = new File(SAVE_DIR + "slot" + slotNumber + ".txt");
        return file.exists();
    }
}