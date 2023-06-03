package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Creator {
    private Stage stage;

    public Creator() {
        stage = new Stage();
        stage.setTitle("Create");
        GridPane gridPane = new GridPane();
        TextField scenario_id = new TextField();
        ComboBox<String> difficulty = new ComboBox<String>();
        TextField mines = new TextField();
        TextField time = new TextField();
        ComboBox<String> supermine = new ComboBox<String>();
        Button cancel = new Button("Cancel");
        Button create = new Button("Create");
        HBox hBox = new HBox(cancel, create);
        VBox vBox = new VBox(gridPane, hBox);
        Scene scene = new Scene(vBox);
        difficulty.getItems().add("1");
        difficulty.getItems().add("2");
        difficulty.getSelectionModel().selectFirst();
        difficulty.setEditable(true);
        supermine.getItems().add("0");
        supermine.getItems().add("1");
        supermine.setEditable(true);
        supermine.getSelectionModel().selectFirst();
        hBox.setAlignment(Pos.CENTER);
        gridPane.add(new Text("SCENARIO-ID"), 0, 0);
        gridPane.add(new Text("Difficulty"), 0, 1);
        gridPane.add(new Text("Mines"), 0, 2);
        gridPane.add(new Text("Time (s)"), 0, 3);
        gridPane.add(new Text("Supermine"), 0, 4);
        gridPane.add(scenario_id, 1, 0);
        gridPane.add(difficulty, 1, 1);
        gridPane.add(mines, 1, 2);
        gridPane.add(time, 1, 3);
        gridPane.add(supermine, 1, 4);
        gridPane.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        cancel.setOnAction(e -> stage.close());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    String[] buffer = new String[4];
                    buffer[0] = difficulty.getValue();
                    buffer[1] = mines.getText();
                    buffer[2] = time.getText();
                    buffer[3] = supermine.getValue();
                    int difficulty, mines, supermine;
                    float time;
                    try {
                        difficulty = Integer.parseInt(buffer[0]);
                        if (difficulty < 1 || difficulty > 2)
                            throw new InvalidValueException("Expected difficulty: 1-2");
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
                    File file = new File("medialab/" + scenario_id.getText() + ".txt");
                    FileWriter writer = new FileWriter(file);
                    PrintWriter printWriter = new PrintWriter(writer);
                    printWriter.println(buffer[0]);
                    printWriter.println(buffer[1]);
                    printWriter.println(buffer[2]);
                    printWriter.print(buffer[3]);
                    printWriter.close();
                    writer.close();
                    stage.close();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.show();
                }
            }
        });
    }

    public void show() {
        stage.show();
    }
}