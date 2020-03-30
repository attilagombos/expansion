package server.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import common.model.GameState;
import common.model.PlayerState;

@Service
public class WebService {

    private static final Logger LOG = LoggerFactory.getLogger(WebService.class);

    private final ILayerWriter layerWriter;

    private final Set<Session> sessions = new HashSet<>();

    private GameState gameStateCache = new GameState();

    @Autowired
    public WebService(@Qualifier("HtmlWriter") ILayerWriter layerWriter) {
        this.layerWriter = layerWriter;
    }

    public void registerSession(Session session) {
        sessions.add(session);

        sendBoardState(session, gameStateCache);
    }

    public void unregisterSession(Session session) {
        sessions.remove(session);
    }

    public boolean hasSession() {
        return !sessions.isEmpty();
    }

    public void broadcast(Board board, List<PlayerState> playerStates) {
        BoardState boardState = new BoardState(layerWriter.writeLayout(board), layerWriter.writeColors(board), layerWriter.writeForces(board));

        GameState gameState = new GameState();
        gameState.setBoardState(boardState);
        gameState.setPlayerStates(playerStates);

        for (Session session : sessions) {
            sendBoardState(session, gameStateCache);
        }

        gameStateCache = gameState;
    }

    private void sendBoardState(Session session, GameState gameState) {
        try {
            session.getBasicRemote().sendObject(gameState);
        } catch (IOException e) {
            LOG.error("Could not send game state for web session: {}", session.getId(), e);
        } catch (EncodeException e) {
            LOG.error("Could not encode game state for web session: {}", session.getId(), e);
        }
    }
}
