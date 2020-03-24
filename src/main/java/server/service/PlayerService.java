package server.service;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import common.model.BoardStatus;
import common.model.Color;
import common.model.Forces;
import server.model.Player;

@Service
public class PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

    private final Map<Session, Player> players = new ConcurrentHashMap<>();

    private List<Color> availableColors;

    public Player registerPlayer(Session session, String playerName) {
        Player player = null;

        if (isNotEmpty(availableColors)) {
            player = new Player(playerName);

            player.setColor(availableColors.remove(0));

            players.put(session, player);
        } else {
            LOG.error("No more available colors for this board");
        }

        return player;
    }

    public void unregisterPlayer(Session session) {
        players.remove(session);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Player getPlayer(Session session) {
        return players.get(session);
    }

    public void broadcastBoardStatus(String layout, Map<Color, List<Forces>> forcesByColor) {
        players.forEach((session, player) -> {
            if (session.isOpen()) {
                BoardStatus boardStatus = new BoardStatus();
                boardStatus.setColor(player.getColor());
                boardStatus.setBase(player.getBase().getLocation());
                boardStatus.setDeployableForces(player.getDeployableForces());
                boardStatus.setLayout(layout);
                boardStatus.setForcesByColor(forcesByColor);

                try {
                    session.getBasicRemote().sendObject(boardStatus);
                } catch (IOException e) {
                    LOG.error("Could not send board status for player: {}", player.getName(), e);
                } catch (EncodeException e) {
                    LOG.error("Could not encode board status for player: {}", player.getName(), e);
                }
            }
        });
    }

    public void setAvailableColors(List<Color> availableColors) {
        this.availableColors = availableColors;
    }
}
