package server.game.stage;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import common.model.Board;
import common.model.Color;
import common.model.region.Region;

public class Engagement {

    public static void engage(Board board) {
        board.getRegions().values()
                .stream()
                .filter(isOverrun())
                .forEach(Engagement::reduceToDominantForces);
    }

    private static Predicate<Region> isOverrun() {
        return region -> region.getChanges().keySet()
                .stream()
                .anyMatch(color -> color != region.getColor());
    }

    private static void reduceToDominantForces(Region region) {
        Map<Color, Integer> changes = region.getChanges();

        if (region.getColor() != null) {
            region.addChange(region.getForces());
            region.setForces(0);
            region.setColor(null);
        }

        List<Map.Entry<Color, Integer>> forces = changes.entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(toList());

        changes.clear();

        Color dominantColor = forces.get(0).getKey();
        int dominantForces = forces.get(0).getValue();

        if (forces.size() > 1) {
            dominantForces -= forces.get(1).getValue();
        }

        if (dominantForces > 0) {
            region.setColor(dominantColor);
            region.setForces(dominantForces);
        }
    }
}
