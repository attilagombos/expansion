package server.model;

import java.util.ArrayList;
import java.util.List;

import common.model.Color;
import common.model.Region;

public class Player {

    private String name;

    private Color color;

    private Region base;

    private int deployableForces;

    private int deployedForces;

    private List<Region> territory = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Region getBase() {
        return base;
    }

    public void setBase(Region base) {
        this.base = base;
    }

    public int getDeployableForces() {
        return deployableForces;
    }

    public void setDeployableForces(int deployableForces) {
        this.deployableForces = deployableForces;
    }

    public int getDeployedForces() {
        return deployedForces;
    }

    public void setDeployedForces(int deployedForces) {
        this.deployedForces = deployedForces;
    }

    public List<Region> getTerritory() {
        return territory;
    }
}
