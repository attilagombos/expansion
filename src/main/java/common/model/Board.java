package common.model;

import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.WALL;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import common.model.region.Region;

public class Board {

    private final MultiKeyMap<Integer, Region> regions = new MultiKeyMap<>();

    private Location begin;
    private Location end;

    private int baseCount = 0;
    private int activeRegionsCount = 0;

    private List<Region> availableBases = new CopyOnWriteArrayList<>();

    private boolean isActive;

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

    public void initialize() {
        regions.values()
                .stream()
                .filter(region -> region.getType() == BASE)
                .forEach(availableBases::add);

        shuffle(availableBases);

        baseCount = availableBases.size();
    }

    public void clean() {
        isActive = false;

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

    public List<Region> getAvailableBases() {
        return availableBases;
    }

    public int getBaseCount() {
        return baseCount;
    }
}
