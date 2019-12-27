package model;

public class Coordinate {

    private int xCoordinate;
    private int yCoordinate;

    public Coordinate(Coordinate coordinate) {
        this.xCoordinate = coordinate.getxCoordinate();
        this.yCoordinate = coordinate.getyCoordinate();
    }

    public Coordinate(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString() {
        return "" + xCoordinate + "," + yCoordinate;
    }

    //Overriding the methods to make sure that the key in the nodes matches with the coordinates
    //Comparing the x and y coordinate in the equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;

        Coordinate that = (Coordinate) o;

        if (getxCoordinate() != that.getxCoordinate()) return false;
        return getyCoordinate() == that.getyCoordinate();
    }

    @Override
    public int hashCode() {
        int result = getxCoordinate();
        result = 31 * result + getyCoordinate();
        return result;
    }
}
