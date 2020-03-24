package common.model;

public class Region {

    private final Location location;

    private final RegionType type;

    private final RegionState state = new RegionState();

    public Region(Location location, RegionType type) {
        this.location = location;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public RegionType getType() {
        return type;
    }

    public void setForces(Color color, int forces) {
        state.setColor(color);
        state.setForces(forces);
    }

    public int getForces() {
        return state.getForces();
    }

    public void setForces(int forces) {
        if (state.getColor() != null) {
            state.setForces(forces);
        }
    }

    public Color getColor() {
        return state.getColor();
    }

    public boolean isOccupied() {
        return state.getForces() > 0;
    }
}
