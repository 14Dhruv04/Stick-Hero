package com.stickhero;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class MainScreenController {
    @FXML
    private ImageView playImage;

    @FXML
    private AnchorPane instructionsOverlay;

    @FXML
    private AnchorPane usernameOverlay;

    @FXML
    private TextField usernameField;

    @FXML
    private AnchorPane leaderboardOverlay;

    @FXML
    private VBox leaderboardBox;

    @FXML
    private void startGame() {
        usernameOverlay.setVisible(true);
        usernameOverlay.toFront();
        usernameField.requestFocus();
    }

    @FXML
    private void confirmStartGame() {
        String username = usernameField.getText().trim();
        if(username.isEmpty()) {
            return;
        }

        GameManager.getInstance().setCurrentPlayerName(username);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stickhero/gameScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) playImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showLeaderboard() {
        leaderboardBox.getChildren().clear();
        Label title = new Label("HIGH SCORES");
        title.setStyle("-fx-font-size:24px;" + "-fx-font-weight:bold;" + "-fx-text-fill:white;");
        leaderboardBox.getChildren().add(title);
        int rank = 1;
        for(ScoreEntry entry : GameManager.getInstance().getHighScores()){
            Label scoreLabel = new Label(rank++ + ". "+ entry.getPlayerName()+ "    "+ entry.getScore() + " pts");
            scoreLabel.setStyle("-fx-font-size:18px;" + "-fx-text-fill:white;");
            leaderboardBox.getChildren().add(scoreLabel);
        }
        leaderboardOverlay.setVisible(true);
        leaderboardOverlay.toFront();
    }
    @FXML
    private void hideLeaderboard() {
        leaderboardOverlay.setVisible(false);
    }

    @FXML
    private void showInstructions() {
        instructionsOverlay.setVisible(true);
        instructionsOverlay.toFront();
    }
    @FXML
    private void hideInstructions() {
        instructionsOverlay.setVisible(false);
    }
}
