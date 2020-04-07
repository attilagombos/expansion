package client.strategy.path;

import org.apache.commons.collections4.map.MultiKeyMap;

import client.model.Grid;
import client.model.Node;

public class NodeTestHelper {

    public static void setAdjacent(Node<String> node1, Node<String> node2) {
        node1.getAdjacency().add(node2);
        node2.getAdjacency().add(node1);
    }

    public static Grid<String> getGrid(String[][] values) {
        MultiKeyMap<Integer, String> map = new MultiKeyMap<>();

        for (int y = 0; y < values.length; y++) {
            for (int x = 0; x < values[y].length; x++) {
                if (values[y][x] != null) {
                    map.put(x, y, values[y][x]);
                }
            }
        }

        Grid<String> grid = new Grid<>();

        grid.addNodes(map);

        return grid;
    }
}
