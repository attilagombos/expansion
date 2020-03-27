package common.model;

import java.io.Serializable;

public class BoardState implements Serializable {

    private String layout;

    private String colors;

    private String forces;

    public BoardState() {
    }

    public BoardState(String layout, String colors, String forces) {
        this.layout = layout;
        this.colors = colors;
        this.forces = forces;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getForces() {
        return forces;
    }

    public void setForces(String forces) {
        this.forces = forces;
    }
}
