package sample;

import java.util.ArrayList;
import java.util.Random;

class GeneratePositions {

    private int maxX;
    private int maxY;
    private ArrayList<Position> positionArrayList;

    GeneratePositions(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        positionArrayList = new ArrayList<>();
    }

    /**************************************************************************
     * Generate random positions for game
     *1. Ranges are set that positions are only in the centre 1/3 of the stage
     *2. If a position is not in list, add position
     *3.      else i--, so that out total number of position stays same
     *4.      Total number of position should be OutputHeight + Output Length
     */
    ArrayList<Position> getPositionArrayList() {
        Random random = new Random();
        int minStartingRangeX = this.maxX / 3;
        int maxStartingRangeX = (this.maxX * 2) / 3;
        int minStartingRangeY = this.maxY / 3;
        int maxStartingRangeY = (this.maxY * 2) / 3;
        for (int i = 0; i < (this.maxX + this.maxY) * 2; i++) {
            //create upper and lower limit of position
            int xPosition = random.nextInt(
                    (maxStartingRangeX - minStartingRangeX) + 1) + minStartingRangeX;
            int yPosition = random.nextInt(
                    (maxStartingRangeY - minStartingRangeY) + 1) + minStartingRangeY;
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
     *
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
