package server.game.stage;

import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.MINE;
import static java.lang.Integer.min;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.dto.instruction.Step;
import common.model.game.Board;
import common.model.game.Region;
import server.configuration.GameConfiguration;
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

    public static void autoDeploy(PlayerService playerService, GameConfiguration gameConfiguration) {
        for (Player player : playerService.getPlayerMapping()) {
            autoDeployForPlayer(player, gameConfiguration);
        }
    }

    private static void deployForPlayer(Board board, Player player, List<Step> deployments) {
        int reinforcements = player.getReinforcements();

        Iterator<Step> iterator = deployments.iterator();

        while (iterator.hasNext()) {
            Step deployment = iterator.next();

            if (reinforcements > 0) {
                Region target = board.getRegion(deployment.getTarget());

                if (isInvalidDeployment(deployment, target, player)) {
                    LOG.warn("Not valid region for deployment. Player color: {}, target region: {} color {}",
                            player.getColor(), target.getCoordinates(), target.getColor());
                    iterator.remove();
                } else {
                    int forcesToDeploy = min(reinforcements, deployment.getForces());

                    target.setForces(target.getForces() + forcesToDeploy);

                    LOG.debug("{} player deployed {} forces to region {}", player.getColor(), forcesToDeploy, target.getCoordinates());

                    reinforcements -= forcesToDeploy;
                }
            } else {
                LOG.warn("No reinforcements have left");
                break;
            }
        }

        player.setReinforcements(reinforcements);
    }

    private static void autoDeployForPlayer(Player player, GameConfiguration gameConfiguration) {
        for (Region region : player.getTerritory()) {
            if (region.getType() == BASE) {
                region.setForces(region.getForces() + gameConfiguration.getBaseValue());
            } else if (region.getType() == MINE) {
                region.setForces(region.getForces() + gameConfiguration.getMineValue());
            }
        }
    }

    private static boolean isInvalidDeployment(Step deployment, Region target, Player player) {
        return target == null
                || deployment.getForces() == null
                || deployment.getForces() == 0
                || target.getColor() != player.getColor();
    }
}
