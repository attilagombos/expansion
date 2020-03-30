package common.layer;

import static common.model.region.RegionType.BASE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.Board;
import common.model.Location;
import common.model.region.Region;

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
}
