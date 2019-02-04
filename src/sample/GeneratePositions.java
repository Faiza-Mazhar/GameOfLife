package sample;

import java.util.ArrayList;
import java.util.Random;

class GeneratePositions {

    private int minX, maxX;
    private int minY, maxY;
    private ArrayList<Position> positionArrayList;

    GeneratePositions(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        positionArrayList = new ArrayList<>();
    }
    /**************************************************************************
     * Parameters: None
     * Returns: ArrayList<Position>
     * Calls: 1. isDuplicate()
     * Called by: 1. Controller generateRandomList();
     * ********************************************************************************
     * Generate random positions for game
     *1. Generate a random position
     *2. If a position is not in list, add position
     *3.      else i--, so that out total number of position stays same
     *4.      Total number of position should be OutputHeight + OutputLength
     ************************************************************************************/
    ArrayList<Position> getPositionArrayList() {
        Random random = new Random();

        for (int i = 0; i < (this.maxX + this.maxY) * 2; i++) {
            //create upper and lower limit of position
            int xPosition = random.nextInt(
                    (maxX - minX) + 1) + minX;
            int yPosition = random.nextInt(
                    (maxY - minY) + 1) + minY;
            Position currentPosition = new Position(xPosition, yPosition);
            if (!isDuplicate(currentPosition)) {
                positionArrayList.add(currentPosition);
            } else {
                i--;
            }
        }
        return positionArrayList;
    }
    /***********************************************************************************
     * Parameters: Position
     * Returns: boolean
     * Calls: None
     * Called by: this.getPositionArrayList()
     * ********************************************************************************
     This method checks that positions are not duplicated in list
     1. Iterate through list,
     2.     if any position in list is same as the the current position
     return true;
     else
     return false
     */
    private boolean isDuplicate(Position currentPosition) {
        if (currentPosition == null)
            return false;
        for (Position position : positionArrayList
        ) {
            if (position.getX() == currentPosition.getX() &&
                    position.getY() == currentPosition.getY()) {
                return true;
            }
        }
        return false;
    }
}
