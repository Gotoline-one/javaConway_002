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
        if(options.flags.height){
            HEIGHT = options.height;
        }
        if(options.flags.width){
            HEIGHT = options.width;
        }
        if(options.flags.timeInSeconds){
            TIME_LIMIT_SEC = options.timeInSeconds;
            //TODO: there is a probabl off by one issue with time logic this fixes
            TIME_LIMIT_SEC++;  // **HOTFIX: fixes off by one in seconds  **
        }

        // Create the board view (which internally creates thcme model)
        gameView        = new GameBoardView(HEIGHT, WIDTH);
        gameLogic       = new GameOfLife(HEIGHT,WIDTH);
        gameController  = new GameBoardController(gameLogic, gameView, TIME_LIMIT_SEC);

        view = new MyAppView(gameView);
        appController = new MyAppController(view, this);
               
        // Set up the scene with dimensions defined in the view
        Scene scene = new Scene(view.getRoot());


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String fname ="defalt";
            if(options.flags.filename){
                fname = options.filename;
            }

            
            if(options.jsvOutput){
                gameController.saveBoardToJSONFile(new File(  "./"+ fname+".json") );
            }

            if(options.csvOutput){
                gameController.saveBoardToCSVFile(new File( "./"+ fname+".csv") );
            }

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
    private static CommandLineOptions options;

    private static void dealWithOptions(String[] args){
        CommandLineParser parser = new CommandLineParser();
        options = parser.parseArguments(args);
        
        if (options.showHelp || options.showHelp) {
            parser.printHelp();
            System.exit(0);
            return;
        }

        if (args.length >= 2 && options.width >0  && options.height >0) {
            WIDTH  = options.width;
            HEIGHT = options.height;
        }

        // print only if hidden debug option is set
        if (options.debug){

            // Use the parsed values as needed. For demo, we print them.
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
