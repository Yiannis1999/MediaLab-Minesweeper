package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

public class Game {

    private int rows;
    private int columns;
    private int mines;
    private Float time;
    private boolean supermine;
    private Grid grid;
    private String str;
    private Text text;
    private Float timer;
    private Timeline timeline;
    private VBox vBox;

    public Game(int rows, int columns, int mines, float time, boolean supermine) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.time = time;
        this.supermine = supermine;
        grid = new Grid(rows, columns, supermine);
        str = "Mines: %d  Flags: %d  Moves: %d  Time: %.2fs";
        text = new Text(String.format(str, mines, 0, 0, time));
        vBox = new VBox(text, grid.getGridPane());
        text.setStyle("-fx-font-size: 14px;");
        vBox.setAlignment(Pos.CENTER);
    }

    public void start() {
        List<Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer, Integer>>();
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++)
                coords.add(new Pair<>(y, x));
        Collections.shuffle(coords);

        File file = new File("medialab/mines.txt");
        try (FileWriter writer = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(writer)) {
            int i;
            for (i = 0; i < mines; i++)
                printWriter.printf(i == mines - 1 ? "%d,%d,%d" : "%d,%d,%d\n", coords.get(i).getKey(),
                        coords.get(i).getValue(), i == 0 && supermine ? 1 : 0);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.show();
        }

        grid.init(coords.subList(0, mines));
        timer = time;
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.01),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent arg0) {
                                timer -= 0.01f;
                                if (timer <= 0 || grid.isGameOver()) {
                                    text.setText("Game Over");
                                    grid.solution();
                                    timeline.stop();
                                    log();
                                } else if (grid.getLeft() == 0) {
                                    text.setText("Congratulations");
                                    timeline.stop();
                                    log();
                                } else
                                    text.setText(String.format(str, mines, grid.getFlags(), grid.getMoves(), timer));
                            }
                        }));
        timeline.playFromStart();
    }

    private void log() {
        File file = new File("medialab/rounds.txt");
        String[] buffer = new String[5];
        int i = 0, j;
        if (file.exists() && file.isFile())
            try (Scanner scanner = new Scanner(file)) {
                for (; i < 5 && scanner.hasNext(); i++)
                    buffer[i] = scanner.nextLine();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        try (FileWriter writer = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(writer)) {
            String winner = grid.isGameOver() ? "Computer" : "Player";
            printWriter.printf("%d %d %.2f %s\n", mines, grid.getMoves(), time - timer, winner);
            for (j = 0; j < Integer.min(i, 4); j++)
                printWriter.println(buffer[j]);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void solution() {
        grid.solution();
    }

    public VBox getVBox() {
        return vBox;
    }
}