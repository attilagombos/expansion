package common.model;

import java.io.Serializable;

public class PlayerState implements Serializable {

    private Color color;

    private Location base;

    private Integer reinforcements;

    public PlayerState() {
    }

    public PlayerState(Color color, Location base, Integer reinforcements) {
        this.color = color;
        this.base = base;
        this.reinforcements = reinforcements;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Location getBase() {
        return base;
    }

    public void setBase(Location base) {
        this.base = base;
    }

    public Integer getReinforcements() {
        return reinforcements;
    }

    public void setReinforcements(Integer reinforcements) {
        this.reinforcements = reinforcements;
    }
}
