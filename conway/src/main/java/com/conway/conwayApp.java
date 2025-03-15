package com.conway;
import java.io.File;

import com.conway.GameBoard.*;
import com.conway.MyApp.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class conwayApp extends Application {
    GameBoardView gameView;
    GameOfLife gameLogic;
    GameBoardController gameController;

    MyAppView view;
    public MyAppController appController;

    @Override
    public void start(Stage primaryStage) {

        // Create the board view (which internally creates the model)
        gameView        = new GameBoardView();
        gameLogic       = new GameOfLife(150,150);
        gameController  = new GameBoardController(gameLogic, gameView);

        view = new MyAppView(gameView);
        appController = new MyAppController(view, this);
               
        // Set up the scene with dimensions defined in the view
        Scene scene = new Scene(view.getRoot());


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // save metrics to JSON
            gameController.saveBoardToJSONFile(new File( "./default.json") );
            gameController.saveBoardToCSVFile(new File( "./default.csv") );

            System.out.println("Shutdown hook triggered (Ctrl+C or SIGTERM).");
            appController.cleanupBeforeExit(); // Final save/flush/log
        }));

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Window close detected.");
            appController.cleanupBeforeExit(); // Called also on GUI close
            
        });

        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }


}
