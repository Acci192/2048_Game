package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class GameHandler {
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 4;
    public static final int HEIGHT = 4;
    public static final int PADDING = 10;
    public static final int WINDOW_WIDTH = TILE_SIZE*WIDTH + PADDING*(WIDTH + 1);
    public static final int WINDOW_HEIGHT = TILE_SIZE*HEIGHT + PADDING*(HEIGHT + 1);

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


    private int[][] gameBoard;
    private Random rnd;
    private GraphicsContext gc;
    private boolean gameOver;
    private boolean winner;

    public GameHandler(GraphicsContext gc) {
        this.gc = gc;
        this.gc.setTextAlign(TextAlignment.CENTER);
        this.gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        rnd = new Random();
        gameBoard = new int[WIDTH][HEIGHT];
        gameOver = false;
        winner = false;
        placePiece();
        placePiece();
    }

    public void drawBoard(){
        if(gameOver) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        } else if(winner) {
            gc.setFill(Color.GREEN);
            gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        } else {
            gc.setFill(BACKGROUND);
            gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            for(int i = 0; i < WIDTH; i++){
                int y = i*TILE_SIZE + (i + 1)*PADDING;
                for(int j = 0; j < HEIGHT; j++){
                    int x = j*TILE_SIZE + (j + 1)*PADDING;
                    setColor(i, j);
                    gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    if(gameBoard[i][j] != 0){
                        gc.setFill(Color.BLACK);
                        gc.fillText("" + gameBoard[i][j], x + TILE_SIZE - TILE_SIZE/2, y + TILE_SIZE - TILE_SIZE/3);
                    }
                }
            }
        }
    }

    public void moveUp(){
        if(canMoveUp()){
            for(int col = 0; col < 4; col++){
                moveColumnUp(col);
            }
            placePiece();
            drawBoard();
        }
    }

    public void moveDown(){
        if(canMoveDown()){
            for(int col = 0; col < 4; col++){
                moveColumnDown(col);
            }
            placePiece();
            drawBoard();
        }
    }

    public void moveRight(){
        if(canMoveRight()){
            for(int row = 0; row < 4; row++){
                moveRowRight(row);
            }
            placePiece();
            drawBoard();
        }
    }

    public void moveLeft(){
        if(canMoveLeft()){
            for(int row = 0; row < 4; row++){
                moveRowLeft(row);
            }
            placePiece();
            drawBoard();
        }
    }

    // Move and merge a row to left
    private void moveRowLeft(int row) {
        // Slide row to left
        slideUp_Or_Left(gameBoard[row]);

        // Merge if possible
        for(int col = 1; col < 4; col++){
            if(gameBoard[row][col] == gameBoard[row][col-1]) {
                gameBoard[row][col-1] *= 2;
                gameBoard[row][col] = 0;
                if(gameBoard[row][col-1] == 2048)
                    winner = true;
            }
        }

        // Slide row to left if possible
        slideUp_Or_Left(gameBoard[row]);

    }

    // Help function to slide each tile toward the left side
    private void slideUp_Or_Left(int[] row){
        boolean change;
        do{
            change = false;
            for(int col = 0; col < 3; col++){
                if(row[col] == 0 && row[col + 1] != 0){
                    row[col] = row[col + 1];
                    row[col + 1] = 0;
                    change = true;
                }
            }
        } while(change);
    }

    // Move and merge a row to right
    private void moveRowRight(int row) {
        // Slide row to right if possible
        slideDown_Or_Right(gameBoard[row]);

        // Merge if possible
        for(int col = 2; col >= 0; col--){
            if(gameBoard[row][col] == gameBoard[row][col+1]) {
                gameBoard[row][col+1] *= 2;
                gameBoard[row][col] = 0;
                if(gameBoard[row][col+1] == 2048)
                    winner = true;
            }
        }

        // Slide row to right if possible
        slideDown_Or_Right(gameBoard[row]);
    }

    // Help function to slide each tile toward the right side
    private void slideDown_Or_Right(int[] row) {
        boolean change;
        do{
            change = false;
            for(int col = 3; col > 0; col--){
                if(row[col] == 0 && row[col - 1] != 0){
                    row[col] = row[col - 1];
                    row[col - 1] = 0;
                    change = true;
                }
            }
        } while(change);
    }

    // Move and merge a column upwards
    private void moveColumnUp(int col) {
        int[] tempCol = getCol(col);

        // Slide column upwards if possible
        slideUp_Or_Left(tempCol);

        // Merge if possible
        for(int row = 1; row < 4; row++){
            if(tempCol[row] == tempCol[row-1]) {
                tempCol[row-1] *= 2;
                tempCol[row] = 0;
                if(tempCol[row-1] == 2048)
                    winner = true;
            }
        }
        // Slide column upwards after merge if possible
        slideUp_Or_Left(tempCol);

        // Update the correct column in gameBoard
        for(int i = 0; i < HEIGHT; i++) {
            gameBoard[i][col] = tempCol[i];
        }
    }

    // Move and merge a column downwards
    private void moveColumnDown(int col) {
        int[] tempCol = getCol(col);

        // Slide column downwards if possible
        slideDown_Or_Right(tempCol);

        // Merge if possible
        for(int row = 2; row >= 0; row--){
            if(tempCol[row] == tempCol[row+1]) {
                tempCol[row+1] *= 2;
                tempCol[row] = 0;
                if(tempCol[row+1] == 2048)
                    winner = true;
            }
        }
        // Slide column downwards after merge if possible
        slideDown_Or_Right(tempCol);

        // Update the correct column in gameBoard
        for(int i = 0; i < HEIGHT; i++) {
            gameBoard[i][col] = tempCol[i];
        }
    }

    // Function that return a column in GameBoard as a 1d array
    private int[] getCol(int col) {
        int[] tempCol = new int[HEIGHT];
        // Copy the column into a 1d array (to be able to reuse slide function)
        for(int i = 0; i < HEIGHT; i++) {
            tempCol[i] = gameBoard[i][col];
        }
        return tempCol;
    }

    private boolean canMoveLeft(){
        for(int i = 0; i < HEIGHT; i++) {
            if (canMoveRow_Up_Or_Left(gameBoard[i]))
                return true;
        }
        return false;
    }

    private boolean canMoveUp(){
        for(int i = 0; i < WIDTH; i++) {
            if(canMoveRow_Up_Or_Left(getCol(i)))
                return true;
        }
        return false;
    }

    private boolean canMoveRight() {
        for(int i = 0; i < HEIGHT; i++) {
            if(canMoveRow_Down_Or_Right(gameBoard[i]))
                return true;
        }
        return false;
    }

    private boolean canMoveDown() {
        for(int i = 0; i < WIDTH; i++) {
            if(canMoveRow_Down_Or_Right(getCol(i)))
                return true;
        }
        return false;
    }


    private boolean canMoveRow_Up_Or_Left(int[] row) {
        boolean empty = false;
        for(int i = 0; i < WIDTH-1; i++) {
            if(row[i] == 0 && !empty){
                // If empty square and empty is still false -> set empty to true
                empty = true;
            } else if((row[i] != 0 && empty) || row[i] == row[i+1]){
                // If there has been an empty tile and current tile is not empty -> return true
                // or if current tile and next tile has the same value -> return true
                return true;
            }
        }
        // Check the last tile of row/column (reason for this is to avoid index out of bounds in for loop)
        return row[WIDTH-1] != 0 && empty;
    }

    private boolean canMoveRow_Down_Or_Right(int[] row) {
        boolean empty = false;
        for(int i = WIDTH - 1; i > 0; i--) {
            if(row[i] == 0 && !empty) {
                // If empty tile and empty is still false -> set empty to true
                empty = true;
            } else if((row[i] != 0 && empty) || row[i] == row[i-1]) {
                // If there has been an empty tile and current tile is not empty -> return true
                // or if current tile and next tile has the same value -> return true
                return true;
            }
        }
        // Check the last tile of row/column (reason for this is to avoid index out of bounds in for loop)
        return row[0] != 0 && empty;
    }

    // Place tile
    private void placePiece() {
        while(true){
            int x = rnd.nextInt(4);
            int y = rnd.nextInt(4);
            int value = rnd.nextInt(10);
            if(value > 1)
                value = 2;
            else
                value = 4;
            if(gameBoard[x][y] == 0){
                gameBoard[x][y] = value;
                break;
            }
        }
        updateGameOver();
    }

    // Set GameOver if there are no allowed movement
    private void updateGameOver() {
        if(!canMoveDown() && !canMoveRight() && !canMoveLeft() && !canMoveUp()){
            gameOver = true;
            drawBoard();
        }
    }

    private void setColor(int x, int y) {
        switch (gameBoard[x][y]){
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

    // For debugging
    private void printBoard(){
        StringBuilder output = new StringBuilder("");
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                output.append(gameBoard[i][j] + "\t");
            }
            output.append("\n");
        }
        System.out.println(output.toString());
    }
}
