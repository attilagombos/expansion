package server.endpoint;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import common.encoder.GameStateEncoder;
import server.configuration.ServerEndpointConfigurator;
import server.service.WebService;

@Component
@ServerEndpoint(
        value= "/expansion/web",
        configurator = ServerEndpointConfigurator.class,
        encoders = GameStateEncoder.class)
public class WebEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(WebEndpoint.class);

    private final WebService webService;

    @Autowired
    public WebEndpoint(WebService webService) {
        this.webService = webService;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Web session open. Id: {}", session.getId());
        webService.registerSession(session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        webService.unregisterSession(session);
        LOG.info("Web session closed. Id: {}, reason: {}", session.getId(), reason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOG.error("Error for session id: {}, reason: {}", session.getId(), throwable.getMessage());
    }
}
