package common.model;

import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.WALL;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import common.model.region.Region;

public class Board {

    private boolean isActive;
    private int loopCounter;

    private final MultiKeyMap<Integer, Region> regions = new MultiKeyMap<>();

    private Location begin;
    private Location end;

    private int activeRegionsCount = 0;

    public void putRegion(Region region) {
        Location location = region.getLocation();

        regions.put(location.getX(), location.getY(), region);

        if (region.getType() != WALL) {
            activeRegionsCount++;
        }

        if (begin == null) {
            begin = location;
            end = begin;
        } else if (location.compareTo(begin) < 0) {
            begin = location;
        } else if (location.compareTo(end) > 0) {
            end = location;
        }
    }

    public Region getRegion(Location location) {
        return regions.get(location.getX(), location.getY());
    }

    public MultiKeyMap<Integer, Region> getRegions() {
        return regions;
    }

    public List<Region> getTerritory(Color color) {
        return regions.values()
                .stream()
                .filter(region -> region.getColor() == color)
                .collect(toList());
    }

    public int getRegionCount() {
        return regions.size();
    }

    public int getActiveRegionsCount() {
        return activeRegionsCount;
    }

    public Pair<Location, Location> getDimensions() {
        return new ImmutablePair<>(begin, end);
    }

    public List<Region> getBases() {
        return regions.values()
                .stream()
                .filter(region -> region.getType() == BASE)
                .collect(toList());
    }

    public void clean() {
        loopCounter = 0;

        regions.values().forEach(region -> {
            region.setColor(null);
            region.setForces(0);
        });
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getLoopCounter() {
        return loopCounter;
    }

    public void incrementLoopCounter() {
        loopCounter++;
    }
}
