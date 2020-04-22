package server.game.stage;

import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.LAND;
import static common.model.game.RegionType.MINE;

import java.util.List;

import common.model.game.Region;
import server.configuration.GameConfiguration;
import server.model.Player;
import server.service.PlayerService;

public class Reinforcement {

    public static void reinforce(PlayerService playerService, GameConfiguration gameConfiguration) {
        for (Player player : playerService.getPlayerMapping()) {
            double reinforcements = 0.0;

            List<Region> territory = player.getTerritory();

            if (territory.size() > 0) {
                reinforcements = gameConfiguration.getBaseDeploy();

                double forcesForLands = territory.stream()
                        .filter(region -> region.getType() == LAND)
                        .count() * gameConfiguration.getLandValue();

                if (gameConfiguration.isAutoDeploy()) {
                    reinforcements += forcesForLands;
                } else {
                    double forcesForBases = territory.stream()
                            .filter(region -> region.getType() == BASE)
                            .count() * gameConfiguration.getBaseValue();
                    double forcesForMines = territory.stream()
                            .filter(region -> region.getType() == MINE)
                            .count() * gameConfiguration.getMineValue();

                    reinforcements += forcesForBases + forcesForMines + forcesForLands;
                }
            }

            player.setReinforcements(player.getReinforcements() + (int) reinforcements);
        }
    }
}
