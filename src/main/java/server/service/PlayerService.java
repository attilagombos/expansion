package server.service;

import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;
import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;
import static java.util.Collections.singletonList;
import static javax.websocket.CloseReason.CloseCodes.NORMAL_CLOSURE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import common.layer.ILayerWriter;
import common.model.Board;
import common.model.BoardState;
import common.model.Color;
import common.model.GameState;
import common.model.PlayerState;
import common.model.region.Region;
import server.model.Player;

@Service
public class PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

    private final Map<Session, Player> playerMapping = new ConcurrentHashMap<>();

    private final List<Color> availableColors = new ArrayList<>(asList(Color.values()));

    private final ILayerWriter layerWriter;

    @Autowired
    public PlayerService(@Qualifier("CsvWriter") ILayerWriter layerWriter) {
        this.layerWriter = layerWriter;
    }

    public Player registerPlayer(Session session, String playerName) {
        Player player = null;

        if (isNotEmpty(availableColors)) {
            shuffle(availableColors);

            player = new Player(playerName, availableColors.remove(0));

            playerMapping.put(session, player);
        } else {
            LOG.error("No more available colors for this board");
        }

        return player;
    }

    public Player unregisterPlayer(Session session) {
        return playerMapping.remove(session);
    }

    public Collection<Player> getPlayerMapping() {
        return playerMapping.values();
    }

    public int getPlayerCount() {
        return playerMapping.size();
    }

    public Player getPlayer(Session session) {
        return playerMapping.get(session);
    }

    public List<PlayerState> broadcast(Board board, boolean isInitialStatus) {
        List<PlayerState> playerStates = new ArrayList<>();

        String layoutLayer = layerWriter.writeLayout(board);
        String colorsLayer = layerWriter.writeColors(board);
        String forcesLayer = layerWriter.writeForces(board);

        BoardState boardState = isInitialStatus ? new BoardState(layoutLayer, EMPTY, EMPTY) : new BoardState(EMPTY, colorsLayer, forcesLayer);

        playerMapping.forEach((session, player) -> {
            if (session.isOpen()) {
                PlayerState playerState = new PlayerState();
                playerState.setName(player.getName());
                playerState.setColor(player.getColor());
                playerState.setBase(player.getBase().getLocation());
                playerState.setTerritory(player.getTerritory().size());
                playerState.setBases((int) player.getTerritory().stream().filter(region -> region.getType() == BASE).count());
                playerState.setMines((int) player.getTerritory().stream().filter(region -> region.getType() == MINE).count());
                playerState.setLands((int) player.getTerritory().stream().filter(region -> region.getType() == LAND).count());
                playerState.setForces(player.getTerritory().stream().mapToInt(Region::getForces).sum());
                playerState.setReinforcements(player.getReinforcements());

                playerStates.add(playerState);

                GameState gameState = new GameState();
                gameState.setRunning(board.isActive());
                gameState.setRounds(board.getLoopCounter());
                gameState.setBoardState(boardState);
                gameState.setPlayerStates(singletonList(playerState));

                try {
                    session.getBasicRemote().sendObject(gameState);
                } catch (IOException e) {
                    LOG.error("Could not send game state for player: {}", player.getName(), e);
                } catch (EncodeException e) {
                    LOG.error("Could not encode game state for player: {}", player.getName(), e);
                }
            }
        });

        return playerStates;
    }

    public void closeSessions() {
        playerMapping.keySet().forEach(session -> {
            try {
                session.close(new CloseReason(NORMAL_CLOSURE,"Game has ended"));
            } catch (IOException e) {
                LOG.error("Could not close session {}", session.getId());
            }
        });

        availableColors.clear();
        availableColors.addAll(asList(Color.values()));
    }
}
