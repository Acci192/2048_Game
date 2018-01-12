package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class GameHandler {

    public static final int SIZE = 4;

    private int[][] gameBoard;
    private Random rnd;
    private GraphicsContext gc;
    private boolean gameOver;
    private boolean winner;

    private int score;

    public enum Movement {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    public GameHandler(GraphicsContext gc) {
        this.gc = gc;
        this.gc.setTextAlign(TextAlignment.CENTER);
        this.gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        this.rnd = new Random();
        this.gameBoard = new int[SIZE][SIZE];
        this.gameOver = false;
        this.winner = false;
        this.score = 0;
        placePiece();
        placePiece();
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }
    public boolean isGameOver(){
        return gameOver;
    }
    public boolean isWinner(){
        return winner;
    }

    public void updateBoard(Movement movement){
        if(!gameOver || !winner){
            switch (movement){
                case UP:
                    moveUp();
                    break;
                case DOWN:
                    moveDown();
                    break;
                case RIGHT:
                    moveRight();
                    break;
                case LEFT:
                    moveLeft();
                    break;
            }
        }
    }

    public int getScore(){
        return score;
    }

    private void moveUp(){
        if(canMoveUp()){
            for(int col = 0; col < SIZE; col++){
                moveColumnUp(col);
            }
            placePiece();
        }
    }
    private void moveDown(){
        if(canMoveDown()){
            for(int col = 0; col < SIZE; col++){
                moveColumnDown(col);
            }
            placePiece();
        }
    }
    private void moveRight(){
        if(canMoveRight()){
            for(int row = 0; row < SIZE; row++){
                moveRowRight(row);
            }
            placePiece();
        }
    }
    private void moveLeft(){
        if(canMoveLeft()){
            for(int row = 0; row < SIZE; row++){
                moveRowLeft(row);
            }
            placePiece();
        }
    }

    // Move and merge a row to left
    private void moveRowLeft(int row) {
        // Slide row to left
        slideUp_Or_Left(gameBoard[row]);

        // Merge if possible
        for(int col = 1; col < SIZE; col++){
            if(gameBoard[row][col] == gameBoard[row][col-1]) {
                gameBoard[row][col-1] *= 2;
                score += gameBoard[row][col-1];
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
            for(int col = 0; col < SIZE-1; col++){
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
        for(int col = SIZE-2; col >= 0; col--){
            if(gameBoard[row][col] == gameBoard[row][col+1]) {
                gameBoard[row][col+1] *= 2;
                score += gameBoard[row][col + 1];
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
            for(int col = SIZE-1; col > 0; col--){
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
        for(int row = 1; row < SIZE; row++){
            if(tempCol[row] == tempCol[row-1]) {
                tempCol[row-1] *= 2;
                score += tempCol[row-1];
                tempCol[row] = 0;
                if(tempCol[row-1] == 2048)
                    winner = true;
            }
        }
        // Slide column upwards after merge if possible
        slideUp_Or_Left(tempCol);

        // Update the correct column in gameBoard
        for(int i = 0; i < SIZE; i++) {
            gameBoard[i][col] = tempCol[i];
        }
    }

    // Move and merge a column downwards
    private void moveColumnDown(int col) {
        int[] tempCol = getCol(col);

        // Slide column downwards if possible
        slideDown_Or_Right(tempCol);

        // Merge if possible
        for(int row = SIZE-2; row >= 0; row--){
            if(tempCol[row] == tempCol[row+1]) {
                tempCol[row+1] *= 2;
                score += tempCol[row+1];
                tempCol[row] = 0;
                if(tempCol[row+1] == 2048)
                    winner = true;
            }
        }
        // Slide column downwards after merge if possible
        slideDown_Or_Right(tempCol);

        // Update the correct column in gameBoard
        for(int i = 0; i < SIZE; i++) {
            gameBoard[i][col] = tempCol[i];
        }
    }

    // Function that return a column in GameBoard as a 1d array
    private int[] getCol(int col) {
        int[] tempCol = new int[SIZE];
        // Copy the column into a 1d array (to be able to reuse slide function)
        for(int i = 0; i < SIZE; i++) {
            tempCol[i] = gameBoard[i][col];
        }
        return tempCol;
    }

    private boolean canMoveLeft(){
        for(int i = 0; i < SIZE; i++) {
            if (canMoveRow_Up_Or_Left(gameBoard[i]))
                return true;
        }
        return false;
    }

    private boolean canMoveUp(){
        for(int i = 0; i < SIZE; i++) {
            if(canMoveRow_Up_Or_Left(getCol(i)))
                return true;
        }
        return false;
    }

    private boolean canMoveRight() {
        for(int i = 0; i < SIZE; i++) {
            if(canMoveRow_Down_Or_Right(gameBoard[i]))
                return true;
        }
        return false;
    }

    private boolean canMoveDown() {
        for(int i = 0; i < SIZE; i++) {
            if(canMoveRow_Down_Or_Right(getCol(i)))
                return true;
        }
        return false;
    }


    private boolean canMoveRow_Up_Or_Left(int[] row) {
        boolean empty = false;
        for(int i = 0; i < SIZE -1; i++) {
            if(row[i] == 0 && !empty){
                // If empty square and empty is still false -> set empty to true
                empty = true;
            } else if((row[i] != 0 && empty) || (row[i] == row[i+1]) && row[i] != 0){
                // If there has been an empty tile and current tile is not empty -> return true
                // or if current tile and next tile has the same value -> return true
                return true;
            }
        }
        // Check the last tile of row/column (reason for this is to avoid index out of bounds in for loop)
        return row[SIZE -1] != 0 && empty;
    }

    private boolean canMoveRow_Down_Or_Right(int[] row) {
        boolean empty = false;
        for(int i = SIZE - 1; i > 0; i--) {
            if(row[i] == 0 && !empty) {
                // If empty tile and empty is still false -> set empty to true
                empty = true;
            } else if((row[i] != 0 && empty) || (row[i] == row[i-1] && row[i] != 0)) {
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
            int x = rnd.nextInt(SIZE);
            int y = rnd.nextInt(SIZE);
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
        }
    }

    // For debugging
    private void printBoard(){
        StringBuilder output = new StringBuilder("");
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++){
                output.append(gameBoard[i][j] + "\t");
            }
            output.append("\n");
        }
        System.out.println(output.toString());
    }
}
