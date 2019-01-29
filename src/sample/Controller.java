package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {

    @FXML
    private Pane outputPane = new Pane();

    @FXML
    private Label generationNumberLabel = new Label();

    @FXML
    private Button playButton = new Button();

    @FXML
    private Button populateButton = new Button();

    @FXML
    private Button pauseStopButton = new Button();

    private Thread gameThread;
    private AtomicBoolean isGameRunning;
    private GameOfLife gameOfLife;
    private int outputCellSize;
    private ArrayList<Position> currentGameState;
    private int generationCounter;

    /*
     * This method with initiate the game with proper variable initialisation
     * **/

    @FXML
    private void initialize() {
        isGameRunning = new AtomicBoolean(false);// game is not running at start
        generationCounter = 0;

        currentGameState = new ArrayList<>();
        outputCellSize = 10; //output cell size is 10X10 pixel on grid
        playButton.setDisable(true);
        pauseStopButton.setDisable(true);


    }



    /*********************************************************************************
     This method with Populate game
     1. clearBoard() --> board must be clear of any existing out before population starts
     2. resetGenerationLabel() --> generation counter should be reset to 0
     3. clear currentGameState ArrayList so it does not hold state from previous game generation
     3. get co-ordinates of current output window and divide with outputCell size so cell are not overlapping
     4. get a list of position which occurs in the initial 1/3 centered location of board
     5. initiate game with the outbounds of output stage
     6. Enable play button so user can start playing game
     7. Display alive cell

     **********************************************************************************/
    @FXML
    private void populateGame() {

        this.clearBoard();
        this.resetGenerationLabel();
        this.currentGameState.clear();

        int maxWidthX = (int) outputPane.getBoundsInParent().getWidth() / outputCellSize;
        int maxHeightY = (int) outputPane.getBoundsInParent().getHeight() / outputCellSize;

        currentGameState = new GeneratePositions(maxWidthX, maxHeightY).getPositionArrayList();
        gameOfLife = new GameOfLife(currentGameState, maxWidthX, maxHeightY);

        this.playButton.setDisable(false);
        this.playButton.setText("Play");
        this.displayOutput();

    }

    /*
     * This method will display output
     * it will take positions of alive cell
     * iterate through ArrayList<position> currentGameState
     * make a rectangle and add to the display pane
     * */
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

    //This method with get next generation of game
    /*
     * 1. Increment generation counter
     * 2. Set value on output label
     * 3. Clear current display
     * 4. Get next state of game with positions of alive cells
     * 5. Display output
     * */
    private void getNextStateOfGOL() {
        generationCounter++;
        generationNumberLabel.setText(String.valueOf(generationCounter));
        clearBoard();
        currentGameState = gameOfLife.nextStateOfGame();
        displayOutput();
    }


    /**
     * This methods runs the game until user pause it
     * 1. Disable play and populate button, so that user does not create multiple threads to run game
     * 2. Create Runnable task
     * 3.   isGameRunning.set(true) --> Game is now in running mode
     * 4.   While the game is running, put the UI thread to sleep so game can get next state and prepare stage to display out
     * 5. Create game thread with the task
     * 6. Set Daemon to true, so thread will die, when application closes
     * 7. Start the thread to start having display on screen
     */

    @FXML
    private void playGame() {

        this.playButton.setDisable(true);
        this.populateButton.setDisable(true);
        this.pauseStopButton.setDisable(false);
        this.pauseStopButton.setText("Pause");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                isGameRunning.set(true);
                Runnable screenUpdater = new Runnable() {
                    @Override
                    public void run() {
                        getNextStateOfGOL();
                    }
                };//screenUpdater
                while (isGameRunning.get()) {
                    try {
                        Thread.sleep(120);

                    } catch (InterruptedException ex) {

                        System.out.println("Exception: " + ex);
                    }
                    Platform.runLater(screenUpdater);
                }
            }
        };

        gameThread = new Thread(task);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /* If game is in running state

        1. Pause the game by setting isGameRunning to false
        2. Enable play button to resume game again
     *
     * */
    @FXML
    private void pauseStopGame() {
        if (isGameRunning.get()) {
            isGameRunning.set(false);
            playButton.setDisable(false);
            playButton.setText("Resume");
            this.pauseStopButton.setText("Stop");
        } else {
            this.clearBoard();
            gameThread.interrupt();
            this.populateButton.setDisable(false);
            this.playButton.setDisable(true);
            this.pauseStopButton.setDisable(true);
            this.generationNumberLabel.setText("0");

        }
    }


    //This method will clear the board
    private void clearBoard() {
        this.outputPane.getChildren().clear();
    }

    //This method will reset Generation label
    private void resetGenerationLabel() {
        this.generationCounter = 0;
        this.generationNumberLabel.setText("0");
    }

    private ArrayList<Position> getPositionList(int xMax, int yMax) {
        return new GeneratePositions(xMax, yMax).getPositionArrayList();
    }
}
