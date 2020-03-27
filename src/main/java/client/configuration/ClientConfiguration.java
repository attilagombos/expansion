package client.configuration;

import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;

@Component
public class ClientConfiguration {

    private static final String DEFAULT_SERVER_HOST = "localhost";

    private static final int DEFAULT_SERVER_PORT = 8080;

    private static final String DEFAULT_GAME_PATH = "expansion";

    private String serverHost;

    private Integer serverPort;

    private String gamePath;

    public String getWebSocketServerUri() {
        return String.format("ws://%s:%d/%s", getServerHost(), getServerPort(), getGamePath());
    }

    public String getServerHost() {
        return ofNullable(serverHost).orElse(DEFAULT_SERVER_HOST);
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public Integer getServerPort() {
        return ofNullable(serverPort).orElse(DEFAULT_SERVER_PORT);
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getGamePath() {
        return ofNullable(gamePath).orElse(DEFAULT_GAME_PATH);
    }

    public void setGamePath(String gamePath) {
        this.gamePath = gamePath;
    }
}
