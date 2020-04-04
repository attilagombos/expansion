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
import javax.websocket.EncodeException;
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
import client.strategy.IStrategy;
import common.decoder.GameStateDecoder;
import common.encoder.InstructionEncoder;
import common.model.GameState;
import common.model.PlayerState;

@Component
@ClientEndpoint(
        configurator = ClientEndpointConfigurator.class,
        decoders = GameStateDecoder.class,
        encoders = InstructionEncoder.class)
public class PlayerEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerEndpoint.class);

    private final ClientConfiguration clientConfiguration;

    private final IStrategy strategy;

    private CountDownLatch latch;

    @Autowired
    public PlayerEndpoint(ClientConfiguration clientConfiguration, IStrategy strategy) {
        this.clientConfiguration = clientConfiguration;
        this.strategy = strategy;
    }

    @PostConstruct
    private void connect() {
        try {
            URI serverUri = new URI(clientConfiguration.getWebSocketServerUri());

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
    public void onMessage(GameState gameState, Session session) throws IOException, EncodeException {
        LOG.info("Received game status from server, session id: {}", session.getId());

        PlayerState playerState = gameState.getPlayerStates().get(0);

        LOG.info("My name: {} color: {} base: {} round: {}",
                playerState.getName(), playerState.getColor(), playerState.getBase().getCoordinates(), gameState.getRounds());
        LOG.info("My territory size: {} bases: {} mines: {} lands: {}",
                playerState.getTerritory(), playerState.getBases(), playerState.getMines(), playerState.getLands());
        LOG.info("My total forces: {} available forces: {} reinforcements: {}",
                playerState.getForces(), playerState.getForces() - playerState.getTerritory(), playerState.getReinforcements());

        if (gameState.getRunning()) {
            session.getBasicRemote().sendObject(strategy.getInstruction(gameState));
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOG.info("Disconnected from server, session id: {}, reason: {}", session.getId(), reason);

        latch.countDown();
    }
}
