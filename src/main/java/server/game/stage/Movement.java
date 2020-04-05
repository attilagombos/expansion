package server.game.stage;

import static java.lang.Integer.min;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.model.dto.instruction.Step;
import common.model.game.Board;
import common.model.game.Region;
import server.model.Player;
import server.service.InstructionService;
import server.service.PlayerService;

public class Movement {

    private static final Logger LOG = LoggerFactory.getLogger(Movement.class);

    public static void move(Board board, PlayerService playerService, InstructionService instructionService) {
        for (Player player : playerService.getPlayerMapping()) {
            List<Step> movements = instructionService.getMovements(player.getColor());

            moveForPlayer(board, player, movements);
        }
    }

    private static void moveForPlayer(Board board, Player player, List<Step> movements) {
        for (Step movement : movements) {
            Region source = board.getRegion(movement.getSource());
            Region target = board.getRegion(movement.getTarget());

            if (isInvalidMovement(movement, source, target)) {
                LOG.warn("Invalid movement. Player color : {}, movement: {}", player.getColor(), movement);
            } else if (source.getColor() == player.getColor()) {
                int forcesToMove = min(source.getForces(), movement.getForces());

                source.setForces(source.getForces() - forcesToMove);

                if (source.getForces() == 0) {
                    source.setColor(null);
                }

                target.addChange(player.getColor(), forcesToMove);
            } else {
                LOG.warn("Not valid region for movement. Player color: {}, source region: {} color {}",
                        player.getColor(), source.getLocation(), source.getColor());
            }
        }
    }

    private static boolean isInvalidMovement(Step movement, Region source, Region target) {
        return source == null || target == null || movement.getForces() == null || movement.getForces() == 0;
    }
}
