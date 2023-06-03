package com.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Log {
    private Text[] mines;
    private Text[] moves;
    private Text[] time;
    private Text[] winner;
    private Stage stage;

    public Log() {
        stage = new Stage();
        GridPane gridPane = new GridPane();
        mines = new Text[5];
        moves = new Text[5];
        time = new Text[5];
        winner = new Text[5];
        Button ok = new Button("Ok");
        VBox vBox = new VBox(gridPane, ok);
        Scene scene = new Scene(vBox);
        stage.setTitle("Rounds");
        ok.setOnAction(e -> stage.close());
        gridPane.add(new Text("  Mines  "), 0, 0);
        gridPane.add(new Text("  Moves  "), 1, 0);
        gridPane.add(new Text("  Time (s)  "), 2, 0);
        gridPane.add(new Text("  Winner  "), 3, 0);
        for (int i = 0; i < 5; i++) {
            mines[i] = new Text();
            moves[i] = new Text();
            time[i] = new Text();
            winner[i] = new Text();
            gridPane.add(mines[i], 0, i + 1);
            gridPane.add(moves[i], 1, i + 1);
            gridPane.add(time[i], 2, i + 1);
            gridPane.add(winner[i], 3, i + 1);
        }
        gridPane.getChildren().forEach(node -> {
            GridPane.setHalignment(node, HPos.CENTER);
        });
        gridPane.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    public void init() {
        File file = new File("medialab/rounds.txt");
        if (file.exists() && file.isFile())
            try (Scanner scanner = new Scanner(file)) {
                for (int i = 0; i < 5 && scanner.hasNext(); i++) {
                    mines[i].setText(scanner.next());
                    moves[i].setText(scanner.next());
                    time[i].setText(scanner.next());
                    winner[i].setText(scanner.next());
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.show();
            }
    }

    public void show() {
        stage.show();
    }
}