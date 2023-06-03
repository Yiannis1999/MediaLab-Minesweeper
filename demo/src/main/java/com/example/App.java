package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private Game game;

    @Override
    public void start(Stage stage) {
        stage.setTitle("MediaLab Minesweeper");
        MenuBar menuBar = new MenuBar();
        Menu application = new Menu("Application");
        Menu details = new Menu("Details");
        MenuItem create = new MenuItem("Create");
        MenuItem load = new MenuItem("Load");
        MenuItem start = new MenuItem("Start");
        MenuItem exit = new MenuItem("Exit");
        MenuItem rounds = new MenuItem("Rounds");
        MenuItem solution = new MenuItem("Solution");
        Creator creator = new Creator();
        Loader loader = new Loader();
        Log log = new Log();
        Button loadButton = loader.getButton();
        game = new Game(12, 12, 6, 15f, true);
        VBox vBox = new VBox(menuBar, game.getVBox());
        scene = new Scene(vBox);
        application.getItems().add(create);
        application.getItems().add(load);
        application.getItems().add(start);
        application.getItems().add(exit);
        details.getItems().add(rounds);
        details.getItems().add(solution);
        menuBar.getMenus().add(application);
        menuBar.getMenus().add(details);
        stage.setScene(scene);
        stage.show();

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                creator.show();
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                loader.init();
                loader.show();
            }
        });

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                game.start();
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                System.exit(0);
            }
        });

        rounds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                log.init();
                log.show();
            }
        });

        solution.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                game.solution();
            }
        });

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Game newGame = loader.parse();
                if (newGame != null) {
                    game = newGame;
                    VBox vBox = new VBox(menuBar, game.getVBox());
                    scene = new Scene(vBox);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}