package edu.utsa.cs3443.hotwheel.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SaveFileManager {
    private static final String SAVE_DIR = "saves/";

    public SaveFileManager() {
        File directory = new File(SAVE_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public void saveGame(int slotNumber, GameState gameState) {
        try (FileWriter writer = new FileWriter(SAVE_DIR + "slot" + slotNumber + ".txt")) {
            writer.write("Money:" + gameState.getMoney() + "\n");
            writer.write("Prestige:" + gameState.getPrestigePoints() + "\n");
            writer.write("TimeRemaining:" + gameState.getTimeRemaining() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(int slotNumber, GameState gameState) {
        File file = new File(SAVE_DIR + "slot" + slotNumber + ".txt");
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Money:")) {
                    gameState.setMoney(Double.parseDouble(line.substring("Money:".length())));
                } else if (line.startsWith("Prestige:")) {
                    gameState.setPrestigePoints(Integer.parseInt(line.substring("Prestige:".length())));
                } else if (line.startsWith("TimeRemaining:")) {
                    gameState.setTimeRemaining(Double.parseDouble(line.substring("TimeRemaining:".length())));
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