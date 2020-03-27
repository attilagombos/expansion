package common.layer;

import static common.model.region.RegionType.BASE_3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.Location;
import common.model.region.Region;
import common.model.Board;

class LayoutFileReaderTest {

    private LayoutFileReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new LayoutFileReader();
    }

    @Test
    void shouldReadLayout() {
        // When
        Board result = underTest.read("src/test/resources/layout1.csv");

        // Then
        assertEquals(1024, result.getRegionCount());

        Region region = result.getRegion(new Location(3, 3));
        assertNotNull(region);
        assertEquals(BASE_3, region.getType());
    }
}
