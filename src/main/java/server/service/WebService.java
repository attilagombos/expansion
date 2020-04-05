package server.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import common.layer.ILayerWriter;
import common.model.dto.BoardState;
import common.model.dto.GameState;
import common.model.dto.PlayerState;
import common.model.dto.instruction.Instruction;
import common.model.game.Board;
import common.model.game.Color;

@Service
public class WebService {

    private static final Logger LOG = LoggerFactory.getLogger(WebService.class);

    private final ILayerWriter layerWriter;

    private final Set<Session> sessions = new HashSet<>();

    private GameState gameStateCache;

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

    public void broadcast(Board board, List<PlayerState> playerStates, int loopCount, Map<Color, Instruction> instructions) {
        BoardState boardState = new BoardState(layerWriter.writeLayout(board), layerWriter.writeColors(board), layerWriter.writeForces(board));

        GameState gameState = new GameState();
        gameState.setRunning(board.isActive());
        gameState.setRounds(loopCount);
        gameState.setBoardState(boardState);
        gameState.setPlayerStates(playerStates);
        gameState.setInstructions(instructions);

        for (Session session : sessions) {
            sendBoardState(session, gameState);
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
