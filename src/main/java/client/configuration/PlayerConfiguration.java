package client.configuration;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;

@Component
public class PlayerConfiguration {

    private String playerName;

    public String getPlayerName() {
        return ofNullable(playerName).orElse(getRuntimeMXBean().getName());
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
