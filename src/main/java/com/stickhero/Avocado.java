package com.stickhero;

import javafx.scene.image.ImageView;

public class Avocado {
    private ImageView avocadoImage;

    private double x;
    private double y;

    private boolean collected;

    public Avocado(ImageView avocadoImage, double x, double y) {
        this.avocadoImage = avocadoImage;
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public void collect(){
        collected  = true;
        avocadoImage.setVisible(false);
    }

    public boolean isCollected(){
        return collected;
    }
    public ImageView getAvocadoImage() {
        return avocadoImage;
    }
}
