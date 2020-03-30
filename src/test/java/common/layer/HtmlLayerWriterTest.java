package common.layer;

import static common.model.Color.GREEN;
import static common.model.Color.RED;
import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;
import static common.model.region.RegionType.WALL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import common.model.Board;
import common.model.Color;
import common.model.Location;
import common.model.region.Region;
import common.model.region.RegionType;

class HtmlLayerWriterTest {

    private HtmlLayerWriter underTest;

    @BeforeEach
    void setUp() {
        underTest = new HtmlLayerWriter();
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
        assertEquals("<table class=\"layout\">" +
                "<tr><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td></tr>" +
                "<tr><td class=\"wall\"></td><td class=\"base\"></td><td class=\"land\"></td><td class=\"mine\"></td><td class=\"wall\"></td></tr>" +
                "<tr><td class=\"wall\"></td><td class=\"land\"></td><td class=\"land\"></td><td class=\"land\"></td><td class=\"wall\"></td></tr>" +
                "<tr><td class=\"wall\"></td><td class=\"mine\"></td><td class=\"land\"></td><td class=\"base\"></td><td class=\"wall\"></td></tr>" +
                "<tr><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td><td class=\"wall\"></td></tr>" +
                "</table>", result);
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
        assertEquals("<table class=\"colors\"><tr><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"red\"></td><td class=\"red\"></td><td class=\"green\"></td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"red\"></td><td class=\"red\"></td><td class=\"green\"></td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"green\"></td><td class=\"green\"></td><td class=\"green\"></td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td></tr>" +
                "</table>", result);
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
        assertEquals("<table class=\"forces\">" +
                "<tr><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"\">1</td><td class=\"\">3</td><td class=\"\">3</td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"\">3</td><td class=\"\">5</td><td class=\"\">1</td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"\">3</td><td class=\"\">1</td><td class=\"\">3</td><td class=\"\"></td></tr>" +
                "<tr><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td><td class=\"\"></td></tr>" +
                "</table>", result);
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
