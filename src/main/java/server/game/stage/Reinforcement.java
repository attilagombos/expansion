package server.game.stage;

import static common.model.region.RegionType.BASE;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;

import java.util.List;

import common.model.region.Region;
import server.model.Player;
import server.service.PlayerService;

public class Reinforcement {

    public static final double BASE_MULTIPLIER = 8.0;

    public static final double MINE_MULTIPLIER = 1.0;

    private static final double LAND_MULTIPLIER = 1.0/16.0;

    public static void reinforce(PlayerService playerService, boolean isAutoDeploy) {
        for (Player player : playerService.getPlayerMapping()) {
            List<Region> territory = player.getTerritory();
            int reinforcements;

            double forcesForLands = territory.stream().filter(region -> region.getType() == LAND).count() * LAND_MULTIPLIER;

            if (isAutoDeploy) {
                reinforcements = new Double(forcesForLands).intValue();
            } else {
                double forcesForBases = territory.stream().filter(region -> region.getType() == BASE).count() * BASE_MULTIPLIER;
                double forcesForMines = territory.stream().filter(region -> region.getType() == MINE).count() * MINE_MULTIPLIER;

                reinforcements = new Double(forcesForBases + forcesForMines + forcesForLands).intValue();
            }

            player.setReinforcements(player.getReinforcements() + reinforcements);
        }
    }
}
