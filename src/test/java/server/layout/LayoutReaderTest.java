package server.layout;

import static common.model.RegionType.BASE_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.Location;
import common.model.Region;
import server.model.Board;

class LayoutReaderTest {

    private LayoutReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new LayoutReader();
    }

    @Test
    void shouldReadLayout() {
        // When
        Board result = underTest.read("src/test/resources/layout1.csv");

        // Then
        assertEquals(1024, result.getRegionCount());

        Region region = result.getRegion(new Location(3, 3));
        assertNotNull(region);
        assertEquals(BASE_1, region.getType());
    }
}
