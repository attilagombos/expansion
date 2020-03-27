package server.game;

import static java.lang.System.currentTimeMillis;
import static server.game.stage.Deployment.deploy;
import static server.game.stage.Engagement.engage;
import static server.game.stage.Movement.move;
import static server.game.stage.Reinforcement.reinforce;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.Board;
import common.model.Color;
import server.service.GameService;
import server.service.InstructionService;
import server.service.PlayerService;

public class Game implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    private final GameService gameService;

    private final PlayerService playerService;

    private final InstructionService instructionService;

    private final Board board;

    private final long loopPeriodMillis;

    private boolean isRunning;

    public Game(GameService gameService, PlayerService playerService, InstructionService instructionService, Board board, long loopPeriodMillis) {
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

        LOG.info("Game started");

        playerService.broadcastBoardStatus(board, true);

        while (isRunning) {
            long loopStartMillis = currentTimeMillis();

            processLoop();

            long processingMillis = currentTimeMillis() - loopStartMillis;

            try {
                long sleepForMillis;

                if (processingMillis <= loopPeriodMillis) {
                    sleepForMillis = loopPeriodMillis - processingMillis;
                } else {
                    sleepForMillis = processingMillis % loopPeriodMillis;
                }

                LOG.info("Game thread is sleeping for {} millis", sleepForMillis);

                Thread.sleep(sleepForMillis);
            } catch (InterruptedException e) {
                LOG.error("Could not make game thread sleep, stopping game!", e);
            }
        }

        LOG.info("Game ended");
    }

    private void processLoop() {
        try {
            if (instructionService.hasInstructions()) {

                deploy(board, playerService, instructionService);

                move(board, playerService, instructionService);

                engage(board);

                instructionService.clearInstructions();
            }

            updateBoard();

            updatePlayers();

            reinforce(playerService);

            playerService.broadcastBoardStatus(board, false);
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
