package sample;

import java.util.ArrayList;

class GameOfLife {
    private ArrayList<Position> aliveSeedList;
    private ArrayList<Position> nextStateAliveSeedList;

    private int maxWidthX;
    private int maxHeightY;

    GameOfLife(ArrayList<Position> aliveSeedList, int maxWidth, int maxHeight) {
        nextStateAliveSeedList = new ArrayList<>();
        this.aliveSeedList = aliveSeedList;
        this.maxWidthX = maxWidth;
        this.maxHeightY = maxHeight;
    }

    /***************************************************************************************
     /*
     * Input: Position
     * Output: int
     *
     * This programs will take a current position of a live cells and return how many neighbours it have
     * It calls following functions:
     *   getNeighbourPositions()
     *   isNeighbourAlive()
     *
     *   1. Create a list of neighbouring position
     *      --> this.getNeighbourPosition();
     *   2. For each neighbouring position check if it is alive
     *      --> isCellAlive()
     *   3. If neighbour is alive, neighbours[0]++
     *   4. Return total number of alive neighbours of currentPosition
     ***************************************************************************************/

    private int countAliveNeighbour(Position currentPosition) {
        int neighbour = 0;
        ArrayList<Position> neighbourPositions = this.getNeighbourPositions(currentPosition);

        for (Position position : neighbourPositions) {
            if (isCellAlive(aliveSeedList, position)) {
                neighbour++;
            }
        }
        return neighbour;
    }
    /***************************************************************************************
     /*
     * Input: position of alive cell
     * Output ArrayList<Position>
     * This program takes the position of a cell and get the Positions of neighbours
     * It uses enum Direction, and find neighbours by adding Directions to current cell position
     * Direction(x,y)
     * NorthWest(-1,-1),
     North(0,-1),
     NorthEast(1,-1),
     East(1,0),
     SouthEast(1,1),
     South(0,1),
     SouthWest(-1, 1),
     West(-1,0);
     * */
    /***************************************************************************************/

    private ArrayList<Position> getNeighbourPositions(Position position) {
        ArrayList<Position> neighbourPositions = new ArrayList<>();
        //iterate through Neighbours, add direction co-ordinates and current alive cell's co-ordinates
        for (Direction direction : Direction.values()) {
            //get  the position of a neighbour by going through enum Direction
            int neighbourPositionX = direction.getX() + position.getX();
            //connect the screen at x-axis
            if (neighbourPositionX > maxWidthX) {
                neighbourPositionX = 0;
            } else if (neighbourPositionX < 0) {
                neighbourPositionX = maxWidthX;
            }
            int neighbourPositionY = direction.getY() + position.getY();
            //connect the screen at y-axis to make loop
            if (neighbourPositionY > maxHeightY) {
                neighbourPositionY = 0;
            } else if (neighbourPositionY < 0) {
                neighbourPositionY = maxHeightY;
            }
            neighbourPositions.add(new Position(neighbourPositionX, neighbourPositionY));
        }
        //now neighbour list has all 8 neighbouring co-ordinates
        return neighbourPositions;
    }

    /********************************************************************************
     * A cell is alive if it has a alive seed
     * This method take a Position and determined that is it alive or dead
     * It iterates through list of alive cell, if position is in aliveSeedList,
     * return true
     * else return false
     *
     */
    private boolean isCellAlive(ArrayList<Position> currentList, Position position) {
        if (position == null)
            return false;
        for (Position currentListPosition : currentList) {
            if (currentListPosition.getX() == position.getX()
                    && currentListPosition.getY() == position.getY()) {
                return true;
            }
        }
        return false;
    }

    /*********************************************************************************
     * This methods determines the survival of seed
     * 1. Iterates through list of alive seed
     * 2. Get numberOfAliveNeighbours
     * 3. If number of neighbours == 2 || 3
     * 4.     add alive seed to next state of game
     *      --> nextStateAliveSeedList.add(position)
     */

    void survivalOfSeed() {
        for (Position position : aliveSeedList) {
            int numberOfAliveNeighbours = countAliveNeighbour(position);
            if (numberOfAliveNeighbours == 2 || numberOfAliveNeighbours == 3) {
                nextStateAliveSeedList.add(position);
            }
        }
    }

    /********************************************************************
     * This method iterates through list of alive cell
     * (life can only be created on a cell adjacent to alive cell)
     * 1. For every alive cell
     * 2.   Find  neighbouring dead cell
     * 3.       For each neighbouring dead cell
     * 4.             count its alive neighbours
     * 5.                 if alive neighbours == 3
     * 6.                    create life at dead cell
     **************************************************************************/
    void creationOfLIfe() {
        ArrayList<Position> neighbourOfAliveCell;
        for (Position currentAlivePosition : aliveSeedList) {
            neighbourOfAliveCell = getNeighbourPositions(currentAlivePosition);
            for (Position isCellAlive : neighbourOfAliveCell) {
                if (!isCellAlive(aliveSeedList, isCellAlive)) { // if the cell is dead
                    int neighbourCount = countAliveNeighbour(isCellAlive);
                    if (neighbourCount == 3 && (!isCellAlive(nextStateAliveSeedList, isCellAlive))) {
                        nextStateAliveSeedList.add(isCellAlive);
                    }
                }
            }
        }
    }

    /********************************************************************
     1. Check survival of seeds of current state of game
     2. Create new life based upon current state of game
     3. Prepare game for next state
     4. Return alive seed list, ready to be displayed
     /********************************************************************/
    ArrayList<Position> nextStateOfGame() {
        this.survivalOfSeed();
        this.creationOfLIfe();

        this.aliveSeedList.clear();
        this.aliveSeedList.addAll(this.nextStateAliveSeedList);
        this.nextStateAliveSeedList.clear();

        return aliveSeedList;
    }
}