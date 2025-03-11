package com.conway;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        
        // Main VBOX for app
        VBox mainBox = new VBox();

        //this seems wrong, but vscode says its less wrong.
        new MyAppController(mainBox);

        // Create the board view (which internally creates the model)
        GameofLifeBoard boardView = new GameofLifeBoard();

        // add game board to main controller's view 
        mainBox.getChildren().add(boardView.getView());

        // Set up the scene with dimensions defined in the view
        Scene scene = new Scene(mainBox, boardView.getWidth(), boardView.getHeight());
        
        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
