package client.endpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import client.configuration.ClientConfiguration;
import client.configuration.ClientEndpointConfigurator;
import client.configuration.PlayerConfiguration;
import client.strategy.Strategy;
import common.decoder.GameStateDecoder;
import common.encoder.InstructionEncoder;
import common.model.BoardState;
import common.model.GameState;
import common.model.Instruction;
import common.model.PlayerState;

@Component
@ClientEndpoint(
        configurator = ClientEndpointConfigurator.class,
        decoders = GameStateDecoder.class,
        encoders = InstructionEncoder.class)
public class PlayerEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerEndpoint.class);

    private final ClientConfiguration clientConfiguration;

    private final PlayerConfiguration playerConfiguration;

    private final Strategy strategy;

    private CountDownLatch latch;

    @Autowired
    public PlayerEndpoint(ClientConfiguration clientConfiguration, PlayerConfiguration playerConfiguration, Strategy strategy) {
        this.clientConfiguration = clientConfiguration;
        this.playerConfiguration = playerConfiguration;
        this.strategy = strategy;
    }

    @PostConstruct
    private void connect() {
        try {
            URI serverUri = new URI(clientConfiguration.getWebSocketServerUri() + "/" + playerConfiguration.getPlayerName());

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxTextMessageBufferSize(1024 * 1024);

            container.connectToServer(this, serverUri);

            latch.await();
        } catch (URISyntaxException | InterruptedException | DeploymentException | IOException e) {
            LOG.error(e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Connected to server, session id: {}", session.getId());

        latch = new CountDownLatch(1);
    }

    @OnMessage
    public Instruction onMessage(GameState gameState, Session session) {
        LOG.info("Received board status from server, session id: {}", session.getId());

        PlayerState playerState = gameState.getPlayerState();

        LOG.info("My color: {} base: {} reinforcements: {}", playerState.getColor(), playerState.getBase(), playerState.getReinforcements());

        BoardState boardState = gameState.getBoardState();

        LOG.info("\r\n{}", boardState.getLayout());
        LOG.info("\r\n{}", boardState.getColors());
        LOG.info("\r\n{}", boardState.getForces());

        return strategy.getInstruction(gameState);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOG.info("Disconnected from server, session id: {}, reason: {}", session.getId(), reason);

        latch.countDown();
    }
}
