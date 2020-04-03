package client.strategy;

import static common.model.StepType.DEPLOY;
import static common.model.StepType.MOVE;
import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.MINE;
import static common.model.region.RegionType.WALL;
import static java.lang.Integer.max;
import static java.lang.Math.abs;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import client.model.Grid;
import client.model.Node;
import common.layer.ILayerReader;
import common.model.Board;
import common.model.BoardState;
import common.model.GameState;
import common.model.Instruction;
import common.model.Location;
import common.model.PlayerState;
import common.model.Step;
import common.model.region.Region;

@Component
public class BaseStrategy implements IStrategy {

    private final ILayerReader layerReader;

    private Board board = null;

    private Grid<Region> grid = null;

    @Autowired
    public BaseStrategy(@Qualifier("CsvReader") ILayerReader layerReader) {
        this.layerReader = layerReader;
    }

    @Override
    public Instruction getInstruction(GameState gameState) {
        BoardState boardState = gameState.getBoardState();

        initializeGrid(boardState);

        Instruction instruction = new Instruction();

        PlayerState playerState = gameState.getPlayerStates().get(0);

        Set<Node<Region>> territory = getTerritory(playerState);

        Set<Node<Region>> borderNodes = new HashSet<>();

        for (Node<Region> node : territory) {
            for (Node<Region> adjacent : node.getAdjacency()) {
                if (node.getValue().getColor() != adjacent.getValue().getColor()) {
                    borderNodes.add(node);
                }
            }
        }

        Set<Node<Region>> innerNodes = new HashSet<>(territory);
        innerNodes.removeAll(borderNodes);

        for (Node<Region> inner : innerNodes) {
            int forcesToMove = inner.getValue().getForces() - 1;

            while (forcesToMove > 0) {
                Set<Node<Region>> closestBorders = getClosestNodes(inner, borderNodes);

                for (Node<Region> border : closestBorders) {
                    if (forcesToMove > 0) {
                        Node<Region> closestAdjacent = getClosestAdjacent(inner, border);
                        instruction.addStep(new Step(MOVE, inner.getValue().getLocation(), closestAdjacent.getValue().getLocation(), 1));
                        forcesToMove--;
                    } else {
                        break;
                    }
                }
            }
        }

        int forcesToDeploy = playerState.getReinforcements();

        for (Node<Region> border : borderNodes) {
            for (Node<Region> adjacent : border.getAdjacency()) {
                if (forcesToDeploy > 0) {
                    if (adjacent.getValue().getColor() != border.getValue().getColor()
                            && (adjacent.getValue().getType() == BASE || adjacent.getValue().getType() == MINE)) {
                        instruction.addStep(new Step(DEPLOY, null, border.getValue().getLocation(), 1));
                        border.getValue().setForces(border.getValue().getForces() + 1);
                        forcesToDeploy--;
                    }
                } else {
                    break;
                }
            }
        }

        while (forcesToDeploy > 0) {
            for (Node<Region> border : borderNodes) {
                if (forcesToDeploy > 0) {
                    instruction.addStep(new Step(DEPLOY, null, border.getValue().getLocation(), 1));
                    border.getValue().setForces(border.getValue().getForces() + 1);
                    forcesToDeploy--;
                } else {
                    break;
                }
            }
        }

        for (Node<Region> border : borderNodes) {
            boolean isEnemyAdjacent = border.getAdjacency().stream()
                    .anyMatch(adjacent -> adjacent.getValue().getColor() != null && adjacent.getValue().getColor() != border.getValue().getColor());

            int forcesToMove = isEnemyAdjacent ? (border.getValue().getForces() - 1) / 2 : border.getValue().getForces() - 1;

            for (Node<Region> adjacent : border.getAdjacency()) {
                if (forcesToMove > 0) {
                    if (adjacent.getValue().getColor() != border.getValue().getColor()
                            && adjacent.isNotVisited()
                            && (adjacent.getValue().getType() == BASE || adjacent.getValue().getType() == MINE)) {
                        adjacent.setVisited();
                        instruction.addStep(new Step(MOVE, border.getValue().getLocation(), adjacent.getValue().getLocation(), 1));
                        forcesToMove--;
                    }
                } else {
                    break;
                }
            }

            for (Node<Region> adjacent : border.getAdjacency()) {
                if (forcesToMove > 0) {
                    if (adjacent.getValue().getColor() == null && adjacent.isNotVisited()) {
                        adjacent.setVisited();
                        instruction.addStep(new Step(MOVE, border.getValue().getLocation(), adjacent.getValue().getLocation(), 1));
                        forcesToMove--;
                    }
                } else {
                    break;
                }
            }

            while (forcesToMove > 0) {
                for (Node<Region> adjacent : border.getAdjacency()) {
                    if (forcesToMove > 0) {
                        if (adjacent.getValue().getColor() != border.getValue().getColor()) {
                            instruction.addStep(new Step(MOVE, border.getValue().getLocation(), adjacent.getValue().getLocation(), 1));
                            forcesToMove--;
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

    private Set<Node<Region>> getClosestNodes(Node<Region> source, Set<Node<Region>> nodes) {
        int minimumDistance = nodes.stream().mapToInt(node -> getDistance(node, source)).min().orElse(0);
        return nodes.stream().filter(node -> getDistance(node, source) == minimumDistance).collect(toSet());
    }

    private Node<Region> getClosestAdjacent(Node<Region> source, Node<Region> target) {
        return source.getAdjacency().stream().min(comparingInt(adjacent -> getDistance(adjacent, target))).orElse(source);
    }

    private int getDistance(Node<Region> node1, Node<Region> node2) {
        Location location1 = node1.getValue().getLocation();
        Location location2 = node2.getValue().getLocation();

        return max(abs(location1.getX() - location2.getX()), abs(location1.getY() - location2.getY()));
    }
}
