package server.configuration;

import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Service;

@Service
public class GameConfiguration {

    private static final String DEFAULT_LAYOUT_PATH = "src/main/resources/layouts/layout1.csv";

    private static final int DEFAULT_PLAYER_LIMIT = 4;

    private static final long DEFAULT_LOOP_PERIOD_MILLIS = 1000L;

    private String layoutPath;

    private Integer playerLimit;

    private Long loopPeriodMillis;

    public String getLayoutPath() {
        return ofNullable(layoutPath).orElse(DEFAULT_LAYOUT_PATH);
    }

    public void setLayoutPath(String layoutPath) {
        this.layoutPath = layoutPath;
    }

    public Integer getPlayerLimit() {
        return ofNullable(playerLimit).orElse(DEFAULT_PLAYER_LIMIT);
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Long getLoopPeriodMillis() {
        return ofNullable(loopPeriodMillis).orElse(DEFAULT_LOOP_PERIOD_MILLIS);
    }

    public void setLoopPeriodMillis(Long loopPeriodMillis) {
        this.loopPeriodMillis = loopPeriodMillis;
    }
}
