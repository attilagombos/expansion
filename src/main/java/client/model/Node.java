package client.model;

import java.util.HashSet;
import java.util.Set;

public class Node<T> {

    private final T value;

    private final Set<Node<T>> adjacency = new HashSet<>();

    private boolean visited;

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Set<Node<T>> getAdjacency() {
        return adjacency;
    }

    public boolean isNotVisited() {
        return !isVisited();
    }

    public void setVisited() {
        this.visited = true;
    }

    public void clearVisited() {
        this.visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
