package server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class GameConfiguration {

    @Value("${game.layout.path}")
    private String layoutPath;

    @Value("${game.player.limit}")
    private int playerLimit;

    @Value("${game.loop.period}")
    private long loopPeriodMillis;

    @Value("${game.bound.deploy}")
    private boolean boundDeploy;

    @Value("${game.default.deploy}")
    private double defaultDeploy;

    @Value("${game.base.value}")
    private int baseValue;

    @Value("${game.mine.value}")
    private int mineValue;

    @Value("${game.land.value}")
    private double landValue;
}
