package com.stickhero;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.control.Label;

import java.util.Optional;

public class GameController {
    @FXML
    private AnchorPane gamePane;

    @FXML
    private Label scoreLabel;

    @FXML
    private ImageView heroImage;

    private Image idleImage;
    private Image runningImage;
    private Avocado currentAvocado;
    private Image avocadoImage;

    @FXML
    private Rectangle leftPlatform;

    @FXML
    private Line stickLine;

    @FXML
    private Label avocadoLabel;
    @FXML
    private AnchorPane pauseOverlay;

    private Player player;
    private Stick stick;
    private static final double FRAME_TIME = 0.016;

    private Platform currentPlatform;
    private Platform nextPlatform;
    private Rectangle nextPlatformRect;

    private PlatformGenerator generator;
    private boolean growingInputActive = false;
    private boolean heroMoving = false;
    private boolean gamePaused = false;
    private Timeline growthTimeline;
    private boolean gameOverTriggered = false;
    private Timeline avocadoCollisionTimeline;

    @FXML
    public void initialize() {
        gamePane.setFocusTraversable(true);

        player = new Player(heroImage,GameManager.getInstance().getCurrentPlayerName());

        idleImage = new Image(getClass().getResourceAsStream("/com/stickhero/Images/Idlestand.png"));
        runningImage = new Image(getClass().getResourceAsStream("/com/stickhero/Images/Runningrun.png"));
        avocadoImage = new Image(getClass().getResourceAsStream("/com/stickhero/Images/avocado.png"));

        currentPlatform = new Platform(leftPlatform.getLayoutX(), leftPlatform.getWidth(), leftPlatform.getHeight());
        double stickBaseX = currentPlatform.getRightEdge();

        double stickBaseY = leftPlatform.getLayoutY();

        stickLine.setLayoutX(stickBaseX);
        stickLine.setLayoutY(stickBaseY);
        stickLine.setVisible(false);

        stick = new Stick(stickLine);

        generator = new PlatformGenerator();

        nextPlatform = generator.generateNext(currentPlatform);
        nextPlatformRect = new Rectangle();

        nextPlatformRect.setWidth(nextPlatform.getWidth());
        nextPlatformRect.setHeight(nextPlatform.getHeight());
        nextPlatformRect.setFill(leftPlatform.getFill());
        nextPlatformRect.setStroke(leftPlatform.getStroke());
        nextPlatformRect.setLayoutX(nextPlatform.getLeftEdge());
        nextPlatformRect.setLayoutY(leftPlatform.getLayoutY());
        gamePane.getChildren().add(nextPlatformRect);

        spawnAvocado();

        growthTimeline = new Timeline(new KeyFrame(Duration.millis(16), e -> stick.grow(FRAME_TIME)));
        growthTimeline.setCycleCount(Timeline.INDEFINITE);

        gamePane.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            System.out.println("Scene Attached");
            newScene.addEventFilter(
                    KeyEvent.KEY_PRESSED,
                    e -> {
                        if(gamePaused) {return;}

                        if(e.getCode() == KeyCode.SPACE) {
                            if(heroMoving){
                                player.flip();
                                return;
                            }
                            if(!growingInputActive) {
                                growingInputActive = true;
                                stick.startGrowing();
                                growthTimeline.play();
                            }
                        }
                    }
            );

            newScene.addEventFilter(
                    KeyEvent.KEY_RELEASED,
                    e -> {
                        if(gamePaused) {return;}

                        if(e.getCode() == KeyCode.SPACE) {
                            if(heroMoving){return;}
                            growingInputActive = false;
                            growthTimeline.stop();
                            stick.stopGrowing();
                            stick.fall(this::checkBridge);
                        }
                    }
            );
        });

        avocadoCollisionTimeline = new Timeline(new KeyFrame(Duration.millis(5), e -> {
            checkAvocadoCollision();
            checkFlippedPlatformCollision();
        }));
        avocadoCollisionTimeline.setCycleCount(Timeline.INDEFINITE);
        avocadoCollisionTimeline.play();

        updateScore();
        updateAvocadoCounter();
        gamePane.requestFocus();
//        System.out.println(gamePane.getScene());
    }

    private void shiftWorld() {
        if(currentAvocado != null) {
            gamePane.getChildren().remove(currentAvocado.getAvocadoImage());
        }

        double shiftDistance = nextPlatform.getLeftEdge() - leftPlatform.getLayoutX();

        TranslateTransition platform1Move = new TranslateTransition(Duration.seconds(1), leftPlatform);
        platform1Move.setByX(-shiftDistance);

        TranslateTransition platform2Move = new TranslateTransition(Duration.seconds(1), nextPlatformRect);
        platform2Move.setByX(-shiftDistance);

        TranslateTransition heroMove = new TranslateTransition(Duration.seconds(1), heroImage);
        heroMove.setByX(-shiftDistance);

        TranslateTransition stickMove = new TranslateTransition(Duration.seconds(1),stickLine);
        stickMove.setByX(-shiftDistance);

        platform1Move.play();
        platform2Move.play();
        heroMove.play();
        stickMove.play();

        platform2Move.setOnFinished(e -> {
            prepareNextRound();
        });
    }

    private void prepareNextRound() {
        if(currentAvocado != null) {
            gamePane.getChildren().remove(currentAvocado.getAvocadoImage());
        }

        double currentX = nextPlatformRect.getLayoutX() + nextPlatformRect.getTranslateX();
        double currentWidth = nextPlatformRect.getWidth();

        currentPlatform = new Platform(currentX, currentWidth, nextPlatformRect.getHeight());

        leftPlatform.setWidth(currentWidth);
        leftPlatform.setHeight(nextPlatformRect.getHeight());

        leftPlatform.setLayoutX(currentX);

        leftPlatform.setTranslateX(0);
        heroImage.setTranslateX(0);
        stickLine.setTranslateX(0);

        gamePane.getChildren().remove(nextPlatformRect);

        nextPlatform = generator.generateNext(currentPlatform);

        nextPlatformRect = new Rectangle();

        nextPlatformRect.setWidth(nextPlatform.getWidth());
        nextPlatformRect.setHeight(nextPlatform.getHeight());
        nextPlatformRect.setFill(leftPlatform.getFill());
        nextPlatformRect.setStroke(leftPlatform.getStroke());
        nextPlatformRect.setLayoutX(nextPlatform.getLeftEdge());
        nextPlatformRect.setLayoutY(leftPlatform.getLayoutY());

        gamePane.getChildren().add(nextPlatformRect);

        spawnAvocado();
        gamePane.getChildren().remove(stickLine);
        createNewStick();
    }

    private void spawnAvocado() {

        if(Math.random() > 0.3) {
            return;
        }

        ImageView avocadoView = new ImageView(avocadoImage);

        avocadoView.setFitWidth(25);
        avocadoView.setFitHeight(25);

        double gap = nextPlatform.getLeftEdge() - currentPlatform.getRightEdge();

        double avocadoX = currentPlatform.getRightEdge() + gap / 2;

        boolean above = Math.random() < 0.5;
        double avocadoY;
        if(above) {
            avocadoY = heroImage.getLayoutY() + 20;
        }
        else {
            avocadoY = heroImage.getLayoutY() + 80;
        }

        avocadoView.setLayoutX(avocadoX);
        avocadoView.setLayoutY(avocadoY);

        gamePane.getChildren().add(avocadoView);

        currentAvocado = new Avocado(avocadoView, avocadoX, avocadoY);
    }

    private void checkAvocadoCollision() {
        if(currentAvocado == null)
            return;

        if(currentAvocado.isCollected())
            return;

        Bounds heroBounds = heroImage.getBoundsInParent();
        Bounds avocadoBounds = currentAvocado.getAvocadoImage().getBoundsInParent();

        if(heroBounds.intersects(avocadoBounds)) {
            currentAvocado.collect();
            gamePane.getChildren().remove(currentAvocado.getAvocadoImage());
            player.increaseAvocados();
            updateAvocadoCounter();
            System.out.println("AVOCADO COLLECTED");
        }
    }

    private void createNewStick() {

        Line newStickLine = new Line();

        newStickLine.setStartX(0);
        newStickLine.setStartY(0);

        newStickLine.setEndX(0);
        newStickLine.setEndY(0);

        newStickLine.setStrokeWidth(4);

        double stickBaseX = leftPlatform.getLayoutX() + leftPlatform.getWidth();

        double stickBaseY = leftPlatform.getLayoutY();

        newStickLine.setLayoutX(stickBaseX);
        newStickLine.setLayoutY(stickBaseY);

        gamePane.getChildren().add(newStickLine);
        newStickLine.setVisible(false);

        stick = new Stick(newStickLine);

        stickLine = newStickLine;
    }
    private void moveHero() {
        heroImage.setImage(runningImage);

        double destinationX = nextPlatform.getLeftEdge();

        TranslateTransition walk = new TranslateTransition(Duration.seconds(1), heroImage);

        walk.setToX(destinationX - heroImage.getLayoutX());

        walk.setOnFinished(e -> {
            heroMoving = false;
            if(player.isFlipped()){
                fallHero();
                return;
            }
            heroImage.setImage(idleImage);
            System.out.println("Reached Platform");
            shiftWorld();
        });
        heroMoving = true;

        walk.play();
    }

    private void updateScore() {
        scoreLabel.setText(String.valueOf(player.getScore()));
    }

    private void moveHeroAndFall() {

        heroImage.setImage(runningImage);

        double destinationX = currentPlatform.getRightEdge() + stick.getLength() - 30;

        TranslateTransition walk = new TranslateTransition(Duration.seconds(1), heroImage);

        walk.setToX(destinationX - heroImage.getLayoutX());

        walk.setOnFinished(e -> {
            heroMoving = false;
            heroImage.setImage(idleImage);
            fallHero();
        });

        heroMoving = true;
        walk.play();
        if(gameOverTriggered){
            walk.stop();
        }
    }

    private void fallHero() {

        TranslateTransition fall = new TranslateTransition(Duration.seconds(0.7),heroImage);
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.1),heroImage);
        rotate.setByAngle(720);
        rotate.play();

        fall.setByY(500);

        fall.setOnFinished(e -> {
            System.out.println("GAME OVER");
            showGameOverScreen();
        });

        fall.play();
    }

    private void checkBridge() {
        double stickTip = currentPlatform.getRightEdge() + stick.getLength();

        if(stickTip >= nextPlatform.getLeftEdge() - 5 && stickTip <= nextPlatform.getRightEdge()) {
            player.increaseScore(1);
            updateScore();
            System.out.println("SUCCESS");
            moveHero();
        }
        else {
//            System.out.println("GAME OVER");
            moveHeroAndFall();
        }
    }

    private void checkFlippedPlatformCollision() {
        if(gameOverTriggered) {
            return;
        }

        if(!player.isFlipped()) {
            return;
        }

        Bounds heroBounds = heroImage.getBoundsInParent();
        Bounds platformBounds = nextPlatformRect.getBoundsInParent();

        if(heroBounds.intersects(platformBounds)) {
            gameOverTriggered = true;
            heroMoving = false;
            fallHero();
        }
    }

    private void updateAvocadoCounter() {
        avocadoLabel.setText(String.valueOf(player.getAvocados()));
    }

    private void showGameOverScreen() {
        try {
            GameManager.getInstance().addScore(player.getScore(), player.getAvocados());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stickhero/gameOverScreen.fxml"));
            Parent root = loader.load();
            GameOverController controller = loader.getController();
            controller.setGameStats(player.getScore(), player.getAvocados());

            System.out.println("gamePane = " + gamePane);
            System.out.println("scene = " + gamePane.getScene());
            System.out.println("window = " + gamePane.getScene().getWindow());

            Stage stage =(Stage) gamePane.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void pauseGame() {
        gamePaused = true;
        pauseOverlay.setVisible(true);
        pauseOverlay.toFront();
    }

    @FXML
    private void resumeGame() {
        gamePaused = false;
        pauseOverlay.setVisible(false);

        gamePane.requestFocus();
    }

    @FXML
    private void restartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stickhero/gameScreen.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) gamePane.getScene().getWindow();

            stage.setScene(new Scene(root));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitGame() {
//        Stage stage =(Stage) gamePane.getScene().getWindow();
//        stage.close();
        showGameOverScreen();
    }
}
