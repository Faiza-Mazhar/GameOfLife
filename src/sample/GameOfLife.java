package sample;

import java.util.ArrayList;

public class GameOfLife {
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
    /***************************************************************************************/
    /*
     * Input: Position
     * Output: int
     *
     * This programs will take a current position of a live cells and return how many neighbours it have
     * It calls following functions:
     *   getNeighbourPositions()
     *   isNeighbourAlive()
     * */

    /***************************************************************************************/

    private int countAliveNeighbour(Position currentPosition) {
        int[] neighbours = {0};
        //neighbourPosition holds the position of all 8 neighbours
        ArrayList<Position> neighbourPositions = this.getNeighbourPositions(currentPosition);

        //for each neighbour, check if it is alive
        //if alive, neighbour++
        neighbourPositions.forEach((position) -> {
            if (isCellAlive(aliveSeedList, position)) {
                neighbours[0]++;
            }
        });
        return neighbours[0];
    }

    /***************************************************************************************/
    /*
    * Input: position of alive cell
    * Output Arraylist<Position>
    * This program takes the position of a cell and get the Positions of neighbours
    * It uses enum Neighbours, and find neighbours in
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
     *
     *
     * @param  position
     * @return boolean
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

        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getX() == position.getX()
                    && currentList.get(i).getY() == position.getY()) {
                return true;
            }
        }
        return false;

    }

    /*********************************************************************************
     * This methods determines the survival of seed
     * it iterates through list of alive seed
     * get numberOfAliveNeighbours
     * if number of neighbours == 2 || 3
     *      add alive seed to next state of game
     *      i.e. nextStateAliveSeedList.add(position)
     */

    //for every alive seed, check its
    protected void survivalOfSeed() {
        int[] numberOfAliveNeighbours = {0};
        aliveSeedList.forEach((position) -> {
            numberOfAliveNeighbours[0] = countAliveNeighbour(position);
            if (numberOfAliveNeighbours[0] == 2 || numberOfAliveNeighbours[0] == 3) {
                nextStateAliveSeedList.add(position);
            }
        });
    }


    /********************************************************************
     * This method iterates through list of alive cell
     * (life can only be created on a cell adjacent to alive cell)
     * finds neighbouring dead cell
     * for each neighbouring dead cell
     *      count its alive neighbours
     *          if alive neighbours == 3
     *              create life at dead cell
     **************************************************************************/
    protected void creationOfLIfe() {
        ArrayList<Position> neighbourOfAliveCell;

        for (Position currentAlivePosition : aliveSeedList) {
            //get the co-ordinates of neighbouring cells of current alive cell
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

    /********************************************************************/
    protected ArrayList<Position> nextStateOfGame() {

        //check survival of seeds and creation of new life
        this.survivalOfSeed();
        this.creationOfLIfe();

        //prepare game for next state
        this.aliveSeedList.clear();
        this.aliveSeedList.addAll(this.nextStateAliveSeedList);
        this.nextStateAliveSeedList.clear();

        return aliveSeedList;
    }

}

