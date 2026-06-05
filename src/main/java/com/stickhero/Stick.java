package com.stickhero;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Stick {
    private double length = 0;
    private boolean growing = false;
    private boolean fallen  = false;
    private Line line;       // JavaFX Line node
    private double baseX, baseY;
    private static final double GROW_SPEED = 150;

    public Stick(Line line){
        this.line = line;
        this.baseX = line.getStartX();
        this.baseY = line.getStartY();
    }

    // Key methods:
    public void startGrowing() {
        line.setVisible(true);
        growing = true;
    }
    public void stopGrowing()  { growing = false; }

    // Called every frame by the game loop
    public void grow(double delta) {
        if (growing) {
            if(!line.isVisible()){
                line.setVisible(true);
            }
            length += delta * GROW_SPEED;
            line.setEndY(-length); // grows upward
        }
    }

    public double getLength() {
        return length;
    }

    public Line getLine() {
        return line;
    }

    public boolean isFallen() {
        return fallen;
    }
    public boolean isGrowing() {
        return growing;
    }

    // Rotate 90° clockwise to "fall"
    public void fall(Runnable onComplete) {

        Rotate rotate = new Rotate(
                0,
                line.getStartX(),
                line.getStartY()
        );

        line.getTransforms().add(rotate);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),new KeyValue(rotate.angleProperty(),90)));

        timeline.setOnFinished(e -> {
            fallen = true;
            onComplete.run();
        });

        timeline.play();
    }

    public void reset() {

        length = 0;
        growing = false;
        fallen = false;

        line.setRotate(0);

        line.setEndX(line.getStartX());
        line.setEndY(line.getStartY());
    }
}
