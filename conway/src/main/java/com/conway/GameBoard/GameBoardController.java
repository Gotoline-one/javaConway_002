package com.conway.GameBoard;
import com.conway.*;

import java.io.File;
import java.util.ArrayList;

import com.conway.FileOutput;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameBoardController {
    private GameBoardView view;
    private GameOfLife game;
    private BoardData boardData;

    private Timeline timeline;
    private AnimationTimer fpsCounter;
    private PauseTransition stopTimer;

    private GameEvent onEnd;
    private GameEvent onStart;

    public GameBoardController(GameOfLife newGame, GameBoardView newView) {
        this.view = newView;
        this.game = newGame;
        
        // Initialize boardData
        boardData = new BoardData();
        boardData.TICK_RATE = 20;
        boardData.frameCountList = new ArrayList<>();
        boardData.nanoTimeList = new ArrayList<>();
        boardData.startNano = System.nanoTime();
        boardData.startMili = System.currentTimeMillis();
        
        //first things first
        game.randomizeBoard();

        // Set up the game loop to update the model and view
        timeline = new Timeline(
            new KeyFrame(Duration.millis(boardData.TICK_RATE), 
                e -> {  
                    game.updateBoard(); 
                    view.drawBoard(game);
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Set up an AnimationTimer to count frames (FPS counter)
        fpsCounter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                boardData.frameCount++;
                if (boardData.lastFpsTime == 0) {
                    boardData.lastFpsTime = now;
                } else if (now - boardData.lastFpsTime >= 1_000_000_000) { // 1 second
                    boardData.frameCountList.add(boardData.frameCount);
                    boardData.frameCount = 0;
                    boardData.lastFpsTime = now;
                    boardData.nanoTimeList.add(System.nanoTime());
                }
            }
        };

        // Set up a timer to stop the game after 5 seconds
        stopTimer = new PauseTransition(Duration.seconds(10));
        stopTimer.setOnFinished(event -> {
            timeline.stop();
            fpsCounter.stop();
            gameEnd();
        });

        // Start the game and all timers
        gameStart();
        timeline.play();
        stopTimer.play();
        fpsCounter.start();
    }

    // Hook registration for game end
    public void setOnEndGame(GameEvent event) {
        this.onEnd = event;
    }

    // Hook registration for game start
    public void setOnStartGame(GameEvent event) {
        this.onStart = event;
    }

    private void gameEnd() {
        System.out.println("Game is exiting...");
        if (onEnd != null) {
            onEnd.execute();
        }
    }

    private void gameStart() {
        System.out.println("Starting Game");
        if (onStart != null) {
            onStart.execute();
        }
    }

    // Methods to save board data to files
    public void saveBoardToCSVFile(File filePointer) {
        FileOutput.outputCsv(boardData, filePointer);
    }

    public void saveBoardToJSONFile(File filePointer) {
        FileOutput.outputJson(boardData, filePointer);
    }
}
