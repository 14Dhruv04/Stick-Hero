package com.stickhero;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void scoreIncreasesCorrectly() {
        ImageView hero = new ImageView();
        Player player = new Player(hero,"Player 1");
        player.increaseScore(1);
        assertEquals(1, player.getScore());
    }

    @Test
    void avocadosIncreasesCorrectly() {
        ImageView hero = new ImageView();
        Player player = new Player(hero,"Player 2");
        player.increaseAvocados();
        assertEquals(1, player.getAvocados());
    }
}
