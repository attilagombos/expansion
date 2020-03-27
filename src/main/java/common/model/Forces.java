package common.model;

import java.io.Serializable;

import common.model.region.Region;

public class Forces implements Serializable {

    private int x;

    private int y;

    private int count;

    public Forces() {
    }

    public Forces(Region region) {
        this(region.getLocation(), region.getForces());
    }

    public Forces(Location location, int count) {
        this(location.getX(), location.getY(), count);
    }

    public Forces(int x, int y, int count) {
        this.x = x;
        this.y = y;
        this.count = count;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Forces{" + "x=" + x + ", y=" + y + ", count=" + count + '}';
    }
}
