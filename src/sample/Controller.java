package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {

    @FXML
    Pane outputPane = new Pane();

    @FXML
    Label iterationNumberLabel = new Label();

    private final AtomicBoolean runGame = new AtomicBoolean(false);
    private int outputCellSize = 10; //every output seed size is 10X10
    private GameOfLife gameOfLife;
    private ArrayList<Position> currentGameState = new ArrayList<>();
    private int generationCounter = 0;
    private Thread gameThread = new Thread();

    /*********************************************************************************
     This method with Populate game

     **********************************************************************************/
    @FXML
    public void populateGame() {
        stopGame();
        resetGenerationCounter();
        //get width and height of current output window
        int maxWidthX = (int) outputPane.getBoundsInParent().getWidth() / outputCellSize;
        int maxHeightY = (int) outputPane.getBoundsInParent().getHeight() / outputCellSize;
        //if game is already running, it will clear the current state of game
        currentGameState.clear();
        // populate game with a list of random positions and display given state of game
        currentGameState = this.getPositionList(maxWidthX, maxHeightY);
        this.displayOutput();
        //initiate game with Position on displayPane and width and height of pane
        gameOfLife = new GameOfLife(currentGameState, maxWidthX, maxHeightY);
    }

    /*
     * This method will display output
     * it will take positions of alive cell
     * iterate through ArrayList<position> currentGameState
     * make a rectangle and add to the display pane
     * */
    @FXML
    private void displayOutput() {
        currentGameState.forEach((position) -> {

            Rectangle rectangle = new Rectangle(
                    position.getX() * outputCellSize,
                    position.getY() * outputCellSize,
                    outputCellSize, outputCellSize);
            rectangle.setStroke(Color.valueOf("#6F5AAC"));
            rectangle.setFill(Color.web("#E3DDF6"));
            outputPane.getChildren().add(rectangle);

        });
    }

    /*
     * for a given gameOfLife, it will get generate next state of game*/
    private void getNextStateOfGOL() {
        generationCounter++;
        iterationNumberLabel.setText(String.valueOf(generationCounter));
        clearDisplay();
        currentGameState = gameOfLife.nextStateOfGame();
        displayOutput();
        runGame.set(true);
    }


    @FXML
    public void playGame() {
        //if game not already populated, then populate it and play game

        if (currentGameState.size() == 0) {
            this.populateGame();
        }
        //create a new thread
        //keep on playing the game until runGame == true
        //it someone tries to pause/clear board
        //game will stop as it will set runGame.set(false)

        Runnable task = new Runnable() {
            @Override
            public void run() {
                runGame.set(true);
                Runnable screenUpdater = new Runnable() {
                    @Override
                    public void run() {
                        getNextStateOfGOL();
                    }
                };//screenUpdater
                while (runGame.get()) {
                    try {
                        Thread.sleep(120);

                    } catch (InterruptedException ex) {
                        System.out.println("Exception: " + ex);
                    }
                    Platform.runLater(screenUpdater);
                }
            }
        };
        Thread gameThread = new Thread(task);
        gameThread.setDaemon(true);
        gameThread.start();
    }
    /*******************************************************************************
     This function will pause/stop the game at any instance
     * if runGame == true
     *  pause game --> runGame == false
     * else
     *  stop game -->reset Game
     */

    @FXML
    public void pauseGame() {

        if (runGame.get()) {
            runGame.set(false);

            stopGame();
        } else {

            stopGame();
            resetGenerationCounter();
        }
    }//

    //this function will clear the
    private void clearDisplay() {
        this.outputPane.getChildren().clear();
    }

    //this method will reset Generation counter to Zero
    private void resetGenerationCounter() {
        this.iterationNumberLabel.setText("0");
        this.generationCounter = 0;
    }

    //this method with stop the game by clearing display and cleaning currentGameState arrayList
    private void stopGame() {
        clearDisplay();
        this.currentGameState = new ArrayList<>();
    }

    /*Generate a a list of Position with random number,
     *xMax: indicates the upper limit of random number of position in x-axis i.e. 0-xMax
     *yMax: indicates the upper limit of random number of position in y-axis i.e. 0-yMax
     *
     *
     */
    private ArrayList<Position> getPositionList(int xMax, int yMax) {

        return new GeneratePositions(xMax, yMax).getPositionArrayList();

    }
    /***************************************************************************/


}
