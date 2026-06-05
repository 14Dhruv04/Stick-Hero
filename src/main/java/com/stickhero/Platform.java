package com.stickhero;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {

    private double x;
    private double width;
    private double height;

    public Platform(double x, double width, double height) {
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public double getLeftEdge() {
        return x;
    }

    public double getRightEdge() {
        return x + width;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
