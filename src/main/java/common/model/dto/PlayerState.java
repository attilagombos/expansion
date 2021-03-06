package common.model.dto;

import java.io.Serializable;

import common.model.game.Color;
import common.model.game.Location;

public class PlayerState implements Serializable {

    private String name;

    private Color color;

    private Location base;

    private Integer territory;

    private Integer bases;

    private Integer mines;

    private Integer lands;

    private Integer forces;

    private Integer reinforcements;

    public PlayerState() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getTerritory() {
        return territory;
    }

    public void setTerritory(Integer territory) {
        this.territory = territory;
    }

    public Integer getBases() {
        return bases;
    }

    public void setBases(Integer bases) {
        this.bases = bases;
    }

    public Integer getMines() {
        return mines;
    }

    public void setMines(Integer mines) {
        this.mines = mines;
    }

    public Integer getLands() {
        return lands;
    }

    public void setLands(Integer lands) {
        this.lands = lands;
    }

    public Integer getForces() {
        return forces;
    }

    public void setForces(Integer forces) {
        this.forces = forces;
    }

    public Integer getReinforcements() {
        return reinforcements;
    }

    public void setReinforcements(Integer reinforcements) {
        this.reinforcements = reinforcements;
    }
}
