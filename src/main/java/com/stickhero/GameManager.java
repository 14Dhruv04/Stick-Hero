package com.stickhero;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private String currentPlayerName;
    private final List<ScoreEntry> highScores;

    private static final String SAVE_FILE = "src/main/data/leaderboard.dat";

    private GameManager() {
        highScores = new ArrayList<>();
        loadScores();
    }

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setCurrentPlayerName(String name){
        currentPlayerName = name;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void addScore(int score, int avocados) {
        while(highScores.size() > 5) {
            highScores.remove(highScores.size() - 1);
        }
        highScores.add(new ScoreEntry(currentPlayerName, score, avocados));
        highScores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        saveScores();
    }

    @SuppressWarnings("unchecked")
    private void loadScores() {
        File file = new File(SAVE_FILE);

        if(!file.exists()) {
            return;
        }

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            highScores.clear();
            highScores.addAll((List<ScoreEntry>) in.readObject());
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))){
            out.writeObject(highScores);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void clearScores() {
        highScores.clear();
    }
    public List<ScoreEntry> getHighScores() {
        return highScores;
    }
}