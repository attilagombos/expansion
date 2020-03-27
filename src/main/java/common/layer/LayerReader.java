package common.layer;

import static common.layer.LayoutMapping.COLUMN_DELIMITER;
import static common.layer.LayoutMapping.ROW_DELIMITER;
import static common.layer.LayoutMapping.symbolToRegionType;
import static common.model.Color.ofOrdinal;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import common.model.Board;
import common.model.Color;
import common.model.Location;
import common.model.region.Region;
import common.model.region.RegionType;

@Component
public class LayerReader {

    private static final Logger LOG = LoggerFactory.getLogger(LayerReader.class);

    public Board readLayout(String layoutLayer) {
        Board board = new Board();

        String[] lines = layoutLayer.split(ROW_DELIMITER);

        int locationY = lines.length;

        for (String line : lines) {
            locationY--;

            String[] row = line.split(COLUMN_DELIMITER);

            for (int locationX = 0; locationX < row.length; locationX++) {
                Location location = new Location(locationX, locationY);
                RegionType regionType = symbolToRegionType(row[locationX].charAt(0));

                Region region = new Region(location, regionType);

                board.putRegion(region);
            }
        }

        return board;
    }

    public void readColors(Board board, String colorsLayer) {
        String[] lines = colorsLayer.split(ROW_DELIMITER);

        int locationY = lines.length;

        for (String line : lines) {
            locationY--;

            String[] row = line.split(COLUMN_DELIMITER);

            for (int locationX = 0; locationX < row.length; locationX++) {
                Location location = new Location(locationX, locationY);

                Region region = board.getRegion(location);

                String cell = row[locationX];

                if (isNumeric(cell)) {
                    Color color = ofOrdinal(parseInt(cell) - 1);
                    region.setColor(color);
                } else {
                    region.setColor(null);
                }
            }
        }
    }

    public void readForces(Board board, String forcesLayer) {
        String[] lines = forcesLayer.split(ROW_DELIMITER);

        int locationY = lines.length;

        for (String line : lines) {
            locationY--;

            String[] row = line.split(COLUMN_DELIMITER);

            for (int locationX = 0; locationX < row.length; locationX++) {
                Location location = new Location(locationX, locationY);

                Region region = board.getRegion(location);

                String cell = row[locationX].trim();

                if (isNumeric(cell)) {
                    int forces = parseInt(cell);
                    region.setForces(forces);
                } else {
                    region.setForces(0);
                }
            }
        }
    }
}
