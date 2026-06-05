package com.stickhero;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player {

    private ImageView hero;

    private String name;
    private int score;
    private int avocados;

    private double x;
    private double y;

    private boolean flipped;
    private static final double FLIP_OFFSET = 60;

    public Player(ImageView hero, String name) {

        this.hero = hero;
        this.name = name;

        this.score = 0;
        this.avocados = 0;

        this.flipped = false;

        this.x = hero.getLayoutX();
        this.y = hero.getLayoutY();
    }

    public void setPosition(double x, double y) {

        this.x = x;
        this.y = y;

        hero.setLayoutX(x);
        hero.setLayoutY(y);
    }

    public void move(double newX) {

        this.x = newX;
        hero.setLayoutX(newX);
    }

    public void flip() {

        flipped = !flipped;

        RotateTransition rotate = new RotateTransition(Duration.millis(5),hero);
        rotate.setToAngle(flipped ? 180 : 0);

        TranslateTransition transition = new TranslateTransition(Duration.millis(5),hero);
        transition.setToY(flipped ? FLIP_OFFSET : 0);

        rotate.play();
        transition.play();
    }

    public void increaseScore(int points) {
        score += points;
    }

    public void increaseAvocados() {
        avocados++;
    }
    public int getAvocados() {
        return avocados;
    }

    public boolean isFlipped() {return flipped;}

    public void resetFlip() {
        flipped = false;
        hero.setRotate(0);
        hero.setTranslateY(0);
    }

    public ImageView getHero() {
        return hero;
    }

    public int getScore() {
        return score;
    }
}
