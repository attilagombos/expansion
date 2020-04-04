package client.configuration;

import static common.configuration.EndpointConfiguration.GAME_ENDPOINT_CLIENT_PATH;
import static java.lang.String.format;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static java.util.Optional.ofNullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ClientConfiguration {

    private static final String SERVER_HOST_PROPERTY = "server.host";
    private static final String SERVER_PORT_PROPERTY = "server.port";
    private static final String PLAYER_NAME_PROPERTY = "player.name";

    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int DEFAULT_SERVER_PORT = 8080;

    private static final String WEB_SOCKET_SERVER_URI_FORMAT = "ws://%s:%d/%s%s";

    private final Environment environment;

    @Autowired
    public ClientConfiguration(Environment environment) {
        this.environment = environment;
    }

    public String getWebSocketServerUri() {
        return format(WEB_SOCKET_SERVER_URI_FORMAT, getServerHost(), getServerPort(), GAME_ENDPOINT_CLIENT_PATH, getPlayerName());
    }

    public String getServerHost() {
        return ofNullable(environment.getProperty(SERVER_HOST_PROPERTY)).orElse(DEFAULT_SERVER_HOST);
    }

    public Integer getServerPort() {
        return ofNullable(environment.getProperty(SERVER_PORT_PROPERTY, Integer.class)).orElse(DEFAULT_SERVER_PORT);
    }

    public String getPlayerName() {
        return ofNullable(environment.getProperty(PLAYER_NAME_PROPERTY)).orElse(getRuntimeMXBean().getName());
    }
}
