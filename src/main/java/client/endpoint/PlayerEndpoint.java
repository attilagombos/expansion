package client.endpoint;

import static common.model.StepType.DEPLOY;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
import common.decoder.BoardStatusDecoder;
import common.encoder.InstructionEncoder;
import common.model.BoardStatus;
import common.model.Instruction;
import common.model.Location;
import common.model.Step;

@Component
@ClientEndpoint(
        configurator = ClientEndpointConfigurator.class,
        decoders = BoardStatusDecoder.class,
        encoders = InstructionEncoder.class)
public class PlayerEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerEndpoint.class);

    private final ClientConfiguration clientConfiguration;

    private final PlayerConfiguration playerConfiguration;

    private CountDownLatch latch;

    @Autowired
    public PlayerEndpoint(ClientConfiguration clientConfiguration, PlayerConfiguration playerConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.playerConfiguration = playerConfiguration;
    }

    @PostConstruct
    private void connect() {
        try {
            URI serverUri = new URI(clientConfiguration.getWebSocketUri() + "/" + playerConfiguration.getPlayerName());

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            container.connectToServer(this, serverUri);

            latch.await();
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        } catch (InterruptedException | DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Connected to server, session id: {}", session.getId());

        latch = new CountDownLatch(1);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOG.info("Disconnected from server, session id: {}", session.getId());

        latch.countDown();
    }

    @OnMessage
    public Instruction onMessage(BoardStatus boardStatus, Session session) {
        LOG.info("Received board status from server, session id: {}", session.getId());

        LOG.info("My color: {} base: {} deployable forces: {}", boardStatus.getColor(), boardStatus.getBase(), boardStatus.getDeployableForces());

        LOG.info("\r\n{}", boardStatus.getForcesByColor().entrySet());

        LOG.info("\r\n{}", boardStatus.getLayout());

        Location base = boardStatus.getBase();

        Instruction instruction = new Instruction();

        List<Step> steps = new ArrayList<>();

        steps.add(new Step(DEPLOY, null, base, boardStatus.getDeployableForces()));

        instruction.setSteps(steps);

        return instruction;
    }

    public void block() throws InterruptedException {
        latch.await();
    }
}
