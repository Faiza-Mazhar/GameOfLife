package sample;

import java.util.ArrayList;

public class GameOfLife {
    private ArrayList<Position> aliveSeedList;
    private ArrayList<Position> nextStateAliveSeedList;

    private int maxWidthX;
    private int maxHeightY;


    protected GameOfLife(ArrayList<Position> aliveSeedList, int maxWidth, int maxHeight) {
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

    private int countNeighbour(Position currentPosition) {
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
            int neighbourPositionX = direction.getX() + position.getX();
            if (neighbourPositionX > maxWidthX) {
                neighbourPositionX = 0;
            } else if (neighbourPositionX < 0) {
                neighbourPositionX = maxWidthX;
            }
            int neighbourPositionY = direction.getY() + position.getY();
            if (neighbourPositionY > maxHeightY) {
                neighbourPositionY = 0;
            } else if (neighbourPositionY < 0) {
                neighbourPositionY = maxHeightY;
            }
            Position neighbourPosition = new Position(neighbourPositionX, neighbourPositionY);
            neighbourPositions.add(neighbourPosition);
        }
        return neighbourPositions;
    }

    /********************************************************************************
     *
     *
     * @param  position
     * @return boolean
     *
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
     * get numberOfNeighbours
     * if number of neighbours == 2 || 3
     *      add alive seed to next state of game
     *      nextStateAliveSeedList.add(position)
     */

    protected void survivalOfSeed() {
        int[] numberOfNeighbours = {0};
        aliveSeedList.forEach((position) -> {
            numberOfNeighbours[0] = countNeighbour(position);
            if (numberOfNeighbours[0] == 2 || numberOfNeighbours[0] == 3) {
                nextStateAliveSeedList.add(position);
                System.out.println("Seed alive at: " + position.getX() + " , " + position.getY());
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
                    int neighbourCount = countNeighbour(isCellAlive);

                    if (neighbourCount == 3 && (!isCellAlive(nextStateAliveSeedList, isCellAlive))) {
                        nextStateAliveSeedList.add(isCellAlive);
                        System.out.println("/***************************/");
                        System.out.println("Cell created at: " + isCellAlive.getX() + " , " + isCellAlive.getY());
                    }
                }
            }

        }
    }

    /********************************************************************/
    protected void nextState() {
        this.aliveSeedList.clear();
        this.aliveSeedList.addAll(this.nextStateAliveSeedList);
        this.nextStateAliveSeedList.clear();
    }


}

