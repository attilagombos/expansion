package client.strategy;

import static common.model.StepType.DEPLOY;
import static common.model.StepType.MOVE;
import static common.model.region.RegionType.WALL;
import static java.lang.Math.abs;
import static java.util.Comparator.comparingInt;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import client.model.Grid;
import client.model.Node;
import common.layer.LayerReader;
import common.model.Board;
import common.model.BoardState;
import common.model.GameState;
import common.model.Instruction;
import common.model.Location;
import common.model.PlayerState;
import common.model.Step;
import common.model.region.Region;

@Component
public class BaseStrategy implements Strategy {

    private final LayerReader layerReader;

    private Board board = null;

    private Grid<Region> grid = null;

    @Autowired
    public BaseStrategy(LayerReader layerReader) {
        this.layerReader = layerReader;
    }

    @Override
    public Instruction getInstruction(GameState gameState) {
        BoardState boardState = gameState.getBoardState();

        initializeGrid(boardState);

        Instruction instruction = new Instruction();

        PlayerState playerState = gameState.getPlayerState();

        Set<Node<Region>> territory = getTerritory(playerState);

        Set<Node<Region>> borderNodes = new HashSet<>();

        Set<Node<Region>> reachableNodes = new HashSet<>();

        for (Node<Region> node : territory) {
            for (Node<Region> adjacent : node.getAdjacency()) {
                if (node.getValue().getColor() != adjacent.getValue().getColor()) {
                    borderNodes.add(node);
                    reachableNodes.add(adjacent);
                }
            }
        }

        Set<Node<Region>> innerNodes = new HashSet<>(territory);
        innerNodes.removeAll(borderNodes);

        for (Node<Region> inner : innerNodes) {
            if (inner.getValue().getForces() > 1) {
                Node<Region> closestBorder = getClosest(inner, borderNodes);
                Node<Region> closestAdjacent = getClosestAdjacent(inner, closestBorder);
                instruction.addStep(new Step(MOVE, inner.getValue().getLocation(), closestAdjacent.getValue().getLocation(), inner.getValue().getForces() - 1));
            }
        }

        //Set<Node<Region>> minesAndBases = new HashSet<>(grid.getNodes().values());
        //minesAndBases.removeAll(territory);
        //minesAndBases = minesAndBases.stream()
        //        .filter(node -> node.getValue().getType() == MINE || BASES.contains(node.getValue().getType()))
        //        .collect(toSet());

        int reinforcements = playerState.getReinforcements();

        while (reinforcements > 0) {
            for (Node<Region> border : borderNodes) {
                if (reinforcements > 0) {
                    int toDeploy = 1;
                    instruction.addStep(new Step(DEPLOY, null, border.getValue().getLocation(), toDeploy));
                    border.getValue().setForces(border.getValue().getForces() + toDeploy);
                    reinforcements -= toDeploy;
                } else {
                    break;
                }
            }
        }

        for (Node<Region> border : borderNodes) {
            //Node<Region> closestMineOrBase = getClosest(border, minesAndBases);
            //Node<Region> closestAdjacent = getClosestDifferentAdjacent(border, closestMineOrBase);
            int forces = border.getValue().getForces() - 1;
            while (forces > 0) {
                for (Node<Region> adjacent : border.getAdjacency()) {
                    if (forces > 0) {
                        if (adjacent.getValue().getColor() != border.getValue().getColor()) {
                            instruction.addStep(new Step(MOVE, border.getValue().getLocation(), adjacent.getValue().getLocation(), 1));
                            forces--;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return instruction;
    }

    private void initializeGrid(BoardState boardState) {
        if (board == null) {
            board = layerReader.readLayout(boardState.getLayout());
        }

        layerReader.readColors(board, boardState.getColors());
        layerReader.readForces(board, boardState.getForces());

        MultiKeyMap<Integer, Region> regions = new MultiKeyMap<>();

        for (Map.Entry<MultiKey<? extends Integer>, Region> entry : board.getRegions().entrySet()) {
            if (entry.getValue().getType() != WALL) {
                regions.put(entry.getKey(), entry.getValue());
            }
        }

        grid = new Grid<>();

        grid.addNodes(regions);
    }

    private Set<Node<Region>> getTerritory(PlayerState playerState) {
        Set<Node<Region>> territory = new HashSet<>();

        grid.getNodes().values().stream()
                .filter(node -> node.getValue().getColor() == playerState.getColor())
                .forEach(territory::add);

        return territory;
    }

    private Node<Region> getClosest(Node<Region> source, Set<Node<Region>> nodes) {
        return nodes.stream().min(comparingInt(node -> getDistance(node, source))).orElse(source);
    }

    private Node<Region> getClosestAdjacent(Node<Region> source, Node<Region> target) {
        return source.getAdjacency().stream().min(comparingInt(adjacent -> getDistance(adjacent, target))).orElse(source);
    }

    private Node<Region> getClosestDifferentAdjacent(Node<Region> source, Node<Region> target) {
        return source.getAdjacency().stream()
                .filter(adjacent -> adjacent.getValue().getColor() != source.getValue().getColor())
                .min(comparingInt(adjacent -> getDistance(adjacent, target)))
                .orElse(source);
    }

    private int getDistance(Node<Region> node1, Node<Region> node2) {
        Location location1 = node1.getValue().getLocation();
        Location location2 = node2.getValue().getLocation();

        return abs(location1.getX() - location2.getX()) + abs(location1.getY() - location2.getY());
    }
}
