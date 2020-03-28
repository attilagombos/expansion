package server.service;

import static common.model.region.RegionType.BASES;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.layer.LayerWriter;
import common.layer.LayoutFileReader;
import common.model.Board;
import common.model.region.Region;
import server.configuration.GameConfiguration;
import server.game.Game;
import server.model.Player;

@Service
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final GameConfiguration gameConfiguration;

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final LayoutFileReader layoutFileReader;

    private final LayerWriter layerWriter;

    private Board board;

    private Game game;

    @Autowired
    public GameService(GameConfiguration gameConfiguration, PlayerService playerService, InstructionService instructionService, LayoutFileReader layoutFileReader,
            LayerWriter layerWriter) {
        this.gameConfiguration = gameConfiguration;
        this.playerService = playerService;
        this.instructionService = instructionService;
        this.layoutFileReader = layoutFileReader;
        this.layerWriter = layerWriter;
    }

    @PostConstruct
    public void initialize() {
        board = layoutFileReader.read(gameConfiguration.getLayoutPath());
    }

    public void playerConnected(Player player) {
        Region base = board.getRegions().values()
                .stream()
                .filter(region -> BASES.contains(region.getType()))
                .filter(region -> region.getColor() == null)
                .findFirst().orElse(null);

        if (base != null) {
            base.setColor(player.getColor());
            base.setForces(1);

            player.setBase(base);
            player.getTerritory().add(base);

            if (playerService.getPlayerCount() == gameConfiguration.getPlayerLimit()) {
                startGame();
            }
        } else {
            LOG.error("Not enough bases for players");
        }
    }

    public boolean isGameRunning() {
        return game != null && game.isRunning();
    }

    private void startGame() {
        if (!isGameRunning()) {
            game = new Game(this, playerService, instructionService, board, gameConfiguration.getLoopPeriodMillis());

            ExecutorService executor = Executors.newFixedThreadPool(10);

            executor.execute(game);
        } else {
            LOG.warn("Game is currently running");
        }
    }
}
