package common.layer;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import common.model.Board;
import common.model.Color;
import common.model.Location;
import common.model.region.Region;
import common.model.region.RegionType;

@Component("HtmlWriter")
public class HtmlLayerWriter implements ILayerWriter {

    private static final String TABLE_FORMAT = "<table class=\"%s\">%s</table>";
    private static final String TABLE_ROW_FORMAT = "<tr>%s</tr>";
    private static final String TABLE_DATA_FORMAT = "<td class=\"%s\">%s</td>";

    private static final String LAYOUT = "layout";
    private static final String COLORS = "colors";
    private static final String FORCES = "forces";

    private String layoutCache = null;

    public String writeLayout(Board board) {
        if (layoutCache == null) {
            List<String> rows = new ArrayList<>();

            Pair<Location, Location> dimensions = board.getDimensions();
            Location begin = dimensions.getLeft();
            Location end = dimensions.getRight();

            for (int locationY = end.getY(); locationY >= begin.getY(); locationY--) {
                List<String> row = new ArrayList<>();

                for (int locationX = begin.getX(); locationX <= end.getX(); locationX++) {
                    Location location = new Location(locationX, locationY);
                    Region region = board.getRegion(location);

                    RegionType type = region.getType();

                    String classes = type.name().toLowerCase();

                    row.add(format(TABLE_DATA_FORMAT, classes, EMPTY));
                }

                rows.add(format(TABLE_ROW_FORMAT, join(EMPTY, row)));
            }

            layoutCache = format(TABLE_FORMAT, LAYOUT, join(EMPTY, rows));
        }

        return layoutCache;
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

                Color color = region.getColor();

                String classes = color != null ? color.name().toLowerCase() : EMPTY;

                row.add(format(TABLE_DATA_FORMAT, classes, EMPTY));
            }

            rows.add(format(TABLE_ROW_FORMAT, join(EMPTY, row)));
        }

        return format(TABLE_FORMAT, COLORS, join(EMPTY, rows));
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

                String forces =  region.getForces() > 1 ? valueOf(region.getForces() - 1) : EMPTY;

                row.add(format(TABLE_DATA_FORMAT, EMPTY, forces));
            }

            rows.add(format(TABLE_ROW_FORMAT, join(EMPTY, row)));
        }

        return format(TABLE_FORMAT, FORCES, join(EMPTY, rows));
    }
}
