package common.layer;

import static common.model.region.RegionType.BASE_1;
import static common.model.region.RegionType.BASE_2;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;
import static common.model.region.RegionType.WALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import common.model.Board;
import common.model.Location;
import common.model.region.Region;
import common.model.region.RegionType;

class LayerWriterTest {

    private LayerWriter underTest;

    @BeforeEach
    void setUp() {
        underTest = new LayerWriter();
    }

    @Test
    void shouldWriteLayout() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(region(0, 4, WALL)).add(region(1, 4, WALL)).add(region(2, 4, WALL)).add(region(3, 4, WALL)).add(region(4, 4, WALL))
                .add(region(0, 3, WALL)).add(region(1, 3, BASE_1)).add(region(2, 3, LAND)).add(region(3, 3, MINE)).add(region(4, 3, WALL))
                .add(region(0, 2, WALL)).add(region(1, 2, LAND)).add(region(2, 2, LAND)).add(region(3, 2, LAND)).add(region(4, 2, WALL))
                .add(region(0, 1, WALL)).add(region(1, 1, MINE)).add(region(2, 1, LAND)).add(region(3, 1, BASE_2)).add(region(4, 1, WALL))
                .add(region(0, 0, WALL)).add(region(1, 0, WALL)).add(region(2, 0, WALL)).add(region(3, 0, WALL)).add(region(4, 0, WALL))
                .build();

        Board board = new Board();

        regions.forEach(board::putRegion);

        // When
        String result = underTest.writeLayout(board);

        // Then
        assertEquals("" +
                        "#,#,#,#,#\r\n" +
                        "#,1, ,$,#\r\n" +
                        "#, , , ,#\r\n" +
                        "#,$, ,2,#\r\n" +
                        "#,#,#,#,#", result);
    }

    private Region region(int x, int y, RegionType type) {
        return new Region(new Location(x, y), type);
    }
}
