package common.layer;

import static common.layer.LayoutMapping.COLUMN_DELIMITER;
import static common.layer.LayoutMapping.ROW_DELIMITER;
import static common.layer.LayoutMapping.symbolToRegionType;
import static common.model.game.Color.ofOrdinal;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import org.springframework.stereotype.Component;

import common.model.game.Board;
import common.model.game.Color;
import common.model.game.Location;
import common.model.game.Region;
import common.model.game.RegionType;

@Component("CsvReader")
public class CsvLayerReader implements ILayerReader {

    public Board readLayout(String layoutLayer) {
        Board board = new Board();

        readLayer(board, layoutLayer, this::readRegionType);

        return board;
    }

    public void readColors(Board board, String colorsLayer) {
        readLayer(board, colorsLayer, this::readColor);
    }

    public void readForces(Board board, String forcesLayer) {
        readLayer(board, forcesLayer, this::readForces);
    }

    public void readLayer(Board board, String layer, TriConsumer<Board, Location, String> cellReader) {
        String[] lines = layer.split(ROW_DELIMITER);

        int locationY = lines.length;

        for (String line : lines) {
            locationY--;

            String[] row = line.split(COLUMN_DELIMITER);

            for (int locationX = 0; locationX < row.length; locationX++) {
                Location location = new Location(locationX, locationY);

                cellReader.accept(board, location, row[locationX]);
            }
        }
    }

    private void readRegionType(Board board, Location location, String cell) {
        RegionType regionType = symbolToRegionType(cell.charAt(0));

        Region region = new Region(location, regionType);

        board.putRegion(region);
    }

    private void readColor(Board board, Location location, String cell) {
        Region region = board.getRegion(location);

        if (isNumeric(cell)) {
            Color color = ofOrdinal(parseInt(cell) - 1);
            region.setColor(color);
        } else {
            region.setColor(null);
        }
    }

    private void readForces(Board board, Location location, String cell) {
        Region region = board.getRegion(location);

        if (isNumeric(cell.trim())) {
            int forces = parseInt(cell.trim());
            region.setForces(forces);
        } else {
            region.setForces(0);
        }
    }
}
