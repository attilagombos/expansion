package common.layer;

import static common.model.region.RegionType.BASE_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.Board;
import common.model.Location;
import common.model.region.Region;

class LayerReaderTest {

    private LayerReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new LayerReader();
    }

    @Test
    void shouldReadLayout() {
        // Given
        String layout = "" +
                "#,#,#,#,#\r\n" +
                "#,1, ,$,#\r\n" +
                "#, , , ,#\r\n" +
                "#,$, ,2,#\r\n" +
                "#,#,#,#,#";

        // When
        Board result = underTest.readLayout(layout);

        // Then
        assertEquals(25, result.getRegionCount());

        Region region = result.getRegion(new Location(3, 1));
        assertNotNull(region);
        assertEquals(BASE_2, region.getType());
    }
}
