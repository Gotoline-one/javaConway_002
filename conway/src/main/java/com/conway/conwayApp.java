package com.conway;
import java.io.File;

import com.conway.GameBoard.*;
import com.conway.MyApp.*;
import com.conway.MyApp.CommandLineParser.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class conwayApp extends Application {
    GameBoardView gameView;
    GameOfLife gameLogic;
    GameBoardController gameController;
    public static int WIDTH = 80, HEIGHT = 50;
    public static double  TIME_LIMIT_SEC = 30;

    MyAppView view;
    public MyAppController appController;

    @Override
    public void start(Stage primaryStage) {

        
        // Create the board view (which internally creates the model)
        gameView        = new GameBoardView(conwayApp.HEIGHT, WIDTH);
        gameLogic       = new GameOfLife(HEIGHT,WIDTH);
        gameController  = new GameBoardController(gameLogic, gameView, TIME_LIMIT_SEC);

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
    
    private static void dealWithOptions(String[] args){
        CommandLineParser parser = new CommandLineParser();
        CommandLineOptions options = parser.parseArguments(args);
        
        if (options.showHelp || options.showHelp) {
            parser.printHelp();
            return;
        }

        // options.flags.getName();
        System.out.println(options.getName());

        System.out.printf("%b" ,options.flags.height);;

        if (args.length >= 2 && options.width >0  && options.height >0) {
            WIDTH  = options.width;
            HEIGHT = options.height;
        }


        // Use the parsed values as needed. For demo, we print them.
        System.out.println("Parsed Values:");
        System.out.println("  Height       : " + options.height);
        System.out.println("  Width        : " + options.width);
        System.out.println("  Time (sec)   : " + options.timeInSeconds);
        System.out.println("  JSV Output   : " + options.jsvOutput);
        System.out.println("  CSV Output   : " + options.csvOutput);
        System.out.println("  Filename     : " + options.filename);

    }



    public static void main(String[] args) {
        dealWithOptions(args);
        
        launch(args);
    }

    
}
