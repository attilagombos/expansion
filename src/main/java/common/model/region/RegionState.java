package common.model.region;

import common.model.Color;

public class RegionState {

    private Color color;

    private int forces;

    public RegionState() {
    }

    public RegionState(Color color, int forces) {
        this.color = color;
        this.forces = forces;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getForces() {
        return forces;
    }

    public void setForces(int forces) {
        this.forces = forces;
    }
}
