package client.model;

import org.apache.commons.collections4.map.MultiKeyMap;

public class Grid<T> {

    private final MultiKeyMap<Integer, Node<T>> nodes = new MultiKeyMap<>();

    public void addNodes(MultiKeyMap<Integer, T> values) {
        values.forEach((key, value) -> {
            Node<T> node = new Node<>(value);

            int x = key.getKey(0);
            int y = key.getKey(1);

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    Node<T> adjacent = getNode(i, j);

                    if (adjacent != null) {
                        node.getAdjacency().add(adjacent);
                        adjacent.getAdjacency().add(node);
                    }
                }
            }

            nodes.put(key.getKey(0), key.getKey(1), node);
        });
    }

    public Node<T> getNode(Integer x, Integer y) {
        return nodes.get(x, y);
    }

    public T getValue(Integer x, Integer y) {
        return nodes.get(x, y).getValue();
    }

    public MultiKeyMap<Integer, Node<T>> getNodes() {
        return nodes;
    }
}
