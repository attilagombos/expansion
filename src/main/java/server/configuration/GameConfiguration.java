package server.configuration;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class GameConfiguration {

    private static final String LAYOUT_PATH_PROPERTY = "game.layout.path";
    private static final String PLAYER_LIMIT_PROPERTY = "game.player.limit";
    private static final String LOOP_PERIOD_PROPERTY = "game.loop.period";
    private static final String AUTO_DEPLOY_PROPERTY = "game.auto.deploy";

    private static final String DEFAULT_LAYOUT_PATH = "src/main/resources/layouts/layout1.csv";
    private static final int DEFAULT_PLAYER_LIMIT = 4;
    private static final long DEFAULT_LOOP_PERIOD = 1000L;
    private static final boolean DEFAULT_AUTO_DEPLOY = FALSE;

    private final Environment environment;

    @Autowired
    public GameConfiguration(Environment environment) {
        this.environment = environment;
    }

    public String getLayoutPath() {
        return ofNullable(environment.getProperty(LAYOUT_PATH_PROPERTY)).orElse(DEFAULT_LAYOUT_PATH);
    }

    public int getPlayerLimit() {
        return ofNullable(environment.getProperty(PLAYER_LIMIT_PROPERTY, Integer.class)).orElse(DEFAULT_PLAYER_LIMIT);
    }

    public long getLoopPeriodMillis() {
        return ofNullable(environment.getProperty(LOOP_PERIOD_PROPERTY, Long.class)).orElse(DEFAULT_LOOP_PERIOD);
    }

    public boolean isAutoDeploy() {
        return ofNullable(environment.getProperty(AUTO_DEPLOY_PROPERTY, Boolean.class)).orElse(DEFAULT_AUTO_DEPLOY);
    }
}
