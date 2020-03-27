package common.model;

import java.io.Serializable;

public class Location implements Comparable<Location>, Serializable {

    private int x;
    private int y;

    public Location() {
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getCoordinates() {
        return "(" + x + "," + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Location location) {
        if (this.equals(location)) {
            return 0;
        } else if (this.y > location.y) {
            return 1;
        } else if (this.y < location.y) {
            return -1;
        } else {
            return Integer.compare(this.x, location.x);
        }
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }
}
