package common.layer;

import static common.model.game.RegionType.WALL;

import common.model.game.Color;
import common.model.game.Location;
import common.model.game.Region;
import common.model.game.RegionType;

public class RegionTestHelper {

    public static Region wall(int x, int y) {
        return region(x, y, WALL);
    }

    public static Region region(int x, int y, RegionType type) {
        Location location = new Location(x, y);

        return new Region(location, type);
    }

    public static Region region(int x, int y, RegionType type, Color color, int forces) {
        Location location = new Location(x, y);

        Region region = new Region(location, type);

        region.setColor(color);
        region.setForces(forces);

        return region;
    }
}
