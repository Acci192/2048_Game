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
        gameWindow.setHeight(Display.WINDOW_HEIGHT);
        gameWindow.setWidth(Display.WINDOW_WIDTH);

        gc = gameWindow.getGraphicsContext2D();
        gameHandler = new GameHandler(gc);

        Display.drawBoard(gc, gameHandler);
    }

    @FXML
    public void update(KeyEvent e) {
        if(e.getCode() == KeyCode.UP){
            gameHandler.updateBoard(GameHandler.Movement.UP);
        } else if(e.getCode() == KeyCode.DOWN){
            gameHandler.updateBoard(GameHandler.Movement.DOWN);
        } else if(e.getCode() == KeyCode.LEFT){
            gameHandler.updateBoard(GameHandler.Movement.LEFT);
        } else if(e.getCode() == KeyCode.RIGHT){
            gameHandler.updateBoard(GameHandler.Movement.RIGHT);
        }
        Display.drawBoard(gc, gameHandler);
    }
}
