package server.game.stage;

import static java.lang.Integer.min;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.Board;
import common.model.Step;
import common.model.region.Region;
import server.model.Player;
import server.service.InstructionService;
import server.service.PlayerService;

public class Deployment {

    private static final Logger LOG = LoggerFactory.getLogger(Deployment.class);

    public static void deploy(Board board, PlayerService playerService, InstructionService instructionService) {
        for (Player player : playerService.getPlayerMapping()) {
            List<Step> deployments = instructionService.getDeployments(player.getColor());

            deployForPlayer(board, player, deployments);
        }
    }

    private static void deployForPlayer(Board board, Player player, List<Step> deployments) {
        int reinforcements = player.getReinforcements();

        for (Step deployment : deployments) {
            if (reinforcements > 0) {
                Region target = board.getRegion(deployment.getTarget());

                if (isInvalidDeployment(deployment, target)) {
                    LOG.warn("Invalid deployment. Player color : {}, deployment: {}", player.getColor(), deployment);
                } else if (target.getColor() == player.getColor()) {
                    int forcesToDeploy = min(reinforcements, deployment.getForces());

                    target.setForces(target.getForces() + forcesToDeploy);

                    LOG.debug("{} player deployed {} forces to region {}", player.getColor(), forcesToDeploy, target.getCoordinates());

                    reinforcements -= forcesToDeploy;
                } else {
                    LOG.warn("Not valid region for deployment. Player color: {}, target region: {} color {}",
                            player.getColor(), target.getCoordinates(), target.getColor());
                }
            } else {
                LOG.warn("No reinforcements have left");
                break;
            }
        }

        player.setReinforcements(reinforcements);
    }

    private static boolean isInvalidDeployment(Step deployment, Region target) {
        return target == null || deployment.getForces() == null || deployment.getForces() == 0;
    }
}
