package com.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Loader {
    private Stage stage;
    private Button load;
    private ComboBox<String> comboBox;

    public Loader() {
        stage = new Stage();
        Text scenario_id = new Text("SCENARIO-ID");
        comboBox = new ComboBox<String>();
        Button cancel = new Button("Cancel");
        load = new Button("Load");
        HBox hBox = new HBox(cancel, load);
        VBox vBox = new VBox(scenario_id, comboBox, hBox);
        Scene scene = new Scene(vBox);
        stage.setTitle("Load");
        comboBox.setEditable(true);
        cancel.setOnAction(e -> stage.close());
        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    public void init() {
        comboBox.getItems().clear();
        File directory = new File("medialab");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".txt") && !name.equals("mines.txt") && !name.equals("rounds.txt"))
                    comboBox.getItems().add(name.substring(0, name.length() - 4));
            }
        }
        comboBox.getSelectionModel().selectFirst();
    }

    public Game parse() {
        File file = new File("medialab/" + comboBox.getValue() + ".txt");
        String[] buffer = new String[4];
        int difficulty, mines, supermine, rows, columns;
        float time;
        try (Scanner scanner = new Scanner(file)) {
            for (int i = 0; i < 4; i++) {
                if (scanner.hasNext())
                    buffer[i] = scanner.nextLine();
                else
                    throw new InvalidDescriptionException("Expected lines: 4");
            }
            if (scanner.hasNext())
                throw new InvalidDescriptionException("Expected lines: 4");
            try {
                difficulty = Integer.parseInt(buffer[0]);
                if (difficulty < 1 || difficulty > 2)
                    throw new InvalidValueException("Expected difficulty: 1-2");
                rows = difficulty == 1 ? 9 : 16;
                columns = difficulty == 1 ? 9 : 16;
            } catch (NumberFormatException e) {
                throw new InvalidValueException("Expected difficulty: 1-2");
            }
            try {
                mines = Integer.parseInt(buffer[1]);
                if (difficulty == 1 && (mines < 9 || mines > 11))
                    throw new InvalidValueException("Expected mines: 9-11");
                if (difficulty == 2 && (mines < 35 || mines > 45))
                    throw new InvalidValueException("Expected mines: 35-45");
            } catch (NumberFormatException e) {
                if (difficulty == 1)
                    throw new InvalidValueException("Expected mines: 9-11");
                throw new InvalidValueException("Expected mines: 35-45");
            }
            try {
                time = Float.parseFloat(buffer[2]);
                if (difficulty == 1 && (time < 120 || time > 180))
                    throw new InvalidValueException("Expected time: 120-180");
                if (difficulty == 2 && (time < 240 || time > 360))
                    throw new InvalidValueException("Expected time: 240-360");
            } catch (NumberFormatException e) {
                if (difficulty == 1)
                    throw new InvalidValueException("Expected time: 120-180");
                throw new InvalidValueException("Expected time: 240-360");
            }
            try {
                supermine = Integer.parseInt(buffer[3]);
                if (difficulty == 1 && supermine != 0)
                    throw new InvalidValueException("Expected supermine: 0");
                if (difficulty == 2 && (supermine < 0 || supermine > 1))
                    throw new InvalidValueException("Expected supermine: 0-1");
            } catch (NumberFormatException e) {
                if (difficulty == 1)
                    throw new InvalidValueException("Expected supermine: 0");
                throw new InvalidValueException("Expected supermine: 0-1");
            }
            stage.close();
            return new Game(rows, columns, mines, time, supermine != 0);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
    }

    public void show() {
        stage.show();
    }

    public Button getButton() {
        return load;
    }
}