package server.endpoint;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import common.decoder.InstructionDecoder;
import common.encoder.GameStateEncoder;
import common.model.Instruction;
import server.configuration.ServerEndpointConfigurator;
import server.model.Player;
import server.service.GameService;
import server.service.InstructionService;
import server.service.PlayerService;

@Component
@ServerEndpoint(
        value= "/expansion/game/{player}",
        configurator = ServerEndpointConfigurator.class,
        encoders = GameStateEncoder.class,
        decoders = InstructionDecoder.class)
public class GameEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(GameEndpoint.class);

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final GameService gameService;

    @Autowired
    public GameEndpoint(PlayerService playerService, InstructionService instructionService, GameService gameService) {
        this.playerService = playerService;
        this.instructionService = instructionService;
        this.gameService = gameService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("player") String playerName) throws IOException {
        if (!gameService.isGameRunning()) {
            LOG.info("Player {} connected, session id: {}", playerName, session.getId());

            Player player = playerService.registerPlayer(session, playerName);

            gameService.playerConnected(player);
        } else {
            session.close();
            LOG.warn("Cannot connect while game is running. Player: {}", playerName);
        }
    }

    @OnMessage
    public void onMessage(Instruction instruction, Session session) {
        Player player = playerService.getPlayer(session);

        if (player != null) {
            LOG.info("Player {} with session id {} sent instruction: {}", player.getName(), session.getId(), instruction);

            if (gameService.isGameRunning()) {
                instructionService.updateInstruction(player.getColor(), instruction);
            } else {
                LOG.warn("Cannot process instruction when game is not running. Player: {}", player.getName());
            }
        } else {
            LOG.error("Unregistered player cannot send instruction");
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Player player = playerService.unregisterPlayer(session);
        LOG.info("Player {} session closed. Id: {}, {}", player.getName(), session.getId(), reason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOG.error("Error for session id: {}, {}", session.getId(), throwable.getMessage());
    }
}
