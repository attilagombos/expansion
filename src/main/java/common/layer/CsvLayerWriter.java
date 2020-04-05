package common.layer;

import static com.google.common.base.Strings.repeat;
import static common.layer.LayoutMapping.COLUMN_DELIMITER;
import static common.layer.LayoutMapping.ROW_DELIMITER;
import static common.layer.LayoutMapping.regionTypeToSymbol;
import static common.model.game.RegionType.LAND;
import static common.model.game.RegionType.WALL;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import common.model.game.Board;
import common.model.game.Color;
import common.model.game.Location;
import common.model.game.Region;

@Component("CsvWriter")
public class CsvLayerWriter implements ILayerWriter {

    private String layoutCache = null;

    public String writeLayout(Board board) {
        return layoutCache == null ? writeLayer(board, this::getRegionTypeCell) : layoutCache;
    }

    public String writeColors(Board board) {
        return writeLayer(board, this::getColorCell);
    }

    public String writeForces(Board board) {
        return writeLayer(board, this::getForcesCell);
    }

    private String writeLayer(Board board, Function<Region, String> cellWriter) {
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

            rows.add(join(COLUMN_DELIMITER, row));
        }

        return join(ROW_DELIMITER, rows);
    }

    private String getRegionTypeCell(Region region) {
        return regionTypeToSymbol(region.getType()).toString();
    }

    private String getColorCell(Region region) {
        if (region.getType() == WALL) {
            return regionTypeToSymbol(WALL).toString();
        } else {
            Color color = region.getColor();
            return color != null ? valueOf(color.ordinal() + 1) : regionTypeToSymbol(LAND).toString();
        }
    }

    private String getForcesCell(Region region) {
        if (region.getType() == WALL) {
            return repeat(regionTypeToSymbol(WALL).toString(), 4);
        } else {
            return format("%4d", region.getForces());
        }
    }
}
