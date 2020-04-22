package client.strategy;

import static common.model.dto.instruction.StepType.DEPLOY;
import static common.model.dto.instruction.StepType.MOVE;
import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.MINE;
import static common.model.game.RegionType.WALL;
import static java.lang.Integer.max;
import static java.lang.Math.abs;
import static java.lang.System.nanoTime;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import client.endpoint.PlayerEndpoint;
import client.model.Grid;
import client.model.Node;
import client.strategy.path.BreadthFirstSearch;
import client.strategy.path.IShortestPath;
import common.layer.ILayerReader;
import common.model.dto.BoardState;
import common.model.dto.GameState;
import common.model.dto.PlayerState;
import common.model.dto.instruction.Instruction;
import common.model.dto.instruction.Step;
import common.model.game.Board;
import common.model.game.Location;
import common.model.game.Region;

@Component
public class BaseStrategy implements IStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerEndpoint.class);

    private final ILayerReader layerReader;

    private IShortestPath<Region> shortestPath;

    private Board board = null;

    private Grid<Region> grid = null;

    @Autowired
    public BaseStrategy(@Qualifier("CsvReader") ILayerReader layerReader) {
        this.layerReader = layerReader;
    }

    @Override
    public Instruction getInstruction(GameState gameState) {
        long loopStartNanos = nanoTime();

        BoardState boardState = gameState.getBoardState();

        initializeGrid(boardState);

        Instruction instruction = new Instruction();

        PlayerState playerState = gameState.getPlayerStates().get(0);

        Set<Node<Region>> territory = getTerritory(playerState);

        Set<Node<Region>> borderNodes = getBorderNodes(territory);

        Set<Node<Region>> innerNodes = getInnerNodes(territory, borderNodes);

        Set<Node<Region>> mines = getMines(playerState);

        Set<Node<Region>> unOwned = getUnowned(playerState);

        moveFromInnerNodes(innerNodes, isNotEmpty(mines) ? mines : unOwned, instruction);

        deployToBorderNodes(playerState, borderNodes, isNotEmpty(mines) ? mines : unOwned, instruction);

        moveFromBorderNodes(borderNodes, isNotEmpty(mines) ? mines : unOwned, instruction);

        long processingNanos = nanoTime() - loopStartNanos;

        LOG.info("Processing took {} ms", ((double) processingNanos) / 1000000);

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

        shortestPath = new BreadthFirstSearch<>(grid.getSizeX() + grid.getSizeY());
    }

    private Set<Node<Region>> getTerritory(PlayerState playerState) {
        Set<Node<Region>> territory = new HashSet<>();

        grid.getNodes().values().stream()
                .filter(node -> node.getValue().getColor() == playerState.getColor())
                .forEach(territory::add);

        return territory;
    }

    private Set<Node<Region>> getBorderNodes(Set<Node<Region>> territory) {
        Set<Node<Region>> borderNodes = new HashSet<>();

        for (Node<Region> node : territory) {
            for (Node<Region> adjacent : node.getAdjacency()) {
                if (node.getValue().getColor() != adjacent.getValue().getColor()) {
                    borderNodes.add(node);
                }
            }
        }
        return borderNodes;
    }

    private Set<Node<Region>> getInnerNodes(Set<Node<Region>> territory, Set<Node<Region>> borderNodes) {
        Set<Node<Region>> innerNodes = new HashSet<>(territory);
        innerNodes.removeAll(borderNodes);
        return innerNodes;
    }

    private Set<Node<Region>> getMines(PlayerState playerState) {
        Set<Node<Region>> mines = new HashSet<>();

        grid.getNodes().values().stream()
                .filter(node -> node.getValue().getType() == MINE || node.getValue().getType() == BASE)
                .filter(node -> node.getValue().getColor() != playerState.getColor())
                .forEach(mines::add);

        return mines;
    }

    private Set<Node<Region>> getUnowned(PlayerState playerState) {
        Set<Node<Region>> territory = new HashSet<>();

        grid.getNodes().values().stream()
                .filter(node -> node.getValue().getColor() != playerState.getColor())
                .forEach(territory::add);

        return territory;
    }

    private void moveFromInnerNodes(Set<Node<Region>> innerNodes, Set<Node<Region>> targetNodes, Instruction instruction) {
        for (Node<Region> inner : innerNodes) {
            Map<Node<Region>, List<Node<Region>>> targetToPath = getTargetsToPaths(inner, targetNodes);

            int forcesToMove = inner.getValue().getForces() - 1;

            while (forcesToMove > 0) {
                for (Node<Region> target : targetToPath.keySet()) {
                    if (forcesToMove > 0) {
                        Node<Region> closestAdjacent = targetToPath.get(target).get(1);
                        instruction.addStep(new Step(MOVE, inner.getValue().getLocation(), closestAdjacent.getValue().getLocation(), 1));
                        forcesToMove--;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private LinkedHashMap<Node<Region>, List<Node<Region>>> getTargetsToPaths(Node<Region> source, Set<Node<Region>> targetNodes) {
        return getClosestNodes(source, targetNodes).stream()
                .collect(toMap(target -> target, target -> shortestPath.findShortestPath(source, target)))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .sorted(comparingInt(entry -> entry.getValue().size()))
                .limit(2)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void deployToBorderNodes(PlayerState playerState, Set<Node<Region>> borderNodes, Set<Node<Region>> targetNodes, Instruction instruction) {
        int forcesToDeploy = playerState.getReinforcements();

        Comparator<Node<Region>> byDistance = comparingInt(border -> getClosestDistance(border, targetNodes));

        Set<Node<Region>> closestToTargets = borderNodes.stream()
                .sorted(byDistance)
                .limit(4)
                .collect(toCollection(() -> new TreeSet<>(byDistance)));

        for (Node<Region> border : closestToTargets) {
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

        for (Node<Region> border : borderNodes) {
            for (Node<Region> adjacent : border.getAdjacency()) {
                if (forcesToDeploy > 0) {
                    if (adjacent.getValue().getColor() != border.getValue().getColor()
                            && adjacent.getValue().getForces() < 2) {
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
            for (Node<Region> border : closestToTargets) {
                if (forcesToDeploy > 0) {
                    instruction.addStep(new Step(DEPLOY, null, border.getValue().getLocation(), 1));
                    border.getValue().setForces(border.getValue().getForces() + 1);
                    forcesToDeploy--;
                } else {
                    break;
                }
            }
        }
    }

    private void moveFromBorderNodes(Set<Node<Region>> borderNodes, Set<Node<Region>> targetNodes, Instruction instruction) {
        for (Node<Region> border : borderNodes) {
            boolean isEnemyAdjacent = border.getAdjacency().stream()
                    .anyMatch(adjacent -> adjacent.getValue().getColor() != null
                            && adjacent.getValue().getColor() != border.getValue().getColor());

            int forcesToMove = isEnemyAdjacent ? border.getValue().getForces() / 2 : border.getValue().getForces() - 1;

            Map<Node<Region>, List<Node<Region>>> targetToPath = getTargetsToPaths(border, targetNodes);

            for (Node<Region> target : targetToPath.keySet()) {
                if (forcesToMove > 0 && targetToPath.get(target).size() < 10) {
                    Node<Region> closestAdjacent = targetToPath.get(target).get(1);
                    closestAdjacent.setVisited();
                    instruction.addStep(new Step(MOVE, border.getValue().getLocation(), closestAdjacent.getValue().getLocation(), 1));
                    forcesToMove--;
                } else {
                    break;
                }
            }

            for (Node<Region> adjacent : border.getAdjacency()) {
                if (forcesToMove > 0) {
                    if (adjacent.getValue().getColor() != border.getValue().getColor()
                            && adjacent.getValue().getForces() < 2
                            && adjacent.isNotVisited()) {
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
    }

    private Node<Region> getClosest(Node<Region> source, Set<Node<Region>> nodes) {
        return nodes.stream().min(comparingInt(node -> getDistance(node, source))).orElse(source);
    }

    private Integer getClosestDistance(Node<Region> source, Set<Node<Region>> nodes) {
        return nodes.stream().mapToInt(node -> getDistance(node, source)).min().orElse(0);
    }

    private Set<Node<Region>> getClosestNodes(Node<Region> source, Set<Node<Region>> nodes) {
        Comparator<Node<Region>> byDistance = comparingInt(node -> getDistance(node, source));
        return nodes.stream()
                .sorted(byDistance).limit(2)
                .collect(toCollection(() -> new TreeSet<>(byDistance)));
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
