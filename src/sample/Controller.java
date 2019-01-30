package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {

    @FXML
    private Pane outputPane = new Pane();

    @FXML
    private ScrollPane scrollPane = new ScrollPane();

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
        System.out.println(outputPane.getPrefHeight());
        System.out.println(outputPane.getPrefWidth());
    }

    /*********************************************************************************
     This method with Populate game
     1. get co-ordinates of current output window and divide with outputCell size so cell are not overlapping
     2. get a list of position which occurs in the initial 1/3 centered location of board
     3. initiate game with the outbounds of output stage
     4. Enable play button so user can start playing game
     5. Display alive cell

     **********************************************************************************/
    @FXML
    private void populateGame() {
        // int maxWidthX = (int) outputPane.getBoundsInParent().getWidth() / outputCellSize;
        // int maxHeightY = (int) outputPane.getBoundsInParent().getHeight() / outputCellSize;
        int maxWidthX = (int) outputPane.getPrefWidth() / outputCellSize;
        int maxHeightY = (int) outputPane.getHeight() / outputCellSize;

        currentGameState = new GeneratePositions(maxWidthX, maxHeightY).getPositionArrayList();

        gameOfLife = new GameOfLife(currentGameState, maxWidthX, maxHeightY);

        this.playButton.setDisable(false);
        this.playButton.setText("Play");
        this.displayOutput();
    }
    /***********************************************************************************
     * This method will display output
     * It iterate through ArrayList<position> currentGameState
     * make a rectangle and add to the display pane
     *
     * 1. Check if cell are growing in -ve axis, and update the positions accordingly
     * 2. For each position in game
     * 3.   Check if position x and y axis are bigger than the outputPane
     *          if yes --> grow the pane to make space for 20 more cells in respective axis
     * 4. Create a rectangle with desired position and display it.
     *
     *
     ***********************************************************************************/
    private void displayOutput() {
        this.checkNegativeAxis();
        currentGameState.forEach((position) -> {
            if (position.getX() * outputCellSize > outputPane.getPrefWidth()) {
                outputPane.setPrefWidth(outputPane.getPrefWidth() + 20 * outputCellSize);
            }

            if (position.getY() * outputCellSize > outputPane.getPrefHeight()) {
                outputPane.setPrefHeight(outputPane.getPrefHeight() + 20 * outputCellSize);
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
     * This program will reset position if cell are growing in -ve axis
     * 1. For every position in currentGameState list
     * 2.   Find minX and minY
     * 3. If there is any growth in -ve axis then minX and/or minY will be <0
     *      minX->minimum it has gone in -ve X-axis and  minY->minimum it has gone in -ve Y-axis
     *      e.g. if there is two position,
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
     This method with get next generation of game
     * 1. Clear current display of alive cells
     * 2. Get next state of game with positions of alive cells
     * 3. Display output
     * 4. Increment generation counter
     * 5. Display generationCounter value on output label
     * *******************************************************************************/
    private void getNextStateOfGOL() {
        clearBoard();
        currentGameState = gameOfLife.nextStateOfGame();
        displayOutput();
        generationCounter++;
        generationNumberLabel.setText(String.valueOf(generationCounter));
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

    /*****************************************************************
     *  1. If game is in running state
     2. Pause the game by setting isGameRunning to false
     3. Set appropriate labels on the buttons
     4. Ig game is paused
     --> Play Button should have "Resume" label and should be enabled -->user can resume game from
     the current state
     --> pauseStop Button should have "Stop" label

     5. If game is paused already
     6.      Stop game by interrupting gameThread, it will stop gameThread
     7.      Clear output
     8. Reset buttons and labels to first state of game

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

            this.populateButton.setDisable(false);
            this.playButton.setDisable(true);
            this.pauseStopButton.setDisable(true);

            this.resetGenerationLabel();
            this.playButton.setText("Play");
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
}
