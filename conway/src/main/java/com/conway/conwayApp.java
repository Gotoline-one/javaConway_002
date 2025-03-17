package com.conway;

import com.conway.GameBoard.*;
import com.conway.MyApp.*;
import com.conway.MyApp.CommandLineParser.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class conwayApp extends Application {
    GameBoardView gameView;
    GameOfLife gameLogic;
    public GameBoardController gameController;
    public static int WIDTH = 80, HEIGHT = 50;
    public static double TIME_LIMIT_SEC = 30;
    public static CommandLineOptions options;
    public MyAppView view;
    public MyAppController appController;

    @Override
    public void start(Stage primaryStage) {

        if (options.flags.height) {
            HEIGHT = options.height;
        }
        if (options.flags.width) {
            WIDTH = options.width;
        }
        if (options.flags.timeInSeconds) {
            TIME_LIMIT_SEC = options.timeInSeconds;
        }

        // Create the board view (which internally creates the model)
        gameView = new GameBoardView(HEIGHT, WIDTH);
        gameLogic = new GameOfLife(HEIGHT, WIDTH);
        gameController = new GameBoardController(gameLogic, gameView, TIME_LIMIT_SEC);

        view = new MyAppView(gameView);
        appController = new MyAppController(this);

        Scene scene = new Scene(view.getRoot());
        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        appController.initialize(primaryStage);
    }


    private static void dealWithOptions(String[] args) {
        CommandLineParser parser = new CommandLineParser();
        options = parser.parseArguments(args);

        if (options.showHelp) {
            parser.printHelp();
            System.exit(0);
            return;
        }

        if (args.length >= 2 && options.width > 0 && options.height > 0) {
            WIDTH = options.width;
            HEIGHT = options.height;
        }

        if (options.debug) {
            System.out.println("Parsed Values:");
            System.out.println("  Height       : " + options.height);
            System.out.println("  Width        : " + options.width);
            System.out.println("  Time (sec)   : " + options.timeInSeconds);
            System.out.println("  JSV Output   : " + options.jsvOutput);
            System.out.println("  CSV Output   : " + options.csvOutput);
            System.out.println("  Filename     : " + options.filename);
        }
    }

    public static void main(String[] args) {
        dealWithOptions(args);
        launch(args);
    }
}
