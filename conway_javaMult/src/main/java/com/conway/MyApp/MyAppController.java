package com.conway.MyApp;

import java.io.File;

import com.conway.GameBoard.GameBoardController;
import com.conway.MyApp.CommandLineParser.CommandLineOptions;
import com.conway.conwayApp;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyAppController {
    private conwayApp model;

    private final MyAppView view;
    private final CommandLineOptions options;
    private final GameBoardController gameController;

    VBox mainNode;
    Label fpsCounterLabel;
    Label statusLabel;

    public MyAppController( conwayApp myModel) {

        this.view = myModel.view;
        this.options = conwayApp.options;
        this.mainNode = (VBox)view.getRoot();
        this.fpsCounterLabel = (Label) mainNode.lookup("#fpsCounterLabel"); 
        this.statusLabel = (Label) mainNode.lookup("#statusLabel");
        
        this.gameController = myModel.gameController;// gameController;

        if (model != null && model.appController != null)
            model.appController.fpsCounterLabel.setText("starting");

        // Register main application events
        if (view.getQMenuItem() == null) {
            System.out.println("getQMenuItem is null");
        } else {
            view.getQMenuItem().setOnAction(this::handleQuit);
        }
    }

    public void initialize(Stage primaryStage) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String fname = "default";
            if (options.flags.filename) {
                fname = options.filename;
            }

            if (options.jsonOutput) {
                gameController.saveBoardToJSONFile(new File("./" + fname + ".json"));
            }

            if (options.csvOutput) {
                gameController.saveBoardToCSVFile(new File("./" + fname + ".csv"));
            }

            if (options.debug) System.out.println("Shutdown hook triggered (Ctrl+C or SIGTERM).");
            cleanupBeforeExit(); // Final save/flush/log
        }));

        primaryStage.setOnCloseRequest(event -> {
            if (options.debug) System.out.println("Window close detected.");
            cleanupBeforeExit(); // Called also on GUI close
        });

        if (options.quitOnEnd) {
            if(options.debug)   {System.out.println("Setting end of game time "+ options.timeInSeconds); }
            
            gameController.setOnEndGame(() -> {
                if (options.debug) System.out.println("Game Ended");
                cleanupBeforeExit(); // Final save/flush/log
                primaryStage.close(); // Close the application window
                System.exit(0);
            });
        }
    }

    public void handleQuit(ActionEvent t) {
        this.cleanupBeforeExit();
        System.exit(1);
    }

    public void cleanupBeforeExit() {
        // Add cleanup logic here
    }

    public void updateFPS(double fps) {
        fpsCounterLabel.setText(String.valueOf(fps));
    }

    public void setStatus(String newStatus) {
        statusLabel.setText(newStatus);
    }
}
