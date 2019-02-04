package sample;

import java.util.ArrayList;

class GameOfLife {
    private ArrayList<Position> aliveCellList;
    private ArrayList<Position> nextStateAliveCellList;

    GameOfLife(ArrayList<Position> aliveCellList) {
        nextStateAliveCellList = new ArrayList<>();
        this.aliveCellList = aliveCellList;
    }

    /***************************************************************************************
     * Parameters: Position
     * Returns: int
     * Calls: this.getNeighbourPositions()
     * Called by: 1. survivalOfCell()
     *            2. creationOfLife()
     * ********************************************************************************
     * This programs will take a current position of a live cells and return its neighbour count
     *   1. Create a list of neighbouring position
     *      --> this.getNeighbourPosition();
     *   2. For each neighbouring position check if it is alive
     *      --> isCellAlive()
     *   3. If neighbour is alive, neighbour++
     *   4. Return total number of alive neighbours of currentPosition
     ***************************************************************************************/

    private int countAliveNeighbours(Position currentPosition) {
        int neighbour = 0;
        ArrayList<Position> neighbourPositions = this.getNeighbourPositions(currentPosition);

        for (Position position : neighbourPositions) {
            if (isCellAlive(aliveCellList, position)) {
                neighbour++;
            }
        }
        return neighbour;
    }
    /***************************************************************************************
     * Parameters: Position
     * Returns: ArrayList<Position>
     * Calls: Direction getX(), getY()
     * Called by: 1. this.countAliveNeighbours()
     *            2. this.creationOfLIfe()
     * ********************************************************************************
     * This program takes the position of a cell and get the Positions of neighbours
     * It uses enum Direction, and find neighbouring position by adding Directions to current cell position
     Direction(x,y)
     NorthWest(-1,-1),
     North(0,-1),
     NorthEast(1,-1),
     East(1,0),
     SouthEast(1,1),
     South(0,1),
     SouthWest(-1, 1),
     West(-1,0);
    /***************************************************************************************/

    private ArrayList<Position> getNeighbourPositions(Position position) {
        ArrayList<Position> neighbourPositions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int neighbourPositionX = direction.getX() + position.getX();
            int neighbourPositionY = direction.getY() + position.getY();
            neighbourPositions.add(new Position(neighbourPositionX, neighbourPositionY));
        }
        return neighbourPositions;
    }
    /********************************************************************************
     * Parameters: ArrayList<Position>, Position
     * Returns: boolean
     * Calls: None
     * Called by: 1. countAliveNeighbours()
     *            2. creationOfLIfe()
     * ********************************************************************************
     * This method take a Position and determined that is it alive or dead
     * It iterates through the list of alive cell, if position is in aliveCellList,
     *      return true
     * else return false
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
     * Parameters: None
     * Returns: void
     * Calls: countAliveNeighbours()
     * Called by: nextStateOfGame()
     * ********************************************************************************
     * This methods determines the survival of seed
     * 1. Iterates through list of alive seed
     * 2. Get numberOfAliveNeighbours
     * 3. If number of neighbours == 2 || 3
     * 4.     add alive seed to next state of game
     *      --> nextStateAliveCellList.add(position)
     */
    private void survivalOfCell() {
        for (Position position : aliveCellList) {
            int numberOfAliveNeighbours = countAliveNeighbours(position);
            if (numberOfAliveNeighbours == 2 || numberOfAliveNeighbours == 3) {
                nextStateAliveCellList.add(position);
            }
        }
    }
    /***************************************************************************
     * Parameters: None
     * Returns: void
     * Calls: 1. this.getNeighbourPositions()
     *        2. isCellAlive()
     *        3. countAliveNeighbours()
     * Called by: this.nextStateOfGame()
     * ********************************************************************************
     * This method iterates through list of alive cell
     * (life can only be created on a cell adjacent to alive cell)
     * 1. For every alive cell
     * 2.   Find neighbouring dead cell
     * 3.       For each neighbouring dead cell
     * 4.             count its alive neighbours
     * 5.                 if alive neighbours == 3
     * 6.                    create life at dead cell
     **************************************************************************/
    private void creationOfLIfe() {
        ArrayList<Position> neighbourOfAliveCell;
        for (Position currentAlivePosition : aliveCellList) {
            neighbourOfAliveCell = getNeighbourPositions(currentAlivePosition);
            for (Position isCellAlive : neighbourOfAliveCell) {
                if (!isCellAlive(aliveCellList, isCellAlive)) { // if the cell is dead
                    int neighbourCount = countAliveNeighbours(isCellAlive);
                    if (neighbourCount == 3 && (!isCellAlive(nextStateAliveCellList, isCellAlive))) {
                        nextStateAliveCellList.add(isCellAlive);
                    }
                }
            }
        }
    }
    /********************************************************************
     * Parameters: None
     * Returns: ArrayList<Position>
     * Calls: 1. this.survivalOfCell()
     *        2.  this.creationOfLIfe()
     * Called by: Controller getNextStateOfGOL()
     * ********************************************************************************
     1. Check survival of seeds of current state of game
     2. Create new life based upon current state of game
     3. Prepare game for next state
     4. Return alive seed list, ready to be displayed
     /********************************************************************/
    ArrayList<Position> nextStateOfGame() {
        this.survivalOfCell();
        this.creationOfLIfe();
        this.aliveCellList.clear();
        this.aliveCellList.addAll(this.nextStateAliveCellList);
        this.nextStateAliveCellList.clear();
        return aliveCellList;
    }
}