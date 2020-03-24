package server.service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.model.Region;
import server.configuration.GameConfiguration;
import server.game.Game;
import server.layout.LayoutReader;
import server.layout.LayoutWriter;
import server.model.Board;
import server.model.Player;

@Service
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final GameConfiguration gameConfiguration;

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final LayoutReader layoutReader;

    private final LayoutWriter layoutWriter;

    private Board board;

    private Game game;

    @Autowired
    public GameService(GameConfiguration gameConfiguration, PlayerService playerService, InstructionService instructionService, LayoutReader layoutReader,
            LayoutWriter layoutWriter) {
        this.gameConfiguration = gameConfiguration;
        this.playerService = playerService;
        this.instructionService = instructionService;
        this.layoutReader = layoutReader;
        this.layoutWriter = layoutWriter;
    }

    @PostConstruct
    public void initialize() {
        board = layoutReader.read(gameConfiguration.getLayoutPath());
        board.setLayout(layoutWriter.write(board));

        playerService.setAvailableColors(new ArrayList<>(board.getBases().keySet()));
    }

    public void playerConnected(Player player) {
        Region base = board.getBases().get(player.getColor());
        base.setForces(player.getColor(), 1);

        player.setBase(base);

        if (playerService.getPlayerCount() == gameConfiguration.getPlayerLimit()) {
            startGame();
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
