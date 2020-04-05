package server.service;

import static java.lang.Math.min;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.layer.LayoutFileReader;
import common.model.dto.PlayerState;
import common.model.game.Board;
import common.model.game.Region;
import server.configuration.GameConfiguration;
import server.game.Game;
import server.model.Player;

@Service
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final GameConfiguration gameConfiguration;

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final WebService webService;

    private final LayoutFileReader layoutFileReader;

    private Board board;

    private Game game;

    @Autowired
    public GameService(GameConfiguration gameConfiguration, PlayerService playerService, InstructionService instructionService,
            WebService webService, LayoutFileReader layoutFileReader) {
        this.gameConfiguration = gameConfiguration;
        this.playerService = playerService;
        this.instructionService = instructionService;
        this.webService = webService;
        this.layoutFileReader = layoutFileReader;
    }

    @PostConstruct
    public void initialize() {
        board = layoutFileReader.read(gameConfiguration.getLayoutPath());

        board.initialize();

        webService.broadcast(board, emptyList(), 0, emptyMap());
    }

    public void playerConnected(Player player) {
        Region baseForPlayer = board.getAvailableBases().remove(0);

        if (baseForPlayer != null) {
            baseForPlayer.setColor(player.getColor());
            baseForPlayer.setForces(1);

            player.setBase(baseForPlayer);
            player.getTerritory().add(baseForPlayer);

            if (playerService.getPlayerCount() == min(gameConfiguration.getPlayerLimit(), board.getBaseCount())) {
                startGame();
            } else {
                webService.broadcast(board, playerService.getPlayerStates(), 0, emptyMap());
            }
        }
    }

    public boolean isGameRunning() {
        return game != null && game.isRunning();
    }

    private void startGame() {
        if (!isGameRunning()) {
            game = new Game(gameConfiguration, this, playerService, instructionService, board, gameConfiguration.getLoopPeriodMillis());

            ExecutorService executor = Executors.newFixedThreadPool(10);

            executor.execute(game);
        } else {
            LOG.warn("Game is currently running");
        }
    }

    public void broadcastInitial(Board board) {
        List<PlayerState> playerStates = playerService.broadcast(board, 0);

        if (webService.hasSession()) {
            webService.broadcast(board, playerStates, 0, instructionService.getInstructionCache());
        }
    }

    public void broadcast(Board board, int loopCount) {
        List<PlayerState> playerStates = playerService.broadcast(board, loopCount);

        if (webService.hasSession()) {
            webService.broadcast(board, playerStates, loopCount, instructionService.getInstructionCache());
        }
    }
}
