package common.model.game;

import java.util.HashMap;
import java.util.Map;

public class Region {

    private final Location location;

    private final RegionType type;

    private final RegionState state = new RegionState();

    private final Map<Color, Integer> changes = new HashMap<>();

    public Region(Location location, RegionType type) {
        this.location = location;
        this.type = type;
    }

    public void addChange(int forces) {
        addChange(state.getColor(), forces);
    }

    public void addChange(Color color, int forces) {
        changes.merge(color, forces, Integer::sum);
    }

    public Color getColor() {
        return state.getColor();
    }

    public void setColor(Color color) {
        state.setColor(color);
    }

    public int getForces() {
        return state.getForces();
    }

    public void setForces(int forces) {
        state.setForces(forces);
    }

    public Location getLocation() {
        return location;
    }

    public RegionType getType() {
        return type;
    }

    public RegionState getState() {
        return state;
    }

    public Map<Color, Integer> getChanges() {
        return changes;
    }

    public String getCoordinates() {
        return location.getCoordinates();
    }

    @Override
    public String toString() {
        return location.getCoordinates();
    }
}
