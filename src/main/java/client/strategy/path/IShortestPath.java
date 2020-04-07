package client.strategy.path;

import java.util.List;

import client.model.Node;

public interface IShortestPath<T> {

    List<Node<T>> findShortestPath(Node<T> source, Node<T> target);

    Node<T> getNext(Node<T> source, Node<T> target);
}
