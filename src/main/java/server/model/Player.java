package server.model;

import java.util.ArrayList;
import java.util.List;

import common.model.game.Color;
import common.model.game.Region;

public class Player {

    private final String name;

    private final Color color;

    private final List<Region> territory = new ArrayList<>();

    private Region base;

    private int reinforcements;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<Region> getTerritory() {
        return territory;
    }

    public Region getBase() {
        return base;
    }

    public void setBase(Region base) {
        this.base = base;
    }

    public int getReinforcements() {
        return reinforcements;
    }

    public void setReinforcements(int reinforcements) {
        this.reinforcements = reinforcements;
    }
}
