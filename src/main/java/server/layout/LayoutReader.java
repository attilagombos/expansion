package server.layout;

import static server.layout.LayoutMapping.COLUMN_DELIMITER;
import static server.layout.LayoutMapping.ROW_DELIMITER;
import static server.layout.LayoutMapping.symbolToRegionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import common.model.Color;
import common.model.Location;
import common.model.Region;
import common.model.RegionType;
import server.model.Board;

@Component
public class LayoutReader {

    private static final Logger LOG = LoggerFactory.getLogger(LayoutReader.class);

    public Board read(String layoutPath) {
        Board board = new Board();

        File layout = new File(layoutPath);

        try (Scanner scanner = new Scanner(layout)) {
            scanner.useDelimiter(ROW_DELIMITER);

            int locationY = 0;

            while (scanner.hasNext()) {
                String[] row = scanner.next().split(COLUMN_DELIMITER);

                for (int locationX = 0; locationX < row.length; locationX++) {
                    Location location = new Location(locationX, locationY);
                    RegionType regionType = symbolToRegionType(row[locationX].charAt(0));
                    Region region = new Region(location, regionType);

                    Color color = Color.getByRegionType(regionType);
                    if (color != null) {
                        board.getBases().put(color, region);
                    }

                    board.putRegion(region);
                }

                locationY++;
            }

        } catch (FileNotFoundException e) {
            LOG.error("Could not find layout file {}", layoutPath);
        }

        return board;
    }
}
