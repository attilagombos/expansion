package client.strategy.path;

import static java.util.Collections.reverse;
import static java.util.Collections.singleton;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import client.model.Node;

public class BreadthFirstSearch<T> implements IShortestPath<T> {

    private final int maxLayers;

    private Set<Node<T>> visited;

    private List<Set<Node<T>>> layers;

    public BreadthFirstSearch(int maxLayers) {
        this.maxLayers = maxLayers;
    }

    @Override
    public List<Node<T>> findShortestPath(Node<T> source, Node<T> target) {
        visited = new HashSet<>();
        layers = new LinkedList<>();

        getNextLayer(singleton(source), target);

        reverse(layers);

        Node<T> node = target;

        List<Node<T>> path = new LinkedList<>();

        for (Set<Node<T>> layer : layers) {
            for (Node<T> adjacent : node.getAdjacency()) {
                if (layer.contains(adjacent)) {
                    path.add(adjacent);
                    node = adjacent;
                    break;
                }
            }
        }

        if (path.size() > 0) {
            reverse(path);
            path.add(target);
        }

        return path;
    }

    private void getNextLayer(Set<Node<T>> previousLayer, Node<T> target) {
        if (layers.size() < maxLayers) {
            layers.add(previousLayer);
            visited.addAll(previousLayer);

            Set<Node<T>> layer = new HashSet<>();

            for (Node<T> adjacent : previousLayer) {
                layer.addAll(adjacent.getAdjacency());
            }

            layer.removeAll(visited);

            if (layer.contains(target) || layer.isEmpty()) {
                return;
            }

            getNextLayer(layer, target);
        }
    }

    @Override
    public Node<T> getNext(Node<T> source, Node<T> target) {
        List<Node<T>> shortestPath = findShortestPath(source, target);

        return shortestPath.isEmpty() ? null : shortestPath.get(1);
    }


}
