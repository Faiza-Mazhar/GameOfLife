package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Controller {

    @FXML
    Pane outputPane = new Pane();

    @FXML
    Button populateGame = new Button();

    @FXML
    ToggleButton playGame = new ToggleButton();

    @FXML
    Button clearOutput = new Button();

    @FXML
    Label iterationNumberLabel = new Label();

    /************************************************************
     *
     */

    private int outputCellSize = 10; //every output seed size is 5X5
    private ArrayList<Position> currentGameState;

    private GameOfLife gameOfLife;

    /*********************************************************************************
     This method with Populate game for the first time
     **********************************************************************************/
    @FXML
    public void populateGame() {

        clearBoard();

        //get width and height of current output window
        int maxWidthX = (int) outputPane.getBoundsInParent().getWidth() / outputCellSize;
        int maxHeightY = (int) outputPane.getBoundsInParent().getHeight() / outputCellSize;

        System.out.println(maxWidthX + " , " + maxHeightY);
        //
        currentGameState = this.getPositionList(maxWidthX, maxHeightY);
        this.displayOutput();

        //initiate game with Position on displayPane and width and height of pane
        gameOfLife = new GameOfLife(currentGameState, maxWidthX, maxHeightY);

    }

    /*
     * This method will take display output
     * it will take positions of alive cell
     * iterate through ArrayList<position> currentGameState
     * make a rectangle and add to the display pane
     * */
    @FXML
    public void displayOutput() {
        currentGameState.forEach((position) -> {

            Rectangle rectangle = new Rectangle(
                    position.getX() * outputCellSize,
                    position.getY() * outputCellSize,
                    outputCellSize, outputCellSize);
            rectangle.setStroke(Color.BEIGE);
            rectangle.setFill(Color.web("#AA3939"));
            outputPane.getChildren().add(rectangle);

        });
    }

    public void playCurrentGameState() {

        clearBoard();
        currentGameState = gameOfLife.nextStateOfGame();
        displayOutput();

    }

    @FXML
    public void playGame() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        playCurrentGameState();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }


    @FXML
    public void clearBoard() {
        this.outputPane.getChildren().clear();
    }


    private ArrayList<Position> getPositionList(int xMax, int yMax) {

        GeneratePositions generatePositions = new GeneratePositions(xMax, yMax);
        return generatePositions.getPositionArrayList();

    }

}
