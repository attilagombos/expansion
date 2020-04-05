package server.game.stage;

import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;

import java.util.List;

import common.model.region.Region;
import server.configuration.GameConfiguration;
import server.model.Player;
import server.service.PlayerService;

public class Reinforcement {

    public static void reinforce(PlayerService playerService, GameConfiguration gameConfiguration) {
        for (Player player : playerService.getPlayerMapping()) {
            List<Region> territory = player.getTerritory();
            int reinforcements;

            double forcesForLands = territory.stream()
                    .filter(region -> region.getType() == LAND)
                    .count() * gameConfiguration.getLandValue();

            if (gameConfiguration.isAutoDeploy()) {
                reinforcements = new Double(forcesForLands).intValue();
            } else {
                double forcesForBases = territory.stream()
                        .filter(region -> region.getType() == BASE)
                        .count() * gameConfiguration.getBaseValue();
                double forcesForMines = territory.stream()
                        .filter(region -> region.getType() == MINE)
                        .count() * gameConfiguration.getMineValue();

                reinforcements = new Double(forcesForBases + forcesForMines + forcesForLands).intValue();
            }

            player.setReinforcements(player.getReinforcements() + reinforcements);
        }
    }
}
