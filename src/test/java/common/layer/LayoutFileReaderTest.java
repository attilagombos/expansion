package common.layer;

import static common.model.game.RegionType.BASE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.game.Board;
import common.model.game.Location;
import common.model.game.Region;

class LayoutFileReaderTest {

    private LayoutFileReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new LayoutFileReader();
    }

    @Test
    void shouldReadLayout() {
        // When
        Board result = underTest.read("src/test/resources/layouts/layout1.csv");

        // Then
        assertEquals(1024, result.getRegionCount());

        Region region = result.getRegion(new Location(3, 3));
        assertNotNull(region);
        assertEquals(BASE, region.getType());
    }
}
