package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Display {
    public static final int TILE_SIZE = 100;
    public static final int PADDING = 10;
    public static final int WINDOW_WIDTH = TILE_SIZE*GameHandler.SIZE + PADDING*(GameHandler.SIZE + 1);
    public static final int WINDOW_HEIGHT = TILE_SIZE*GameHandler.SIZE + PADDING*(GameHandler.SIZE + 1);


    private static final Color BACKGROUND = Color.valueOf("#BBADA0");
    private static final Color EMPTY = Color.valueOf("#CDC1B4");
    private static final Color TWO = Color.valueOf("#EEE4DA");
    private static final Color FOUR = Color.valueOf("#EDE0C8");
    private static final Color EIGHT = Color.valueOf("#F2B179");
    private static final Color SIXTEEN = Color.valueOf("#F59563");
    private static final Color THIRTY = Color.valueOf("#F67C5F");
    private static final Color SIXTY = Color.valueOf("#F65E3B");
    private static final Color ONEHUNDRED = Color.valueOf("#EDCF72");
    private static final Color TWOHUNDRED = Color.valueOf("#EDCC61");
    private static final Color FIVEHUNDRED = Color.valueOf("#EDC850");
    private static final Color ONETHOUSAND = Color.valueOf("#EDC53F");
    private static final Color TWOTHOUSAND = Color.valueOf("#FBC52D");

    public static void drawBoard(GraphicsContext gc, GameHandler gameHandler){
        int[][] gameBoard = gameHandler.getGameBoard();
        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        for(int i = 0; i < GameHandler.SIZE; i++){
            int y = i*TILE_SIZE + (i + 1)*PADDING;
            for(int j = 0; j < GameHandler.SIZE; j++){
                int x = j*TILE_SIZE + (j + 1)*PADDING;
                setColor(gc, gameBoard[i][j]);
                gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                if(gameBoard[i][j] != 0){
                    gc.setFill(Color.BLACK);
                    gc.fillText("" + gameBoard[i][j], x + TILE_SIZE - TILE_SIZE/2, y + TILE_SIZE - TILE_SIZE/3);
                }
            }
        }
        if(gameHandler.isGameOver()){
            drawGameOver(gc);
        } else if(gameHandler.isWinner()){
            drawWin(gc);
        }

    }

    public static void drawGameOver(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public static void drawWin(GraphicsContext gc){
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private static void setColor(GraphicsContext gc,int value) {
        switch (value){
            case 0:
                gc.setFill(EMPTY);
                break;
            case 2:
                gc.setFill(TWO);
                break;
            case 4:
                gc.setFill(FOUR);
                break;
            case 8:
                gc.setFill(EIGHT);
                break;
            case 16:
                gc.setFill(SIXTEEN);
                break;
            case 32:
                gc.setFill(THIRTY);
                break;
            case 64:
                gc.setFill(SIXTY);
                break;
            case 128:
                gc.setFill(ONEHUNDRED);
                break;
            case 256:
                gc.setFill(TWOHUNDRED);
                break;
            case 512:
                gc.setFill(FIVEHUNDRED);
                break;
            case 1024:
                gc.setFill(ONETHOUSAND);
                break;
            case 2048:
                gc.setFill(TWOTHOUSAND);
                break;
        }
    }
}
