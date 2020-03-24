package server.layout;

import static java.lang.String.join;
import static server.layout.LayoutMapping.COLUMN_DELIMITER;
import static server.layout.LayoutMapping.ROW_DELIMITER;
import static server.layout.LayoutMapping.regionTypeToSymbol;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import common.model.Location;
import common.model.Region;
import server.model.Board;

@Component
public class LayoutWriter {

    public String write(Board board) {
        List<String> rows = new ArrayList<>();

        Pair<Location, Location> dimensions = board.getDimensions();
        Location begin = dimensions.getLeft();
        Location end = dimensions.getRight();

        for (int locationY = begin.getY(); locationY <= end.getY(); locationY++) {
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
}
