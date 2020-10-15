package client.strategy.path;

import static client.strategy.path.NodeTestHelper.getGrid;
import static client.strategy.path.NodeTestHelper.setAdjacent;
import static java.lang.System.nanoTime;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.model.Grid;
import client.model.Node;

class BreadthFirstSearchTest {

    private BreadthFirstSearch<String> underTest;

    @BeforeEach
    void setUp() {
        underTest = new BreadthFirstSearch<>(20);
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoPath() {
        // Given
        Node<String> source = new Node<>("source");
        Node<String> target = new Node<>("target");

        // When
        List<Node<String>> result = underTest.findShortestPath(source, target);

        // Then
        assertEquals(emptyList(), result);
    }

    @Test
    void shouldReturnPathWhenTargetIsAdjacent() {
        // Given
        Node<String> source = new Node<>("source");
        Node<String> target = new Node<>("target");
        Node<String> node1 = new Node<>("node1");

        setAdjacent(source, target);
        setAdjacent(source, node1);

        // When
        List<Node<String>> result = underTest.findShortestPath(source, target);

        // Then
        assertEquals(asList(source, target), result);
    }

    @Test
    void shouldReturnShortestPathWhenTargetIsAdjacentAndThereAreRedundantPaths() {
        // Given
        Node<String> source = new Node<>("source");
        Node<String> target = new Node<>("target");
        Node<String> node1 = new Node<>("node1");
        Node<String> node2 = new Node<>("node2");

        setAdjacent(node1, source);
        setAdjacent(node1, target);
        setAdjacent(node1, node2);
        setAdjacent(node2, target);

        // When
        List<Node<String>> result = underTest.findShortestPath(source, target);

        // Then
        assertEquals(asList(source, node1, target), result);
    }

    @Test
    void shouldReturnShortestPath() {
        // Given
        String[][] values = {
                {"00", "10", "20", "30", "40"},
                {"01", "11", "21", "31", "41"},
                {"02", "12", "22", "32", "42"},
                {"03", "13", "23", "33", "43"},
                {"04", "14", "24", "34", "44"}
        };

        Grid<String> grid = getGrid(values);

        // When
        List<Node<String>> result = underTest.findShortestPath(grid.getNode(0, 0), grid.getNode(4, 4));

        // Then
        assertEquals(5, result.size());
    }

    @Test
    void shouldReturnShortestPathWhenObstaclesArePresent() {
        // Given
        String[][] values = {
                {"00", "10", "20", "30", "40"},
                {"01", "11", null, "31", "41"},
                {"02", null, null, "32", "42"},
                {"03", "13", "23", "33", "43"},
                {"04", "14", "24", "34", "44"}
        };

        Grid<String> grid = getGrid(values);

        // When
        List<Node<String>> result = underTest.findShortestPath(grid.getNode(0, 0), grid.getNode(4, 4));

        // Then
        assertEquals(7, result.size());
    }

    @Test
    void shouldReturnShortestPathWhenObstaclesArePresent2() {
        // Given
        String[][] values = {
                {"00", "10", null, "30", "40"},
                {"01", "11", null, "31", "41"},
                {"02", null, null, "32", "42"},
                {"03", "13", "23", "33", "43"},
                {"04", "14", "24", "34", "44"}
        };

        Grid<String> grid = getGrid(values);

        // When
        List<Node<String>> result = underTest.findShortestPath(grid.getNode(0, 0), grid.getNode(4, 0));

        // Then
        assertEquals(8, result.size());
    }

    @Test
    void shouldReturnEmptyPathWhenObstaclesAreDividing() {
        // Given
        String[][] values = {
                {"00", "10", null, "30", "40"},
                {"01", "11", null, "31", "41"},
                {"02", null, null, null, null},
                {"03", "13", "23", "33", "43"},
                {"04", "14", "24", "34", "44"}
        };

        Grid<String> grid = getGrid(values);

        // When
        List<Node<String>> result = underTest.findShortestPath(grid.getNode(0, 0), grid.getNode(4, 0));

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnShortestPathWhenLargeGraph() {
        // Given
        String[][] values = {
                {"00", "10", "20", "30", "40", "50", "60", "70", "80", "90"},
                {"01", "11", "21", "31", "41", "51", "61", "71", "81", "91"},
                {"02", "12", "22", "32", "42", "52", "62", "72", "82", "92"},
                {"03", "13", "23", "33", "43", "53", "63", "73", "83", "93"},
                {"04", "14", "24", "34", "44", "54", "64", "74", "84", "94"},
                {"05", "15", "25", "35", "45", "55", "65", "75", "85", "95"},
                {"06", "16", "26", "36", "46", "56", "66", "76", "86", "96"},
                {"07", "17", "27", "37", "47", "57", "67", "77", "87", "97"},
                {"08", "18", "28", "38", "48", "58", "68", "78", "88", "98"},
                {"09", "19", "29", "39", "49", "59", "69", "79", "89", "99"}
        };

        Grid<String> grid = getGrid(values);

        // When
        long loopStartNanos = nanoTime();

        List<Node<String>> result = underTest.findShortestPath(grid.getNode(0, 0), grid.getNode(9, 9));

        long processingNanos = nanoTime() - loopStartNanos;

        System.out.println(processingNanos);

        // Then
        assertEquals(10, result.size());
    }

    @Test
    void shouldReturnShortestPathWhenLargeGraphWithObstacles() {
        // Given
        String[][] values = {
                {"00", "10", "20", "30", "40", "50", "60", "70", "80", "90"},
                {"01", "11", "21", "31", null, "51", null, "71", "81", "91"},
                {"02", "12", "22", null, null, "52", "62", "72", "82", "92"},
                {"03", "13", "23", null, "43", "53", "63", null, "83", "93"},
                {"04", "14", "24", null, "44", "54", "64", null, "84", "94"},
                {"05", "15", "25", "35", "45", "55", null, null, "85", "95"},
                {"06", "16", "26", null, "46", "56", null, "76", "86", "96"},
                {"07", "17", "27", null, "47", "57", null, "77", "87", "97"},
                {"08", "18", "28", "38", "48", "58", null, "78", "88", "98"},
                {"09", "19", "29", "39", "49", "59", null, "79", "89", "99"}
        };

        Grid<String> grid = getGrid(values);

        // When
        long loopStartNanos = nanoTime();

        List<Node<String>> result = underTest.findShortestPath(grid.getNode(1, 2), grid.getNode(8, 7));

        long processingNanos = nanoTime() - loopStartNanos;

        System.out.println(processingNanos);

        // Then
        assertEquals(12, result.size());
    }
}
