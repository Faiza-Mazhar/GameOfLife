package sample;

//Create an Enum of direction and co-ordination of a Cell's neighbours w.r.t to current alive cell
public enum Direction {

    NorthWest(-1, -1),
    North(0, -1),
    NorthEast(1, -1),
    East(1, 0),
    SouthEast(1, 1),
    South(0, 1),
    SouthWest(-1, 1),
    West(-1, 0);

    private int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
