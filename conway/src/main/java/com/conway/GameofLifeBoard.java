package com.conway;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameofLifeBoard  {
    private static int WIDTH = 50;
    private static int HEIGHT = 50;
    private static int CELL_SIZE = 20;

    private Rectangle[][] rectangles;// = new Rectangle[HEIGHT][WIDTH];
    private GameofLife game; 
    private GridPane grid; 


    // provides calling app Node to set into larger Java FX app
    protected GridPane getView() {
        return grid;
    }

    // Draw current board state
    private void drawBoard() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
               rectangles[row][col].setFill(game.getCell(row,col) ? Color.BLACK : Color.WHITE);
            }
        }
    }
    
    public GameofLifeBoard() {
        // Create the model
        game = new GameofLife(HEIGHT, WIDTH);
        
        // Random initialization
        game.randomizeBoard();   
        
        // Set up the grid layout for the board
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        
        // Instanciate the rectangles 2D array
        rectangles = new Rectangle[HEIGHT][WIDTH];

        // Initialize rectangles (view)
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                rectangles[row][col] = new Rectangle(CELL_SIZE, CELL_SIZE);
                rectangles[row][col].setStroke(Color.LIGHTGRAY);
                // Initially set based on the model's state
                rectangles[row][col].setFill(game.getCell(row,col) ? Color.BLACK : Color.WHITE);
                grid.add(rectangles[row][col], col, row);
            }
        }
        
        // Set up the game loop (controller updating the model and view)
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(20), 
                e -> {  game.updateBoard(); drawBoard(); }
            )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
    }


  // Methods to provide dimensions for the Scene

    public int getWidth() {
        return WIDTH * CELL_SIZE;
    }

    public int getHeight() {
        return HEIGHT * CELL_SIZE;
    }
        
} 