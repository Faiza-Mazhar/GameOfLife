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
     * This method with initiate the game with variables' initialisation
     * **/

    @FXML
    private void initialize() {
        isGameRunning = new AtomicBoolean(false);// game is not running at start
        generationCounter = 0;
        currentGameState = new ArrayList<>();
        outputCellSize = 10; //output cell size is 10X10 pixel on grid
        playButton.setDisable(true); // at the start Play button is disables
        pauseStopButton.setDisable(true); // at the start pauseStop button is disables
    }

    /*********************************************************************************
     * Parameters: None
     * Returns: void
     * Calls:   this.clearBoard()
     * Called by: None
     * ********************************************************************************
     This method with Populate game
     1. Get co-ordinates of current output window and divide with outputCell size so cell are not overlapping
     2. Get a list of random position
     3. Initiate Game of Life
     4. Enable Play button
     5. Display given state of game
     **********************************************************************************/

    @FXML
    private void populateGame() {
        int maxWidthX = (int) outputPane.getPrefWidth() / outputCellSize;
        int maxHeightY = (int) outputPane.getHeight() / outputCellSize;

        this.currentGameState.clear();
        this.clearBoard();

        currentGameState = generateRandomList(maxWidthX, maxHeightY);
        gameOfLife = new GameOfLife(currentGameState);

        this.playButton.setDisable(false);
        this.playButton.setText("Play");
        this.displayOutput();
    }
    /***********************************************************************************
     * Parameters: None
     * Returns: void
     * Calls:
     *            1. this.checkNegativeAxis()
     * Called by:
     *            1. getNextStateOfGOL()
     *            2. populateGame()
     * ********************************************************************************
     * This method will display output
     * 1. Check if cell are growing in -ve axis, and update the positions accordingly
     * 2. For each position in game
     * 3.   Check if position x and y axis are bigger than the outputPane
     *          if yes -> grow the pane to make space for 20 more cells in respective axis
     * 4. Create a rectangle with desired position and display it.
     ***********************************************************************************/
    private void displayOutput() {
        this.checkNegativeAxis();
        currentGameState.forEach((position) -> {
            if (position.getX() * outputCellSize > outputPane.getPrefWidth()) {
                outputPane.setPrefWidth(outputPane.getPrefWidth() + (20 * outputCellSize));
            }

            if (position.getY() * outputCellSize > outputPane.getPrefHeight()) {
                outputPane.setPrefHeight(outputPane.getPrefHeight() + (20 * outputCellSize));
            }

            Rectangle rectangle = new Rectangle(
                    position.getX() * outputCellSize,
                    position.getY() * outputCellSize,
                    outputCellSize, outputCellSize);
            rectangle.setStroke(Color.valueOf("#6F5AAC"));
            rectangle.setFill(Color.web("#E3DDF6"));
            outputPane.getChildren().add(rectangle);
        });
    }
    /*******************************************************************************
     * Parameters: None
     * Returns: void
     * Calls: None
     * Called by: displayOutput()
     * ********************************************************************************
     * This program will reset positions if cell are growing in -ve axis
     * 1. For every position in currentGameState list
     * 2.   Find minimum numbers in -ve axis
     * 3. If there is any growth in -ve axis then minX and/or minY will be <0
     *      minX-> minimum it has gone in -ve X-axis and  minY-> minimum it has gone in -ve Y-axis
     *      e.g. if there are two position such as,
     *      Position1 =  (x: -4, y: -5) and Position2 =(x: -3, y: -6)
     *      then minX = -4, minY = -6
     * 4.   Reset the position in currentGameState w.r.t minX and minY e.g.Position1(0, 1) and Position2(1 , 0)
     */
    private void checkNegativeAxis() {
        int minX = 0;
        int minY = 0;
        for (Position position : currentGameState) {
            if (position.getX() < minX) {
                minX = position.getX();
            }
            if (position.getY() < minY) {
                minY = position.getY();
            }
        }

        if (minX < 0 || minY < 0) {
            for (Position position : currentGameState) {
                if (minX < 0)
                    position.setX(position.getX() + (minX * (-1)));
                if (minY < 0)
                    position.setY(position.getY() + (minY * (-1)));
            }
        }
    }
    /******************************************************************************
     * Parameters: None
     * Returns: void
     * Calls:  1. this.clearBoard();
     *         2. GameOfLife nextStateOfGame();
     *         3. this.displayOutput();
     * Called by: 1. this.playGame()
     * ********************************************************************************
     This method with get next generation of game
     * 1. Clear current display of alive cells
     * 2. Get next state of game with positions of alive cells
     * 3. Display output
     * 4. Increment generation counter and update
     * *******************************************************************************/
    private void getNextStateOfGOL() {
        this.clearBoard();
        currentGameState = gameOfLife.nextStateOfGame();
        this.displayOutput();
        generationCounter++;
        generationNumberLabel.setText(String.valueOf(generationCounter));
    }
    /*************************************************************************************
     * Parameters: None
     * Returns: void
     * Calls: getNextStateOfGOL();
     * Called by: Play button in GUI
     * ********************************************************************************
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

    /*****************************************************************
     * Parameters: None
     * Returns: void
     * Calls:  this.clearBoard()
     * Called by: Pause/Stop button in GUI
     *
     * ********************************************************************************
     *  1. If game is in running state
     2. Pause the game by setting isGameRunning to false
     3. Set appropriate labels on the buttons
     4. e.g. game is paused
     --> Play Button should have "Resume" label and should be enabled
     --> pauseStop Button should have "Stop" label
     5. If game is paused
     6.      Stop game by interrupting gameThread
     7.      Clear output
     8. Reset game

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
            gameThread.interrupt();
            this.clearBoard();
            this.resetGame();
        }
    }

    /*****************************************************************************************
     * Parameters: None
     * Returns: void
     * Calls: None
     * Called by: 1. populateGame()
     *            2. getNextStateOfGOL()
     *            3. pauseStopGame()
     *
     * ********************************************************************************
     //This method will clear the board*/
    private void clearBoard() {
        this.outputPane.getChildren().clear();
    }

    /*****************************************************************************************
     * Parameters: None
     * Returns: void
     * Calls: None
     * Called by: 1. pauseStopGame()
     *
     * ********************************************************************************
     * This method will reset game to initial state*/
    private void resetGame() {
        this.generationCounter = 0;
        generationNumberLabel.setText(String.valueOf(generationCounter));

        this.populateButton.setDisable(false);
        this.playButton.setDisable(true);
        this.pauseStopButton.setDisable(true);
        this.playButton.setText("Play");

        outputPane.setPrefWidth(2000);
        outputPane.setPrefHeight(1000);
    }

    /*****************************************************************************************
     * Parameters: int, int
     * Returns: ArrayList<Position></Position>
     * Calls: GeneratePositions getPositionArrayList()
     * Called by: populateGame()
     *
     * ********************************************************************************
     This method will create max n min ranges to Randomly generate position for first population of game.
     1. Ranges are set that positions are only in the centre 1/3 of the stage
     ************************************************************************************/
    private ArrayList<Position> generateRandomList(int maxWidthX, int maxHeightY) {
        int minStartingRangeX = maxWidthX / 3;
        int maxStartingRangeX = (maxWidthX * 2) / 3;
        int minStartingRangeY = maxHeightY / 3;
        int maxStartingRangeY = (maxHeightY * 2) / 3;
        return new GeneratePositions(minStartingRangeX, maxStartingRangeX, minStartingRangeY, maxStartingRangeY).getPositionArrayList();
    }

}