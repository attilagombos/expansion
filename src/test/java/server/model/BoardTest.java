package server.model;

import static common.model.Color.GREEN;
import static common.model.Color.RED;
import static common.model.RegionType.BASE_1;
import static common.model.RegionType.BASE_2;
import static common.model.RegionType.LAND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import common.model.Color;
import common.model.Forces;
import common.model.Location;
import common.model.Region;
import common.model.RegionType;

class BoardTest {

    private Board underTest;

    @BeforeEach
    void setUp() {
        underTest = new Board();
    }

    @Test
    void shouldCollect() {
        // Given
        List<Region> regions = ImmutableList.<Region>builder()
                .add(region(0, 0, BASE_1, RED, 1))
                .add(region(1, 0, LAND, RED, 5))
                .add(region(1, 1, LAND, RED, 5))
                .add(region(0, 1, LAND, RED, 5))
                .add(region(4, 4, BASE_2, GREEN, 4))
                .add(region(3, 4, LAND, GREEN, 3))
                .add(region(3, 3, LAND, GREEN, 3))
                .add(region(4, 3, LAND, GREEN, 3))
                .add(region(4, 2, LAND, GREEN, 3))
                .build();

        regions.forEach(underTest::putRegion);

        // When
        Map<Color, List<Forces>> result = underTest.getForcesByColor();

        // Then
        assertEquals(2, result.keySet().size());

        List<Forces> redForces = result.get(RED);
        assertEquals(4, redForces.size());

        List<Forces> greenForces = result.get(GREEN);
        assertEquals(5, greenForces.size());
    }

    private Region region(int x, int y, RegionType type, Color color, int forces) {
        Location location = new Location(x, y);

        Region region = new Region(location, type);

        region.setForces(color, forces);

        return region;
    }
}
