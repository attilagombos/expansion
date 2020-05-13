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
    private static final String DEFAULT_DEPLOY_PROPERTY = "game.default.deploy";
    private static final String BASE_VALUE_PROPERTY = "game.base.value";
    private static final String MINE_VALUE_PROPERTY = "game.mine.value";
    private static final String LAND_VALUE_PROPERTY = "game.land.value";

    private static final String DEFAULT_LAYOUT_PATH = "src/main/resources/layouts/layout1.csv";
    private static final int DEFAULT_PLAYER_LIMIT = 4;
    private static final long DEFAULT_LOOP_PERIOD = 1000L;
    private static final boolean DEFAULT_AUTO_DEPLOY = FALSE;

    private static final double DEFAULT_DEFAULT_DEPLOY = 0.0;
    private static final double DEFAULT_BASE_VALUE = 8.0;
    private static final double DEFAULT_MINE_VALUE = 1.0;
    private static final double DEFAULT_LAND_VALUE = 0.0/16.0;

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

    public double getBaseDeploy() {
        return ofNullable(environment.getProperty(DEFAULT_DEPLOY_PROPERTY, Double.class)).orElse(DEFAULT_DEFAULT_DEPLOY);
    }

    public double getBaseValue() {
        return ofNullable(environment.getProperty(BASE_VALUE_PROPERTY, Double.class)).orElse(DEFAULT_BASE_VALUE);
    }

    public double getMineValue() {
        return ofNullable(environment.getProperty(MINE_VALUE_PROPERTY, Double.class)).orElse(DEFAULT_MINE_VALUE);
    }

    public double getLandValue() {
        return ofNullable(environment.getProperty(LAND_VALUE_PROPERTY, Double.class)).orElse(DEFAULT_LAND_VALUE);
    }
}
