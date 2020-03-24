package common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BoardStatus implements Serializable {

    private Color color;

    private Location base;

    private Integer deployableForces;

    private String layout;

    private Map<Color, List<Forces>> forcesByColor;

    public BoardStatus() {
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

    public Integer getDeployableForces() {
        return deployableForces;
    }

    public void setDeployableForces(Integer deployableForces) {
        this.deployableForces = deployableForces;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Map<Color, List<Forces>> getForcesByColor() {
        return forcesByColor;
    }

    public void setForcesByColor(Map<Color, List<Forces>> forcesByColor) {
        this.forcesByColor = forcesByColor;
    }
}
