package server.game;

import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.math.NumberUtils.min;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.Region;
import common.model.Step;
import server.model.Board;
import server.model.Player;
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

        broadcastStatus();

        while (isRunning) {
            long loopStartMillis = currentTimeMillis();

            try {
                if (instructionService.hasInstructions()) {
                    deployForces();

                    advanceForces();

                    instructionService.clearInstructions();

                    resolveCombat();

                    generateForces();

                    broadcastStatus();
                }
            } catch (Exception e) {
                LOG.error("Exception during game loop", e);
            }

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

    private void deployForces() {
        for (Player player : playerService.getPlayers()) {
            List<Step> deployments = instructionService.getDeployments(player.getColor());
            int deployableForces = player.getDeployableForces();

            for (Step deployment : deployments) {
                if (deployableForces > 0) {
                    Region target = board.getRegion(deployment.getTarget());

                    if (target.getColor() == player.getColor()) {
                        int forcesToDeploy = min(deployableForces, deployment.getForces());

                        deployableForces -= forcesToDeploy;

                        target.setForces(target.getForces() + forcesToDeploy);
                    } else {
                        LOG.warn("Not valid region for deployment. Player color: {}, target region: {} color {}",
                                player.getColor(), target.getLocation(), target.getColor());
                    }
                } else {
                    LOG.warn("No deployable forces have left");
                    break;
                }
            }

            player.setDeployableForces(deployableForces);
        }
    }

    private void advanceForces() {

    }

    private void resolveCombat() {

    }

    private void generateForces() {
        for (Player player : playerService.getPlayers()) {
            player.setDeployableForces(player.getDeployableForces() + 10);
        }
    }

    private void broadcastStatus() {
        playerService.broadcastBoardStatus(board.getLayout(), board.getForcesByColor());
    }
}
