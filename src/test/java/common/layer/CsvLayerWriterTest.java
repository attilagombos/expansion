package common.layer;

import static common.model.game.Color.GREEN;
import static common.model.game.Color.RED;
import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.LAND;
import static common.model.game.RegionType.MINE;
import static common.model.game.RegionType.WALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import common.model.game.Board;
import common.model.game.Color;
import common.model.game.Location;
import common.model.game.Region;
import common.model.game.RegionType;

class CsvLayerWriterTest {

    private CsvLayerWriter underTest;

    @BeforeEach
    void setUp() {
        underTest = new CsvLayerWriter();
    }

    @Test
    void shouldWriteLayout() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(wall(0, 4)).add(wall(1, 4)).add(wall(2, 4)).add(wall(3, 4)).add(wall(4, 4))
                .add(wall(0, 3)).add(region(1, 3, BASE, RED, 1)).add(region(2, 3, LAND, RED, 3)).add(region(3, 3, MINE, GREEN, 3)).add(wall(4, 3))
                .add(wall(0, 2)).add(region(1, 2, LAND, RED, 3)).add(region(2, 2, LAND, RED, 5)).add(region(3, 2, LAND, GREEN, 1)).add(wall(4, 2))
                .add(wall(0, 1)).add(region(1, 1, MINE, GREEN, 3)).add(region(2, 1, LAND, GREEN, 1)).add(region(3, 1, BASE, GREEN, 3)).add(wall(4, 1))
                .add(wall(0, 0)).add(wall(1, 0)).add(wall(2, 0)).add(wall(3, 0)).add(wall(4, 0))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        // When
        String result = underTest.writeLayout(board);

        // Then
        assertEquals("" +
                        "#,#,#,#,#\r\n" +
                        "#,@, ,$,#\r\n" +
                        "#, , , ,#\r\n" +
                        "#,$, ,@,#\r\n" +
                        "#,#,#,#,#", result);
    }

    @Test
    void shouldWriteColors() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(wall(0, 4)).add(wall(1, 4)).add(wall(2, 4)).add(wall(3, 4)).add(wall(4, 4))
                .add(wall(0, 3)).add(region(1, 3, BASE, RED, 1)).add(region(2, 3, LAND, RED, 3)).add(region(3, 3, MINE, GREEN, 3)).add(wall(4, 3))
                .add(wall(0, 2)).add(region(1, 2, LAND, RED, 3)).add(region(2, 2, LAND, RED, 5)).add(region(3, 2, LAND, GREEN, 1)).add(wall(4, 2))
                .add(wall(0, 1)).add(region(1, 1, MINE, GREEN, 3)).add(region(2, 1, LAND, GREEN, 1)).add(region(3, 1, BASE, GREEN, 3)).add(wall(4, 1))
                .add(wall(0, 0)).add(wall(1, 0)).add(wall(2, 0)).add(wall(3, 0)).add(wall(4, 0))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        // When
        String result = underTest.writeColors(board);

        // Then
        assertEquals("" +
                "#,#,#,#,#\r\n" +
                "#,1,1,2,#\r\n" +
                "#,1,1,2,#\r\n" +
                "#,2,2,2,#\r\n" +
                "#,#,#,#,#", result);
    }

    @Test
    void shouldWriteForces() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(wall(0, 4)).add(wall(1, 4)).add(wall(2, 4)).add(wall(3, 4)).add(wall(4, 4))
                .add(wall(0, 3)).add(region(1, 3, BASE, RED, 1)).add(region(2, 3, LAND, RED, 3)).add(region(3, 3, MINE, GREEN, 3)).add(wall(4, 3))
                .add(wall(0, 2)).add(region(1, 2, LAND, RED, 3)).add(region(2, 2, LAND, RED, 5)).add(region(3, 2, LAND, GREEN, 1)).add(wall(4, 2))
                .add(wall(0, 1)).add(region(1, 1, MINE, GREEN, 3)).add(region(2, 1, LAND, GREEN, 1)).add(region(3, 1, BASE, GREEN, 3)).add(wall(4, 1))
                .add(wall(0, 0)).add(wall(1, 0)).add(wall(2, 0)).add(wall(3, 0)).add(wall(4, 0))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        // When
        String result = underTest.writeForces(board);

        // Then
        assertEquals("" +
                "####,####,####,####,####\r\n" +
                "####,   1,   3,   3,####\r\n" +
                "####,   3,   5,   1,####\r\n" +
                "####,   3,   1,   3,####\r\n" +
                "####,####,####,####,####", result);
    }

    private Region wall(int x, int y) {
        return region(x, y, WALL, null, 0);
    }

    private Region region(int x, int y, RegionType type, Color color, int forces) {
        Location location = new Location(x, y);

        Region region = new Region(location, type);

        region.setColor(color);
        region.setForces(forces);

        return region;
    }
}
