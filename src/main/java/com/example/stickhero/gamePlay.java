package com.example.stickhero;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class gamePlay {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Rectangle stick;

    @FXML
    private ImageView hero;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Rectangle first;
    @FXML
    private Rectangle second;
    @FXML
    private Text scoreText;

    private int score = 0;
    private double stickLength = 0;
    private static final double minDistancebwPlatforms = 40;
    private static final double maxDistancebwPlatforms = 40;
    private boolean isIncreasing = false;
    private boolean isFalling = false;
    private boolean isMoving = false;
    private boolean isRotating = false;
    private boolean isGameOver = false;
    private boolean isPlatformGenerated = false;
    private boolean canGeneratePlatform = false;
    private ArrayList<Rectangle> platforms = new ArrayList<>(2);

    private Rotate rotation = new Rotate(0,0,0);

    @FXML
    public void initialize(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        stick.getTransforms().add(rotation);
        platforms.add(first);
        platforms.add(second);
    }
    public void extendStick(MouseEvent event) {
        isIncreasing = true;
        isFalling = false;
        isMoving = false;
        isPlatformGenerated = false;
//        stick.setHeight(stick.getHeight()+10);
    }
    public void dropStick(MouseEvent event){
        isIncreasing = false;
        isFalling = true;
        isMoving = true;
        moveCharacter();
    }
    private void moveCharacter(){
        double firstX = first.localToScene(first.getWidth(),0).getX();
        double firstY = first.localToScene(first.getWidth(),0).getY();
        double secondX = second.localToScene(0,0).getX();
        double secondY = second.localToScene(0,0).getY();
        double secondXX = second.localToScene(second.getWidth(),0).getX();
        double secondYY = second.localToScene(second.getWidth(),0).getY();
        double distance = Math.sqrt(Math.pow(secondX - firstX, 2) + Math.pow(secondY - firstY, 2));
        double exceedDistance = Math.sqrt(Math.pow(secondXX - firstX,2)+ Math.pow(secondYY-firstY,2));
        double distanceToMove = stickLength - Math.abs(second.getTranslateX()-firstX);

        if(stickLength>=distance &&  stickLength<=exceedDistance) {

//
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), second);
            translateTransition.setToX(first.getTranslateX());
            translateTransition.play();

            for (Rectangle platform : platforms) {
                TranslateTransition platformMove = new TranslateTransition(Duration.seconds(1), platform);
                platformMove.setByX(-distanceToMove);
                platformMove.play();
            }

            if (!isPlatformGenerated) {
                generatePlatform();
                isPlatformGenerated = true;
            }
            canGeneratePlatform = false;
        }
        else{
//            isGameOver = true;
//            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3),hero);
//            translateTransition.setToX(hero.getX()+stickLength);
//            translateTransition.setOnFinished(event->theFall());
//            translateTransition.play();

            isGameOver = true;
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3), hero);
            translateTransition.setToX(hero.getX() + stickLength);
            translateTransition.setOnFinished(event -> theFall());
            translateTransition.play();
//            System.out.println("dayumn");
        }
    }

//    private void resetStick(){
//        stickLength = 0;
//        stick.setHeight(stickLength);
//        stick.setTranslateY(0);
//        isPlatformGenerated = false;
//        canGeneratePlatform = true;
//    }
    private void theFall(){
        TranslateTransition fall = new TranslateTransition(Duration.seconds(3),hero);
        double fallY = gamePane.getHeight();
        fall.setToY(fallY);
        fall.setOnFinished(event-> System.out.println("Game Over"));
        fall.play();
    }
    private boolean flag = false;
    private void update(){
        if(isIncreasing){
            stickLength+=2;
            stick.setTranslateY(stick.getTranslateY()-2);
        }
        stick.setHeight(stickLength);
        if(isFalling) {
//            stick.setTranslateX(stick.getTranslateX()+rotationSpeed);
            double pivotX = stick.getX()+ stick.getWidth()/2;
            double pivotY = stick.getY() + stickLength;
            if(!isRotating && rotation.getAngle()<90) {
                rotateStick(pivotX,pivotY);
            }
        }
//        if(isMoving && !isFalling){
//            score++;
//            scoreText.setText(new String (String.valueOf(score)));
//        }
        if(isMoving && !platforms.isEmpty()){
            if(!flag){
                score++;
                scoreText.setText(Integer.toString(score));
                flag =true;
            }
            for(Rectangle platform : platforms){
                platform.setTranslateX(platform.getTranslateX()-2);
            }
            generatePlatform();
            screenMove();
        }
        else{
            flag = false;
        }
    }
    private void rotateStick(double pivotX, double pivotY) {
        isRotating = true;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(rotation.angleProperty(), 90))
        );

        timeline.setOnFinished(event -> {
            isRotating = false;
            rotation.setAngle(90);
        });
        rotation.setPivotX(pivotX);
        rotation.setPivotY(pivotY);

        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.play();
    }
    private void screenMove(){
        platforms.remove(1);
        second.setTranslateX(first.getTranslateX()-first.getX());
    }
    private void generatePlatform(){
        Rectangle platform = new Rectangle(119,191);
        Color color = Color.web("#0f6e22");
        platform.setFill(color);

        platform.setX(second.getLayoutX());
        if(!platforms.isEmpty()){
            Rectangle lastPlatform = platforms.get(platforms.size()-1);
            double newX = lastPlatform.getTranslateX()+lastPlatform.getWidth()+100;

            platform.setTranslateX(newX);
            double maxWidth = gamePane.getWidth()/2;
            if(newX+platform.getWidth()<=gamePane.getWidth()){
                platform.setTranslateX(newX);
                platform.setWidth(Math.min(maxWidth,platform.getWidth()));
            }
            else{
                platform.setTranslateX(gamePane.getWidth()-platform.getWidth());
            }
        }
        else{
            platform.setTranslateX(second.getX());
        }
//        platform.setTranslateX(second.getX());
        platform.setTranslateY(second.getY());

        platforms.add(platform);
        gamePane.getChildren().add(platform);

    }

}

