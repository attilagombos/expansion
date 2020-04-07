package client.strategy.path;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import client.model.Node;

public class DepthFirstSearch<T> implements IShortestPath<T> {

    private final int maxLength;

    private int shortestLength;

    private TreeSet<List<Node<T>>> paths;

    public DepthFirstSearch(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public List<Node<T>> findShortestPath(Node<T> source, Node<T> target) {
        shortestLength = maxLength;

        paths = new TreeSet<>(Comparator.comparingInt(List::size));

        findShortestPath(emptyList(), source, target);

        return !paths.isEmpty() ? paths.first() : emptyList();
    }

    @Override
    public Node<T> getNext(Node<T> source, Node<T> target) {
        List<Node<T>> shortestPath = findShortestPath(source, target);

        return shortestPath.size() > 1 ? shortestPath.get(1) : null;
    }

    private void findShortestPath(List<Node<T>> path, Node<T> source, Node<T> target) {
        List<Node<T>> newPath = new ArrayList<>(path);

        newPath.add(source);

        if (source == target) {
            paths.add(newPath);
            if (newPath.size() < shortestLength) {
                shortestLength = newPath.size();
            }
            return;
        } else if (newPath.size() >= shortestLength){
            return;
        }

        for (Node<T> adjacent : source.getAdjacency()) {
            if (!newPath.contains(adjacent)) {
                findShortestPath(newPath, adjacent, target);
            }
        }
    }
}
