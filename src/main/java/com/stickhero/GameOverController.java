package com.stickhero;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private Label finalScoreLabel;

    @FXML
    private Label finalAvocadoLabel;

    public void setGameStats(
            int score,
            int avocados
    ) {

        finalScoreLabel.setText(
                String.valueOf(score)
        );

        finalAvocadoLabel.setText(
                String.valueOf(avocados)
        );
    }

    @FXML
    private void restartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stickhero/gameScreen.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) finalScoreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToMenu(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stickhero/mainScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) finalScoreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}