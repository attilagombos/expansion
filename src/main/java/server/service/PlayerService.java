package server.service;

import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.logging.log4j.util.Strings.EMPTY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.layer.LayerWriter;
import common.model.Board;
import common.model.BoardState;
import common.model.Color;
import common.model.GameState;
import common.model.PlayerState;
import server.model.Player;

@Service
public class PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

    private final Map<Session, Player> playerMapping = new ConcurrentHashMap<>();

    private final List<Color> availableColors = new ArrayList<>(asList(Color.values()));

    private final LayerWriter layerWriter;

    @Autowired
    public PlayerService(LayerWriter layerWriter) {
        this.layerWriter = layerWriter;
    }

    public Player registerPlayer(Session session, String playerName) {
        Player player = null;

        if (isNotEmpty(availableColors)) {
            player = new Player(playerName, availableColors.remove(0));

            playerMapping.put(session, player);
        } else {
            LOG.error("No more available colors for this board");
        }

        return player;
    }

    public void unregisterPlayer(Session session) {
        playerMapping.remove(session);
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

    public void broadcastBoardStatus(Board board, boolean isInitialStatus) {
        BoardState boardState = isInitialStatus
                ? new BoardState(layerWriter.writeLayout(board), EMPTY, EMPTY)
                : new BoardState(EMPTY, layerWriter.writeColors(board), layerWriter.writeForces(board));

        playerMapping.forEach((session, player) -> {
            if (session.isOpen()) {
                PlayerState playerState = new PlayerState(player.getColor(), player.getBase().getLocation(), player.getReinforcements());

                GameState gameState = new GameState(playerState, boardState);

                try {
                    session.getBasicRemote().sendObject(gameState);
                } catch (IOException e) {
                    LOG.error("Could not send board status for player: {}", player.getName(), e);
                } catch (EncodeException e) {
                    LOG.error("Could not encode board status for player: {}", player.getName(), e);
                }
            }
        });
    }
}
