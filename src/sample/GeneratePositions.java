package sample;

import java.util.ArrayList;
import java.util.Random;

class GeneratePositions {

    private int maxX;
    private int maxY;
    private ArrayList<Position> positionArrayList;

    protected GeneratePositions(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        positionArrayList = new ArrayList<Position>();
    }

    protected ArrayList<Position> getPositionArrayList() {

        Random random = new Random();
        int minStartingRangeX = this.maxX / 3;
        int maxStartingRangeX = (this.maxX * 2) / 3;
        int minStartingRangeY = this.maxY / 3;
        int maxStartingRangeY = (this.maxY * 2) / 3;


        for (int i = 0; i < maxStartingRangeX + maxStartingRangeY; i++) {
            int xPosition = random.nextInt((maxStartingRangeX - minStartingRangeX) + 1) + minStartingRangeX;
            int yPosition = random.nextInt((maxStartingRangeY - minStartingRangeY) + 1) + minStartingRangeY;
            Position currentPosition = new Position(xPosition, yPosition);
            //System.out.println("In getPositionArrayList: xPosition " + xPosition + " :: yPosition" + yPosition );
            if (!isCellAlive(currentPosition)) {
                positionArrayList.add(currentPosition);
            }
        }

        return positionArrayList;
    }


    private boolean isCellAlive(Position position) {
        if (position == null)
            return false;
        for (int i = 0; i < positionArrayList.size(); i++) {
            if (positionArrayList.get(i).getX() == position.getX()
                    && positionArrayList.get(i).getY() == position.getY()) {
                return true;
            }
        }
        return false;
    }
}
