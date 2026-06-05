package com.stickhero;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {
    @BeforeEach
    void setup() {
        GameManager.getInstance().clearScores();
    }

    @Test
    void singletonReturnsSameInstance() {
        GameManager first = GameManager.getInstance();
        GameManager second = GameManager.getInstance();

        assertSame(first, second);
    }

    @Test
    void usernameStoredCorrectly(){
        GameManager manager = GameManager.getInstance();
        manager.setCurrentPlayerName("Player 1");

        assertEquals("Player 1", manager.getCurrentPlayerName());
    }

    @Test
    void scoreAddedToLeaderboard() {
        GameManager manager = GameManager.getInstance();
        manager.setCurrentPlayerName("Player 1");
        int initialSize = manager.getHighScores().size();
        manager.addScore(20,5);

        assertEquals(initialSize + 1,manager.getHighScores().size());
    }

    @Test
    void leaderboardSortedDescending() {
        GameManager manager = GameManager.getInstance();
        manager.setCurrentPlayerName("A");
        manager.addScore(10, 0);

        manager.setCurrentPlayerName("B");
        manager.addScore(50, 0);

        manager.setCurrentPlayerName("C");
        manager.addScore(30, 0);

        assertEquals(50, manager.getHighScores().get(0).getScore());
    }
}