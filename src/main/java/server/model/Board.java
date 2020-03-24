package server.model;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import common.model.Color;
import common.model.Forces;
import common.model.Location;
import common.model.Region;

public class Board {

    private final MultiKeyMap<Integer, Region> regions = new MultiKeyMap<>();

    private final Map<Color, Region> bases = new HashMap<>();

    private String layout;

    private Location begin;
    private Location end;

    private Map<Color, List<Forces>> forcesByColor;

    public void putRegion(Region region) {
        Location location = region.getLocation();

        regions.put(location.getX(), location.getY(), region);

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
        return regions.clone();
    }

    public int getRegionCount() {
        return regions.size();
    }

    public Pair<Location, Location> getDimensions() {
        return new ImmutablePair<>(begin, end);
    }

    public Map<Color, Region> getBases() {
        return bases;
    }

    public Map<Color, List<Forces>> getForcesByColor() {
        forcesByColor = regions.values()
                .stream()
                .filter(region -> region.getColor() != null)
                .collect(groupingBy(Region::getColor, mapping(Forces::new, toList())));

        return forcesByColor;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
