package sample;

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

    private int outputCellSize = 5; //every output seed size is 5X5
    private ArrayList<Position> currentGameState;

    /*********************************************************************************
     This method with Populate game for the first time
     **********************************************************************************/
    @FXML
    public void populateGame() {

        //every


        //get width and height of current output window
        int maxWidthX = (int) outputPane.getBoundsInParent().getWidth() / 5;
        int maxHeightY = (int) outputPane.getBoundsInParent().getHeight() / 5;
        System.out.println(maxWidthX + " , " + maxHeightY);
        //
        currentGameState = this.getPositionList(maxWidthX, maxHeightY);
        this.displayOutput();
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
                    5, 5);
            rectangle.setStroke(Color.BEIGE);
            rectangle.setFill(Color.web("#AA3939"));
            outputPane.getChildren().add(rectangle);

        });
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
