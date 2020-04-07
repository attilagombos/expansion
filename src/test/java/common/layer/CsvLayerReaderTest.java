package common.layer;

import static common.layer.RegionTestHelper.region;
import static common.layer.RegionTestHelper.wall;
import static common.model.game.Color.GREEN;
import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.LAND;
import static common.model.game.RegionType.MINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import common.model.game.Board;
import common.model.game.Location;
import common.model.game.Region;

class CsvLayerReaderTest {

    private CsvLayerReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new CsvLayerReader();
    }

    @Test
    void shouldReadLayout() {
        // Given
        String layout = "" +
                "#,#,#,#,#\r\n" +
                "#,@, ,$,#\r\n" +
                "#, , , ,#\r\n" +
                "#,$, ,@,#\r\n" +
                "#,#,#,#,#";

        // When
        Board result = underTest.readLayout(layout);

        // Then
        assertEquals(25, result.getRegionCount());

        Region region = result.getRegion(new Location(3, 1));
        assertNotNull(region);
        assertEquals(BASE, region.getType());
    }

    @Test
    void shouldReadColors() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(wall(0, 4)).add(wall(1, 4)).add(wall(2, 4)).add(wall(3, 4)).add(wall(4, 4))
                .add(wall(0, 3)).add(region(1, 3, BASE)).add(region(2, 3, LAND)).add(region(3, 3, MINE)).add(wall(4, 3))
                .add(wall(0, 2)).add(region(1, 2, LAND)).add(region(2, 2, LAND)).add(region(3, 2, LAND)).add(wall(4, 2))
                .add(wall(0, 1)).add(region(1, 1, MINE)).add(region(2, 1, LAND)).add(region(3, 1, BASE)).add(wall(4, 1))
                .add(wall(0, 0)).add(wall(1, 0)).add(wall(2, 0)).add(wall(3, 0)).add(wall(4, 0))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        String colors = "" +
                "#,#,#,#,#\r\n" +
                "#,1,1,2,#\r\n" +
                "#,1,1,2,#\r\n" +
                "#,2,2,2,#\r\n" +
                "#,#,#,#,#";

        // When
        underTest.readColors(board, colors);

        // Then
        Region region = board.getRegion(new Location(3, 1));
        assertNotNull(region);
        assertEquals(GREEN, region.getColor());
    }

    @Test
    void shouldReadForces() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(wall(0, 4)).add(wall(1, 4)).add(wall(2, 4)).add(wall(3, 4)).add(wall(4, 4))
                .add(wall(0, 3)).add(region(1, 3, BASE)).add(region(2, 3, LAND)).add(region(3, 3, MINE)).add(wall(4, 3))
                .add(wall(0, 2)).add(region(1, 2, LAND)).add(region(2, 2, LAND)).add(region(3, 2, LAND)).add(wall(4, 2))
                .add(wall(0, 1)).add(region(1, 1, MINE)).add(region(2, 1, LAND)).add(region(3, 1, BASE)).add(wall(4, 1))
                .add(wall(0, 0)).add(wall(1, 0)).add(wall(2, 0)).add(wall(3, 0)).add(wall(4, 0))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        String forces = "" +
                "####,####,####,####,####\r\n" +
                "####,   1,   3,   3,####\r\n" +
                "####,   3,   5,   1,####\r\n" +
                "####,   3,   1,   3,####\r\n" +
                "####,####,####,####,####";

        // When
        underTest.readForces(board, forces);

        // Then
        Region region = board.getRegion(new Location(3, 1));
        assertNotNull(region);
        assertEquals(3, region.getForces());
    }
}
