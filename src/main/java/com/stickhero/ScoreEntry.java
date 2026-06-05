package com.stickhero;

import java.io.Serializable;

public class ScoreEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String playerName;
    private int score;
    private int avocados;

    public ScoreEntry(String playerName, int score, int avocados) {
        this.playerName = playerName;
        this.score = score;
        this.avocados = avocados;
    }

    public String getPlayerName() {
        return playerName;
    }
    public int getScore() {
        return score;
    }
    public int getAvocados() {
        return avocados;
    }
}