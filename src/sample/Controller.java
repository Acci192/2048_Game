package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Canvas gameWindow;

    private GraphicsContext gc;

    private GameHandler gameHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameWindow.setHeight(GameHandler.WINDOW_HEIGHT);
        gameWindow.setWidth(GameHandler.WINDOW_WIDTH);

        gc = gameWindow.getGraphicsContext2D();
        gameHandler = new GameHandler(gc);

        gameHandler.drawBoard();
    }

    @FXML
    public void update(KeyEvent e) {
        if(e.getCode() == KeyCode.UP){
            gameHandler.moveUp();
        } else if(e.getCode() == KeyCode.DOWN){
            gameHandler.moveDown();
        } else if(e.getCode() == KeyCode.LEFT){
            gameHandler.moveLeft();
        } else if(e.getCode() == KeyCode.RIGHT){
            gameHandler.moveRight();
        }
    }
}
