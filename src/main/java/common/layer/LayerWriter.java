package common.layer;

import static com.google.common.base.Strings.repeat;
import static common.layer.LayoutMapping.COLUMN_DELIMITER;
import static common.layer.LayoutMapping.ROW_DELIMITER;
import static common.layer.LayoutMapping.regionTypeToSymbol;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.WALL;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import common.model.Board;
import common.model.Color;
import common.model.Location;
import common.model.region.Region;

@Component
public class LayerWriter {

    public String writeLayout(Board board) {
        List<String> rows = new ArrayList<>();

        Pair<Location, Location> dimensions = board.getDimensions();
        Location begin = dimensions.getLeft();
        Location end = dimensions.getRight();

        for (int locationY = end.getY(); locationY >= begin.getY(); locationY--) {
            List<String> row = new ArrayList<>();

            for (int locationX = begin.getX(); locationX <= end.getX(); locationX++) {
                Location location = new Location(locationX, locationY);
                Region region = board.getRegion(location);

                row.add(regionTypeToSymbol(region.getType()).toString());
            }

            rows.add(join(COLUMN_DELIMITER, row));
        }

        return join(ROW_DELIMITER, rows);
    }

    public String writeColors(Board board) {
        List<String> rows = new ArrayList<>();

        Pair<Location, Location> dimensions = board.getDimensions();
        Location begin = dimensions.getLeft();
        Location end = dimensions.getRight();

        for (int locationY = end.getY(); locationY >= begin.getY(); locationY--) {
            List<String> row = new ArrayList<>();

            for (int locationX = begin.getX(); locationX <= end.getX(); locationX++) {
                Location location = new Location(locationX, locationY);
                Region region = board.getRegion(location);

                if (region.getType() == WALL) {
                    row.add(regionTypeToSymbol(WALL).toString());
                } else {
                    Color color = region.getColor();
                    row.add(color != null ? valueOf(color.ordinal() + 1) : regionTypeToSymbol(LAND).toString());
                }
            }

            rows.add(join(COLUMN_DELIMITER, row));
        }

        return join(ROW_DELIMITER, rows);
    }

    public String writeForces(Board board) {
        List<String> rows = new ArrayList<>();

        Pair<Location, Location> dimensions = board.getDimensions();
        Location begin = dimensions.getLeft();
        Location end = dimensions.getRight();

        for (int locationY = end.getY(); locationY >= begin.getY(); locationY--) {
            List<String> row = new ArrayList<>();

            for (int locationX = begin.getX(); locationX <= end.getX(); locationX++) {
                Location location = new Location(locationX, locationY);
                Region region = board.getRegion(location);

                if (region.getType() == WALL) {
                    row.add(repeat(regionTypeToSymbol(WALL).toString(), 4));
                } else {
                    row.add(format("%4d", region.getForces()));
                }
            }

            rows.add(join(COLUMN_DELIMITER, row));
        }

        return join(ROW_DELIMITER, rows);
    }
}
