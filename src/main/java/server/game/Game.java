package server.game;

import static java.lang.System.currentTimeMillis;
import static server.game.stage.Deployment.autoDeploy;
import static server.game.stage.Deployment.deploy;
import static server.game.stage.Engagement.engage;
import static server.game.stage.Movement.move;
import static server.game.stage.Reinforcement.reinforce;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.game.Board;
import common.model.game.Color;
import server.configuration.GameConfiguration;
import server.service.GameService;
import server.service.InstructionService;
import server.service.PlayerService;

public class Game implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    private final GameConfiguration gameConfiguration;

    private final GameService gameService;

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final Board board;

    private final long loopPeriodMillis;

    private int loopCount = 0;

    private boolean isRunning;

    private boolean isAutoDeploy;

    public Game(GameConfiguration gameConfiguration, GameService gameService, PlayerService playerService,
            InstructionService instructionService, Board board, long loopPeriodMillis) {
        this.gameConfiguration = gameConfiguration;
        this.gameService = gameService;
        this.playerService = playerService;
        this.instructionService = instructionService;
        this.board = board;
        this.loopPeriodMillis = loopPeriodMillis;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isRunning = true;

        isAutoDeploy = gameConfiguration.isBoundDeploy();

        LOG.info("Game started");

        board.setActive(isRunning);

        gameService.broadcastInitial(board);

        while (isRunning) {
            long loopStartMillis = currentTimeMillis();

            processLoop();

            if (isRunning) {

                long processingMillis = currentTimeMillis() - loopStartMillis;

                long sleepForMillis;

                if (processingMillis <= loopPeriodMillis) {
                    sleepForMillis = loopPeriodMillis - processingMillis;
                } else {
                    sleepForMillis = processingMillis % loopPeriodMillis;
                }

                LOG.info("Game thread is sleeping for {} millis", sleepForMillis);

                try {
                    Thread.sleep(sleepForMillis);
                } catch (InterruptedException e) {
                    LOG.error("Could not make game thread sleep, stopping game!", e);
                }
            }
        }

        gameService.broadcast(board, loopCount);

        LOG.info("Game ended");

        playerService.closeSessions();

        loopCount = 0;

        board.clean();

        board.initialize();

        instructionService.clearInstructions();
    }

    private void processLoop() {
        try {
            loopCount++;

            if (instructionService.hasInstructions()) {

                deploy(board, playerService, instructionService);

                move(board, playerService, instructionService);

                instructionService.clearInstructions();

                engage(board);
            }

            updateBoard();

            updatePlayers();

            reinforce(playerService, gameConfiguration);

            if (isAutoDeploy) {
                autoDeploy(playerService, gameConfiguration);
            }

            board.setActive(isRunning);

            gameService.broadcast(board, loopCount);
        } catch (Exception e) {
            LOG.error("Exception during game loop", e);
        }
    }

    private void updateBoard() {
        board.getRegions().values()
                .stream()
                .filter(region -> region.getChanges().size() > 0)
                .forEach(region -> {
                    Map.Entry<Color, Integer> forces = new ArrayList<>(region.getChanges().entrySet()).get(0);

                    if (region.getColor() == forces.getKey()) {
                        region.setForces(region.getForces() + forces.getValue());
                    }

                    region.getChanges().clear();
                });
    }

    private void updatePlayers() {
        playerService.getPlayerMapping().forEach(player -> {
            player.getTerritory().clear();
            player.getTerritory().addAll(board.getTerritory(player.getColor()));

            if (player.getTerritory().size() == board.getActiveRegionsCount()) {
                isRunning = false;
            }
        });
    }
}
