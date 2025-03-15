package com.conway;

public class GameOfLife {
    private static int WIDTH  = 50;
    private static int HEIGHT = 50;
    private boolean[][] board;

    public GameOfLife(int height, int width){
        HEIGHT = height;
        WIDTH = width;
        board = new boolean[HEIGHT][WIDTH];

    }
    public boolean getCell(int row, int col){ return board[row][col]; }

    public  void randomizeBoard() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                board[row][col] = Math.random() > 0.7;
            }
        }
    }

      // Update board to next generation
    public void updateBoard() {
        boolean[][] nextGen = new boolean[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                int neighbors = countNeighbors(row, col);
                if (board[row][col]) {
                    nextGen[row][col] = (neighbors == 2 || neighbors == 3);
                } else {
                    nextGen[row][col] = (neighbors == 3);
                }
            }
        }

        board = nextGen;
    }

    // Count live neighbors
    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {// Skip self
                    continue;
                }
                int r = (row + i + HEIGHT) % HEIGHT; // Wrap-around
                int c = (col + j + WIDTH) % WIDTH;
                if (board[r][c]) count++;
            }
        }
        return count;
    }
}
