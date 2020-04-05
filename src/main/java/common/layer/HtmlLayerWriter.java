package common.layer;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import common.model.game.Board;
import common.model.game.Color;
import common.model.game.Location;
import common.model.game.Region;
import common.model.game.RegionType;

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
        return layoutCache == null ? writeLayer(board, LAYOUT, this::getRegionTypeCell) : layoutCache;
    }

    public String writeColors(Board board) {
        return writeLayer(board, COLORS, this::getColorCell);
    }

    public String writeForces(Board board) {
        return writeLayer(board, FORCES, this::getForcesCell);
    }

    private String writeLayer(Board board, String tableClass, Function<Region, String> cellWriter) {
        List<String> rows = new ArrayList<>();

        Pair<Location, Location> dimensions = board.getDimensions();
        Location begin = dimensions.getLeft();
        Location end = dimensions.getRight();

        for (int locationY = end.getY(); locationY >= begin.getY(); locationY--) {
            List<String> row = new ArrayList<>();

            for (int locationX = begin.getX(); locationX <= end.getX(); locationX++) {
                Location location = new Location(locationX, locationY);
                Region region = board.getRegion(location);

                row.add(cellWriter.apply(region));
            }

            rows.add(format(TABLE_ROW_FORMAT, join(EMPTY, row)));
        }

        return format(TABLE_FORMAT, tableClass, join(EMPTY, rows));
    }

    private String getRegionTypeCell(Region region) {
        RegionType type = region.getType();
        String classes = type.name().toLowerCase();

        return format(TABLE_DATA_FORMAT, classes, EMPTY);
    }

    private String getColorCell(Region region) {
        Color color = region.getColor();
        String classes = color != null ? color.name().toLowerCase() : EMPTY;

        return format(TABLE_DATA_FORMAT, classes, EMPTY);
    }

    private String getForcesCell(Region region) {
        String forces = region.getForces() > 1 ? valueOf(region.getForces() - 1) : EMPTY;

        return format(TABLE_DATA_FORMAT, EMPTY, forces);
    }
}
