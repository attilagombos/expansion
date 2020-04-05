package common.layer;

import static common.layer.LayoutMapping.COLUMN_DELIMITER;
import static common.layer.LayoutMapping.symbolToRegionType;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import common.model.game.Board;
import common.model.game.Location;
import common.model.game.Region;
import common.model.game.RegionType;

@Component
public class LayoutFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(LayoutFileReader.class);

    public Board read(String layoutPath) {
        Board board = new Board();

        File layout = new File(layoutPath);

        try (ReversedLinesFileReader reversedReader = new ReversedLinesFileReader(layout, UTF_8)) {

            int locationY = 0;

            while (true) {
                String line = reversedReader.readLine();

                if (line != null) {
                    String[] row = line.split(COLUMN_DELIMITER);

                    for (int locationX = 0; locationX < row.length; locationX++) {
                        Location location = new Location(locationX, locationY);
                        RegionType regionType = symbolToRegionType(row[locationX].charAt(0));

                        Region region = new Region(location, regionType);

                        board.putRegion(region);
                    }

                    locationY++;
                } else {
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            LOG.error("Could not find layout file {}", layoutPath);
        } catch (IOException e) {
            LOG.error("Could not open layout file {}", layoutPath);
        }

        return board;
    }
}
